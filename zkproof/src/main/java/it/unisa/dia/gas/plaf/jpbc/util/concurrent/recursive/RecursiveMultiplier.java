package it.unisa.dia.gas.plaf.jpbc.util.concurrent.recursive;

import it.unisa.dia.gas.jpbc.Element;

import java.util.concurrent.RecursiveTask;

/**
 * The type Recursive multiplier.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class RecursiveMultiplier extends RecursiveTask<Element> {
    /**
     * The Sequential threshold.
     */
    static final int SEQUENTIAL_THRESHOLD = 2;

    /**
     * The Elements.
     */
    Element[] elements;
    /**
     * The Low.
     */
    int low;
    /**
     * The High.
     */
    int high;

    /**
     * Instantiates a new Recursive multiplier.
     *
     * @param elements the elements
     * @param lo       the lo
     * @param hi       the hi
     */
    public RecursiveMultiplier(Element[] elements, int lo, int hi) {
        this.elements = elements;
        this.low = lo;
        this.high = hi;
    }

    protected Element compute() {
        if (high == low) {
            return elements[low];
        }

        if (high - low < SEQUENTIAL_THRESHOLD) {
            return elements[low].mul(elements[high]);
        } else {
            int mid = low + (high - low) / 2;

            RecursiveMultiplier left = new RecursiveMultiplier(elements, low, mid);
            RecursiveMultiplier right = new RecursiveMultiplier(elements, mid + 1, high);
            left.fork();

            Element rightAns = right.compute();
            Element leftAns = left.join();
            return rightAns.mul(leftAns);
        }
    }

}

