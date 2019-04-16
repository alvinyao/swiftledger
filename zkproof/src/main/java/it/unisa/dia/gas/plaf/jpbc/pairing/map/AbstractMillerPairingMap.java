package it.unisa.dia.gas.plaf.jpbc.pairing.map;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * The type Abstract miller pairing map.
 *
 * @param <E> the type parameter
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractMillerPairingMap<E extends Element> extends AbstractPairingMap {

    /**
     * Instantiates a new Abstract miller pairing map.
     *
     * @param pairing the pairing
     */
    protected AbstractMillerPairingMap(final Pairing pairing) {
        super(pairing);
    }

    /**
     * Line step.
     *
     * @param f0  the f 0
     * @param a   the a
     * @param b   the b
     * @param c   the c
     * @param Vx  the vx
     * @param Vy  the vy
     * @param V1x the v 1 x
     * @param V1y the v 1 y
     * @param e0  the e 0
     * @param Qx  the qx
     * @param Qy  the qy
     * @param f   the f
     */
    protected final void lineStep(final Point<E> f0,
                                  final Element a, final Element b, final Element c,
                                  final Element Vx, final Element Vy,
                                  final Element V1x, final Element V1y,
                                  final Element e0,
                                  final E Qx, final E Qy,
                                  final Element f) {
        // computeLine(a, b, c, Vx, Vy, V1x, V1y, e0);
        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(e0.set(Vy).mul(V1x));

        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    /**
     * Line step.
     *
     * @param f0  the f 0
     * @param a   the a
     * @param b   the b
     * @param c   the c
     * @param Vs  the vs
     * @param V1s the v 1 s
     * @param e0  the e 0
     * @param Qs  the qs
     * @param f   the f
     */
    protected final void lineStep(final Point<E> f0,
                                  final Element a, final Element b, final Element c,
                                  final Element[] Vs,
                                  final Element[] V1s,
                                  final Element e0,
                                  final Element[] Qs,
                                  final Element f) {
        // computeLine(a, b, c, Vx, Vy, V1x, V1y, e0);
        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        for (int i = 0; i < Vs.length; i++) {
            Point V = (Point) Vs[i];
            Point V1 = (Point) V1s[i];
            Point Q = (Point) Qs[i];

            a.set(V.getY()).sub(V1.getY());
            b.set(V1.getX()).sub(V.getX());
            c.set(V.getX()).mul(V1.getY()).sub(e0.set(V.getY()).mul(V1.getX()));

            millerStep(f0, a, b, c, (E) Q.getX(), (E) Q.getY());
            f.mul(f0);
        }
    }

    /**
     * Tangent step.
     *
     * @param f0     the f 0
     * @param a      the a
     * @param b      the b
     * @param c      the c
     * @param Vx     the vx
     * @param Vy     the vy
     * @param curveA the curve a
     * @param e0     the e 0
     * @param Qx     the qx
     * @param Qy     the qy
     * @param f      the f
     */
    protected final void tangentStep(final Point<E> f0,
                                     final Element a, final Element b, final Element c,
                                     final Element Vx, final Element Vy,
                                     final Element curveA,
                                     final Element e0,
                                     final E Qx, final E Qy,
                                     final Element f) {
        //computeTangent(a, b, c, Vx, Vy, curveA, e0);
        //a = -slope_tangent(V.x, V.y);
        //b = 1;
        //c = -(V.y + aV.x);
        //but we multiply by -2*V.y to avoid division so:
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        a.set(Vx).square().mul(3).add(curveA).negate();
        b.set(Vy).twice();
        c.set(a).mul(Vx).add(e0.set(b).mul(Vy)).negate();

        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    /**
     * Tangent step.
     *
     * @param f0     the f 0
     * @param a      the a
     * @param b      the b
     * @param c      the c
     * @param Vs     the vs
     * @param curveA the curve a
     * @param e0     the e 0
     * @param Qs     the qs
     * @param f      the f
     */
    protected final void tangentStep(final Point<E> f0,
                                     final Element a, final Element b, final Element c,
                                     final Element[] Vs,
                                     final Element curveA,
                                     final Element e0,
                                     final Element[] Qs,
                                     final Element f) {
        //computeTangent(a, b, c, Vx, Vy, curveA, e0);
        //a = -slope_tangent(V.x, V.y);
        //b = 1;
        //c = -(V.y + aV.x);
        //but we multiply by -2*V.y to avoid division so:
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        for (int i = 0; i < Vs.length; i++) {
            Point V = (Point) Vs[i];
            Point Q = (Point) Qs[i];

            a.set(V.getX()).square().mul(3).add(curveA).negate();
            b.set(V.getY()).twice();
            c.set(a).mul(V.getX()).add(e0.set(b).mul(V.getY())).negate();

            millerStep(f0, a, b, c, (E) Q.getX(), (E) Q.getY());
            f.mul(f0);
        }
    }

    /**
     * Tangent step projective.
     *
     * @param f0 the f 0
     * @param a  the a
     * @param b  the b
     * @param c  the c
     * @param Vx the vx
     * @param Vy the vy
     * @param z  the z
     * @param z2 the z 2
     * @param e0 the e 0
     * @param Qx the qx
     * @param Qy the qy
     * @param f  the f
     */
    protected final void tangentStepProjective(final Point<E> f0,
                                               final Element a, final Element b, final Element c,
                                               final Element Vx, final Element Vy, final Element z,
                                               final Element z2,
                                               final Element e0,
                                               final E Qx, final E Qy,
                                               final Element f) {
        // Compute the tangent line T (aX + bY + c) at point V = (Vx, Vy, z)

        a.set(z2).square();
        b.set(Vx).square();
        a.add(b.add(e0.set(b).twice())).negate();
        // Now:
        // a = -(3x^2 + cca z^4)     with cca = 1

        b.set(e0.set(Vy).twice()).mul(z2).mul(z);
        // Now:
        // b = 2 y z^3

        c.set(Vx).mul(a);
        a.mul(z2);
        c.add(e0.mul(Vy)).negate();

        // Now:
        // a = -3x^2 z^2 - z^6
        // c = 3x^3 + z^4 x - 2x^2 y

        millerStep(f0, a, b, c, Qx, Qy);
        f.mul(f0);
    }

    /**
     * Miller step.
     *
     * @param out the out
     * @param a   the a
     * @param b   the b
     * @param c   the c
     * @param Qx  the qx
     * @param Qy  the qy
     */
    protected abstract void millerStep(Point<E> out,
                                       Element a, Element b, Element c,
                                       E Qx, E Qy);

    /**
     * Compute tangent.
     *
     * @param a      the a
     * @param b      the b
     * @param c      the c
     * @param Vx     the vx
     * @param Vy     the vy
     * @param curveA the curve a
     * @param temp   the temp
     */
    protected final void computeTangent(final Element a, final Element b, final Element c,
                                        final Element Vx, final Element Vy,
                                        final Element curveA,
                                        final Element temp) {
        //a = -slope_tangent(V.x, V.y);
        //b = 1;
        //c = -(V.y + aV.x);
        //but we multiply by -2*V.y to avoid division so:
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        a.set(Vx).square().mul(3).add(curveA).negate();
        b.set(Vy).twice();
        c.set(a).mul(Vx).add(temp.set(b).mul(Vy)).negate();
    }

    /**
     * Compute tangent.
     *
     * @param info   the info
     * @param a      the a
     * @param b      the b
     * @param c      the c
     * @param Vx     the vx
     * @param Vy     the vy
     * @param curveA the curve a
     * @param temp   the temp
     */
    protected final void computeTangent(final MillerPreProcessingInfo info,
                                        final Element a, final Element b, final Element c,
                                        final Element Vx, final Element Vy,
                                        final Element curveA,
                                        final Element temp) {
        //a = -slope_tangent(Z.x, Z.y);
        //b = 1;
        //c = -(Z.y + a * Z.x);
        //but we multiply by 2*Z.y to avoid division

        //a = -Vx * (3 Vx + twicea_2) - a_4;
        //Common curves: a2 = 0 (and cc->a is a_4), so
        //a = -(3 Vx^2 + cc->a)
        //b = 2 * Vy
        //c = -(2 Vy^2 + a Vx);

        a.set(Vx).square();
//        a.add(a).add(a);
        a.mul(3);
        a.add(curveA);
        a.negate();

        b.set(Vy).twice();

        temp.set(b).mul(Vy);
        c.set(a).mul(Vx);
        c.add(temp).negate();

        info.addRow(a, b, c);
    }

    /**
     * Compute the tangent line L (aX + bY + c) through the points V = (Vx, Vy) e V1 = (V1x, V1y).
     *
     * @param a    the coefficient of X of tangent line T.
     * @param b    the coefficient of Y of tangent line T.
     * @param c    the constant term f tangent line T.
     * @param Vx   V's x.
     * @param Vy   V's y.
     * @param V1x  V1's x.
     * @param V1y  V1's y.
     * @param temp temp element.
     */
    protected final void computeLine(final Element a, final Element b, final Element c,
                                     final Element Vx, final Element Vy,
                                     final Element V1x, final Element V1y,
                                     final Element temp) {

        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(temp.set(Vy).mul(V1x));
    }

    /**
     * Compute line.
     *
     * @param info the info
     * @param a    the a
     * @param b    the b
     * @param c    the c
     * @param Vx   the vx
     * @param Vy   the vy
     * @param V1x  the v 1 x
     * @param V1y  the v 1 y
     * @param temp the temp
     */
    protected final void computeLine(final MillerPreProcessingInfo info,
                                     final Element a, final Element b, final Element c,
                                     final Element Vx, final Element Vy,
                                     final Element V1x, final Element V1y,
                                     final Element temp) {
        // a = -(V1y - Vy) / (V1x - Vx);
        // b = 1;
        // c = -(Vy + a * Vx);
        //
        // but we will multiply by V1x - Vx to avoid division, so
        //
        // a = -(V1y - Vy)
        // b = V1x - Vx
        // c = -(Vy b + a Vx);

        a.set(Vy).sub(V1y);
        b.set(V1x).sub(Vx);
        c.set(Vx).mul(V1y).sub(temp.set(Vy).mul(V1x));

        info.addRow(a, b, c);
    }

    /**
     * Lucas even element.
     *
     * @param in       the in
     * @param cofactor the cofactor
     * @return the element
     */
    protected final Element lucasEven(final Point in, final BigInteger cofactor) {
        //assumes cofactor is even
        //mangles in
        //in cannot be out
        if (in.isOne()) {
            return in.duplicate();
        }

        Point out = (Point) in.getField().newElement();
        Point temp = (Point) in.getField().newElement();

        Element in0 = in.getX();
        Element in1 = in.getY();

        Element v0 = out.getX();
        Element v1 = out.getY();

        Element t0 = temp.getX();
        Element t1 = temp.getY();

        t0.set(2);
        t1.set(in0).twice();
        v0.set(t0);
        v1.set(t1);

        int j = cofactor.bitLength() - 1;
        while (true) {
            if (j == 0) {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);
                break;
            }

            if (cofactor.testBit(j)) {
                v0.mul(v1).sub(t1);
                v1.square().sub(t0);
            } else {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);
            }

            j--;
        }

        v0.twice();
        in0.set(t1).mul(v1).sub(v0);

        t1.square().sub(t0).sub(t0);

        v0.set(v1).halve();
        v1.set(in0).div(t1);
        v1.mul(in1);

        return out;
    }

    /**
     * Lucas odd.
     *
     * @param out      the out
     * @param in       the in
     * @param temp     the temp
     * @param cofactor the cofactor
     */
    protected final void lucasOdd(final Point out, final Point in, final Point temp, final BigInteger cofactor) {
        //assumes cofactor is odd
        //overwrites in and temp, out must not be in
        //luckily this touchy routine is only used internally
        //TODO: rewrite to allow (out == in)? would simplify a_finalpow()

        Element in0 = in.getX();
        Element in1 = in.getY();

        Element v0 = out.getX();
        Element v1 = out.getY();

        Element t0 = temp.getX();
        Element t1 = temp.getY();

        t0.set(2);
        t1.set(in0).twice();

        v0.set(t0);
        v1.set(t1);

        int j = cofactor.bitLength() - 1;
        for (; ; ) {
            if (j == 0) {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);

                break;
            }

            if (cofactor.testBit(j)) {
                v0.mul(v1).sub(t1);
                v1.square().sub(t0);

            } else {
                v1.mul(v0).sub(t1);
                v0.square().sub(t0);
            }
            j--;
        }

        v1.twice().sub(in0.set(v0).mul(t1));

        t1.square().sub(t0).sub(t0);
        v1.div(t1);

        v0.halve();
        v1.mul(in1);
    }

    /**
     * The type Miller pre processing info.
     */
    public static class MillerPreProcessingInfo {
        /**
         * The Num row.
         */
        public int numRow = 0;
        /**
         * The Table.
         */
        public final Element[][] table;

        /**
         * Instantiates a new Miller pre processing info.
         *
         * @param size the size
         */
        public MillerPreProcessingInfo(int size) {
            this.table = new Element[size][3];
        }

        /**
         * Instantiates a new Miller pre processing info.
         *
         * @param pairing the pairing
         * @param source  the source
         * @param offset  the offset
         */
        public MillerPreProcessingInfo(Pairing pairing, byte[] source, int offset) {
            PairingStreamReader in = new PairingStreamReader(pairing, source, offset);

            this.numRow = in.readInt();
            this.table = new Element[numRow][3];
            Field field = ((FieldOver) pairing.getG1()).getTargetField();
            for (int i = 0; i < numRow; i++) {
                table[i][0] = in.readFieldElement(field);
                table[i][1] = in.readFieldElement(field);
                table[i][2] = in.readFieldElement(field);
            }
        }

        /**
         * Add row.
         *
         * @param a the a
         * @param b the b
         * @param c the c
         */
        public void addRow(Element a, Element b, Element c) {
            table[numRow][0] = a.duplicate();
            table[numRow][1] = b.duplicate();
            table[numRow][2] = c.duplicate();

            numRow++;
        }

        /**
         * To bytes byte [ ].
         *
         * @return the byte [ ]
         */
        public byte[] toBytes() {
            try {
                PairingStreamWriter out = new PairingStreamWriter(table[0][0].getField().getLengthInBytes() * numRow * 3 + 4);

                out.writeInt(numRow);
                for (int i = 0; i < numRow; i++)  {
                    for (Element element : table[i])
                        out.write(element);
                }
                return out.toBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The type Jacob point.
     */
    public static class JacobPoint {

        private Element x;
        private Element y;
        private Element z;

        /**
         * Instantiates a new Jacob point.
         *
         * @param x the x
         * @param y the y
         * @param z the z
         */
        public JacobPoint(Element x, Element y, Element z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Gets x.
         *
         * @return the x
         */
        public Element getX() {
            return this.x;
        }

        /**
         * Gets y.
         *
         * @return the y
         */
        public Element getY() {
            return this.y;
        }

        /**
         * Gets z.
         *
         * @return the z
         */
        public Element getZ() {
            return this.z;
        }

        /**
         * Is infinity boolean.
         *
         * @return the boolean
         */
        public boolean isInfinity() {
            //return this.equals(JacobPoint.INFINITY);
            return this.z.isZero();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((x == null) ? 0 : x.hashCode());
            result = prime * result + ((y == null) ? 0 : y.hashCode());
            result = prime * result + ((z == null) ? 0 : z.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            JacobPoint other = (JacobPoint) obj;
            if (x == null) {
                if (other.x != null)
                    return false;
            } else if (!x.equals(other.x))
                return false;
            if (y == null) {
                if (other.y != null)
                    return false;
            } else if (!y.equals(other.y))
                return false;
            if (z == null) {
                if (other.z != null)
                    return false;
            } else if (!z.equals(other.z))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "," + z + "]";
        }

        /**
         * Sets x.
         *
         * @param newX the new x
         */
        public void setX(Element newX) {
            this.x = newX;
        }

        /**
         * Sets y.
         *
         * @param newY the new y
         */
        public void setY(Element newY) {
            this.y = newY;
        }

        /**
         * Sets z.
         *
         * @param newZ the new z
         */
        public void setZ(Element newZ) {
            this.z = newZ;
        }


    }

}
