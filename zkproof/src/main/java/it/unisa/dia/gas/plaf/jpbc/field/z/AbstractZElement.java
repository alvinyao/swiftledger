package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractElement;
import it.unisa.dia.gas.plaf.jpbc.field.base.AbstractField;

import java.math.BigInteger;

/**
 * The type Abstract z element.
 *
 * @param <F> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractZElement<F extends AbstractField> extends AbstractElement<F> {

    /**
     * The Value.
     */
    public BigInteger value;

    /**
     * Instantiates a new Abstract z element.
     *
     * @param field the field
     */
    protected AbstractZElement(F field) {
        super(field);
    }
}
