package it.unisa.dia.gas.plaf.jpbc.pairing.a1;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.DegreeTwoExtensionQuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The type Type a 1 pairing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeA1Pairing extends AbstractPairing {
    /**
     * The constant NAF_MILLER_PROJECTTIVE_METHOD.
     */
    public static final String NAF_MILLER_PROJECTTIVE_METHOD = "naf-miller-projective";
    /**
     * The constant MILLER_AFFINE_METHOD.
     */
    public static final String MILLER_AFFINE_METHOD = "miller-affine";

    /**
     * The R.
     */
    protected BigInteger r;
    /**
     * The P.
     */
    protected BigInteger p;
    /**
     * The L.
     */
    protected long l;

    /**
     * The Phik onr.
     */
    protected BigInteger phikOnr;

    /**
     * The Fp.
     */
    protected Field Fp;
    /**
     * The Fq 2.
     */
    protected Field<? extends Point> Fq2;
    /**
     * The Eq.
     */
    protected Field<? extends Point> Eq;

    /**
     * Instantiates a new Type a 1 pairing.
     *
     * @param params the params
     */
    public TypeA1Pairing(PairingParameters params) {
        this(new SecureRandom(), params);
    }

    /**
     * Instantiates a new Type a 1 pairing.
     *
     * @param random the random
     * @param params the params
     */
    public TypeA1Pairing(SecureRandom random, PairingParameters params) {
        super(random);

        initParams(params);
        initMap(params);
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
        if (type == null || !"a1".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'a1'.");

        // load params
        p = curveParams.getBigInteger("p");
        r = curveParams.getBigInteger("n");
        l = curveParams.getLong("l");
    }

    /**
     * Init fields.
     */
    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fp
        Fp = initFp(p);

        //k=2, hence phi_k(q) = q + 1, phikOnr = (q+1)/r
        phikOnr = BigInteger.valueOf(l);

        // Init Eq
        Eq = initEq();

        // Init Fq2
        Fq2 = initFi();

        // Init G1, G2, GT
        G1 = Eq;
        G2 = G1;
        GT = initGT();
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
     * Init eq field.
     *
     * @return the field
     */
    protected Field<? extends Point> initEq() {
        return new CurveField<Field>(random, Fp.newOneElement(), Fp.newZeroElement(), r, phikOnr);
    }

    /**
     * Init fi field.
     *
     * @return the field
     */
    protected Field<? extends Point> initFi() {
        return new DegreeTwoExtensionQuadraticField<Field>(random, Fp);
    }

    /**
     * Init gt field.
     *
     * @return the field
     */
    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fq2);
    }

    /**
     * Init map.
     *
     * @param curveParams the curve params
     */
    protected void initMap(PairingParameters curveParams) {
        String method = curveParams.getString("method", NAF_MILLER_PROJECTTIVE_METHOD);

        if (NAF_MILLER_PROJECTTIVE_METHOD.endsWith(method)) {
            pairingMap = new TypeA1TateNafProjectiveMillerPairingMap(this);
        } else if (MILLER_AFFINE_METHOD.equals(method))
            pairingMap = new TypeA1TateAffineMillerPairingMap(this);
        else
            throw new IllegalArgumentException("Pairing method not recognized. Method = " + method);
    }

}