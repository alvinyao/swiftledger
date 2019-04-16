package it.unisa.dia.gas.plaf.jpbc.pairing.parameters;

/**
 * The type Mutable map parameters.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class MutableMapParameters extends MapParameters implements MutablePairingParameters {

    /**
     * Instantiates a new Mutable map parameters.
     */
    public MutableMapParameters() {
    }

    public void put(String key,String value){
        throw new IllegalStateException("Not Implemented yet!");
    }

}
