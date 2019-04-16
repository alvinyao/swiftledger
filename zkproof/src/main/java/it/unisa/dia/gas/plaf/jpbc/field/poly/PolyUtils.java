package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;

/**
 * The type Poly utils.
 *
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyUtils {

    /**
     * Const mul poly element.
     *
     * @param a    the a
     * @param poly the poly
     * @return the poly element
     */
    public static PolyElement constMul(Element a, PolyElement poly) {
        int n = poly.getCoefficients().size();

        PolyElement res = poly.getField().newElement();
        res.ensureSize(n);

        for (int i = 0; i < n; i++) {
            res.getCoefficient(i).set(a).mul(poly.getCoefficient(i));
        }
        res.removeLeadingZeroes();

        return res;
    }

    /**
     * Div.
     *
     * @param quot the quot
     * @param rem  the rem
     * @param a    the a
     * @param b    the b
     */
    public static void div(Element quot, Element rem, PolyElement a, PolyElement b) {
        if (b.isZero())
            throw new IllegalArgumentException("Division by zero!");

        int n = b.getDegree();
        int m = a.getDegree();

        if (n > m) {
            rem.set(a);
            quot.setToZero();

            return;
        }

        int k = m - n;

        PolyElement r = a.duplicate();
        PolyElement q = a.getField().newElement();
        q.ensureSize(k + 1);

        Element temp = a.getField().getTargetField().newElement();
        Element bn = b.getCoefficient(n).duplicate().invert();

        while (k >= 0) {
            Element qk = q.getCoefficient(k);
            qk.set(bn).mul(r.getCoefficient(m));

            for (int i = 0; i <= n; i++) {
                temp.set(qk).mul(b.getCoefficient(i));
                r.getCoefficient(i + k).sub(temp);
            }
            k--; m--;
        }
        r.removeLeadingZeroes();
        
        quot.set(q);
        rem.set(r);
    }

    /**
     * Reminder.
     *
     * @param rem the rem
     * @param a   the a
     * @param b   the b
     */
    public static void reminder(Element rem, PolyElement a, PolyElement b) {
        if (b.isZero())
            throw new IllegalArgumentException("Division by zero!");

        int n = b.getDegree();
        int m = a.getDegree();

        if (n > m) {
            rem.set(a);

            return;
        }

        int k = m - n;

        PolyElement r = a.duplicate();
        PolyElement q = a.getField().newElement();
        q.ensureSize(k + 1);

        Element temp = a.getField().getTargetField().newElement();
        Element bn = b.getCoefficient(n).duplicate().invert();

        while (k >= 0) {
            Element qk = q.getCoefficient(k);
            qk.set(bn).mul(r.getCoefficient(m));

            for (int i = 0; i <= n; i++) {
                temp.set(qk).mul(b.getCoefficient(i));
                r.getCoefficient(i + k).sub(temp);
            }
            k--; m--;
        }
        r.removeLeadingZeroes();

        rem.set(r);
    }

}
