package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.*;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigInteger;

/**
 * The type Pairing data input.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PairingDataInput implements DataInput {

    private DataInput dataInput;
    private Pairing pairing;

    /**
     * Instantiates a new Pairing data input.
     *
     * @param dataInput the data input
     */
    public PairingDataInput(DataInput dataInput) {
        this.dataInput = dataInput;
    }

    /**
     * Instantiates a new Pairing data input.
     *
     * @param dataInput the data input
     * @param pairing   the pairing
     */
    public PairingDataInput(DataInput dataInput, Pairing pairing) {
        this.dataInput = dataInput;
        this.pairing = pairing;
    }


    public void readFully(byte[] b) throws IOException {
        dataInput.readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        dataInput.readFully(b, off, len);
    }

    public int skipBytes(int n) throws IOException {
        return dataInput.skipBytes(n);
    }

    public boolean readBoolean() throws IOException {
        return dataInput.readBoolean();
    }

    public byte readByte() throws IOException {
        return dataInput.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return dataInput.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return dataInput.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return dataInput.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return dataInput.readChar();
    }

    public int readInt() throws IOException {
        return dataInput.readInt();
    }

    public long readLong() throws IOException {
        return dataInput.readLong();
    }

    public float readFloat() throws IOException {
        return dataInput.readFloat();
    }

    public double readDouble() throws IOException {
        return dataInput.readDouble();
    }

    public String readLine() throws IOException {
        return dataInput.readLine();
    }

    public String readUTF() throws IOException {
        return dataInput.readUTF();
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
     * Read field field.
     *
     * @return the field
     * @throws IOException the io exception
     */
    public Field readField() throws IOException {
        int identifier = readInt();
        return pairing.getFieldAt(identifier);
    }

    /**
     * Read element element.
     *
     * @param fieldIdentifier the field identifier
     * @return the element
     * @throws IOException the io exception
     */
    public Element readElement(int fieldIdentifier) throws IOException {
        byte[] buffer = new byte[readInt()];
        readFully(buffer);

        return pairing.getFieldAt(fieldIdentifier).newElementFromBytes(buffer);
    }

    /**
     * Read elements element [ ].
     *
     * @param identifier the identifier
     * @return the element [ ]
     * @throws IOException the io exception
     */
    public Element[] readElements(int identifier) throws IOException{
        int num = readInt();
        Element[] elements = new Element[num];
        for (int i = 0; i < num; i++) {
            elements[i] = readElement(identifier);
        }

        return elements;
    }

    /**
     * Read pairing pre processing pairing pre processing.
     *
     * @return the pairing pre processing
     * @throws IOException the io exception
     */
    public PairingPreProcessing readPairingPreProcessing() throws IOException {
        int size = readInt();
        byte[] buffer = new byte[size];
        readFully(buffer);

        return getPairing().getPairingPreProcessingFromBytes(buffer, 0);
    }

    /**
     * Read element pow pre processing element pow pre processing.
     *
     * @return the element pow pre processing
     * @throws IOException the io exception
     */
    public ElementPowPreProcessing readElementPowPreProcessing() throws IOException {
        // Read field identifier
        Field field = readField();

        // read the preprocessing information
        int size = readInt();
        byte[] buffer = new byte[size];
        readFully(buffer);

        return field.getElementPowPreProcessingFromBytes(buffer, 0);
    }

    /**
     * Read ints int [ ].
     *
     * @return the int [ ]
     * @throws IOException the io exception
     */
    public int[] readInts() throws IOException {
        int num = readInt();
        int[] elements = new int[num];
        for (int i = 0; i < num; i++) {
            elements[i] = readInt();
        }

        return elements;
    }

    /**
     * Read bytes byte [ ].
     *
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] readBytes() throws IOException {
        int length = readInt();
        byte[] buffer = new byte[length];
        readFully(buffer);

        return buffer;
    }

    /**
     * Read big integer big integer.
     *
     * @return the big integer
     * @throws IOException the io exception
     */
    public BigInteger readBigInteger() throws IOException {
        return new BigInteger(readBytes());
    }

    /**
     * Read big integers big integer [ ].
     *
     * @return the big integer [ ]
     * @throws IOException the io exception
     */
    public BigInteger[] readBigIntegers() throws IOException {
        int num = readInt();
        BigInteger[] bigIntegers = new BigInteger[num];
        for (int i = 0; i < bigIntegers.length; i++) {
            bigIntegers[i] = readBigInteger();
        }

        return bigIntegers;
    }

}