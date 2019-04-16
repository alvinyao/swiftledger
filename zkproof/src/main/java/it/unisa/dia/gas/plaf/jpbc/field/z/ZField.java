package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Z field.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZField extends AbstractField<ZElement> {

    /**
     * Instantiates a new Z field.
     */
    public ZField() {
        this(new SecureRandom());
    }

    /**
     * Instantiates a new Z field.
     *
     * @param random the random
     */
    public ZField(SecureRandom random) {
        super(random);
    }


    public ZElement newElement() {
        return new ZElement(this);
    }

    public BigInteger getOrder() {
        return BigInteger.ZERO;
    }

    public ZElement getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return -1;
    }

    public void setFromString(String str) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
