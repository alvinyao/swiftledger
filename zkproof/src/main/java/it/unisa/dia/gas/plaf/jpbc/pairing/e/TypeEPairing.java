package it.unisa.dia.gas.plaf.jpbc.pairing.e;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Type e pairing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeEPairing extends AbstractPairing {
    /**
     * The Exp 2.
     */
    protected int exp2;
    /**
     * The Exp 1.
     */
    protected int exp1;
    /**
     * The Sign 1.
     */
    protected int sign1;
    /**
     * The Sign 0.
     */
    protected int sign0;

    /**
     * The R.
     */
    protected BigInteger r;
    /**
     * The Q.
     */
    protected BigInteger q;
    /**
     * The H.
     */
    protected BigInteger h;
    /**
     * The A.
     */
    protected BigInteger a;
    /**
     * The B.
     */
    protected BigInteger b;

    /**
     * The Phikonr.
     */
    protected BigInteger phikonr;
    /**
     * The R.
     */
    protected Point R;

    /**
     * The Fq.
     */
    protected Field Fq;

    /**
     * Instantiates a new Type e pairing.
     *
     * @param properties the properties
     */
    public TypeEPairing(PairingParameters properties) {
        this(new SecureRandom(), properties);
    }

    /**
     * Instantiates a new Type e pairing.
     *
     * @param random     the random
     * @param properties the properties
     */
    public TypeEPairing(SecureRandom random, PairingParameters properties) {
        super(random);

        initParams(properties);
        initMap();
        initFields();
    }

    /**
     * Init params.
     *
     * @param curveParams the curve params
     */
    protected void initParams(PairingParameters curveParams) {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !"e".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'e'.");

        // load params
        exp2 = curveParams.getInt("exp2");
        exp1 = curveParams.getInt("exp1");
        sign1 = curveParams.getInt("sign1");
        sign0 = curveParams.getInt("sign0");

        r = curveParams.getBigInteger("r");
        q = curveParams.getBigInteger("q");
        h = curveParams.getBigInteger("h");

        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
    }

    /**
     * Init fields.
     */
    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init Eq
        CurveField<Field> Eq = initEq();

        // k=1, hence phikOnr = (q-1)/r
        phikonr = Fq.getOrder().subtract(BigInteger.ONE).divide(r);

        // Init G1, G2, GT
        G1 = Eq;
        G2 = G1;
        GT = initGT();

        R = (Point) Eq.getGenNoCofac().duplicate();
    }

    /**
     * Init fp field.
     *
     * @param order the order
     * @return the field
     */
    protected Field initFp(BigInteger order) {
        return new ZrField(random, order);
    }

    /**
     * Init eq curve field.
     *
     * @return the curve field
     */
    protected CurveField<Field> initEq() {
        return new CurveField<Field>(random,
                                     Fq.newElement().set(a), Fq.newElement().set(b),
                                     r, h);
    }

    /**
     * Init gt field.
     *
     * @return the field
     */
    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fq);
    }

    /**
     * Init map.
     */
    protected void initMap() {
        pairingMap = new TypeETateProjectiveMillerPairingMap(this);
    }
}