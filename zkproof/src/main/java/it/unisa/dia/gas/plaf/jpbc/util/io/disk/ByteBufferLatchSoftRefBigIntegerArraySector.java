package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import it.unisa.dia.gas.plaf.jpbc.util.collection.FlagMap;

import java.io.IOException;
import java.math.BigInteger;

/**
 * The type Byte buffer latch soft ref big integer array sector.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferLatchSoftRefBigIntegerArraySector extends ByteBufferSoftRefBigIntegerArraySector {

    /**
     * The Flags.
     */
    protected FlagMap<Integer> flags;

    /**
     * Instantiates a new Byte buffer latch soft ref big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @throws IOException the io exception
     */
    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.flags = new FlagMap<Integer>();
    }

    /**
     * Instantiates a new Byte buffer latch soft ref big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @param labels     the labels
     * @throws IOException the io exception
     */
    public ByteBufferLatchSoftRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.flags = new FlagMap<Integer>();
    }


    public BigInteger getAt(int index) {
        flags.get(index);

        return super.getAt(index);
    }

    public void setAt(int index, BigInteger value) {
        super.setAt(index, value);

        flags.set(index);
    }

}
