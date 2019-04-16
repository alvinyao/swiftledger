package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Abstract vector element.
 *
 * @param <E> the type parameter
 * @param <F> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractVectorElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Vector<E> {

    /**
     * The Coeff.
     */
    protected List<E> coeff;

    /**
     * Instantiates a new Abstract vector element.
     *
     * @param field the field
     */
    protected AbstractVectorElement(F field) {
        super(field);

        this.coeff = new ArrayList<E>();
    }


    public E getAt(int index) {
        return coeff.get(index);
    }

    public int getSize() {
        return coeff.size();
    }

}