package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.util.Arrays;

import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;

/**
 * The type Pairing data output.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PairingDataOutput implements DataOutput {
    private DataOutput dataOutput;

    private Pairing pairing;

    /**
     * Instantiates a new Pairing data output.
     *
     * @param dataOutput the data output
     */
    public PairingDataOutput(DataOutput dataOutput) {
        this(null, dataOutput);
    }

    /**
     * Instantiates a new Pairing data output.
     *
     * @param pairing    the pairing
     * @param dataOutput the data output
     */
    public PairingDataOutput(Pairing pairing, DataOutput dataOutput) {
        this.pairing = pairing;
        this.dataOutput = dataOutput;
    }


    public void write(int b) throws IOException {
        dataOutput.write(b);
    }

    public void write(byte[] b) throws IOException {
        dataOutput.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        dataOutput.write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        dataOutput.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        dataOutput.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        dataOutput.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        dataOutput.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        dataOutput.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        dataOutput.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        dataOutput.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        dataOutput.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        dataOutput.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        dataOutput.writeChars(s);
    }

    public void writeUTF(String s) throws IOException {
        dataOutput.writeUTF(s);
    }

    /**
     * Write element.
     *
     * @param element the element
     * @throws IOException the io exception
     */
    public void writeElement(Element element) throws IOException {
        if (element == null)
            writeInt(0);
        else {
            byte[] bytes = element.toBytes();
            writeInt(bytes.length);
            write(bytes);
        }
    }

    /**
     * Write elements.
     *
     * @param elements the elements
     * @throws IOException the io exception
     */
    public void writeElements(Element[] elements) throws IOException {
        if (elements == null)
            writeInt(0);
        else {
            writeInt(elements.length);
            for (Element e : elements)
                writeElement(e);
        }
    }

    /**
     * Write pre processing.
     *
     * @param processing the processing
     * @throws IOException the io exception
     */
    public void writePreProcessing(PreProcessing processing) throws IOException {
        byte[] buffer = processing.toBytes();

        if (processing instanceof ElementPowPreProcessing) {
            // write additional information on the field
            ElementPowPreProcessing powPreProcessing = (ElementPowPreProcessing) processing;
            writePairingFieldIndex(powPreProcessing.getField());
        }

        writeInt(buffer.length);
        writeBytes(buffer);
    }

    /**
     * Write ints.
     *
     * @param ints the ints
     * @throws IOException the io exception
     */
    public void writeInts(int[] ints) throws IOException {
        if (ints == null) {
            writeInt(0);
        } else {
            writeInt(ints.length);
            for (int anInt : ints) writeInt(anInt);
        }
    }

    /**
     * Write bytes.
     *
     * @param buffer the buffer
     * @throws IOException the io exception
     */
    public void writeBytes(byte[] buffer) throws IOException{
        writeInt(buffer.length);
        write(buffer);
    }

    /**
     * Gets pairing.
     *
     * @return the pairing
     */
    public Pairing getPairing() {
        return pairing;
    }

    /**
     * Write big integer.
     *
     * @param bigInteger the big integer
     * @throws IOException the io exception
     */
    public void writeBigInteger(BigInteger bigInteger) throws IOException {
        writeBytes(bigInteger.toByteArray());
    }

    /**
     * Write big integer.
     *
     * @param bigInteger   the big integer
     * @param ensureLength the ensure length
     * @throws IOException the io exception
     */
    public void writeBigInteger(BigInteger bigInteger, int ensureLength) throws IOException {
        byte[] bytes = bigInteger.toByteArray();

        if (bytes.length > ensureLength) {
            // strip the zero prefix
            if (bytes[0] == 0 && bytes.length == ensureLength + 1) {
                // Remove it
                bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
            } else
                throw new IllegalStateException("result has more than allowed bytes.");
        } else if (bytes.length < ensureLength) {
            byte[] result = new byte[ensureLength];
            System.arraycopy(bytes, 0, result, ensureLength - bytes.length, bytes.length);
            bytes = result;
        }

        writeBytes(bytes);
    }

    /**
     * Write big integers.
     *
     * @param bigIntegers the big integers
     * @throws IOException the io exception
     */
    public void writeBigIntegers(BigInteger[] bigIntegers) throws IOException {
        writeInt(bigIntegers.length);
        for (BigInteger bigInteger : bigIntegers) {
            writeBigInteger(bigInteger);
        }
    }

    /**
     * Write big integers.
     *
     * @param bigIntegers  the big integers
     * @param ensureLength the ensure length
     * @throws IOException the io exception
     */
    public void writeBigIntegers(BigInteger[] bigIntegers, int ensureLength) throws IOException {
        writeInt(bigIntegers.length);
        for (BigInteger bigInteger : bigIntegers) {
            writeBigInteger(bigInteger, ensureLength);
        }
    }

    /**
     * Write pairing field index.
     *
     * @param field the field
     * @throws IOException the io exception
     */
    protected void writePairingFieldIndex(Field field) throws IOException {
        int index = getPairing().getFieldIndex(field);
        if (index == -1)
            throw new IllegalArgumentException("The field does not belong to the current pairing instance.");
        writeInt(index);
    }

}