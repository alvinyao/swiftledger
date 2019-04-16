package it.unisa.dia.gas.plaf.jpbc.pairing.a;

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
 * The type Type a pairing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeAPairing extends AbstractPairing {
    /**
     * The constant NAF_MILLER_PROJECTTIVE_METHOD.
     */
    public static final String NAF_MILLER_PROJECTTIVE_METHOD = "naf-miller-projective";
    /**
     * The constant MILLER_PROJECTTIVE_METHOD.
     */
    public static final String MILLER_PROJECTTIVE_METHOD = "miller-projective";
    /**
     * The constant MILLER_AFFINE_METHOD.
     */
    public static final String MILLER_AFFINE_METHOD = "miller-affine";

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
     * The Phik onr.
     */
    protected BigInteger phikOnr;

    /**
     * The Gen no cofac.
     */
    protected byte[] genNoCofac;

    /**
     * The Fq.
     */
    protected Field Fq;
    /**
     * The Fq 2.
     */
    protected Field<? extends Point> Fq2;
    /**
     * The Eq.
     */
    protected Field<? extends Point> Eq;

    /**
     * Instantiates a new Type a pairing.
     *
     * @param random the random
     * @param params the params
     */
    public TypeAPairing(SecureRandom random, PairingParameters params) {
        super(random);

        initParams(params);
        initMap(params);
        initFields();
    }

    /**
     * Instantiates a new Type a pairing.
     *
     * @param params the params
     */
    public TypeAPairing(PairingParameters params) {
        this(new SecureRandom(), params);
    }

    /**
     * Init params.
     *
     * @param curveParams the curve params
     */
    protected void initParams(PairingParameters curveParams) {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !"a".equalsIgnoreCase(type))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'a'.");

        // load params
        exp2 = curveParams.getInt("exp2");
        exp1 = curveParams.getInt("exp1");
        sign1 = curveParams.getInt("sign1");

        r = curveParams.getBigInteger("r"); // r = 2^exp2 + sign1 * 2^exp1 + sign0 * 1
        q = curveParams.getBigInteger("q"); // we work in E(F_q) (and E(F_q^2))
        h = curveParams.getBigInteger("h");  // r * h = q + 1

        genNoCofac = curveParams.getBytes("genNoCofac", null);
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
        Eq = initEq();

        // Init Fq2
        Fq2 = initFi();

        // k=2, hence phi_k(q) = q + 1, phikOnr = (q+1)/r
        phikOnr = h;

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
        // Remember the curve is: y^2 = x^3 + ax
        return new CurveField<Field>(random,
                                     Fq.newOneElement(),   // a
                                     Fq.newZeroElement(),  // b
                                     r,                    // order
                                     h,                    // cofactor  (r*h)=q+1=#E(F_q)
                                     genNoCofac);
    }

    /**
     * Init fi field.
     *
     * @return the field
     */
    protected Field<? extends Point> initFi() {
        return new DegreeTwoExtensionQuadraticField<Field>(random, Fq);
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
            pairingMap = new TypeATateNafProjectiveMillerPairingMap(this);
        } else if (MILLER_PROJECTTIVE_METHOD.equals(method))
            pairingMap = new TypeATateProjectiveMillerPairingMap(this);
        else if (MILLER_AFFINE_METHOD.equals(method))
            pairingMap = new TypeATateAffineMillerPairingMap(this);
        else
            throw new IllegalArgumentException("Pairing method not recognized. Method = " + method);
    }
}
