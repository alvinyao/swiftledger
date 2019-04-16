package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Poly field.
 *
 * @param <F> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyField<F extends Field> extends AbstractFieldOver<F, PolyElement> {

    /**
     * Instantiates a new Poly field.
     *
     * @param random      the random
     * @param targetField the target field
     */
    public PolyField(SecureRandom random, F targetField) {
        super(random, targetField);
    }

    /**
     * Instantiates a new Poly field.
     *
     * @param targetField the target field
     */
    public PolyField(F targetField) {
        super(new SecureRandom(), targetField);
    }


    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public PolyElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public void setFromString(String str) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
