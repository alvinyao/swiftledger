package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Quadratic field.
 *
 * @param <F> the type parameter
 * @param <E> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class QuadraticField<F extends Field, E extends QuadraticElement> extends AbstractFieldOver<F, E> {
    /**
     * The Order.
     */
    protected BigInteger order;
    /**
     * The Fixed length in bytes.
     */
    protected int fixedLengthInBytes;

    /**
     * Instantiates a new Quadratic field.
     *
     * @param random      the random
     * @param targetField the target field
     */
    public QuadraticField(SecureRandom random, F targetField) {
        super(random, targetField);

        this.order = targetField.getOrder().multiply(targetField.getOrder());

        if (targetField.getLengthInBytes() < 0) {
            //f->length_in_bytes = fq_length_in_bytes;
            fixedLengthInBytes = -1;
        } else {
            fixedLengthInBytes = 2 * targetField.getLengthInBytes();
        }
    }


    public E newElement() {
        return (E) new QuadraticElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public E getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public void setFromString(String str) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}