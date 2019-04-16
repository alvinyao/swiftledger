package com.higgschain.trust.contract;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Execute config.
 *
 * @author duhongming
 * @date 2018 /6/11
 */
public class ExecuteConfig {

    private final Set<String> allowedClasses;
    private int instructionCountQuota = 100000;

    /**
     * The constant DEBUG.
     */
    public static boolean DEBUG = false;

    /**
     * Instantiates a new Execute config.
     */
    public ExecuteConfig() {
        allowedClasses = new HashSet<>();
    }

    /**
     * Allow execute config.
     *
     * @param fullClassName the full class name
     * @return the execute config
     */
    public ExecuteConfig allow(final String fullClassName) {
        if (StringUtils.isEmpty(fullClassName)) {
            throw new IllegalArgumentException("fullClassName");
        }
        allowedClasses.add(fullClassName);
        return this;
    }

    /**
     * Allow execute config.
     *
     * @param clazz the clazz
     * @return the execute config
     */
    public ExecuteConfig allow(final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz");
        }
        this.allow(clazz.getName());
        return this;
    }

    /**
     * Gets allowed classes.
     *
     * @return the allowed classes
     */
    public  Set<String> getAllowedClasses() {
        return allowedClasses;
    }

    /**
     * get the instruction count quota
     *
     * @return instruction count quota
     */
    public int getInstructionCountQuota() {
        return instructionCountQuota;
    }

    /**
     * set the instruction count quota when contract executing,
     * throw QuotaExceededException if quota exceeded
     *
     * @param instructionCountQuota the instruction count quota
     * @return instruction count quota
     */
    public ExecuteConfig setInstructionCountQuota(final int instructionCountQuota) {
        this.instructionCountQuota = instructionCountQuota;
        return this;
    }
}