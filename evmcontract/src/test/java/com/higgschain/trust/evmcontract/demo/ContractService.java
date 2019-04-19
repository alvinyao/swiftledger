package com.higgschain.trust.evmcontract.demo;

import com.higgschain.trust.evmcontract.config.SystemProperties;
import com.higgschain.trust.evmcontract.core.Block;
import com.higgschain.trust.evmcontract.core.Repository;
import com.higgschain.trust.evmcontract.core.Transaction;
import com.higgschain.trust.evmcontract.datasource.DbSource;
import com.higgschain.trust.evmcontract.datasource.rocksdb.RocksDbDataSource;
import com.higgschain.trust.evmcontract.db.BlockStore;
import com.higgschain.trust.evmcontract.db.RepositoryRoot;
import com.higgschain.trust.evmcontract.util.ByteUtil;
import com.higgschain.trust.evmcontract.vm.VM;
import com.higgschain.trust.evmcontract.vm.program.Program;
import com.higgschain.trust.evmcontract.vm.program.ProgramResult;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvoke;
import com.higgschain.trust.evmcontract.vm.program.invoke.ProgramInvokeImpl;
import org.apache.commons.io.IOUtils;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;

/**
 * The type Contract service.
 *
 * @author duhongming
 * @date 2018 /11/15
 */
public class ContractService {

    private SystemProperties config;
    private DbSource<byte[]> db;
    private RepositoryRoot repository;

    //private boolean initialized;

    private final  static byte[] gasPrice = Hex.decode("e8d4a51000");  // 1000000000000
    private final  static byte[] gas = Hex.decode("27100000000000");            // 10000

    /**
     * Instantiates a new Contract service.
     *
     * @param rootHash the root hash
     */
    public ContractService(byte[] rootHash) {
        this.config = SystemProperties.getDefault();
        this.db = new RocksDbDataSource("trust");
//        this.db = new HashMapDB<>();
        db.init();
        if (rootHash == null || rootHash.length == 0) {
            rootHash = db.get("RootHash".getBytes());
        }

        if (rootHash != null) {
            System.out.println("Get RootHash:" + Hex.toHexString(rootHash));
        }

        this.repository = new RepositoryRoot(db, rootHash);

//        this.repository.addBalance(Hex.decode("13434c369b163A16F969C15F965618a724b3a634"), new BigInteger("10000000"));
//        BigInteger balance = this.repository.getBalance(Hex.decode("13434c369b163A16F969C15F965618a724b3a634"));
//        System.out.println("balance: " + balance);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        byte[] root = Hex.decode("");
        ContractService contractService = new ContractService(root);
        byte[] contractAddress = contractService.createContract();
        contractService.commit();

        contractService.invoke(contractAddress);
        contractService.commit();


    }

    /**
     * Destroy.
     */
    public void destroy() {
        repository.close();
        db.close();
    }

    /**
     * Commit.
     */
    public void commit() {
        this.repository.commit();
//        String dumpStr = this.repository.dumpStateTrie();
//        System.out.println(dumpStr);
        System.out.printf("RootHash=%s\n", Hex.toHexString(repository.getRoot()));
        db.put("RootHash".getBytes(), repository.getRoot());
    }

    /**
     * Create contract byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] createContract() {
        byte[] nonce = BigIntegers.asUnsignedByteArray(BigInteger.ZERO);
        byte[] code = Hex.decode("608060405234801561001057600080fd5b5061016e806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063e7564f4414610046575b600080fd5b34801561005257600080fd5b5061005b61005d565b005b61006561011f565b61006d61011f565b68706f6c6963795f696460010281600060018110151561008957fe5b6020020190600019169081600019168152505060208260208360007f53544143530000000000000000000000000000000000000000000000000000016000f1507f206c99af80077bd66fda00313ef6a84748262ff79fed184db845e6d9e0f0b6078260006001811015156100f957fe5b602002015160405180826000191660001916815260200191505060405180910390a15050565b6020604051908101604052806001906020820280388339808201915050905050905600a165627a7a723058208f9effaf60833feac9e6e0f8feabcaeccfad3ebfada2e6437692f02c09b544f10029");
        byte[] receiveAddress = Hex.decode("");//13978aee95f38490e9769c39b2773ed763d9cd5f
        byte[] value = Hex.decode("");          //10000000000000000 2386f26fc10000"
        byte[] address = createContract(nonce, receiveAddress, value, code);
        System.out.printf("address=%s\n", Hex.toHexString(address));
        System.out.printf("RootHash=%s\n", Hex.toHexString(repository.getRoot()));
        return address;
    }

    /**
     * Invoke.
     *
     * @param contractAddress the contract address
     */
    public void invoke(byte[] contractAddress) {
        byte[] value = Hex.decode(""); //10000000000000000 2386f26fc10000"
        byte[] nonce = BigIntegers.asUnsignedByteArray(BigInteger.ZERO);

        //call contract
        byte[] data = Hex.decode("e7564f44");
        System.out.println("thread id:" + Thread.currentThread().getName());
        invokeContract(nonce, contractAddress, value, data);
    }

    /**
     * Create contract byte [ ].
     *
     * @param nonce          the nonce
     * @param receiveAddress the receive address
     * @param value          the value
     * @param data           the data
     * @return the byte [ ]
     */
    public byte[] createContract(byte[] nonce, byte[] receiveAddress, byte[] value, byte[] data) {
        Transaction tx = new Transaction(nonce, gasPrice, gas, receiveAddress, value, data);

        Block block = new Block();
        Repository txTrack = repository.startTracking();
        Repository contractTrack = txTrack.startTracking();

        ProgramInvoke programInvoke = createProgramInvoke(tx, block, contractTrack, null);
        Program program = new Program(tx.getData(), programInvoke, tx, SystemProperties.getDefault());

        VM vm = new VM(config);
        vm.play(program);

        ProgramResult result = program.getResult();
        if (!result.isRevert()) {
            contractTrack.saveCode(tx.getContractAddress(), result.getHReturn());
        }
        contractTrack.commit();
        txTrack.commit();
//        repository.commit();

//        System.out.println("合約result:" + Hex.toHexString(program.getResult().getHReturn()));
//        byte[] root = repository.getRoot();
//        System.out.println("RootHash:" + Hex.toHexString(root));
        return tx.getContractAddress();
    }

    /**
     * Invoke contract byte [ ].
     *
     * @param nonce           the nonce
     * @param contractAddress the contract address
     * @param value           the value
     * @param data            the data
     * @return the byte [ ]
     */
    public byte[] invokeContract(byte[] nonce, byte[] contractAddress, byte[] value, byte[] data) {
        Transaction tx = new Transaction(nonce, gasPrice, gas, contractAddress, value, data);

        Block block = new Block();
        Repository txTrack = repository.startTracking();
        Repository contractTrack = txTrack.startTracking();

        ProgramInvoke programInvoke = createProgramInvoke(tx, block, contractTrack, null);
        Program  program = new Program(contractTrack.getCode(contractAddress), programInvoke, tx, SystemProperties.getDefault());

        VM vm = new VM(config);
        vm.play(program);

        ProgramResult result = program.getResult();
        contractTrack.commit();
        txTrack.commit();
//        repository.commit();
//        System.out.println("合約result:" + Hex.toHexString(result.getHReturn()));
//        byte[] root = repository.getRoot();
//        System.out.println("RootHash:" + Hex.toHexString(root));
        return result.getHReturn();
    }

    /**
     * Create program invoke program invoke.
     *
     * @param tx         the tx
     * @param block      the block
     * @param repository the repository
     * @param blockStore the block store
     * @return the program invoke
     */
    public ProgramInvoke createProgramInvoke(Transaction tx, Block block, Repository repository,
                                             BlockStore blockStore) {
        /***         ADDRESS op       ***/
        // YP: Get address of currently executing account.
        byte[] address = tx.isContractCreation() ? tx.getContractAddress() : tx.getReceiveAddress();

        /***         ORIGIN op       ***/
        // YP: This is the sender of original transaction; it is never a contract.
        byte[] origin = tx.getSender();

        /***         CALLER op       ***/
        // YP: This is the address of the account that is directly responsible for this execution.
        byte[] caller = tx.getSender();

        /***         BALANCE op       ***/
        byte[] balance = repository.getBalance(address).toByteArray();

        /***         GASPRICE op       ***/
        byte[] gasPrice = tx.getGasPrice();

        /*** GAS op ***/
        byte[] gas = tx.getGasLimit();

        /***        CALLVALUE op      ***/
        byte[] callValue = nullToEmpty(tx.getValue());

        /***     CALLDATALOAD  op   ***/
        /***     CALLDATACOPY  op   ***/
        /***     CALLDATASIZE  op   ***/
        byte[] data = tx.isContractCreation() ? ByteUtil.EMPTY_BYTE_ARRAY : nullToEmpty(tx.getData());

        /***    PREVHASH  op  ***/
        byte[] lastHash = block.getParentHash();

        /***   COINBASE  op ***/
        byte[] coinbase = block.getCoinbase();

        /*** TIMESTAMP  op  ***/
        long timestamp = block.getTimestamp();

        /*** NUMBER  op  ***/
        long number = block.getNumber();

        /*** DIFFICULTY  op  ***/
        byte[] difficulty = block.getDifficulty();

        /*** GASLIMIT op ***/
        byte[] gaslimit = block.getGasLimit();


        return new ProgramInvokeImpl(address, origin, caller, balance, gasPrice, gas, callValue, data,
                lastHash, coinbase, timestamp, number, difficulty, gaslimit,
                repository, blockStore);
    }

    /**
     * Load code from resource file byte [ ].
     *
     * @param filePath the file path
     * @return the byte [ ]
     */
    public static byte[] loadCodeFromResourceFile(String filePath) {
        try {
            String code = IOUtils.toString(new File(filePath).toURI(), "UTF-8");
            return Hex.decode(code);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
