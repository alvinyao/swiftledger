//package com.higgschain.trust.consensus.bftsmart.started.example;
//
//import com.higgschain.trust.consensus.bft.core.ConsensusClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class Example {
//
//    @Autowired
//    private ConsensusClient consensusClient;
//
//    @PostConstruct
//    public void start() {
//        for (int i=0;;i++) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(50L);
//            } catch (InterruptedException e) {
//                Logger.printError(e.getMessage(),e);
//            }
//            StringCommand stringCommand = new StringCommand("zyf test -- " + i);
//            consensusClient.submit(stringCommand);
//        }
//    }
//
//}
