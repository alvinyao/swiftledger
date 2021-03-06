package com.higgschain.trust.zkproof;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;

/**
 * The type Bgn encryption.
 */
public class BGNEncryption implements HomomorphicEncryption {

    /**
     * The constant start.
     */
    public static final String start = "start";
    /**
     * The constant end.
     */
    public static final String end = "end";
	private BGNKey pk;

    /**
     * Instantiates a new Bgn encryption.
     *
     * @param bits the bits
     */
    BGNEncryption(int bits){
		pk = new BGNKey(bits);
	}

    /**
     * Instantiates a new Bgn encryption.
     *
     * @param key the key
     */
    BGNEncryption(String key){
		pk = new BGNKey(key);
	}

    /**
     * Encrypt element.
     *
     * @param msg the msg
     * @param r   the r
     * @return the element
     */
    public Element encrypt(BigInteger msg, BigInteger r) {
		//BigInteger t = BigIntegerUtils.getRandom(PK.getN());
		Field f = pk.getField();
		Element A = f.newElement();
		Element B = f.newElement();
		Element C = f.newElement();
		A = A.set(pk.getP());
		A = A.mul(msg);
		B = B.set(pk.getQ());
		B = B.mul(r);
		C = C.set(A);
		C = C.add(B);
		return C;
	}

    /**
     * Add element.
     *
     * @param A the a
     * @param B the b
     * @return the element
     */
    public Element add(Element A, Element B) {
		//BigInteger t = BigIntegerUtils.getRandom(PK.getN());
		Field f = pk.getField();
		Element output = f.newElement();
		Element aux = f.newElement();
		aux.set(pk.getQ());
		//aux.mul(t);
		output.set(A);
		output.add(B);
		//output.add(aux);
		return output;
	}

    /**
     * Mul element.
     *
     * @param C the c
     * @param D the d
     * @return the element
     */
    public Element mul(Element C, Element D) {
		BigInteger t = BigIntegerUtils.getRandom(pk.getN());

		Element T = pk.doPairing(C, D);

		Element K = pk.doPairing(pk.getQ(), pk.getQ());
		K = K.pow(t);
		return T.mul(K);
	}

    /**
     * Decrypt mul string.
     *
     * @param PK the pk
     * @param sk the sk
     * @param C  the c
     * @return the string
     */
    public String decryptMul(BGNKey PK, BigInteger sk, Element C) {
		Element PSK = PK.doPairing(PK.getP(), PK.getP());
		PSK.pow(sk);

		Element CSK = C.duplicate();
		CSK.pow(sk);
		Element aux = PSK.duplicate();

		BigInteger m = new BigInteger("1");
		while (!aux.isEqual(CSK)) {
			aux = aux.mul(PSK);
			m = m.add(BigInteger.valueOf(1));
		}
		return m.toString();
	}

    /**
     * Decrypt string.
     *
     * @param sk the sk
     * @param C  the c
     * @return the string
     */
    public String decrypt(BigInteger sk, Element C) {
		Field f = pk.getField();
		Element T = f.newElement();
		Element K = f.newElement();
		Element aux = f.newElement();
		T = T.set(pk.getP());
		T = T.mul(sk);
		K = K.set(C);
		K = K.mul(sk);
		aux = aux.set(T);
		BigInteger m = new BigInteger("1");
		while (!aux.isEqual(K)) {
			// This is a brute force implementation of finding the discrete
			// logarithm.
			// Performance may be improved using algorithms such as Pollard's
			// Kangaroo.
			aux = aux.add(T);
			m = m.add(BigInteger.valueOf(1));
		}
		return m.toString();
	}

	public String exportFullKey() {
		return pk.exportFullKey();
	}

	public String exportPubKey() {
		return  pk.exportPubKey();
	}

	public boolean hasFullKey() {
		if (pk != null){
			return pk.hasFullKey();
		}

		return false;
	}

	public boolean hasPubKey() {
		if (pk != null){
			return pk.hasPubKey();
		}
		return false;
	}

	public String cipherAdd(String em1, String em2) {
        if (hasPubKey()) {
            Field f = pk.getField();
            Element output = f.newElement();
            Element A = f.newElement();
            Element B = f.newElement();
            A.setFromBytes(Base58.decode(em1));
            B.setFromBytes(Base58.decode(em2));
            output.set(A);
            output.add(B);
            return Base58.encode(output.toBytes());
        }
        else{
            return Base58.encode(BigInteger.ZERO.toByteArray());
        }
	}

	public BigInteger Decryption(String em) {

		if (this.hasFullKey()) {
			Field f = pk.getField();
			Element T = f.newElement();
			Element K = f.newElement();
			Element aux = f.newElement();
			T = T.set(pk.getP());
			T = T.mul(pk.getParam().getBigInteger("n1"));
			K.setFromBytes(Base58.decode(em));
			K = K.mul(pk.getParam().getBigInteger("n1"));
			aux = aux.set(T);
			BigInteger m = new BigInteger("1");
			while (!aux.isEqual(K)) {
				// This is a brute force implementation of finding the discrete
				// logarithm.
				// Performance may be improved using algorithms such as Pollard's
				// Kangaroo.
				aux = aux.add(T);
				m = m.add(BigInteger.valueOf(1));
			}
			return m;
		}

		return  BigInteger.ZERO;
	}

    public String Encryption(BigInteger b, BigInteger r) {
	    if (hasPubKey()){
            Field f = pk.getField();
            Element A = f.newElement();
            Element B = f.newElement();
            Element C = f.newElement();
            A = A.set(pk.getP());
            A = A.mul(b);
            B = B.set(pk.getQ());
            B = B.mul(r);
            C = C.set(A);
            C = C.add(B);
            return Base58.encode(C.toBytes());
        }
        else{
	        return Base58.encode(BigInteger.ZERO.toByteArray());
        }
    }

	public boolean tooBig(BigInteger b) {
        return b.bitLength() >= pk.getN().bitLength() / 4;
    }

	public boolean tooBigRandom(BigInteger r) {
		return tooBig(r);
	}

}