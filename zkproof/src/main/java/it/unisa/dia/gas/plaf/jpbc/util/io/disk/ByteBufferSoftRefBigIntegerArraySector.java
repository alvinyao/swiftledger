package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Byte buffer soft ref big integer array sector.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ByteBufferSoftRefBigIntegerArraySector extends ByteBufferBigIntegerArraySector {

    /**
     * The Cache.
     */
    protected Map<Integer, SoftReference<BigInteger>> cache;

    /**
     * Instantiates a new Byte buffer soft ref big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @throws IOException the io exception
     */
    public ByteBufferSoftRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }

    /**
     * Instantiates a new Byte buffer soft ref big integer array sector.
     *
     * @param recordSize the record size
     * @param numRecords the num records
     * @param labels     the labels
     * @throws IOException the io exception
     */
    public ByteBufferSoftRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }


    public synchronized BigInteger getAt(int index) {
        BigInteger result = null;
        SoftReference<BigInteger> sr = cache.get(index);

        if (sr != null)
            result = sr.get();

        if (result == null) {
            result = super.getAt(index);
            cache.put(index, new SoftReference<BigInteger>(result));
        }

        return result;
    }

    public synchronized void setAt(int index, BigInteger value) {
        cache.put(index, new SoftReference<BigInteger>(value));

        super.setAt(index, value);
    }

}
