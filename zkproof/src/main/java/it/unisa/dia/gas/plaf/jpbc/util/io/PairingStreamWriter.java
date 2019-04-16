package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The type Pairing stream writer.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingStreamWriter {

    private ByteArrayOutputStream baos;
    private DataOutputStream dos;

    /**
     * Instantiates a new Pairing stream writer.
     *
     * @param size the size
     */
    public PairingStreamWriter(int size) {
        this.baos = new ByteArrayOutputStream(size);
        this.dos = new DataOutputStream(baos);
    }

    /**
     * Write.
     *
     * @param s the s
     * @throws IOException the io exception
     */
    public void write(String s) throws IOException {
        dos.writeUTF(s);
    }

    /**
     * Write.
     *
     * @param element the element
     * @throws IOException the io exception
     */
    public void write(Element element) throws IOException {
        dos.write(element.toBytes());
    }

    /**
     * Write int.
     *
     * @param value the value
     * @throws IOException the io exception
     */
    public void writeInt(int value) throws IOException {
        dos.writeInt(value);
    }

    /**
     * Write.
     *
     * @param bytes the bytes
     * @throws IOException the io exception
     */
    public void write(byte[] bytes) throws IOException {
        dos.write(bytes);
    }

    /**
     * To bytes byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] toBytes() {
        return baos.toByteArray();
    }

}
