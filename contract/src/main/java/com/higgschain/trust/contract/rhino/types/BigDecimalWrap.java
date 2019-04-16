package com.higgschain.trust.contract.rhino.types;

import com.higgschain.trust.contract.rhino.function.MathFuncs;
import com.higgschain.trust.contract.rhino.function.MathNativeFunction;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The type Big decimal wrap.
 *
 * @author duhongming
 * @date 2018 /7/17
 */
public class BigDecimalWrap extends Number implements Scriptable {

    private BigDecimal rawBigDecimal;

    /**
     * Instantiates a new Big decimal wrap.
     *
     * @param bigDecimal the big decimal
     */
    public BigDecimalWrap(BigDecimal bigDecimal) {
        this.rawBigDecimal = bigDecimal;
    }

    /**
     * Instantiates a new Big decimal wrap.
     *
     * @param bigInteger the big integer
     */
    public BigDecimalWrap(BigInteger bigInteger) {
        this.rawBigDecimal = new BigDecimal(bigInteger);
    }

    /**
     * Instantiates a new Big decimal wrap.
     *
     * @param integer the integer
     */
    public BigDecimalWrap(Integer integer) {
        this.rawBigDecimal = new BigDecimal(integer);
    }

    /**
     * Instantiates a new Big decimal wrap.
     *
     * @param d the d
     */
    public BigDecimalWrap(Double d) {
        this.rawBigDecimal = new BigDecimal(d);
    }

    /**
     * Add big decimal.
     *
     * @param x the x
     * @return the big decimal
     */
    public BigDecimal add(Object x) {
        if (x instanceof BigDecimal) {
            return rawBigDecimal.add((BigDecimal) x);
        }
        return rawBigDecimal.add(new BigDecimal(x.toString()));
    }

    /**
     * Subtract big decimal.
     *
     * @param x the x
     * @return the big decimal
     */
    public BigDecimal subtract(Object x) {
        if (x instanceof BigDecimal) {
            return rawBigDecimal.subtract((BigDecimal) x);
        }
        return rawBigDecimal.subtract(new BigDecimal(x.toString()));
    }

    /**
     * Multiply big decimal.
     *
     * @param x the x
     * @return the big decimal
     */
    public BigDecimal multiply(Object x) {
        if (x instanceof BigDecimal) {
            return rawBigDecimal.multiply((BigDecimal) x);
        }
        return rawBigDecimal.multiply(new BigDecimal(x.toString()));
    }

    /**
     * Divide big decimal.
     *
     * @param x the x
     * @return the big decimal
     */
    public BigDecimal divide(Object x) {
        if (x instanceof BigDecimal) {
            return rawBigDecimal.divide((BigDecimal) x);
        }
        return rawBigDecimal.divide(new BigDecimal(x.toString()));
    }

    /**
     * equal
     *
     * @param x the x
     * @return boolean
     */
    public boolean eq(Object x) {
        BigDecimal bigDecimal = MathNativeFunction.toBigDecimal(x);
        return rawBigDecimal.compareTo(bigDecimal) == 0;
    }

    /**
     * greater than
     *
     * @param x the x
     * @return boolean
     */
    public boolean gt(Object x) {
        BigDecimal bigDecimal = MathNativeFunction.toBigDecimal(x);
        return rawBigDecimal.compareTo(bigDecimal) > 0;
    }

    /**
     * equal or greater than
     *
     * @param x the x
     * @return boolean
     */
    public boolean gte(Object x) {
        BigDecimal bigDecimal = MathNativeFunction.toBigDecimal(x);
        return rawBigDecimal.compareTo(bigDecimal) >= 0;
    }

    /**
     * less than
     *
     * @param x the x
     * @return boolean
     */
    public boolean lt(Object x) {
        BigDecimal bigDecimal = MathNativeFunction.toBigDecimal(x);
        return rawBigDecimal.compareTo(bigDecimal) < 0;
    }

    /**
     * equal or less than
     *
     * @param x the x
     * @return boolean
     */
    public boolean lte(Object x) {
        BigDecimal bigDecimal = MathNativeFunction.toBigDecimal(x);
        return rawBigDecimal.compareTo(bigDecimal) <= 0;
    }

    /**
     * Gets raw big decimal.
     *
     * @return the raw big decimal
     */
    public BigDecimal getRawBigDecimal() {
        return rawBigDecimal;
    }

    @Override
    public int intValue() {
        return rawBigDecimal.intValue();
    }

    @Override
    public long longValue() {
        return rawBigDecimal.longValue();
    }

    @Override
    public float floatValue() {
        return rawBigDecimal.floatValue();
    }

    @Override
    public double doubleValue() {
        return rawBigDecimal.doubleValue();
    }

    @Override
    public String toString() {
        return rawBigDecimal.toString();
    }


    // implements Scriptable

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public Object get(String name, Scriptable start) {
        return MathFuncs.getFuncByName(name);
    }

    @Override
    public Object get(int index, Scriptable start) {
        return Undefined.instance;
    }

    @Override
    public boolean has(String name, Scriptable start) {
        return false;
    }

    @Override
    public boolean has(int index, Scriptable start) {
        return false;
    }

    @Override
    public void put(String name, Scriptable start, Object value) {

    }

    @Override
    public void put(int index, Scriptable start, Object value) {

    }

    @Override
    public void delete(String name) {

    }

    @Override
    public void delete(int index) {

    }

    @Override
    public Scriptable getPrototype() {
        return null;
    }

    @Override
    public void setPrototype(Scriptable prototype) {

    }

    @Override
    public Scriptable getParentScope() {
        return null;
    }

    @Override
    public void setParentScope(Scriptable parent) {

    }

    @Override
    public Object[] getIds() {
        return new Object[0];
    }

    @Override
    public Object getDefaultValue(Class<?> hint) {
        return toString();
    }

    @Override
    public boolean hasInstance(Scriptable instance) {
        return false;
    }
}
