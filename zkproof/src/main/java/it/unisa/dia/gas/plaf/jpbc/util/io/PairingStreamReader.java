package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.DataInputStream;

/**
 * The type Pairing stream reader.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingStreamReader {

    private Pairing pairing;
    private byte[] buffer;
    private int offset;

    private int cursor;

    private DataInputStream dis;
    private ExByteArrayInputStream bais;

    /**
     * Instantiates a new Pairing stream reader.
     *
     * @param pairing the pairing
     * @param buffer  the buffer
     * @param offset  the offset
     */
    public PairingStreamReader(Pairing pairing, byte[] buffer, int offset) {
        this.pairing = pairing;
        this.buffer = buffer;
        this.offset = offset;

        this.cursor = offset;

        this.bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        this.dis = new DataInputStream(bais);
    }

    /**
     * Reset.
     */
    public void reset() {
        this.cursor = this.offset;
    }

    /**
     * Read elements element [ ].
     *
     * @param ids the ids
     * @return the element [ ]
     */
    public Element[] readElements(int... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            Field field = pairing.getFieldAt(ids[i]);
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    /**
     * Read elements element [ ].
     *
     * @param id    the id
     * @param count the count
     * @return the element [ ]
     */
    public Element[] readElements(int id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getFieldAt(id);
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElementFromBytes(buffer, cursor);
            jump(field.getLengthInBytes(elements[i]));
        }

        return elements;
    }

    /**
     * Read g 1 elements element [ ].
     *
     * @param count the count
     * @return the element [ ]
     */
    public Element[] readG1Elements(int count) {
        return readElements(1, count);
    }

    /**
     * Read g 1 element element.
     *
     * @return the element
     */
    public Element readG1Element() {
        Element element = pairing.getG1().newElementFromBytes(buffer, cursor);
        jump(pairing.getG1().getLengthInBytes(element));

        return element;
    }

    /**
     * Read gt element element.
     *
     * @return the element
     */
    public Element readGTElement() {
        Element element = pairing.getGT().newElementFromBytes(buffer, cursor);
        jump(pairing.getGT().getLengthInBytes(element));
        return element;
    }

    /**
     * Read field element element.
     *
     * @param field the field
     * @return the element
     */
    public Element readFieldElement(Field field) {
        Element element = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(element));
        return element;
    }

    /**
     * Read string string.
     *
     * @return the string
     */
    public String readString() {
        try {
            return dis.readUTF();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }

    /**
     * Read int int.
     *
     * @return the int
     */
    public int readInt() {
        try {
            return dis.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }


    private void jump(int length) {
        cursor += length;
        bais.skip(length);
    }

}
