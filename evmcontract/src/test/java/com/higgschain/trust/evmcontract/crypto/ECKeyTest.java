package com.higgschain.trust.evmcontract.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * @author tangkun
 * @date 2019-03-29
 */
@Slf4j
public class ECKeyTest {

   private String privString = "c85ef7d79691fe79573b1a7064c19c1a9819ebdbd1faaab1a8ec92344438aaf4";
   // private String privString = "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7";

    private BigInteger privateKey = new BigInteger(privString, 16);

    private String pubString = "040947751e3022ecf3016be03ec77ab0ce3c2662b4843898cb068d74f698ccc8ad75aa17564ae80a20bb044ee7a6d903e8e8df624b089c95d66a0570f051e5a05b";
    private String compressedPubString = "030947751e3022ecf3016be03ec77ab0ce3c2662b4843898cb068d74f698ccc8ad";
    private byte[] pubKey = Hex.decode(pubString);

    @Test
    public void verifyTest() {
        String pri = "87385e10d018f971f66cf2c065663d4aa427286f259f85bb8b2438130f4f1ee7";
        ECKey ecKey = ECKey.fromPrivate(Hex.decode(pri));

        String sourceHash = "7d50e029128286afba20cb62cf1b874d76a527a896f3d39167289a9b0446c518";
         ecKey = ECKey.fromPrivate(privateKey);
        byte[] sign = ecKey.sign(Hex.decode(sourceHash)).toByteArray();
        System.out.println(sign.length);
    }


    @Test
    public void testFromPrivateKey() {
        ECKey ecKey = ECKey.fromPrivate(privateKey);
        String sourceHash = "7d50e029128286afba20cb62cf1b874d76a527a896f3d39167289a9b0446c518";
        byte[] sign = ecKey.sign(Hex.decode(sourceHash)).toByteArray();
        System.out.println(sign.length);
//        assertTrue(key.isPubKeyCanonical());
//        assertTrue(key.hasPrivKey());
//        assertArrayEquals(pubKey, key.getPubKey());
    }

    @Test
    public void testECKey() {
        ECKey key = new ECKey();
        assertTrue(key.isPubKeyCanonical());
        assertNotNull(key.getPubKey());
        assertNotNull(key.getPrivKeyBytes());
        log.debug(Hex.toHexString(key.getPrivKeyBytes()) + " :Generated privkey");
        log.debug(Hex.toHexString(key.getPubKey()) + " :Generated pubkey");
    }

}