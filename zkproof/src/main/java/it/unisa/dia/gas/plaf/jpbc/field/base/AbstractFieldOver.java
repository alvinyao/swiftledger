package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.FieldOver;

import java.security.SecureRandom;

/**
 * The type Abstract field over.
 *
 * @param <F> the type parameter
 * @param <E> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractFieldOver<F extends Field, E extends Element> extends AbstractField<E> implements FieldOver<F, E> {
    /**
     * The Target field.
     */
    protected F targetField;

    /**
     * Instantiates a new Abstract field over.
     *
     * @param random      the random
     * @param targetField the target field
     */
    protected AbstractFieldOver(SecureRandom random, F targetField) {
        super(random);
        this.targetField = targetField;
    }


    public F getTargetField() {
        return targetField;
    }

}
