package it.unisa.dia.gas.plaf.jpbc.pairing.f;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.Point;
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
 * The type Type f pairing.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TypeFPairing extends AbstractPairing {
    /**
     * The Curve params.
     */
    protected PairingParameters curveParams;

    /**
     * The Q.
     */
    protected BigInteger q;
    /**
     * The R.
     */
    protected BigInteger r;
    /**
     * The B.
     */
    protected BigInteger b;
    /**
     * The Beta.
     */
    protected BigInteger beta;
    /**
     * The Alpha 0.
     */
    protected BigInteger alpha0;
    /**
     * The Alpha 1.
     */
    protected BigInteger alpha1;

    /**
     * The X powq 2.
     */
    protected Element xPowq2, /**
     * The X powq 6.
     */
    xPowq6, /**
     * The X powq 8.
     */
    xPowq8;
    /**
     * The Neg alpha.
     */
    protected Element negAlpha, /**
     * The Neg alpha inv.
     */
    negAlphaInv;
    /**
     * The Tate exp.
     */
    protected BigInteger tateExp;

    /**
     * The Fq.
     */
    protected Field Fq, /**
     * The Fq 2 x.
     */
    Fq2x;
    /**
     * The Fq 2.
     */
    protected Field Fq2;
    /**
     * The Fq 12.
     */
    protected PolyModField Fq12;
    /**
     * The Eq.
     */
    protected CurveField Eq, /**
     * The Etwist.
     */
    etwist;

    /**
     * Instantiates a new Type f pairing.
     *
     * @param curveParams the curve params
     */
    public TypeFPairing(PairingParameters curveParams) {
        this(new SecureRandom(), curveParams);
    }

    /**
     * Instantiates a new Type f pairing.
     *
     * @param random      the random
     * @param curveParams the curve params
     */
    public TypeFPairing(SecureRandom random, PairingParameters curveParams) {
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
        if (type == null || !type.equalsIgnoreCase("f"))
            throw new IllegalArgumentException("Type not valid. Found '" + type + "'. Expected 'f'.");

        // load params
        q = curveParams.getBigInteger("q");
        r = curveParams.getBigInteger("r");
        b = curveParams.getBigInteger("b");
        beta = curveParams.getBigInteger("beta");
        alpha0 = curveParams.getBigInteger("alpha0");
        alpha1 = curveParams.getBigInteger("alpha1");
    }

    /**
     * Init fields.
     */
    protected void initFields() {
        // Init Zr
        Zr = initFp(r, null);

        // Init Fq
        Fq = initFp(q, beta);

        // Init Fq2
        Fq2 = initQuadratic();

        // Init Fq2x
        Fq2x = initPoly();

        Point tmp = (Point) Fq2.newElement();
        tmp.getX().set(alpha0);
        tmp.getY().set(alpha1);
        negAlpha = tmp;

        PolyElement irreduciblePoly = (PolyElement) Fq2x.newElement();
        List<Element> irreduciblePolyCoeff = irreduciblePoly.getCoefficients();
        irreduciblePolyCoeff.add(negAlpha.duplicate());
        for (int i = 1; i < 6; i++)
            irreduciblePolyCoeff.add(Fq2.newElement());
        irreduciblePolyCoeff.add(Fq2.newOneElement());

        // init Fq12
        Fq12 = initPolyMod(irreduciblePoly);

        negAlphaInv = negAlpha.negate().duplicate().invert();

        // Initialize the curve Y^2 = X^3 + b.
        Eq = initEq();

        // Initialize the curve Y^2 = X^3 - alpha0 b - alpha1 sqrt(beta) b.
        etwist = initEqMap();

        // ndonr temporarily holds the trace.
        BigInteger ndonr = q.subtract(r).add(BigInteger.ONE);

        // TODO: We can use a smaller quotientCmp, but I have to figure out
        // BN curves again.
        ndonr = BigIntegerUtils.pbc_mpz_curve_order_extn(q, ndonr, 12);
        ndonr = ndonr.divide(r);
        ndonr = ndonr.divide(r);
        etwist.setQuotientCmp(ndonr);

        /*
            For k = 12, the final exponent becomes
            (q^12-1)/r = (p^6 - 1 )(p^2+1) ( (p^4-p^2+1)/r )
            Lets compute the final factor (p^4-p^2+1)/r
        */
        tateExp = q.multiply(q).subtract(BigInteger.ONE).multiply(q).multiply(q).add(BigInteger.ONE).divide(r);

        PolyModElement polyModElement = Fq12.newElement();
        polyModElement.getCoefficient(1).setToOne();
        polyModElement.pow(q);
        polyModElement.pow(q);
        xPowq2 = polyModElement.getCoefficient(1).duplicate();
        polyModElement.pow(q);
        polyModElement.pow(q);
        polyModElement.pow(q);
        polyModElement.pow(q);
        xPowq6 = polyModElement.getCoefficient(1).duplicate();
        polyModElement.pow(q);
        polyModElement.pow(q);
        xPowq8 = polyModElement.getCoefficient(1).duplicate();

        // Init G1, G2, GT
        G1 = Eq;
        G2 = etwist;
        GT = initGT();
    }

    /**
     * Init fp field.
     *
     * @param order the order
     * @param nqr   the nqr
     * @return the field
     */
    protected Field initFp(BigInteger order, BigInteger nqr) {
        return new ZrField(random, order, nqr);
    }

    /**
     * Init eq curve field.
     *
     * @return the curve field
     */
    protected CurveField initEq() {
        return new CurveField(random, Fq.newElement(), Fq.newElement().set(b), r);
    }

    /**
     * Init eq map curve field.
     *
     * @return the curve field
     */
    protected CurveField initEqMap() {
        Point tmp = (Point) Fq2.newElement();
        tmp.getX().set(Fq.newElement().set(alpha0).negate().mul(b));
        tmp.getY().set(Fq.newElement().set(alpha1).negate().mul(b));
        return new CurveField(random, Fq2.newElement(), tmp, r);
    }

    /**
     * Init poly poly field.
     *
     * @return the poly field
     */
    protected PolyField initPoly() {
        return new PolyField(random, Fq2);
    }

    /**
     * Init poly mod poly mod field.
     *
     * @param irred the irred
     * @return the poly mod field
     */
    protected PolyModField initPolyMod(PolyElement irred) {
        return new PolyModField(random, irred);
    }

    /**
     * Init quadratic quadratic field.
     *
     * @return the quadratic field
     */
    protected QuadraticField initQuadratic() {
        return new QuadraticField(random, Fq);
    }

    /**
     * Init gt field.
     *
     * @return the field
     */
    protected Field initGT() {
        return new GTFiniteField(random, r, pairingMap, Fq12);
    }

    /**
     * Init map.
     */
    protected void initMap() {
        pairingMap = new TypeFTateNoDenomMillerPairingMap(this);
    }
}
