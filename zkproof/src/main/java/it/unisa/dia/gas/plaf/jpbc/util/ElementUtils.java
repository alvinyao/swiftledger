package it.unisa.dia.gas.plaf.jpbc.util;

import it.unisa.dia.gas.jpbc.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Element utils.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ElementUtils {

    /**
     * Duplicate element [ ].
     *
     * @param source the source
     * @return the element [ ]
     */
    public static Element[] duplicate(Element[] source) {
        Element[] target = new Element[source.length];
        for (int i = 0; i < target.length; i++)
            target[i] = source[i].duplicate();

        return target;
    }

    /**
     * Clone immutable element [ ].
     *
     * @param source the source
     * @return the element [ ]
     */
    public static Element[] cloneImmutable(Element[] source) {
        Element[] target = Arrays.copyOf(source, source.length);

        for (int i = 0; i < target.length; i++) {
            Element uElement = target[i];
            if (uElement != null && !uElement.isImmutable())
                target[i] = target[i].getImmutable();
        }

        return target;
    }

    /**
     * Clone immutable map.
     *
     * @param <K>    the type parameter
     * @param source the source
     * @return the map
     */
    public static <K> Map<K, Element[]> cloneImmutable(Map<K, Element[]> source) {
        Map<K, Element[]> dest = new HashMap<K, Element[]>(source.size());

        for (Map.Entry<K, Element[]> kEntry : source.entrySet())
            dest.put(kEntry.getKey(), cloneImmutable(kEntry.getValue()));

        return dest;
    }

    /**
     * Clone immutable 2 map.
     *
     * @param <K>    the type parameter
     * @param source the source
     * @return the map
     */
    public static <K> Map<K, Element> cloneImmutable2(Map<K, Element> source) {
        Map<K, Element> dest = new HashMap<K, Element>(source.size());

        for (Map.Entry<K, Element> kEntry : source.entrySet())
            dest.put(kEntry.getKey(), kEntry.getValue().getImmutable());

        return dest;
    }

    /**
     * Clone to element pow element pow [ ].
     *
     * @param source the source
     * @return the element pow [ ]
     */
    public static ElementPow[] cloneToElementPow(Element[] source) {
        ElementPow[] target = new ElementPow[source.length];

        for (int i = 0; i < target.length; i++) {
            target[i] = source[i].getElementPowPreProcessing();
        }

        return target;
    }

    /**
     * Random in element.
     *
     * @param pairing   the pairing
     * @param generator the generator
     * @return the element
     */
    public static Element randomIn(Pairing pairing, Element generator) {
        return generator.duplicate().powZn(pairing.getZr().newRandomElement());
    }

    /**
     * Gets generator.
     *
     * @param pairing       the pairing
     * @param generator     the generator
     * @param parameters    the parameters
     * @param subgroupIndex the subgroup index
     * @param numPrimes     the num primes
     * @return the generator
     */
    public static Element getGenerator(Pairing pairing, Element generator, PairingParameters parameters, int subgroupIndex, int numPrimes) {
        BigInteger prod = BigInteger.ONE;
        for (int j = 0; j < numPrimes; j++) {
            if (j != subgroupIndex)
                prod = prod.multiply(parameters.getBigIntegerAt("n", j));
        }

        return generator.pow(prod);
    }

    /**
     * Print.
     *
     * @param matrix the matrix
     */
    public static void print(Element[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix[i][j] + ", ");

            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Transpose element [ ] [ ].
     *
     * @param matrix the matrix
     * @return the element [ ] [ ]
     */
    public static Element[][] transpose(Element[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {

            for (int j = i+1; j < n; j++) {

                Element temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }

        return matrix;
    }

    /**
     * Multiply element [ ] [ ].
     *
     * @param a the a
     * @param b the b
     * @return the element [ ] [ ]
     */
    public static Element[][] multiply(Element[][] a, Element[][] b) {
        int n = a.length;
        Field field = a[0][0].getField();

        Element[][] res = new Element[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                res[i][j] = field.newZeroElement();
                for (int k = 0; k < n; k++)
                    res[i][j].add(a[i][k].duplicate().mul(b[k][j]));
            }
        }

        return res;
    }

    /**
     * Copy array.
     *
     * @param target the target
     * @param source the source
     * @param sizeY  the size y
     * @param sizeX  the size x
     * @param y      the y
     * @param x      the x
     */
    public static void copyArray(Element[][] target, Element[][] source, int sizeY, int sizeX, int y, int x) {
        for (int i = y; i < sizeY; i++) {
            for (int j = x; j < sizeX; j++) {
                target[i - y][j - x] = source[i][j].duplicate();
            }
        }
    }
}
