/*
 * Copyright by the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.higgschain.trust.common.crypto.ecc;

import org.spongycastle.math.ec.ECCurve;
import org.spongycastle.math.ec.ECFieldElement;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A wrapper around ECPoint that delays decoding of the point for as long as
 * possible. This is useful because point encode/decode in Bouncy Castle is
 * quite slow especially on Dalvik, as it often involves
 * decompression/recompression.
 */
public class LazyECPoint {
    // If curve is set, bits is also set. If curve is unset, point is set and
    // bits is unset. Point can be set along
    // with curve and bits when the cached form has been accessed and thus must
    // have been converted.

    private final ECCurve curve;
    private final byte[] bits;

    // This field is effectively final - once set it won't change again. However
    // it can be set after
    // construction.
    
    private ECPoint point;

    /**
     * Instantiates a new Lazy ec point.
     *
     * @param curve the curve
     * @param bits  the bits
     */
    public LazyECPoint(ECCurve curve, byte[] bits) {
        this.curve = curve;
        this.bits = bits;
    }

    /**
     * Instantiates a new Lazy ec point.
     *
     * @param point the point
     */
    public LazyECPoint(ECPoint point) {
        this.point = checkNotNull(point);
        this.curve = null;
        this.bits = null;
    }

    /**
     * Get ec point.
     *
     * @return the ec point
     */
    public ECPoint get() {
        if (point == null)
            point = curve.decodePoint(bits);
        return point;
    }

    // Delegated methods.

    /**
     * Gets detached point.
     *
     * @return the detached point
     */
    public ECPoint getDetachedPoint() {
        return get().getDetachedPoint();
    }

    /**
     * Get encoded byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getEncoded() {
        if (bits != null)
            return Arrays.copyOf(bits, bits.length);
        else
            return get().getEncoded();
    }

    /**
     * Is infinity boolean.
     *
     * @return the boolean
     */
    public boolean isInfinity() {
        return get().isInfinity();
    }

    /**
     * Times pow 2 ec point.
     *
     * @param e the e
     * @return the ec point
     */
    public ECPoint timesPow2(int e) {
        return get().timesPow2(e);
    }

    /**
     * Gets y coord.
     *
     * @return the y coord
     */
    public ECFieldElement getYCoord() {
        return get().getYCoord();
    }

    /**
     * Get z coords ec field element [ ].
     *
     * @return the ec field element [ ]
     */
    public ECFieldElement[] getZCoords() {
        return get().getZCoords();
    }

    /**
     * Is normalized boolean.
     *
     * @return the boolean
     */
    public boolean isNormalized() {
        return get().isNormalized();
    }

    /**
     * Is compressed boolean.
     *
     * @return the boolean
     */
    public boolean isCompressed() {
        if (bits != null)
            return bits[0] == 2 || bits[0] == 3;
        else
            return get().isCompressed();
    }

    /**
     * Multiply ec point.
     *
     * @param k the k
     * @return the ec point
     */
    public ECPoint multiply(BigInteger k) {
        return get().multiply(k);
    }

    /**
     * Subtract ec point.
     *
     * @param b the b
     * @return the ec point
     */
    public ECPoint subtract(ECPoint b) {
        return get().subtract(b);
    }

    /**
     * Is valid boolean.
     *
     * @return the boolean
     */
    public boolean isValid() {
        return get().isValid();
    }

    /**
     * Scale y ec point.
     *
     * @param scale the scale
     * @return the ec point
     */
    public ECPoint scaleY(ECFieldElement scale) {
        return get().scaleY(scale);
    }

    /**
     * Gets x coord.
     *
     * @return the x coord
     */
    public ECFieldElement getXCoord() {
        return get().getXCoord();
    }

    /**
     * Scale x ec point.
     *
     * @param scale the scale
     * @return the ec point
     */
    public ECPoint scaleX(ECFieldElement scale) {
        return get().scaleX(scale);
    }

    /**
     * Equals boolean.
     *
     * @param other the other
     * @return the boolean
     */
    public boolean equals(ECPoint other) {
        return get().equals(other);
    }

    /**
     * Negate ec point.
     *
     * @return the ec point
     */
    public ECPoint negate() {
        return get().negate();
    }

    /**
     * Three times ec point.
     *
     * @return the ec point
     */
    public ECPoint threeTimes() {
        return get().threeTimes();
    }

    /**
     * Gets z coord.
     *
     * @param index the index
     * @return the z coord
     */
    public ECFieldElement getZCoord(int index) {
        return get().getZCoord(index);
    }

    /**
     * Get encoded byte [ ].
     *
     * @param compressed the compressed
     * @return the byte [ ]
     */
    public byte[] getEncoded(boolean compressed) {
        if (compressed == isCompressed() && bits != null)
            return Arrays.copyOf(bits, bits.length);
        else
            return get().getEncoded(compressed);
    }

    /**
     * Add ec point.
     *
     * @param b the b
     * @return the ec point
     */
    public ECPoint add(ECPoint b) {
        return get().add(b);
    }

    /**
     * Twice plus ec point.
     *
     * @param b the b
     * @return the ec point
     */
    public ECPoint twicePlus(ECPoint b) {
        return get().twicePlus(b);
    }

    /**
     * Gets curve.
     *
     * @return the curve
     */
    public ECCurve getCurve() {
        return get().getCurve();
    }

    /**
     * Normalize ec point.
     *
     * @return the ec point
     */
    public ECPoint normalize() {
        return get().normalize();
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public ECFieldElement getY() {
        return this.normalize().getYCoord();
    }

    /**
     * Twice ec point.
     *
     * @return the ec point
     */
    public ECPoint twice() {
        return get().twice();
    }

    /**
     * Gets affine y coord.
     *
     * @return the affine y coord
     */
    public ECFieldElement getAffineYCoord() {
        return get().getAffineYCoord();
    }

    /**
     * Gets affine x coord.
     *
     * @return the affine x coord
     */
    public ECFieldElement getAffineXCoord() {
        return get().getAffineXCoord();
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public ECFieldElement getX() {
        return this.normalize().getXCoord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return Arrays.equals(getCanonicalEncoding(), ((LazyECPoint) o).getCanonicalEncoding());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getCanonicalEncoding());
    }

    private byte[] getCanonicalEncoding() {
        return getEncoded(true);
    }
}