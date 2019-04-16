package it.unisa.dia.gas.plaf.jpbc.util;

/**
 * The type Arrays.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class Arrays {

    /**
     * Copy of byte [ ].
     *
     * @param original  the original
     * @param newLength the new length
     * @return the byte [ ]
     */
    public static byte[] copyOf(byte[] original, int newLength) {
        int len = Math.min(original.length, newLength);
        byte[] copy = new byte[len];
        System.arraycopy(original, 0, copy, 0, len);
        return copy;
    }

    /**
     * Copy of byte [ ].
     *
     * @param original  the original
     * @param offset    the offset
     * @param newLength the new length
     * @return the byte [ ]
     */
    public static byte[] copyOf(byte[] original, int offset, int newLength) {
        int len = Math.min(original.length - offset, newLength);
        byte[] copy = new byte[len];
        System.arraycopy(original, offset, copy, 0, len);
        return copy;
    }

    /**
     * Copy of range byte [ ].
     *
     * @param original the original
     * @param from     the from
     * @param to       the to
     * @return the byte [ ]
     */
    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

}
