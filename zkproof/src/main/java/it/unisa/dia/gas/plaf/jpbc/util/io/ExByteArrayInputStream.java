package it.unisa.dia.gas.plaf.jpbc.util.io;

import java.io.ByteArrayInputStream;

/**
 * The type Ex byte array input stream.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public class ExByteArrayInputStream extends ByteArrayInputStream {

    /**
     * Instantiates a new Ex byte array input stream.
     *
     * @param buf the buf
     */
    public ExByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    /**
     * Instantiates a new Ex byte array input stream.
     *
     * @param buf    the buf
     * @param offset the offset
     * @param length the length
     */
    public ExByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    /**
     * Gets pos.
     *
     * @return the pos
     */
    public int getPos() {
        return pos;
    }


}
