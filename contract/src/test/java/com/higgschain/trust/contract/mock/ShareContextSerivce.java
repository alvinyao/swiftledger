package com.higgschain.trust.contract.mock;

import com.higgschain.trust.contract.ContractApiService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * The type Share context serivce.
 */
public class ShareContextSerivce extends ContractApiService {

    private ShareBlockSerivce blockSerivce;
    private Object ctxObj;

    /**
     * Instantiates a new Share context serivce.
     */
    public ShareContextSerivce() {
        blockSerivce = new ShareBlockSerivce();
    }

    /**
     * Add big decimal.
     *
     * @param x the x
     * @param y the y
     * @return the big decimal
     */
    public BigDecimal add(String x, String y) {
        System.out.println(y);
        return new BigDecimal(x).add(new BigDecimal(y));
    }

    /**
     * The Big integer.
     */
    public BigInteger bigInteger = new BigInteger("199999999999999999999999999999999999999");

    /**
     * Gets block serivce.
     *
     * @return the block serivce
     */
    public ShareBlockSerivce getBlockSerivce() {
        return blockSerivce;
    }

    /**
     * Gets admin.
     *
     * @return the admin
     */
    public Object getAdmin() {
        return getContext().getData("admin");
    }

    /**
     * Say hello string.
     *
     * @param name the name
     * @return the string
     */
    public String sayHello(String name) {
        return "Hello " + name + "  " + Thread.currentThread().getName();
    }

    /**
     * Gets ctx obj.
     *
     * @return the ctx obj
     */
    public Object getCtxObj() {
        return ctxObj;
    }

    /**
     * Sets ctx obj.
     *
     * @param ctxObj the ctx obj
     */
    public void setCtxObj(Object ctxObj) {
        this.ctxObj = ctxObj;
    }

    /**
     * Get max block height long.
     *
     * @return the long
     */
    public Long getMaxBlockHeight(){
        return 122344L;
    }

    /**
     * Get package time long.
     *
     * @return the long
     */
    public Long getPackageTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return calendar.getTimeInMillis();
    }
}
