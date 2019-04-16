package it.unisa.dia.gas.plaf.jpbc.pairing.g;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveField;
import it.unisa.dia.gas.plaf.jpbc.field.gt.GTFiniteField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import it.unisa.dia.gas.plaf.jpbc.pairing.AbstractPairing;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * The type Type g pairing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeGPairing extends AbstractPairing {
    /**
     * The Curve params.
     */
    protected PairingParameters curveParams;

    /**
     * The Q.
     */
    protected BigInteger q, /**
     * The N.
     */
    n, /**
     * The R.
     */
    r, /**
     * The H.
     */
    h;
    /**
     * The A.
     */
    protected BigInteger a, /**
     * The B.
     */
    b;
    /**
     * The Nqr.
     */
    protected BigInteger nqr;

    /**
     * The X powq.
     */
    protected PolyModElement xPowq, /**
     * The X powq 2.
     */
    xPowq2, /**
     * The X powq 3.
     */
    xPowq3, /**
     * The X powq 4.
     */
    xPowq4;
    /**
     * The Nqr inverse.
     */
    protected Element nqrInverse, /**
     * The Nqr inverse square.
     */
    nqrInverseSquare;
    /**
     * The Phik onr.
     */
    protected BigInteger phikOnr;

    /**
     * The Fq.
     */
    protected Field Fq, /**
     * The Fqx.
     */
    Fqx;
    /**
     * The Fqk.
     */
    protected Field<? extends Point<Polynomial>> Fqk;
    /**
     * The Fqd.
     */
    protected PolyModField Fqd;
    /**
     * The Eq.
     */
    protected CurveField Eq, /**
     * The Etwist.
     */
    Etwist;

    /**
     * Instantiates a new Type g pairing.
     *
     * @param curveParams the curve params
     */
    public TypeGPairing(PairingParameters curveParams) {
        this(new SecureRandom(), curveParams);
    }

    /**
     * Instantiates a new Type g pairing.
     *
     * @param random      the random
     * @param curveParams the curve params
     */
    public TypeGPairing(SecureRandom random, PairingParameters curveParams) {
        super(random);

        this.curveParams = curveParams;

        initParams();
        initMap();
        initFields();
    }

    public boolean isSymmetric() {
        return false;
    }

    /**
     * Init params.
     */
    protected void initParams() {
        // validate the type
        String type = curveParams.getString("type");
        if (type == null || !type.equalsIgnoreCase("g"))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'g'.");

        // load params
        q = curveParams.getBigInteger("q");
        n = curveParams.getBigInteger("n");
        h = curveParams.getBigInteger("h");
        r = curveParams.getBigInteger("r");
        a = curveParams.getBigInteger("a");
        b = curveParams.getBigInteger("b");
//        k = curveParams.getBigInteger("k");
//        nk = curveParams.getBigInteger("nk");
//        hk = curveParams.getBigInteger("hk");
        nqr = curveParams.getBigInteger("nqr");
    }

    /**
     * Init fields.
     */
    protected void initFields() {
        // Init Zr
        Zr = initFp(r);

        // Init Fq
        Fq = initFp(q);

        // Init the curve
        Eq = initEq();

        // Init Fqx
        Fqx = initPoly();

        // Init polymod
        // First set the coefficient of x^5 to 1 so we can call element_item()
        // for the other coefficients.
        PolyElement irreduciblePoly = (PolyElement) Fqx.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        for (int i = 0; i < 5; i++)
            irreduciblePolyCoeff.add(Fq.newElement().set(curveParams.getBigIntegerAt("coeff", i)));
        irreduciblePolyCoeff.add(Fq.newOneElement());

        // init Fq12
        Fqd = initPolyMod(irreduciblePoly);

        Fqk = initQuadratic();

        Etwist = initEqMap().twist();

        nqrInverse = Fqd.getNqr().duplicate().invert();
        nqrInverseSquare = nqrInverse.duplicate().square();

        // ndonr temporarily holds the trace.
        BigInteger ndonr = q.subtract(n).add(BigInteger.ONE);

        // Negate because we want the order of the twist.
        ndonr = ndonr.negate();
        ndonr = BigIntegerUtils.pbc_mpz_curve_order_extn(q, ndonr, 5);
        ndonr = ndonr.divide(r);
        Etwist.setQuotientCmp(ndonr);

        // Compute phi(k)/r = (q^4 - q^3 + ... + 1)/r.
        phikOnr = BigInteger.ONE;
        phikOnr = phikOnr.subtract(q);
        BigInteger z0 = q.multiply(q);
        phikOnr = phikOnr.add(z0);
        z0 = z0.multiply(q);
        phikOnr = phikOnr.subtract(z0);
        z0 = z0.multiply(q);
        phikOnr = phikOnr.add(z0);
        phikOnr = phikOnr.divide(r);

        // Compute xPowq's
        xPowq = Fqd.newElement();
        xPowq.getCoefficient(1).setToOne();
        xPowq.pow(q);
        xPowq2 = xPowq.duplicate().square();
        xPowq4 = xPowq2.duplicate().square();
        xPowq3 = xPowq2.duplicate().mul(xPowq);

        // Init G1, G2, GT
        G1 = Eq;
        G2 = Etwist;
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
     * Init eq curve field.
     *
     * @return the curve field
     */
    protected CurveField initEq() {
        return new CurveField(random, Fq.newElement().set(a), Fq.newElement().set(b), r, h);
    }

    /**
     * Init eq map curve field.
     *
     * @return the curve field
     */
    protected CurveField initEqMap() {
        return new CurveField(random, Fqd.newElement().map(Eq.getA()), Fqd.newElement().map(Eq.getB()), r);
    }

    /**
     * Init poly poly field.
     *
     * @return the poly field
     */
    protected PolyField initPoly() {
        return new PolyField(random, Fq);
    }

    /**
     * Init poly mod poly mod field.
     *
     * @param irred the irred
     * @return the poly mod field
     */
    protected PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(random, irred, nqr);
    }

    /**
     * Init quadratic quadratic field.
     *
     * @return the quadratic field
     */
    protected QuadraticField initQuadratic() {
        return new QuadraticField(random, Fqd);
    }

    /**
     * Init gt field.
     *
     * @return the field
     */
    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fqk);
    }

    /**
     * Init map.
     */
    protected void initMap() {
        pairingMap = new TypeGTateAffineNoDenomMillerPairingMap(this);
    }
}