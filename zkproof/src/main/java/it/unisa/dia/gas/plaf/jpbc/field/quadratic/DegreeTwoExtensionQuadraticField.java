package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Field;

import java.security.SecureRandom;

/**
 * The type Degree two extension quadratic field.
 *
 * @param <F> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class DegreeTwoExtensionQuadraticField<F extends Field> extends QuadraticField<F, DegreeTwoExtensionQuadraticElement> {

    /**
     * Instantiates a new Degree two extension quadratic field.
     *
     * @param random      the random
     * @param targetField the target field
     */
    public DegreeTwoExtensionQuadraticField(SecureRandom random, F targetField) {
        super(random, targetField);
    }


    public DegreeTwoExtensionQuadraticElement newElement() {
        return new DegreeTwoExtensionQuadraticElement(this);
    }

}
