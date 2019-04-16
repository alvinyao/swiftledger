/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.reconfiguration.util;

import bftsmart.tom.util.Logger;
import com.higgschain.trust.consensus.bftsmartcustom.started.custom.CustomKeyLoader;
import com.higgschain.trust.consensus.bftsmartcustom.started.custom.SpringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.StringTokenizer;

/**
 * The type Tom configuration.
 */
public class TOMConfiguration extends Configuration {

    /**
     * The N.
     */
    protected int n;
    /**
     * The F.
     */
    protected int f;
    /**
     * The Request timeout.
     */
    protected int requestTimeout;
    /**
     * The Tom period.
     */
    protected int tomPeriod;
    /**
     * The Paxos high mark.
     */
    protected int paxosHighMark;
    /**
     * The Revival high mark.
     */
    protected int revivalHighMark;
    /**
     * The Timeout high mark.
     */
    protected int timeoutHighMark;
    /**
     * The Reply verification time.
     */
    protected int replyVerificationTime;
    /**
     * The Max batch size.
     */
    protected int maxBatchSize;
    /**
     * The Number of nonces.
     */
    protected int numberOfNonces;
    /**
     * The In queue size.
     */
    protected int inQueueSize;
    /**
     * The Out queue size.
     */
    protected int outQueueSize;
    /**
     * The Apply queue size.
     */
    protected int applyQueueSize;
    /**
     * The Shutdown hook enabled.
     */
    protected boolean shutdownHookEnabled;
    /**
     * The Use sender thread.
     */
    protected boolean useSenderThread;
    /**
     * The Rsa loader.
     */
    protected RSAKeyLoader rsaLoader;
    private int debug;
    private int numNIOThreads;
    private int useMACs;
    private int useSignatures;
    private boolean stateTransferEnabled;
    private int checkpointPeriod;
    private int globalCheckpointPeriod;
    private int useControlFlow;
    private int[] initialView;
    private int ttpId;
    private boolean isToLog;
    private boolean syncLog;
    private boolean parallelLog;
    private boolean logToDisk;
    private boolean isToWriteCkpsToDisk;
    private boolean syncCkp;
    private boolean isBFT;
    private int numRepliers;
    private int numNettyWorkers;

    /**
     * Creates a new instance of TOMConfiguration
     *
     * @param processId the process id
     */
    public TOMConfiguration(int processId) {
        super(processId);
    }

    /**
     * Creates a new instance of TOMConfiguration
     *
     * @param processId  the process id
     * @param configHome the config home
     */
    public TOMConfiguration(int processId, String configHome) {
        super(processId, configHome);
    }

    /**
     * Creates a new instance of TOMConfiguration
     *
     * @param processId     the process id
     * @param configHome    the config home
     * @param hostsFileName the hosts file name
     */
    public TOMConfiguration(int processId, String configHome, String hostsFileName) {
        super(processId, configHome, hostsFileName);
    }

    @Override protected void init() {
        super.init();
        try {
            n = Integer.parseInt(configs.remove("system.servers.num").toString());
            String s = (String)configs.remove("system.servers.f");
            if (s == null) {
                f = (int)Math.ceil((n - 1) / 3);
            } else {
                f = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.shutdownhook");
            shutdownHookEnabled = (s != null) ? Boolean.parseBoolean(s) : false;

            s = (String)configs.remove("system.totalordermulticast.period");
            if (s == null) {
                tomPeriod = n * 5;
            } else {
                tomPeriod = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.totalordermulticast.timeout");
            if (s == null) {
                requestTimeout = 10000;
            } else {
                requestTimeout = Integer.parseInt(s);
                if (requestTimeout < 0) {
                    requestTimeout = 0;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.highMark");
            if (s == null) {
                paxosHighMark = 10000;
            } else {
                paxosHighMark = Integer.parseInt(s);
                if (paxosHighMark < 10) {
                    paxosHighMark = 10;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.revival_highMark");
            if (s == null) {
                revivalHighMark = 10;
            } else {
                revivalHighMark = Integer.parseInt(s);
                if (revivalHighMark < 1) {
                    revivalHighMark = 1;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.timeout_highMark");
            if (s == null) {
                timeoutHighMark = 100;
            } else {
                timeoutHighMark = Integer.parseInt(s);
                if (timeoutHighMark < 1) {
                    timeoutHighMark = 1;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.maxbatchsize");
            if (s == null) {
                maxBatchSize = 100;
            } else {
                maxBatchSize = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.debug");
            if (s == null) {
                Logger.debug = false;
            } else {
                debug = Integer.parseInt(s);
                if (debug == 0) {
                    Logger.debug = false;
                } else {
                    Logger.debug = true;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.replayVerificationTime");
            if (s == null) {
                replyVerificationTime = 0;
            } else {
                replyVerificationTime = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.totalordermulticast.nonces");
            if (s == null) {
                numberOfNonces = 0;
            } else {
                numberOfNonces = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.communication.useSenderThread");
            if (s == null) {
                useSenderThread = false;
            } else {
                useSenderThread = Boolean.parseBoolean(s);
            }

            s = (String)configs.remove("system.communication.numNIOThreads");
            if (s == null) {
                numNIOThreads = 2;
            } else {
                numNIOThreads = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.communication.useMACs");
            if (s == null) {
                useMACs = 0;
            } else {
                useMACs = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.communication.useSignatures");
            if (s == null) {
                useSignatures = 0;
            } else {
                useSignatures = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.totalordermulticast.state_transfer");
            if (s == null) {
                stateTransferEnabled = false;
            } else {
                stateTransferEnabled = Boolean.parseBoolean(s);
            }

            s = (String)configs.remove("system.totalordermulticast.checkpoint_period");
            if (s == null) {
                checkpointPeriod = 1;
            } else {
                checkpointPeriod = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.communication.useControlFlow");
            if (s == null) {
                useControlFlow = 0;
            } else {
                useControlFlow = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.initial.view");
            if (s == null) {
                initialView = new int[n];
                for (int i = 0; i < n; i++) {
                    initialView[i] = i;
                }
            } else {
                StringTokenizer str = new StringTokenizer(s, ",");
                initialView = new int[str.countTokens()];
                for (int i = 0; i < initialView.length; i++) {
                    initialView[i] = Integer.parseInt(str.nextToken());
                }
            }

            s = (String)configs.remove("system.ttp.id");
            if (s == null) {
                ttpId = -1;
            } else {
                ttpId = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.communication.inQueueSize");
            if (s == null) {
                inQueueSize = 1000;
            } else {

                inQueueSize = Integer.parseInt(s);
                if (inQueueSize < 1) {
                    inQueueSize = 1000;
                }

            }

            s = (String)configs.remove("system.communication.outQueueSize");
            if (s == null) {
                outQueueSize = 1000;
            } else {
                outQueueSize = Integer.parseInt(s);
                if (outQueueSize < 1) {
                    outQueueSize = 1000;
                }
            }

            s = (String)configs.remove("system.communication.applyQueueSize");
            if (s == null) {
                applyQueueSize = 1000;
            } else {
                applyQueueSize = Integer.parseInt(s);
                if (applyQueueSize < 1) {
                    applyQueueSize = 1000;
                }
            }

            s = (String)configs.remove("system.totalordermulticast.log");
            if (s != null) {
                isToLog = Boolean.parseBoolean(s);
            } else {
                isToLog = false;
            }

            s = (String)configs.remove("system.totalordermulticast.log_parallel");
            if (s != null) {
                parallelLog = Boolean.parseBoolean(s);
            } else {
                parallelLog = false;
            }

            s = (String)configs.remove("system.totalordermulticast.log_to_disk");
            if (s != null) {
                logToDisk = Boolean.parseBoolean(s);
            } else {
                logToDisk = false;
            }

            s = (String)configs.remove("system.totalordermulticast.sync_log");
            if (s != null) {
                syncLog = Boolean.parseBoolean(s);
            } else {
                syncLog = false;
            }

            s = (String)configs.remove("system.totalordermulticast.checkpoint_to_disk");
            if (s == null) {
                isToWriteCkpsToDisk = false;
            } else {
                isToWriteCkpsToDisk = Boolean.parseBoolean(s);
            }

            s = (String)configs.remove("system.totalordermulticast.sync_ckp");
            if (s == null) {
                syncCkp = false;
            } else {
                syncCkp = Boolean.parseBoolean(s);
            }

            s = (String)configs.remove("system.totalordermulticast.global_checkpoint_period");
            if (s == null) {
                globalCheckpointPeriod = 1;
            } else {
                globalCheckpointPeriod = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.bft");
            isBFT = (s != null) ? Boolean.parseBoolean(s) : true;

            s = (String)configs.remove("system.numrepliers");
            if (s == null) {
                numRepliers = 0;
            } else {
                numRepliers = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.numnettyworkers");
            if (s == null) {
                numNettyWorkers = 0;
            } else {
                numNettyWorkers = Integer.parseInt(s);
            }
            //TODO zyf modified
            //            rsaLoader = new RSAKeyLoader(processId, TOMConfiguration.configHome, defaultKeys);
            rsaLoader = SpringUtil.getBean(CustomKeyLoader.class);
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
        }

    }

    /**
     * Gets view store class.
     *
     * @return the view store class
     */
    public String getViewStoreClass() {
        String s = (String)configs.remove("view.storage.handler");
        if (s == null) {
            return "bftsmartcustom.reconfiguration.views.DefaultViewStorage";
        } else {
            return s;
        }

    }

    /**
     * Is the ttp boolean.
     *
     * @return the boolean
     */
    public boolean isTheTTP() {
        return (this.getTTPId() == this.getProcessId());
    }

    /**
     * Get initial view int [ ].
     *
     * @return the int [ ]
     */
    public final int[] getInitialView() {
        return this.initialView;
    }

    /**
     * Gets ttp id.
     *
     * @return the ttp id
     */
    public int getTTPId() {
        return ttpId;
    }

    /**
     * Gets request timeout.
     *
     * @return the request timeout
     */
    public int getRequestTimeout() {
        return requestTimeout;
    }

    /**
     * Gets reply verification time.
     *
     * @return the reply verification time
     */
    public int getReplyVerificationTime() {
        return replyVerificationTime;
    }

    /**
     * Gets n.
     *
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * Gets f.
     *
     * @return the f
     */
    public int getF() {
        return f;
    }

    /**
     * Gets paxos high mark.
     *
     * @return the paxos high mark
     */
    public int getPaxosHighMark() {
        return paxosHighMark;
    }

    /**
     * Gets revival high mark.
     *
     * @return the revival high mark
     */
    public int getRevivalHighMark() {
        return revivalHighMark;
    }

    /**
     * Gets timeout high mark.
     *
     * @return the timeout high mark
     */
    public int getTimeoutHighMark() {
        return timeoutHighMark;
    }

    /**
     * Gets max batch size.
     *
     * @return the max batch size
     */
    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    /**
     * Is shutdown hook enabled boolean.
     *
     * @return the boolean
     */
    public boolean isShutdownHookEnabled() {
        return shutdownHookEnabled;
    }

    /**
     * Is state transfer enabled boolean.
     *
     * @return the boolean
     */
    public boolean isStateTransferEnabled() {
        return stateTransferEnabled;
    }

    /**
     * Gets in queue size.
     *
     * @return the in queue size
     */
    public int getInQueueSize() {
        return inQueueSize;
    }

    /**
     * Gets out queue size.
     *
     * @return the out queue size
     */
    public int getOutQueueSize() {
        return outQueueSize;
    }

    /**
     * Gets apply queue size.
     *
     * @return the apply queue size
     */
    public int getApplyQueueSize() {
        return applyQueueSize;
    }

    /**
     * Is use sender thread boolean.
     *
     * @return the boolean
     */
    public boolean isUseSenderThread() {
        return useSenderThread;
    }

    /**
     * *
     *
     * @return the number of nio threads
     */
    public int getNumberOfNIOThreads() {
        return numNIOThreads;
    }

    /**
     * Gets number of nonces.
     *
     * @return the numberOfNonces
     */
    public int getNumberOfNonces() {
        return numberOfNonces;
    }

    /**
     * Indicates if signatures should be used (1) or not (0) to authenticate client requests
     *
     * @return the use signatures
     */
    public int getUseSignatures() {
        return useSignatures;
    }

    /**
     * Indicates if MACs should be used (1) or not (0) to authenticate client-server and server-server messages
     *
     * @return the use ma cs
     */
    public int getUseMACs() {
        return useMACs;
    }

    /**
     * Indicates the checkpoint period used when fetching the state from the application
     *
     * @return the checkpoint period
     */
    public int getCheckpointPeriod() {
        return checkpointPeriod;
    }

    /**
     * Is to write ckps to disk boolean.
     *
     * @return the boolean
     */
    public boolean isToWriteCkpsToDisk() {
        return isToWriteCkpsToDisk;
    }

    /**
     * Is to write sync ckp boolean.
     *
     * @return the boolean
     */
    public boolean isToWriteSyncCkp() {
        return syncCkp;
    }

    /**
     * Is to log boolean.
     *
     * @return the boolean
     */
    public boolean isToLog() {
        return isToLog;
    }

    /**
     * Is to write sync log boolean.
     *
     * @return the boolean
     */
    public boolean isToWriteSyncLog() {
        return syncLog;
    }

    /**
     * Log to disk boolean.
     *
     * @return the boolean
     */
    public boolean logToDisk() {
        return logToDisk;
    }

    /**
     * Is to log parallel boolean.
     *
     * @return the boolean
     */
    public boolean isToLogParallel() {
        // TODO Auto-generated method stub
        return parallelLog;
    }

    /**
     * Indicates the checkpoint period used when fetching the state from the application
     *
     * @return the global checkpoint period
     */
    public int getGlobalCheckpointPeriod() {
        return globalCheckpointPeriod;
    }

    /**
     * Indicates if a simple control flow mechanism should be used to avoid an overflow of client requests
     *
     * @return the use control flow
     */
    public int getUseControlFlow() {
        return useControlFlow;
    }

    /**
     * Gets rsa public key.
     *
     * @return the rsa public key
     */
    public PublicKey getRSAPublicKey() {
        try {
            return rsaLoader.loadPublicKey();
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
            return null;
        }

    }

    /**
     * Gets rsa public key.
     *
     * @param id the id
     * @return the rsa public key
     */
    public PublicKey getRSAPublicKey(int id) {
        try {
            return rsaLoader.loadPublicKey(id);
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
            return null;
        }

    }

    /**
     * Gets rsa public key.
     *
     * @param pubKeyStr the pub key str
     * @return the rsa public key
     */
    //TODO zyf add
    public PublicKey getRSAPublicKey(String pubKeyStr) {
        try {
            return rsaLoader.loadPublicKey(pubKeyStr);
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
            return null;
        }

    }

    /**
     * Gets rsa private key.
     *
     * @return the rsa private key
     */
    public PrivateKey getRSAPrivateKey() {
        try {
            return rsaLoader.loadPrivateKey();
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Is bft boolean.
     *
     * @return the boolean
     */
    public boolean isBFT() {

        return this.isBFT;
    }

    /**
     * Gets num repliers.
     *
     * @return the num repliers
     */
    public int getNumRepliers() {
        return numRepliers;
    }

    /**
     * Gets num netty workers.
     *
     * @return the num netty workers
     */
    public int getNumNettyWorkers() {
        return numNettyWorkers;
    }
}
