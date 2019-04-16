package com.higgschain.trust.evmcontract.config;


import com.higgschain.trust.evmcontract.vm.DataWord;
import com.higgschain.trust.evmcontract.vm.GasCost;
import com.higgschain.trust.evmcontract.vm.OpCode;
import com.higgschain.trust.evmcontract.vm.program.Program;
import org.apache.commons.lang3.StringUtils;

/**
 * The type System properties.
 *
 * @author tangkun
 * @date 2018 -09-06
 */
public class SystemProperties {

    private static SystemProperties CONFIG;
    private static boolean useOnlySpringConfig = false;
    private static final GasCost GAS_COST = new GasCost();

    /**
     * Returns the static config instance. If the config is passed
     * as a Spring bean by the application this instance shouldn't
     * be used
     * This method is mainly used for testing purposes
     * (Autowired fields are initialized with this static instance
     * but when running within Spring context they replaced with the
     * bean config instance)
     *
     * @return the default
     */
    public static SystemProperties getDefault() {
        return useOnlySpringConfig ? null : getSpringDefault();
    }

    /**
     * Gets spring default.
     *
     * @return the spring default
     */
    static SystemProperties getSpringDefault() {
        if (CONFIG == null) {
            CONFIG = new SystemProperties();
        }
        return CONFIG;
    }

    private String databaseDir = null;

    /**
     * Vm trace dir string.
     *
     * @return the string
     */
    public String vmTraceDir() {
//        return System.getProperty("vm.structured.dir");
        return "trace";
    }

    /**
     * Dump block int.
     *
     * @return the int
     */
    public int dumpBlock() {
//        return System.getProperty("dump.block");
        return 0;
    }

    /**
     * Dump style string.
     *
     * @return the string
     */
    public String dumpStyle() {
//        return config.getString("dump.style");
        return "pretty";
    }

    /**
     * Vm trace boolean.
     *
     * @return the boolean
     */
    public boolean vmTrace() {
//        return Boolean.valueOf(System.getProperty("vm.structured.trace"));
        return true;
    }

    /**
     * Database dir string.
     *
     * @return the string
     */
    public String databaseDir() {
        if (this.databaseDir != null) {
            return this.databaseDir;
        }

        String dir = System.getProperty("database.dir");
        this.databaseDir = StringUtils.isNotEmpty(dir) ? dir : "db";
        return this.databaseDir ;
    }

    /**
     * Play vm boolean.
     *
     * @return the boolean
     */
    public boolean playVM() {
        return true;
    }

    /**
     * Gets gas cost.
     *
     * @return the gas cost
     */
    public static GasCost getGasCost() {
        return GAS_COST;
    }

    /**
     * Gets call gas.
     *
     * @param op           the op
     * @param requestedGas the requested gas
     * @param availableGas the available gas
     * @return the call gas
     * @throws OutOfGasException the out of gas exception
     */
    public DataWord getCallGas(OpCode op, DataWord requestedGas, DataWord availableGas) throws Program.OutOfGasException {
        if (requestedGas.compareTo(availableGas) > 0) {
            throw Program.Exception.notEnoughOpGas(op, requestedGas, availableGas);
        }
        return requestedGas.clone();
    }

    /**
     * Record internal transactions data boolean.
     *
     * @return the boolean
     */
    public boolean recordInternalTransactionsData() {
//        if (recordInternalTransactionsData == null) {
//            recordInternalTransactionsData = config.getBoolean("record.internal.transactions.data");
//        }
//        return recordInternalTransactionsData;
        return true;
    }

    /**
     * Gets blockchain config.
     *
     * @return the blockchain config
     */
    public BlockChainConfig getBlockchainConfig() {
        return new ByzantiumConfig();
    }

    /**
     * Gets crypto provider name.
     *
     * @return the crypto provider name
     */
    public String getCryptoProviderName() {
        return "SC";
    }

    /**
     * Gets hash 256 alg name.
     *
     * @return the hash 256 alg name
     */
    public String getHash256AlgName() {
        return "ETH-KECCAK-256";
    }

    /**
     * Gets hash 512 alg name.
     *
     * @return the hash 512 alg name
     */
    public String getHash512AlgName() {
        return "ETH-KECCAK-512";
    }

    /**
     * Custom solc path string.
     *
     * @return the string
     */
    public String customSolcPath() {
        String path = System.getProperty("solcPath");
        if (StringUtils.isNotEmpty(path)) {
            return path;
        }

        path = System.getenv("SolcPath");
        if (StringUtils.isNotEmpty(path)) {
            return path;
        }

//        return "/solc/solc.exe";
        return null;
    }
}

