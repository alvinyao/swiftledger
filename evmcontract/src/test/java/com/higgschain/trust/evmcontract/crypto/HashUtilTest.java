package com.higgschain.trust.evmcontract.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;

import static org.junit.Assert.*;

/**
 * @author tangkun
 * @date 2019-04-17
 */
@Slf4j
public class HashUtilTest {

    @Test
    public void testSha256() throws Exception {
     // log.info("hash:{}", Hex.toHexString(HashUtil.sha256("123".getBytes())));
      log.info("hash:{}", Hex.toHexString("123".getBytes()));

    }
}