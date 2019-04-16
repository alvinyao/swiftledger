package it.unisa.dia.gas.plaf.jpbc.field.base;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.io.FieldStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * The type Abstract element pow pre processing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class AbstractElementPowPreProcessing implements ElementPowPreProcessing {
    /**
     * The constant DEFAULT_K.
     */
    public static final int DEFAULT_K = 5;

    /**
     * The Field.
     */
    protected Field field;

    /**
     * The K.
     */
    protected int k;
    /**
     * The Bits.
     */
    protected int bits;
    /**
     * The Num lookups.
     */
    protected int numLookups;
    /**
     * The Table.
     */
    protected Element table[][];

    /**
     * Instantiates a new Abstract element pow pre processing.
     *
     * @param g the g
     * @param k the k
     */
    public AbstractElementPowPreProcessing(Element g, int k) {
        this.field = g.getField();
        this.bits = field.getOrder().bitLength();
        this.k = k;

        initTable(g);
    }

    /**
     * Instantiates a new Abstract element pow pre processing.
     *
     * @param field  the field
     * @param k      the k
     * @param source the source
     * @param offset the offset
     */
    public AbstractElementPowPreProcessing(Field field, int k, byte[] source, int offset) {
        this.field = field;
        this.bits = field.getOrder().bitLength();
        this.k = k;

        initTableFromBytes(source, offset);
    }

    public Field getField() {
        return field;
    }

    public Element pow(BigInteger n) {
        return powBaseTable(n);
    }

    public Element powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public byte[] toBytes() {
        try {
            PairingStreamWriter out = new PairingStreamWriter(field.getLengthInBytes() * table.length * table[0].length);
            for (Element[] row : table) 
                for (Element element : row) 
                    out.write(element);
            return out.toBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Init table from bytes.
     *
     * @param source the source
     * @param offset the offset
     */
    protected void initTableFromBytes(byte[] source, int offset) {
        int lookupSize = 1 << k;
        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        FieldStreamReader in = new FieldStreamReader(field, source, offset);

        for (int i = 0; i < numLookups; i++)
            for (int j = 0; j < lookupSize; j++)
                table[i][j] = in.readElement();
    }

    /**
     * build k-bit base table for n-bit exponentiation w/ base a
     *
     * @param g an element
     */
    protected void initTable(Element g) {
        int lookupSize = 1 << k;

        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        Element multiplier = g.duplicate();

        for (int i = 0; i < numLookups; i++) {
            table[i][0] = field.newOneElement();

            for (int j = 1; j < lookupSize; j++) {
                table[i][j] = multiplier.duplicate().mul(table[i][j - 1]);
            }
            multiplier.mul(table[i][lookupSize - 1]);
        }
    }

    /**
     * Pow base table element.
     *
     * @param n the n
     * @return the element
     */
    protected Element powBaseTable(BigInteger n) {
        /* early abort if raising to power 0 */
        if (n.signum() == 0) {
            return field.newOneElement();
        }

        if (n.compareTo(field.getOrder()) > 0)
            n = n.mod(field.getOrder());

        Element result = field.newOneElement();
        int numLookups = n.bitLength() / k + 1;

        for (int row = 0; row < numLookups; row++) {
            int word = 0;
            for (int s = 0; s < k; s++) {
                word |= (n.testBit(k * row + s) ? 1 : 0) << s;
            }

            if (word > 0) {
                result.mul(table[row][word]);
            }
        }

        return result;
    }

}
