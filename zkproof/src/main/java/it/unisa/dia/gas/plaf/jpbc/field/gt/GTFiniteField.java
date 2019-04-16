package it.unisa.dia.gas.plaf.jpbc.field.gt;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractFieldOver;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.PairingMap;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Gt finite field.
 *
 * @param <F> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GTFiniteField<F extends Field> extends AbstractFieldOver<F, GTFiniteElement> {
    /**
     * The Pairing.
     */
    protected PairingMap pairing;
    /**
     * The Order.
     */
    protected BigInteger order;

    /**
     * Instantiates a new Gt finite field.
     *
     * @param random      the random
     * @param order       the order
     * @param pairing     the pairing
     * @param targetField the target field
     */
    public GTFiniteField(SecureRandom random, BigInteger order, PairingMap pairing, F targetField) {
        super(random, targetField);

        this.order = order;
        this.pairing = pairing;
    }

    
    public GTFiniteElement newElement() {
        return new GTFiniteElement(pairing, this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public GTFiniteElement getNqr() {
        throw new IllegalStateException("Not Implemented yet!");
    }

    public int getLengthInBytes() {
        return getTargetField().getLengthInBytes();
    }

    public void setFromString(String str) {
        throw new IllegalStateException("Not Implemented yet!");
    }

}
