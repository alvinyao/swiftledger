package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.DataInputStream;

/**
 * The type Field stream reader.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class FieldStreamReader {

    private Field field;
    private byte[] buffer;
    private int offset;

    private int cursor;

    private DataInputStream dis;
    private ExByteArrayInputStream bais;

    /**
     * Instantiates a new Field stream reader.
     *
     * @param field  the field
     * @param buffer the buffer
     * @param offset the offset
     */
    public FieldStreamReader(Field field, byte[] buffer, int offset) {
        this.field = field;
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
     * Read element element.
     *
     * @return the element
     */
    public Element readElement() {
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
