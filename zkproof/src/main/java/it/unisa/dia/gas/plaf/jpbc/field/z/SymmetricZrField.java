package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Symmetric zr field.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class SymmetricZrField extends AbstractField<SymmetricZrElement> {
    /**
     * The Order.
     */
    protected BigInteger order;
    /**
     * The Half order.
     */
    protected BigInteger halfOrder;

    /**
     * The Nqr.
     */
    protected SymmetricZrElement nqr;
    /**
     * The Fixed length in bytes.
     */
    protected int fixedLengthInBytes;
    /**
     * The Two inverse.
     */
    protected BigInteger twoInverse;

    /**
     * Instantiates a new Symmetric zr field.
     *
     * @param order the order
     */
    public SymmetricZrField(BigInteger order) {
        this(new SecureRandom(), order, null);
    }

    /**
     * Instantiates a new Symmetric zr field.
     *
     * @param random the random
     * @param order  the order
     */
    public SymmetricZrField(SecureRandom random, BigInteger order) {
        this(random, order, null);
    }

    /**
     * Instantiates a new Symmetric zr field.
     *
     * @param order the order
     * @param nqr   the nqr
     */
    public SymmetricZrField(BigInteger order, BigInteger nqr) {
        this(new SecureRandom(), order, nqr);
    }

    /**
     * Instantiates a new Symmetric zr field.
     *
     * @param random the random
     * @param order  the order
     * @param nqr    the nqr
     */
    public SymmetricZrField(SecureRandom random, BigInteger order, BigInteger nqr) {
        super(random);
        this.order = order;
        this.orderIsOdd = BigIntegerUtils.isOdd(order);

        this.fixedLengthInBytes = (order.bitLength() + 7) / 8;

        this.twoInverse = BigIntegerUtils.TWO.modInverse(order);

        this.halfOrder = order.divide(BigInteger.valueOf(2));

        if (nqr != null)
            this.nqr = newElement().set(nqr);
    }


    public SymmetricZrElement newElement() {
        return new SymmetricZrElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public SymmetricZrElement getNqr() {
        if (nqr == null) {
            nqr = newElement();
            do {
                nqr.setToRandom();
            } while (nqr.isSqr());
        }
        
        return nqr.duplicate();
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public void setFromString(String str) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
