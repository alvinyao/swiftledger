package com.higgschain.trust.consensus.p2pvalid.api.controller;

import com.higgschain.trust.consensus.p2pvalid.example.StringValidConsensus;
import com.higgschain.trust.consensus.p2pvalid.example.slave.BlockHeader;
import com.higgschain.trust.consensus.p2pvalid.example.slave.ValidateCommand;
import com.higgschain.trust.consensus.p2pvalid.p2pBaseTest;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * The type P 2 p consensus controller test.
 */
@Slf4j
public class P2pConsensusControllerTest extends p2pBaseTest {

    private StringValidConsensus stringValidConsensus;

    /**
     * Test receive command.
     *
     * @throws Exception the exception
     */
    @Test()
    public void testReceiveCommand() throws Exception {
        BlockHeader header = new BlockHeader();
        header.setHeight(10L);
        header.setPreviousHash("abc");
        int num = 1;
        while (num > 0) {
            log.info("wait for service registry ........................");
            Thread.sleep(2000);
            ValidateCommand validateCommand = new ValidateCommand(header.getHeight()+ new Random().nextInt(1000), header, -1);
            stringValidConsensus.submit(validateCommand);
            num--;
        }
        Thread.sleep(300000);
    }
}