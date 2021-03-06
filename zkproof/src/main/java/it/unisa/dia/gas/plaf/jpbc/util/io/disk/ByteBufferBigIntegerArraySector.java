package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Byte buffer big integer array sector.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferBigIntegerArraySector implements ArraySector<BigInteger> {

    /**
     * The Buffer.
     */
    protected ByteBuffer buffer;
    /**
     * The Offset.
     */
    protected int offset, /**
     * The Record size.
     */
    recordSize, /**
     * The Record length.
     */
    recordLength, /**
     * The Num records.
     */
    numRecords;
    /**
     * The Length in bytes.
     */
    protected int lengthInBytes;

    /**
     * The In.
     */
    protected PairingDataInput in;
    /**
     * The Out.
     */
    protected PairingDataOutput out;

    /**
     * The Labels map.
     */
    protected Map<String, Integer> labelsMap;

    /**
     * Instantiates a new Byte buffer big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @throws IOException the io exception
     */
    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        this.lengthInBytes = 4 + ((recordSize + 4) * numRecords);

        this.offset = 4;
        this.recordSize = recordSize;
        this.recordLength = recordSize + 4;
        this.numRecords = numRecords;
    }

    /**
     * Instantiates a new Byte buffer big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @param labels     the labels
     * @throws IOException the io exception
     */
    public ByteBufferBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        this(recordSize, numRecords);

        labelsMap = new HashMap<String, Integer>(labels.length);
        for (int i = 0; i < labels.length; i++) {
            labelsMap.put(labels[i], i);
        }
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        return numRecords;
    }

    public synchronized ArraySector<BigInteger> mapTo(Mode mode, ByteBuffer buffer) {
        this.buffer = buffer;
        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));

        switch (mode) {
            case INIT:
                try {
                    out.writeInt(numRecords);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case READ:
                break;
            default:
                throw new IllegalStateException("Invalid mode!");
        }

        return this;
    }

    public synchronized BigInteger getAt(int index) {
        try {
            buffer.position(offset + (index * recordLength));
            return in.readBigInteger();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setAt(int index, BigInteger value) {
        try {
            buffer.position(offset + (index * recordLength));
            out.writeBigInteger(value, recordSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigInteger getAt(String label) {
        if (labelsMap == null)
            throw new IllegalStateException();

        return getAt(labelsMap.get(label));
    }

    public void setAt(String label, BigInteger value) {
        if (labelsMap == null)
            throw new IllegalStateException();

        setAt(labelsMap.get(label), value);
    }
}
