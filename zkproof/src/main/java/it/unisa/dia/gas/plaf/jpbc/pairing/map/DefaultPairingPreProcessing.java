package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;

/**
 * The type Default pairing pre processing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class DefaultPairingPreProcessing implements PairingPreProcessing {

    /**
     * The Pairing.
     */
    protected Pairing pairing;
    /**
     * The In 1.
     */
    protected Element in1;

    /**
     * Instantiates a new Default pairing pre processing.
     *
     * @param pairing the pairing
     * @param in1     the in 1
     */
    public DefaultPairingPreProcessing(Pairing pairing, Element in1) {
        this.pairing = pairing;
        this.in1 = in1;
    }

    /**
     * Instantiates a new Default pairing pre processing.
     *
     * @param pairing the pairing
     * @param field   the field
     * @param source  the source
     * @param offset  the offset
     */
    public DefaultPairingPreProcessing(Pairing pairing, Field field, byte[] source, int offset) {
        this.pairing = pairing;
        this.in1 = field.newElementFromBytes(source, offset);
    }

    public Element pairing(Element in2) {
        return pairing.pairing(in1, in2);
    }

    public byte[] toBytes() {
        return in1.toBytes();
    }

}
