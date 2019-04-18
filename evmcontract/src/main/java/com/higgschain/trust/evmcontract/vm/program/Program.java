/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.higgschain.trust.evmcontract.vm.program;

import com.higgschain.trust.evmcontract.config.BlockChainConfig;
import com.higgschain.trust.evmcontract.config.SystemProperties;
import com.higgschain.trust.evmcontract.core.AccountState;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.core.Transaction;
import com.higgschain.trust.evmcontract.crypto.HashUtil;
import com.higgschain.trust.evmcontract.db.ContractDetails;
import com.higgschain.trust.evmcontract.util.ByteArraySet;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.util.FastByteComparisons;
import com.higgschain.trust.evmcontract.util.Utils;
import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.MessageCall;
import com.higgschain.trust.evmcontract.vm.OpCode;
import com.higgschain.trust.evmcontract.vm.VM;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvoke;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvokeFactory;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvokeFactoryImpl;
import com.higgschain.trust.evmcontract.vm.program.listener.CompositeProgramListener;
import com.higgschain.trust.evmcontract.vm.program.listener.ProgramListenerAware;
import com.higgschain.trust.evmcontract.vm.program.listener.ProgramStorageChangeListener;
import com.higgschain.trust.evmcontract.vm.trace.ProgramTrace;
import com.higgschain.trust.evmcontract.vm.trace.ProgramTraceListener;
import com.higgschain.trust.evmcontract.vm.PrecompiledContracts;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.*;

import static com.higgschain.trust.evmcontract.util.BIUtil.*;
import static com.higgschain.trust.evmcontract.util.ByteUtil.EMPTY_BYTE_ARRAY;
import static com.higgschain.trust.evmcontract.util.ByteUtil.toHexString;
import static java.lang.StrictMath.min;
import static java.lang.String.format;
import static java.math.BigInteger.ZERO;
import static org.apache.commons.lang3.ArrayUtils.*;

/**
 * The type Program.
 *
 * @author Roman Mandeleil
 * @since 01.06.2014
 */
public class Program {

    private static final Logger logger = LoggerFactory.getLogger("VM");

    /**
     * This attribute defines the number of recursive calls allowed in the EVM
     * Note: For the JVM to reach this level without a StackOverflow exception,
     * ethereumj may need to be started with a JVM argument to increase
     * the stack size. For example: -Xss10m
     */
    private static final int MAX_DEPTH = 1024;

    /**
     * Max size for stack checks
     */
    private static final int MAX_STACKSIZE = 1024;

    private Transaction transaction;

    private ProgramInvoke invoke;
    private ProgramInvokeFactory programInvokeFactory = new ProgramInvokeFactoryImpl();

    private ProgramOutListener listener;
    private ProgramTraceListener traceListener;
    private ProgramStorageChangeListener storageDiffListener = new ProgramStorageChangeListener();
    private CompositeProgramListener programListener = new CompositeProgramListener();

    private com.higgschain.trust.evmcontract.vm.program.Stack stack;
    private Memory memory;
    private Storage storage;
    private byte[] returnDataBuffer;

    private ProgramResult result = new ProgramResult();
    private ProgramTrace trace = new ProgramTrace();

    private byte[] codeHash;
    private byte[] ops;
    private int pc;
    private byte lastOp;
    private byte previouslyExecutedOp;
    private boolean stopped;
    private ByteArraySet touchedAccounts = new ByteArraySet();

    private ProgramPrecompile programPrecompile;

    //  CommonConfig commonConfig = CommonConfig.getDefault();

    private final SystemProperties config;

    private final BlockChainConfig blockchainConfig;

    /**
     * Instantiates a new Program.
     *
     * @param ops           the ops
     * @param programInvoke the program invoke
     */
    public Program(byte[] ops, ProgramInvoke programInvoke) {
        this(ops, programInvoke, null);
    }

    /**
     * Instantiates a new Program.
     *
     * @param ops           the ops
     * @param programInvoke the program invoke
     * @param transaction   the transaction
     */
    public Program(byte[] ops, ProgramInvoke programInvoke, Transaction transaction) {
        this(ops, programInvoke, transaction, SystemProperties.getDefault());
    }

    /**
     * Instantiates a new Program.
     *
     * @param ops           the ops
     * @param programInvoke the program invoke
     * @param transaction   the transaction
     * @param config        the config
     */
    public Program(byte[] ops, ProgramInvoke programInvoke, Transaction transaction, SystemProperties config) {
        this(null, ops, programInvoke, transaction, config);
    }

    /**
     * Instantiates a new Program.
     *
     * @param codeHash      the code hash
     * @param ops           the ops
     * @param programInvoke the program invoke
     * @param transaction   the transaction
     * @param config        the config
     */
    public Program(byte[] codeHash, byte[] ops, ProgramInvoke programInvoke, Transaction transaction, SystemProperties config) {
        this.config = config;
        this.invoke = programInvoke;
        this.transaction = transaction;

        this.codeHash = codeHash == null || FastByteComparisons.equal(HashUtil.EMPTY_DATA_HASH, codeHash) ? null : codeHash;
        this.ops = nullToEmpty(ops);

        traceListener = new ProgramTraceListener(config.vmTrace());
        this.memory = setupProgramListener(new Memory());
        this.stack = setupProgramListener(new com.higgschain.trust.evmcontract.vm.program.Stack());
        this.storage = setupProgramListener(new Storage(programInvoke));
        this.trace = new ProgramTrace(config, programInvoke);
        this.blockchainConfig = config.getBlockchainConfig();
    }

    /**
     * Gets program precompile.
     *
     * @return the program precompile
     */
    public ProgramPrecompile getProgramPrecompile() {
        if (programPrecompile == null) {
//            if (codeHash != null && commonConfig.precompileSource() != null) {
//                programPrecompile = commonConfig.precompileSource().get(codeHash);
//            }
            if (programPrecompile == null) {
                programPrecompile = ProgramPrecompile.compile(ops);

//                if (codeHash != null && commonConfig.precompileSource() != null) {
//                    commonConfig.precompileSource().put(codeHash, programPrecompile);
//                }
            }
        }
        return programPrecompile;
    }

//    public Program withCommonConfig(CommonConfig commonConfig) {
//        this.commonConfig = commonConfig;
//        return this;
//    }

    /**
     * Gets call deep.
     *
     * @return the call deep
     */
    public int getCallDeep() {
        return invoke.getCallDeep();
    }

    private InternalTransaction addInternalTx(byte[] nonce, DataWord gasLimit, byte[] senderAddress, byte[] receiveAddress,
                                              BigInteger value, byte[] data, String note) {

        InternalTransaction result = null;
        if (transaction != null) {
            byte[] senderNonce = isEmpty(nonce) ? getStorage().getNonce(senderAddress).toByteArray() : nonce;

            data = config.recordInternalTransactionsData() ? data : null;
            result = getResult().addInternalTransaction(transaction.getHash(), getCallDeep(), senderNonce,
                    getGasPrice(), gasLimit, senderAddress, receiveAddress, value.toByteArray(), data, note);
        }

        return result;
    }

    private <T extends ProgramListenerAware> T setupProgramListener(T programListenerAware) {
        if (programListener.isEmpty()) {
            programListener.addListener(traceListener);
            programListener.addListener(storageDiffListener);
        }

        programListenerAware.setProgramListener(programListener);

        return programListenerAware;
    }

    /**
     * Gets storage diff.
     *
     * @return the storage diff
     */
    public Map<DataWord, DataWord> getStorageDiff() {
        return storageDiffListener.getDiff();
    }

    /**
     * Gets op.
     *
     * @param pc the pc
     * @return the op
     */
    public byte getOp(int pc) {
        return (getLength(ops) <= pc) ? 0 : ops[pc];
    }

    /**
     * Gets current op.
     *
     * @return the current op
     */
    public byte getCurrentOp() {
        return isEmpty(ops) ? 0 : ops[pc];
    }

    /**
     * Last Op can only be set publicly (no getLastOp method), is used for logging.
     *
     * @param op the op
     */
    public void setLastOp(byte op) {
        this.lastOp = op;
    }

    /**
     * Should be set only after the OP is fully executed.
     *
     * @param op the op
     */
    public void setPreviouslyExecutedOp(byte op) {
        this.previouslyExecutedOp = op;
    }

    /**
     * Returns the last fully executed OP.
     *
     * @return the previously executed op
     */
    public byte getPreviouslyExecutedOp() {
        return this.previouslyExecutedOp;
    }

    /**
     * Stack push.
     *
     * @param data the data
     */
    public void stackPush(byte[] data) {
        stackPush(new DataWord(data));
    }

    /**
     * Stack push zero.
     */
    public void stackPushZero() {
        stackPush(new DataWord(0));
    }

    /**
     * Stack push one.
     */
    public void stackPushOne() {
        DataWord stackWord = new DataWord(1);
        stackPush(stackWord);
    }

    /**
     * Stack push.
     *
     * @param stackWord the stack word
     */
    public void stackPush(DataWord stackWord) {
        //Sanity Check
        verifyStackOverflow(0, 1);
        stack.push(stackWord);
    }

    /**
     * Gets stack.
     *
     * @return the stack
     */
    public com.higgschain.trust.evmcontract.vm.program.Stack getStack() {
        return this.stack;
    }

    /**
     * Gets pc.
     *
     * @return the pc
     */
    public int getPC() {
        return pc;
    }

    /**
     * Sets pc.
     *
     * @param pc the pc
     */
    public void setPC(DataWord pc) {
        this.setPC(pc.intValue());
    }

    /**
     * Sets pc.
     *
     * @param pc the pc
     */
    public void setPC(int pc) {
        this.pc = pc;

        if (this.pc >= ops.length) {
            stop();
        }
    }

    /**
     * Is stopped boolean.
     *
     * @return the boolean
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Stop.
     */
    public void stop() {
        stopped = true;
    }

    /**
     * Sets h return.
     *
     * @param buff the buff
     */
    public void setHReturn(byte[] buff) {
        getResult().setHReturn(buff);
    }

    /**
     * Step.
     */
    public void step() {
        setPC(pc + 1);
    }

    /**
     * Sweep byte [ ].
     *
     * @param n the n
     * @return the byte [ ]
     */
    public byte[] sweep(int n) {

        if (pc + n > ops.length) {
            stop();
        }
        //从ops字节数组中拷贝pc到pc+n个长度的字节到一个新数组中
        byte[] data = Arrays.copyOfRange(ops, pc, pc + n);
        pc += n;
        if (pc >= ops.length) {
            stop();
        }

        return data;
    }

    /**
     * Stack pop data word.
     *
     * @return the data word
     */
    public DataWord stackPop() {
        return stack.pop();
    }

    /**
     * Verifies that the stack is at least <code>stackSize</code>
     *
     * @param stackSize int
     * @throws StackTooSmallException If the stack is                                smaller than <code>stackSize</code>
     */
    public void verifyStackSize(int stackSize) {
        if (stack.size() < stackSize) {
            throw Program.Exception.tooSmallStack(stackSize, stack.size());
        }
    }

    /**
     * Verify stack overflow.
     *
     * @param argsReqs   the args reqs
     * @param returnReqs the return reqs
     */
    public void verifyStackOverflow(int argsReqs, int returnReqs) {
        if ((stack.size() - argsReqs + returnReqs) > MAX_STACKSIZE) {
            throw new StackTooLargeException("Expected: overflow " + MAX_STACKSIZE + " elements stack limit");
        }
    }

    /**
     * Gets mem size.
     *
     * @return the mem size
     */
    public int getMemSize() {
        return memory.size();
    }

    /**
     * Memory save.
     *
     * @param addrB the addr b
     * @param value the value
     */
    public void memorySave(DataWord addrB, DataWord value) {
        memory.write(addrB.intValue(), value.getData(), value.getData().length, false);
    }

    /**
     * Memory save limited.
     *
     * @param addr     the addr
     * @param data     the data
     * @param dataSize the data size
     */
    public void memorySaveLimited(int addr, byte[] data, int dataSize) {
        memory.write(addr, data, dataSize, true);
    }

    /**
     * Memory save.
     *
     * @param addr  the addr
     * @param value the value
     */
    public void memorySave(int addr, byte[] value) {
        memory.write(addr, value, value.length, false);
    }

    /**
     * Memory expand.
     *
     * @param outDataOffs the out data offs
     * @param outDataSize the out data size
     */
    public void memoryExpand(DataWord outDataOffs, DataWord outDataSize) {
        if (!outDataSize.isZero()) {
            memory.extend(outDataOffs.intValue(), outDataSize.intValue());
        }
    }

    /**
     * Allocates a piece of memory and stores value at given offset address
     *
     * @param addr      is the offset address
     * @param allocSize size of memory needed to write
     * @param value     the data to write to memory
     */
    public void memorySave(int addr, int allocSize, byte[] value) {
        memory.extendAndWrite(addr, allocSize, value);
    }

    /**
     * Memory load data word.
     *
     * @param addr the addr
     * @return the data word
     */
    public DataWord memoryLoad(DataWord addr) {
        return memory.readWord(addr.intValue());
    }

    /**
     * Memory load data word.
     *
     * @param address the address
     * @return the data word
     */
    public DataWord memoryLoad(int address) {
        return memory.readWord(address);
    }

    /**
     * Memory chunk byte [ ].
     *
     * @param offset the offset
     * @param size   the size
     * @return the byte [ ]
     */
    public byte[] memoryChunk(int offset, int size) {
        return memory.read(offset, size);
    }

    /**
     * Allocates extra memory in the program for
     * a specified size, calculated from a given offset
     *
     * @param offset the memory address offset
     * @param size   the number of bytes to allocate
     */
    public void allocateMemory(int offset, int size) {
        memory.extend(offset, size);
    }

    /**
     * Suicide.
     *
     * @param obtainerAddress the obtainer address
     */
    public void suicide(DataWord obtainerAddress) {

        byte[] owner = getOwnerAddress().getLast20Bytes();
        byte[] obtainer = obtainerAddress.getLast20Bytes();
        BigInteger balance = getStorage().getBalance(owner);

        if ( logger.isInfoEnabled()) {
            logger.info("Transfer to: [{}] heritage: [{}]",
                    toHexString(obtainer),
                    balance);
        }

        addInternalTx(null, null, owner, obtainer, balance, null, "suicide");

        if (FastByteComparisons.compareTo(owner, 0, 20, obtainer, 0, 20) == 0) {
            // if owner == obtainer just zeroing account according to Yellow Paper
            getStorage().addBalance(owner, balance.negate());
        } else {
            transfer(getStorage(), owner, obtainer, balance);
        }

        getResult().addDeleteAccount(this.getOwnerAddress());
    }

    /**
     * Gets storage.
     *
     * @return the storage
     */
    public Repository getStorage() {
        return this.storage;
    }

    /**
     * Create contract.
     *
     * @param value    the value
     * @param memStart the mem start
     * @param memSize  the mem size
     */
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    public void createContract(DataWord value, DataWord memStart, DataWord memSize) {
        // reset return buffer right before the call
        returnDataBuffer = null;

        if (getCallDeep() == MAX_DEPTH) {
            stackPushZero();
            return;
        }

        byte[] senderAddress = this.getOwnerAddress().getLast20Bytes();
        BigInteger endowment = value.value();
        if (isNotCovers(getStorage().getBalance(senderAddress), endowment)) {
            stackPushZero();
            return;
        }

        // [1] FETCH THE CODE FROM THE MEMORY
        byte[] programCode = memoryChunk(memStart.intValue(), memSize.intValue());

        if (logger.isInfoEnabled()) {
            logger.info("creating a new contract inside contract executeContract: [{}]", toHexString(senderAddress));
        }

        //  actual gas subtract
        DataWord gasLimit = getGas();
        spendGas(gasLimit.longValue(), "internal call");

        // [2] CREATE THE CONTRACT ADDRESS
        byte[] nonce = getStorage().getNonce(senderAddress).toByteArray();
        byte[] newAddress = HashUtil.calcNewAddr(getOwnerAddress().getLast20Bytes(), nonce);

        AccountState existingAddr = getStorage().getAccountState(newAddress);
        boolean contractAlreadyExists = existingAddr != null;

        if (byTestingSuite()) {
            // This keeps track of the contracts created for a test
            getResult().addCallCreate(programCode, EMPTY_BYTE_ARRAY,
                    gasLimit.getNoLeadZeroesData(),
                    value.getNoLeadZeroesData());
        }

        // [3] UPDATE THE NONCE
        // (THIS STAGE IS NOT REVERTED BY ANY EXCEPTION)
        if (!byTestingSuite()) {
            getStorage().increaseNonce(senderAddress);
        }

        Repository track = getStorage().startTracking();

        //In case of hashing collisions, check for any balance before createAccount()
        BigInteger oldBalance = track.getBalance(newAddress);
        track.createAccount(newAddress);
        if (blockchainConfig.eip161()) {
            track.increaseNonce(newAddress);
        }
        track.addBalance(newAddress, oldBalance);

        // [4] TRANSFER THE BALANCE
        BigInteger newBalance = ZERO;
        if (!byTestingSuite()) {
            track.addBalance(senderAddress, endowment.negate());
            newBalance = track.addBalance(newAddress, endowment);
        }


        // [5] COOK THE INVOKE AND EXECUTE
        InternalTransaction internalTx = addInternalTx(nonce, getGasLimit(), senderAddress, null, endowment, programCode, "create");
        ProgramInvoke programInvoke = programInvokeFactory.createProgramInvoke(
                this, new DataWord(newAddress), getOwnerAddress(), value, gasLimit,
                newBalance, null, track, null, false, byTestingSuite());

        ProgramResult result = ProgramResult.createEmpty();

        if (contractAlreadyExists) {
            result.setException(new BytecodeExecutionException("Trying to create a contract with existing contract address: 0x" + toHexString(newAddress)));
        } else if (isNotEmpty(programCode)) {
            VM vm = new VM(config);
            Program program = new Program(programCode, programInvoke, internalTx, config);
            vm.play(program);
            result = program.getResult();

            getResult().merge(result);
        }

        // 4. CREATE THE CONTRACT OUT OF RETURN
        byte[] code = result.getHReturn();

        long storageCost = getLength(code) * getBlockchainConfig().getGasCost().getCREATE_DATA();
        long afterSpend = programInvoke.getGas().longValue() - storageCost - result.getGasUsed();
        if (afterSpend < 0) {
            if (!blockchainConfig.getConstants().createEmptyContractOnOOG()) {
                result.setException(Program.Exception.notEnoughSpendingGas("No gas to return just created contract",
                        storageCost, this));
            } else {
                track.saveCode(newAddress, EMPTY_BYTE_ARRAY);
            }
        } else if (getLength(code) > blockchainConfig.getConstants().getMAX_CONTRACT_SZIE()) {
            result.setException(Program.Exception.notEnoughSpendingGas("Contract size too large: " + getLength(result.getHReturn()),
                    storageCost, this));
        } else if (!result.isRevert()) {
            result.spendGas(storageCost);
            track.saveCode(newAddress, code);
        }

        if (result.getException() != null || result.isRevert()) {
            logger.debug("contract executeContract halted by Exception: contract: [{}], exception: [{}]",
                    toHexString(newAddress),
                    result.getException());

            internalTx.reject();
            result.rejectInternalTransactions();

            track.rollback();
            stackPushZero();

            if (result.getException() != null) {
                return;
            } else {
                returnDataBuffer = result.getHReturn();
            }
        } else {
            if (!byTestingSuite()) {
                track.commit();
            }

            // IN SUCCESS PUSH THE ADDRESS INTO THE STACK
            stackPush(new DataWord(newAddress));
        }

        // 5. REFUND THE REMAIN GAS
        long refundGas = gasLimit.longValue() - result.getGasUsed();
        if (refundGas > 0) {
            refundGas(refundGas, "remain gas from the internal call");
            if (logger.isInfoEnabled()) {
                logger.info("The remaining gas is refunded, account: [{}], gas: [{}] ",
                        toHexString(getOwnerAddress().getLast20Bytes()),
                        refundGas);
            }
        }
    }

    /**
     * That method is for internal code invocations
     * <p/>
     * - Normal calls invoke a specified contract which updates itself
     * - Stateless calls invoke code from another contract, within the context of the caller
     *
     * @param msg is the message call object
     */
    public void callToAddress(MessageCall msg) {
        // reset return buffer right before the call
        returnDataBuffer = null;

        if (getCallDeep() == MAX_DEPTH) {
            stackPushZero();
            refundGas(msg.getGas().longValue(), " call deep limit reach");
            return;
        }

        byte[] data = memoryChunk(msg.getInDataOffs().intValue(), msg.getInDataSize().intValue());

        // FETCH THE SAVED STORAGE
        byte[] codeAddress = msg.getCodeAddress().getLast20Bytes();
        byte[] senderAddress = getOwnerAddress().getLast20Bytes();
        byte[] contextAddress = msg.getType().callIsStateless() ? senderAddress : codeAddress;

        if ( logger.isInfoEnabled()) {
            logger.info(msg.getType().name() + " for existing contract: address: [{}], outDataOffs: [{}], outDataSize: [{}]  ",
                    toHexString(contextAddress), msg.getOutDataOffs().longValue(), msg.getOutDataSize().longValue());
        }

        Repository track = getStorage().startTracking();

        // 2.1 PERFORM THE VALUE (endowment) PART
        BigInteger endowment = msg.getEndowment().value();
        BigInteger senderBalance = track.getBalance(senderAddress);
        if (isNotCovers(senderBalance, endowment)) {
            stackPushZero();
            refundGas(msg.getGas().longValue(), "refund gas from message call");
            return;
        }


        // FETCH THE CODE
        byte[] programCode = getStorage().isExist(codeAddress) ? getStorage().getCode(codeAddress) : EMPTY_BYTE_ARRAY;


        BigInteger contextBalance = ZERO;
        if (byTestingSuite()) {
            // This keeps track of the calls created for a test
            getResult().addCallCreate(data, contextAddress,
                    msg.getGas().getNoLeadZeroesData(),
                    msg.getEndowment().getNoLeadZeroesData());
        } else {
            // senderAddress:调用合约的地址 //contextAddress合约地址
            track.addBalance(senderAddress, endowment.negate());
            contextBalance = track.addBalance(contextAddress, endowment);
        }

        // CREATE CALL INTERNAL TRANSACTION
        InternalTransaction internalTx = addInternalTx(null, getGasLimit(), senderAddress, contextAddress, endowment, data, "call");

        ProgramResult result = null;
        if (isNotEmpty(programCode)) {
            ProgramInvoke programInvoke = programInvokeFactory.createProgramInvoke(
                    this, new DataWord(contextAddress),
                    msg.getType().callIsDelegate() ? getCallerAddress() : getOwnerAddress(),
                    msg.getType().callIsDelegate() ? getCallValue() : msg.getEndowment(),
                    msg.getGas(), contextBalance, data, track, null,
                    msg.getType().callIsStatic() || isStaticCall(), byTestingSuite());

            VM vm = new VM(config);
            Program program = new Program(getStorage().getCodeHash(codeAddress), programCode, programInvoke, internalTx, config);
            vm.play(program);
            result = program.getResult();

            getTrace().merge(program.getTrace());
            getResult().merge(result);

            if (result.getException() != null || result.isRevert()) {
                logger.debug("contract executeContract halted by Exception: contract: [{}], exception: [{}]",
                        toHexString(contextAddress),
                        result.getException());

                internalTx.reject();
                result.rejectInternalTransactions();

                track.rollback();
                stackPushZero();

                if (result.getException() != null) {
                    return;
                }
            } else {
                // 4. THE FLAG OF SUCCESS IS ONE PUSHED INTO THE STACK
                track.commit();
                stackPushOne();
            }

            if (byTestingSuite()) {
                logger.info("Testing executeContract, skipping storage diff listener");
            } else if (Arrays.equals(transaction.getReceiveAddress(), internalTx.getReceiveAddress())) {
                storageDiffListener.merge(program.getStorageDiff());
            }
        } else {
            // 4. THE FLAG OF SUCCESS IS ONE PUSHED INTO THE STACK
            track.commit();
            stackPushOne();
        }

        // 3. APPLY RESULTS: result.getHReturn() into out_memory allocated
        if (result != null) {
            byte[] buffer = result.getHReturn();
            int offset = msg.getOutDataOffs().intValue();
            int size = msg.getOutDataSize().intValue();

            memorySaveLimited(offset, buffer, size);

            returnDataBuffer = buffer;
        }

        // 5. REFUND THE REMAIN GAS
        if (result != null) {
            BigInteger refundGas = msg.getGas().value().subtract(toBI(result.getGasUsed()));
            if (isPositive(refundGas)) {
                refundGas(refundGas.longValue(), "remaining gas from the internal call");
                if (logger.isInfoEnabled()) {
                    logger.info("The remaining gas refunded, account: [{}], gas: [{}] ",
                            toHexString(senderAddress),
                            refundGas.toString());
                }
            }
        } else {
            refundGas(msg.getGas().longValue(), "remaining gas from the internal call");
        }
    }

    /**
     * Spend gas.
     *
     * @param gasValue the gas value
     * @param cause    the cause
     */
    public void spendGas(long gasValue, String cause) {
        if ( logger.isDebugEnabled()) {
            logger.debug("[{}] Spent for cause: [{}], gas: [{}]", invoke.hashCode(), cause, gasValue);
        }

        if (getGasLong() < gasValue) {
            throw Program.Exception.notEnoughSpendingGas(cause, gasValue, this);
        }
        getResult().spendGas(gasValue);
    }

    /**
     * Spend all gas.
     */
    public void spendAllGas() {
        spendGas(getGas().longValue(), "Spending all remaining");
    }

    /**
     * Refund gas.
     *
     * @param gasValue the gas value
     * @param cause    the cause
     */
    public void refundGas(long gasValue, String cause) {
        logger.info("[{}] Refund for cause: [{}], gas: [{}]", invoke.hashCode(), cause, gasValue);
        getResult().refundGas(gasValue);
    }

    /**
     * Future refund gas.
     *
     * @param gasValue the gas value
     */
    public void futureRefundGas(long gasValue) {
        logger.info("Future refund added: [{}]", gasValue);
        getResult().addFutureRefund(gasValue);
    }

    /**
     * Reset future refund.
     */
    public void resetFutureRefund() {
        getResult().resetFutureRefund();
    }

    /**
     * Storage save.
     *
     * @param word1 the word 1
     * @param word2 the word 2
     */
    public void storageSave(DataWord word1, DataWord word2) {
        storageSave(word1.getData(), word2.getData());
    }

    /**
     * Storage save.
     *
     * @param key the key
     * @param val the val
     */
    public void storageSave(byte[] key, byte[] val) {
        DataWord keyWord = new DataWord(key);
        DataWord valWord = new DataWord(val);
        getStorage().addStorageRow(getOwnerAddress().getLast20Bytes(), keyWord, valWord);
    }

    /**
     * Get code byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getCode() {
        return ops;
    }

    /**
     * Get code at byte [ ].
     *
     * @param address the address
     * @return the byte [ ]
     */
    public byte[] getCodeAt(DataWord address) {
        byte[] code = invoke.getRepository().getCode(address.getLast20Bytes());
        return nullToEmpty(code);
    }

    /**
     * Gets owner address.
     *
     * @return the owner address
     */
    public DataWord getOwnerAddress() {
        return invoke.getOwnerAddress().clone();
    }

    /**
     * Gets block hash.
     *
     * @param index the index
     * @return the block hash
     */
    public DataWord getBlockHash(int index) {
        return index < this.getNumber().longValue() && index >= Math.max(256, this.getNumber().intValue()) - 256 ?
                new DataWord(this.invoke.getBlockStore().getBlockHashByNumber(index, getPrevHash().getData())).clone() :
                DataWord.ZERO.clone();
    }

    /**
     * Gets balance.
     *
     * @param address the address
     * @return the balance
     */
    public DataWord getBalance(DataWord address) {
        BigInteger balance = getStorage().getBalance(address.getLast20Bytes());
        return new DataWord(balance.toByteArray());
    }

    /**
     * Gets origin address.
     *
     * @return the origin address
     */
    public DataWord getOriginAddress() {
        return invoke.getOriginAddress().clone();
    }

    /**
     * Gets caller address.
     *
     * @return the caller address
     */
    public DataWord getCallerAddress() {
        return invoke.getCallerAddress().clone();
    }

    /**
     * Gets gas price.
     *
     * @return the gas price
     */
    public DataWord getGasPrice() {
        return invoke.getMinGasPrice().clone();
    }

    /**
     * Gets gas long.
     *
     * @return the gas long
     */
    public long getGasLong() {
        return invoke.getGasLong() - getResult().getGasUsed();
    }

    /**
     * Gets gas.
     *
     * @return the gas
     */
    public DataWord getGas() {
        return new DataWord(invoke.getGasLong() - getResult().getGasUsed());
    }

    /**
     * Gets call value.
     *
     * @return the call value
     */
    public DataWord getCallValue() {
        return invoke.getCallValue().clone();
    }

    /**
     * Gets data size.
     *
     * @return the data size
     */
    public DataWord getDataSize() {
        return invoke.getDataSize().clone();
    }

    /**
     * Gets data value.
     *
     * @param index the index
     * @return the data value
     */
    public DataWord getDataValue(DataWord index) {
        return invoke.getDataValue(index);
    }

    /**
     * Get data copy byte [ ].
     *
     * @param offset the offset
     * @param length the length
     * @return the byte [ ]
     */
    public byte[] getDataCopy(DataWord offset, DataWord length) {
        return invoke.getDataCopy(offset, length);
    }

    /**
     * Gets return data buffer size.
     *
     * @return the return data buffer size
     */
    public DataWord getReturnDataBufferSize() {
        return new DataWord(getReturnDataBufferSizeI());
    }

    private int getReturnDataBufferSizeI() {
        return returnDataBuffer == null ? 0 : returnDataBuffer.length;
    }

    /**
     * Get return data buffer data byte [ ].
     *
     * @param off  the off
     * @param size the size
     * @return the byte [ ]
     */
    public byte[] getReturnDataBufferData(DataWord off, DataWord size) {
        if ((long) off.intValueSafe() + size.intValueSafe() > getReturnDataBufferSizeI()) {
            return null;
        }
        return returnDataBuffer == null ? new byte[0] :
                Arrays.copyOfRange(returnDataBuffer, off.intValueSafe(), off.intValueSafe() + size.intValueSafe());
    }

    /**
     * Storage load data word.
     *
     * @param key the key
     * @return the data word
     */
    public DataWord storageLoad(DataWord key) {
        DataWord ret = getStorage().getStorageValue(getOwnerAddress().getLast20Bytes(), key.clone());
        return ret == null ? null : ret.clone();
    }

    /**
     * Gets prev hash.
     *
     * @return the prev hash
     */
    public DataWord getPrevHash() {
        return invoke.getPrevHash().clone();
    }

    /**
     * Gets coinbase.
     *
     * @return the coinbase
     */
    public DataWord getCoinbase() {
        return invoke.getCoinbase().clone();
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public DataWord getTimestamp() {
        return invoke.getTimestamp().clone();
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public DataWord getNumber() {
        return invoke.getNumber().clone();
    }

    /**
     * Gets blockchain config.
     *
     * @return the blockchain config
     */
    public BlockChainConfig getBlockchainConfig() {
        return blockchainConfig;
    }

    /**
     * Gets difficulty.
     *
     * @return the difficulty
     */
    public DataWord getDifficulty() {
        return invoke.getDifficulty().clone();
    }

    /**
     * Gets gas limit.
     *
     * @return the gas limit
     */
    public DataWord getGasLimit() {
        return invoke.getGaslimit().clone();
    }

    /**
     * Is static call boolean.
     *
     * @return the boolean
     */
    public boolean isStaticCall() {
        return invoke.isStaticCall();
    }

    /**
     * Gets result.
     *
     * @return the result
     */
    public ProgramResult getResult() {
        return result;
    }

    /**
     * Sets runtime failure.
     *
     * @param e the e
     */
    public void setRuntimeFailure(RuntimeException e) {
        getResult().setException(e);
    }

    /**
     * Memory to string string.
     *
     * @return the string
     */
    public String memoryToString() {
        return memory.toString();
    }

    /**
     * Full trace.
     */
    public void fullTrace() {

        if (logger.isTraceEnabled() || listener != null) {

            StringBuilder stackData = new StringBuilder();
            for (int i = 0; i < stack.size(); ++i) {
                stackData.append(" ").append(stack.get(i));
                if (i < stack.size() - 1) {
                    stackData.append("\n");
                }
            }

            if (stackData.length() > 0) {
                stackData.insert(0, "\n");
            }

            ContractDetails contractDetails = getStorage().
                    getContractDetails(getOwnerAddress().getLast20Bytes());
            StringBuilder storageData = new StringBuilder();
            if (contractDetails != null) {
                try {
                    List<DataWord> storageKeys = new ArrayList<>(contractDetails.getStorage().keySet());
                    Collections.sort(storageKeys);
                    for (DataWord key : storageKeys) {
                        storageData.append(" ").append(key).append(" -> ").
                                append(contractDetails.getStorage().get(key)).append("\n");
                    }
                    if (storageData.length() > 0) {
                        storageData.insert(0, "\n");
                    }
                } catch (java.lang.Exception e) {
                    storageData.append("Failed to print storage: ").append(e.getMessage());
                }
            }

            StringBuilder memoryData = new StringBuilder();
            StringBuilder oneLine = new StringBuilder();
            if (memory.size() > 320) {
                memoryData.append("... Memory Folded.... ")
                        .append("(")
                        .append(memory.size())
                        .append(") bytes");
            } else {
                for (int i = 0; i < memory.size(); ++i) {

                    byte value = memory.readByte(i);
                    oneLine.append(ByteUtil.oneByteToHexString(value)).append(" ");

                    if ((i + 1) % 16 == 0) {
                        String tmp = format("[%4s]-[%4s]", Integer.toString(i - 15, 16),
                                Integer.toString(i, 16)).replace(" ", "0");
                        memoryData.append("").append(tmp).append(" ");
                        memoryData.append(oneLine);
                        if (i < memory.size()) {
                            memoryData.append("\n");
                        }
                        oneLine.setLength(0);
                    }
                }
            }
            if (memoryData.length() > 0) {
                memoryData.insert(0, "\n");
            }

            StringBuilder opsString = new StringBuilder();
            for (int i = 0; i < ops.length; ++i) {

                String tmpString = Integer.toString(ops[i] & 0xFF, 16);
                tmpString = tmpString.length() == 1 ? "0" + tmpString : tmpString;

                if (i != pc) {
                    opsString.append(tmpString);
                } else {
                    opsString.append(" >>").append(tmpString).append("");
                }

            }
            if (pc >= ops.length) {
                opsString.append(" >>");
            }
            if (opsString.length() > 0) {
                opsString.insert(0, "\n ");
            }

            logger.trace(" -- OPS --     {}", opsString);
            logger.trace(" -- STACK --   {}", stackData);
            logger.trace(" -- MEMORY --  {}", memoryData);
            logger.trace(" -- STORAGE -- {}\n", storageData);
            logger.trace("\n  Spent Gas: [{}]/[{}]\n  Left Gas:  [{}]\n",
                    getResult().getGasUsed(),
                    invoke.getGas().longValue(),
                    getGas().longValue());

            StringBuilder globalOutput = new StringBuilder("\n");
            if (stackData.length() > 0) {
                stackData.append("\n");
            }

            if (pc != 0) {
                globalOutput.append("[Op: ").append(OpCode.code(lastOp).name()).append("]\n");
            }

            globalOutput.append(" -- OPS --     ").append(opsString).append("\n");
            globalOutput.append(" -- STACK --   ").append(stackData).append("\n");
            globalOutput.append(" -- MEMORY --  ").append(memoryData).append("\n");
            globalOutput.append(" -- STORAGE -- ").append(storageData).append("\n");

            if (getResult().getHReturn() != null) {
                globalOutput.append("\n  HReturn: ").append(
                        Hex.toHexString(getResult().getHReturn()));
            }

            // sophisticated assumption that msg.data != codedata
            // means we are calling the contract not creating it
            byte[] txData = invoke.getDataCopy(DataWord.ZERO, getDataSize());
            if (!Arrays.equals(txData, ops)) {
                globalOutput.append("\n  msg.data: ").append(Hex.toHexString(txData));
            }
            globalOutput.append("\n\n  Spent Gas: ").append(getResult().getGasUsed());

            if (listener != null) {
                listener.output(globalOutput.toString());
            }
        }
    }

    /**
     * Save op trace.
     */
    public void saveOpTrace() {
        if (this.pc < ops.length) {
            trace.addOp(ops[pc], pc, getCallDeep(), getGas(), traceListener.resetActions());
        }
    }

    /**
     * Gets trace.
     *
     * @return the trace
     */
    public ProgramTrace getTrace() {
        return trace;
    }

    /**
     * Format bin data string.
     *
     * @param binData the bin data
     * @param startPC the start pc
     * @return the string
     */
    static String formatBinData(byte[] binData, int startPC) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < binData.length; i += 16) {
            ret.append(Utils.align("" + Integer.toHexString(startPC + (i)) + ":", ' ', 8, false));
            ret.append(Hex.toHexString(binData, i, min(16, binData.length - i))).append('\n');
        }
        return ret.toString();
    }

    /**
     * Stringify multiline string.
     *
     * @param code the code
     * @return the string
     */
    public static String stringifyMultiline(byte[] code) {
        int index = 0;
        StringBuilder sb = new StringBuilder();
        BitSet mask = buildReachableBytecodesMask(code);
        ByteArrayOutputStream binData = new ByteArrayOutputStream();
        int binDataStartPC = -1;

        while (index < code.length) {
            final byte opCode = code[index];
            OpCode op = OpCode.code(opCode);

            if (!mask.get(index)) {
                if (binDataStartPC == -1) {
                    binDataStartPC = index;
                }
                binData.write(code[index]);
                index++;
                if (index < code.length) {
                    continue;
                }
            }

            if (binDataStartPC != -1) {
                sb.append(formatBinData(binData.toByteArray(), binDataStartPC));
                binDataStartPC = -1;
                binData = new ByteArrayOutputStream();
                if (index == code.length) {
                    continue;
                }
            }

            sb.append(Utils.align("" + Integer.toHexString(index) + ":", ' ', 8, false));

            if (op == null) {
                sb.append("<UNKNOWN>: ").append(0xFF & opCode).append("\n");
                index++;
                continue;
            }

            if (op.name().startsWith("PUSH")) {
                sb.append(' ').append(op.name()).append(' ');

                int nPush = op.val() - OpCode.PUSH1.val() + 1;
                byte[] data = Arrays.copyOfRange(code, index + 1, index + nPush + 1);
                BigInteger bi = new BigInteger(1, data);
                sb.append("0x").append(bi.toString(16));
                if (bi.bitLength() <= 32) {
                    sb.append(" (").append(new BigInteger(1, data).toString()).append(") ");
                }

                index += nPush + 1;
            } else {
                sb.append(' ').append(op.name());
                index++;
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    /**
     * The type Byte code iterator.
     */
    static class ByteCodeIterator {
        /**
         * The Code.
         */
        byte[] code;
        /**
         * The Pc.
         */
        int pc;

        /**
         * Instantiates a new Byte code iterator.
         *
         * @param code the code
         */
        public ByteCodeIterator(byte[] code) {
            this.code = code;
        }

        /**
         * Sets pc.
         *
         * @param pc the pc
         */
        public void setPC(int pc) {
            this.pc = pc;
        }

        /**
         * Gets pc.
         *
         * @return the pc
         */
        public int getPC() {
            return pc;
        }

        /**
         * Gets cur opcode.
         *
         * @return the cur opcode
         */
        public OpCode getCurOpcode() {
            return pc < code.length ? OpCode.code(code[pc]) : null;
        }

        /**
         * Is push boolean.
         *
         * @return the boolean
         */
        public boolean isPush() {
            return getCurOpcode() != null ? getCurOpcode().name().startsWith("PUSH") : false;
        }

        /**
         * Get cur opcode arg byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] getCurOpcodeArg() {
            if (isPush()) {
                int nPush = getCurOpcode().val() - OpCode.PUSH1.val() + 1;
                byte[] data = Arrays.copyOfRange(code, pc + 1, pc + nPush + 1);
                return data;
            } else {
                return new byte[0];
            }
        }

        /**
         * Next boolean.
         *
         * @return the boolean
         */
        public boolean next() {
            pc += 1 + getCurOpcodeArg().length;
            return pc < code.length;
        }
    }

    /**
     * Build reachable bytecodes mask bit set.
     *
     * @param code the code
     * @return the bit set
     */
    static BitSet buildReachableBytecodesMask(byte[] code) {
        NavigableSet<Integer> gotos = new TreeSet<>();
        ByteCodeIterator it = new ByteCodeIterator(code);
        BitSet ret = new BitSet(code.length);
        int lastPush = 0;
        int lastPushPC = 0;
        do {
            // reachable bytecode
            ret.set(it.getPC());
            if (it.isPush()) {
                lastPush = new BigInteger(1, it.getCurOpcodeArg()).intValue();
                lastPushPC = it.getPC();
            }
            if (it.getCurOpcode() == OpCode.JUMP || it.getCurOpcode() == OpCode.JUMPI) {
                if (it.getPC() != lastPushPC + 1) {
                    // some PC arithmetic we totally can't deal with
                    // assuming all bytecodes are reachable as a fallback
                    ret.set(0, code.length);
                    return ret;
                }
                int jumpPC = lastPush;
                if (!ret.get(jumpPC)) {
                    // code was not explored yet
                    gotos.add(jumpPC);
                }
            }
            if (it.getCurOpcode() == OpCode.JUMP || it.getCurOpcode() == OpCode.RETURN ||
                    it.getCurOpcode() == OpCode.STOP) {
                if (gotos.isEmpty()) {
                    break;
                }
                it.setPC(gotos.pollFirst());
            }
        } while (it.next());
        return ret;
    }

    /**
     * Stringify string.
     *
     * @param code the code
     * @return the string
     */
    public static String stringify(byte[] code) {
        int index = 0;
        StringBuilder sb = new StringBuilder();
        BitSet mask = buildReachableBytecodesMask(code);
        String binData = "";

        while (index < code.length) {
            final byte opCode = code[index];
            OpCode op = OpCode.code(opCode);

            if (op == null) {
                sb.append(" <UNKNOWN>: ").append(0xFF & opCode).append(" ");
                index++;
                continue;
            }

            if (op.name().startsWith("PUSH")) {
                sb.append(' ').append(op.name()).append(' ');

                int nPush = op.val() - OpCode.PUSH1.val() + 1;
                byte[] data = Arrays.copyOfRange(code, index + 1, index + nPush + 1);
                BigInteger bi = new BigInteger(1, data);
                sb.append("0x").append(bi.toString(16)).append(" ");

                index += nPush + 1;
            } else {
                sb.append(' ').append(op.name());
                index++;
            }
        }

        return sb.toString();
    }

    /**
     * Add listener.
     *
     * @param listener the listener
     */
    public void addListener(ProgramOutListener listener) {
        this.listener = listener;
    }

    /**
     * Verify jump dest int.
     *
     * @param nextPC the next pc
     * @return the int
     */
    public int verifyJumpDest(DataWord nextPC) {
        if (nextPC.bytesOccupied() > 4) {
            throw Program.Exception.badJumpDestination(-1);
        }
        int ret = nextPC.intValue();
        if (!getProgramPrecompile().hasJumpDest(ret)) {
            throw Program.Exception.badJumpDestination(ret);
        }
        return ret;
    }

    /**
     * Call to precompiled address.
     *
     * @param msg      the msg
     * @param contract the contract
     */
    public void callToPrecompiledAddress(MessageCall msg, PrecompiledContracts.PrecompiledContract contract) {
        // reset return buffer right before the call
        returnDataBuffer = null;

        if (getCallDeep() == MAX_DEPTH) {
            stackPushZero();
            this.refundGas(msg.getGas().longValue(), " call deep limit reach");
            return;
        }

        Repository track = getStorage().startTracking();

        byte[] senderAddress = this.getOwnerAddress().getLast20Bytes();
        byte[] codeAddress = msg.getCodeAddress().getLast20Bytes();
        byte[] contextAddress = msg.getType().callIsStateless() ? senderAddress : codeAddress;


        BigInteger endowment = msg.getEndowment().value();
        BigInteger senderBalance = track.getBalance(senderAddress);
        if (senderBalance.compareTo(endowment) < 0) {
            stackPushZero();
            this.refundGas(msg.getGas().longValue(), "refund gas from message call");
            return;
        }

        byte[] data = this.memoryChunk(msg.getInDataOffs().intValue(),
                msg.getInDataSize().intValue());

        // Charge for endowment - is not reversible by rollback
        transfer(track, senderAddress, contextAddress, msg.getEndowment().value());

        if (byTestingSuite()) {
            // This keeps track of the calls created for a test
            this.getResult().addCallCreate(data,
                    msg.getCodeAddress().getLast20Bytes(),
                    msg.getGas().getNoLeadZeroesData(),
                    msg.getEndowment().getNoLeadZeroesData());

            stackPushOne();
            return;
        }

        long requiredGas = contract.getGasForData(data);
        if (requiredGas > msg.getGas().longValue()) {
            //matches cpp logic
            this.refundGas(0, "call pre-compiled");
            this.stackPushZero();
            track.rollback();
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Call {}(data = {})", contract.getClass().getSimpleName(), toHexString(data));
            }
            contract.setExtendsParamMap(transaction.getExtendsParamMap());
            Pair<Boolean, byte[]> out = contract.execute(data);
            // success
            if (out.getLeft()) {
                this.refundGas(msg.getGas().longValue() - requiredGas, "call pre-compiled");
                this.stackPushOne();
                returnDataBuffer = out.getRight();
                track.commit();
            } else {
                // spend all gas on failure, push zero and revert state changes
                this.refundGas(0, "call pre-compiled");
                this.stackPushZero();
                track.rollback();
            }

            this.memorySave(msg.getOutDataOffs().intValue(), out.getRight());
        }
    }

    /**
     * By testing suite boolean.
     *
     * @return the boolean
     */
    public boolean byTestingSuite() {
        return invoke.byTestingSuite();
    }

    /**
     * The interface Program out listener.
     */
    public interface ProgramOutListener {
        /**
         * Output.
         *
         * @param out the out
         */
        void output(String out);
    }

    /**
     * Denotes problem when executing Ethereum bytecode.
     * From blockchain and peer perspective this is quite normal situation
     * and doesn't mean exceptional situation in terms of the program execution
     */
    @SuppressWarnings("serial")
    public static class BytecodeExecutionException extends RuntimeException {
        /**
         * Instantiates a new Bytecode execution exception.
         *
         * @param message the message
         */
        public BytecodeExecutionException(String message) {
            super(message);
        }
    }

    /**
     * The type Out of gas exception.
     */
    @SuppressWarnings("serial")
    public static class OutOfGasException extends BytecodeExecutionException {

        /**
         * Instantiates a new Out of gas exception.
         *
         * @param message the message
         * @param args    the args
         */
        public OutOfGasException(String message, Object... args) {
            super(format(message, args));
        }
    }

    /**
     * The type Illegal operation exception.
     */
    @SuppressWarnings("serial")
    public static class IllegalOperationException extends BytecodeExecutionException {

        /**
         * Instantiates a new Illegal operation exception.
         *
         * @param message the message
         * @param args    the args
         */
        public IllegalOperationException(String message, Object... args) {
            super(format(message, args));
        }
    }

    /**
     * The type Bad jump destination exception.
     */
    @SuppressWarnings("serial")
    public static class BadJumpDestinationException extends BytecodeExecutionException {

        /**
         * Instantiates a new Bad jump destination exception.
         *
         * @param message the message
         * @param args    the args
         */
        public BadJumpDestinationException(String message, Object... args) {
            super(format(message, args));
        }
    }

    /**
     * The type Stack too small exception.
     */
    @SuppressWarnings("serial")
    public static class StackTooSmallException extends BytecodeExecutionException {

        /**
         * Instantiates a new Stack too small exception.
         *
         * @param message the message
         * @param args    the args
         */
        public StackTooSmallException(String message, Object... args) {
            super(format(message, args));
        }
    }

    /**
     * The type Return data copy illegal bounds exception.
     */
    @SuppressWarnings("serial")
    public static class ReturnDataCopyIllegalBoundsException extends BytecodeExecutionException {
        /**
         * Instantiates a new Return data copy illegal bounds exception.
         *
         * @param off            the off
         * @param size           the size
         * @param returnDataSize the return data size
         */
        public ReturnDataCopyIllegalBoundsException(DataWord off, DataWord size, long returnDataSize) {
            super(String.format("Illegal RETURNDATACOPY arguments: offset (%s) + size (%s) > RETURNDATASIZE (%d)", off, size, returnDataSize));
        }
    }

    /**
     * The type Static call modification exception.
     */
    @SuppressWarnings("serial")
    public static class StaticCallModificationException extends BytecodeExecutionException {
        /**
         * Instantiates a new Static call modification exception.
         */
        public StaticCallModificationException() {
            super("Attempt to call a state modifying opcode inside STATICCALL");
        }
    }

    /**
     * The type Exception.
     */
    public static class Exception {

        /**
         * Not enough op gas out of gas exception.
         *
         * @param op         the op
         * @param opGas      the op gas
         * @param programGas the program gas
         * @return the out of gas exception
         */
        public static OutOfGasException notEnoughOpGas(OpCode op, long opGas, long programGas) {
            return new OutOfGasException("Not enough gas for '%s' operation executing: opGas[%d], programGas[%d];", op, opGas, programGas);
        }

        /**
         * Not enough op gas out of gas exception.
         *
         * @param op         the op
         * @param opGas      the op gas
         * @param programGas the program gas
         * @return the out of gas exception
         */
        public static OutOfGasException notEnoughOpGas(OpCode op, DataWord opGas, DataWord programGas) {
            return notEnoughOpGas(op, opGas.longValue(), programGas.longValue());
        }

        /**
         * Not enough op gas out of gas exception.
         *
         * @param op         the op
         * @param opGas      the op gas
         * @param programGas the program gas
         * @return the out of gas exception
         */
        public static OutOfGasException notEnoughOpGas(OpCode op, BigInteger opGas, BigInteger programGas) {
            return notEnoughOpGas(op, opGas.longValue(), programGas.longValue());
        }

        /**
         * Not enough spending gas out of gas exception.
         *
         * @param cause    the cause
         * @param gasValue the gas value
         * @param program  the program
         * @return the out of gas exception
         */
        public static OutOfGasException notEnoughSpendingGas(String cause, long gasValue, Program program) {
            return new OutOfGasException("Not enough gas for '%s' cause spending: invokeGas[%d], gas[%d], usedGas[%d];",
                    cause, program.invoke.getGas().longValue(), gasValue, program.getResult().getGasUsed());
        }

        /**
         * Gas overflow out of gas exception.
         *
         * @param actualGas the actual gas
         * @param gasLimit  the gas limit
         * @return the out of gas exception
         */
        public static OutOfGasException gasOverflow(BigInteger actualGas, BigInteger gasLimit) {
            return new OutOfGasException("Gas value overflow: actualGas[%d], gasLimit[%d];", actualGas.longValue(), gasLimit.longValue());
        }

        /**
         * Invalid op code illegal operation exception.
         *
         * @param opCode the op code
         * @return the illegal operation exception
         */
        public static IllegalOperationException invalidOpCode(byte... opCode) {
            return new IllegalOperationException("Invalid operation code: opCode[%s];", Hex.toHexString(opCode, 0, 1));
        }

        /**
         * Bad jump destination bad jump destination exception.
         *
         * @param pc the pc
         * @return the bad jump destination exception
         */
        public static BadJumpDestinationException badJumpDestination(int pc) {
            return new BadJumpDestinationException("Operation with pc isn't 'JUMPDEST': PC[%d];", pc);
        }

        /**
         * Too small stack stack too small exception.
         *
         * @param expectedSize the expected size
         * @param actualSize   the actual size
         * @return the stack too small exception
         */
        public static StackTooSmallException tooSmallStack(int expectedSize, int actualSize) {
            return new StackTooSmallException("Expected stack size %d but actual %d;", expectedSize, actualSize);
        }
    }

    /**
     * The type Stack too large exception.
     */
    @SuppressWarnings("serial")
    public class StackTooLargeException extends BytecodeExecutionException {
        /**
         * Instantiates a new Stack too large exception.
         *
         * @param message the message
         */
        public StackTooLargeException(String message) {
            super(message);
        }
    }

    /**
     * used mostly for testing reasons
     *
     * @return the byte [ ]
     */
    public byte[] getMemory() {
        return memory.read(0, memory.size());
    }

    /**
     * used mostly for testing reasons
     *
     * @param data the data
     */
    public void initMem(byte[] data) {
        this.memory.write(0, data, data.length, false);
    }


}
