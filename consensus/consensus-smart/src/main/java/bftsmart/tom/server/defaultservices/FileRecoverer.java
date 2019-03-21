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
package bftsmart.tom.server.defaultservices;

import bftsmart.tom.util.Logger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class FileRecoverer {

    private byte[] ckpHash;
    private int ckpLastConsensusId;
    private int logLastConsensusId;

    private int replicaId;
    private String defaultDir;

    public FileRecoverer(int replicaId, String defaultDir) {
        this.replicaId = replicaId;
        this.defaultDir = defaultDir;
        ckpLastConsensusId = 0;
        logLastConsensusId = 0;
    }

    /**
     * Reads all log messages from the last log file created
     *
     * @return an array with batches of messages executed for each consensus
     */
    //	public CommandsInfo[] getLogState() {
    //		String lastLogFilename = getLatestFile(".log");
    //		if(lastLogFilename != null)
    //			return getLogState(0, lastLogFilename);
    //		return null;
    //	}
    public CommandsInfo[] getLogState(int index, String logPath) {
        RandomAccessFile log = null;

        Logger.println("GETTING LOG FROM " + logPath);
        if ((log = openLogFile(logPath)) != null) {

            CommandsInfo[] logState = recoverLogState(log, index);

            try {
                log.close();
            } catch (IOException e) {
                Logger.printError(e.getMessage(), e);
            }

            return logState;
        }

        return null;
    }

    /**
     * Recover portions of the log for collaborative state transfer.
     *
     * @param start  the index for which the commands start to be collected
     * @param number the number of commands retrieved
     * @return The commands for the period selected
     */
    public CommandsInfo[] getLogState(long pointer, int startOffset, int number, String logPath) {
        RandomAccessFile log = null;

        Logger.println("GETTING LOG FROM " + logPath);
        if ((log = openLogFile(logPath)) != null) {

            CommandsInfo[] logState = recoverLogState(log, pointer, startOffset, number);

            try {
                log.close();
            } catch (IOException e) {
                Logger.printError(e.getMessage(), e);
            }

            return logState;
        }

        return null;
    }

    public byte[] getCkpState(String ckpPath) {
        RandomAccessFile ckp = null;

        Logger.println("GETTING CHECKPOINT FROM " + ckpPath);
        if ((ckp = openLogFile(ckpPath)) != null) {

            byte[] ckpState = recoverCkpState(ckp);

            try {
                ckp.close();
            } catch (IOException e) {
                Logger.printError(e.getMessage(), e);
            }

            return ckpState;
        }

        return null;
    }

    public void recoverCkpHash(String ckpPath) {
        RandomAccessFile ckp = null;

        Logger.println("GETTING HASH FROM CHECKPOINT" + ckpPath);
        if ((ckp = openLogFile(ckpPath)) != null) {
            byte[] ckpHash = null;
            try {
                int ckpSize = ckp.readInt();
                ckp.skipBytes(ckpSize);
                int hashLength = ckp.readInt();
                ckpHash = new byte[hashLength];
                ckp.read(ckpHash);
                Logger.println("--- Last ckp size: " + ckpSize + " Last ckp hash: " + Arrays.toString(ckpHash));
            } catch (Exception e) {
                Logger.printError("State recover was aborted due to an unexpected exception", e);
            }
            this.ckpHash = ckpHash;
        }
    }

    private byte[] recoverCkpState(RandomAccessFile ckp) {
        byte[] ckpState = null;
        try {
            long ckpLength = ckp.length();
            boolean mayRead = true;
            while (mayRead) {
                try {
                    if (ckp.getFilePointer() < ckpLength) {
                        int size = ckp.readInt();
                        if (size > 0) {
                            ckpState = new byte[size];//ckp state
                            int read = ckp.read(ckpState);
                            if (read == size) {
                                int hashSize = ckp.readInt();
                                if (hashSize > 0) {
                                    ckpHash = new byte[hashSize];//ckp hash
                                    read = ckp.read(ckpHash);
                                    if (read == hashSize) {
                                        mayRead = false;
                                    } else {
                                        ckpHash = null;
                                        ckpState = null;
                                    }
                                }
                            } else {
                                mayRead = false;
                                ckp = null;
                            }
                        } else {
                            mayRead = false;
                        }
                    } else {
                        mayRead = false;
                    }
                } catch (Exception e) {
                    Logger.printError(e.getMessage(), e);
                    ckp = null;
                    mayRead = false;
                }
            }
            if (ckp.readInt() == 0) {
                ckpLastConsensusId = ckp.readInt();
                Logger.println("LAST CKP read from file: " + ckpLastConsensusId);
            }
        } catch (Exception e) {
            Logger.printError("State recover was aborted due to an unexpected exception", e);
        }

        return ckpState;
    }

    public void transferLog(SocketChannel sChannel, int index, String logPath) {
        RandomAccessFile log = null;

        Logger.println("GETTING STATE FROM LOG " + logPath);
        if ((log = openLogFile(logPath)) != null) {
            transferLog(log, sChannel, index);
        }
    }

    private void transferLog(RandomAccessFile logFile, SocketChannel sChannel, int index) {
        try {
            long totalBytes = logFile.length();
            Logger.println("---Called transferLog." + totalBytes + " " + (sChannel == null));
            FileChannel fileChannel = logFile.getChannel();
            long bytesTransfered = 0;
            while (bytesTransfered < totalBytes) {
                long bufferSize = 65536;
                if (totalBytes - bytesTransfered < bufferSize) {
                    bufferSize = (int)(totalBytes - bytesTransfered);
                    if (bufferSize <= 0)
                        bufferSize = (int)totalBytes;
                }
                long bytesSent = fileChannel.transferTo(bytesTransfered, bufferSize, sChannel);
                if (bytesSent > 0) {
                    bytesTransfered += bytesSent;
                }
            }
        } catch (Exception e) {
            Logger.printError("State recover was aborted due to an unexpected exception", e);
        }
    }

    public void transferCkpState(SocketChannel sChannel, String ckpPath) {
        RandomAccessFile ckp = null;

        Logger.println("GETTING CHECKPOINT FROM " + ckpPath);
        if ((ckp = openLogFile(ckpPath)) != null) {

            transferCkpState(ckp, sChannel);

            try {
                ckp.close();
            } catch (IOException e) {
                Logger.printError(e.getMessage(), e);
            }
        }
    }

    private void transferCkpState(RandomAccessFile ckp, SocketChannel sChannel) {
        try {
            long milliInit = System.currentTimeMillis();
            Logger.println("--- Sending checkpoint." + ckp.length() + " " + (sChannel == null));
            FileChannel fileChannel = ckp.getChannel();
            long totalBytes = ckp.length();
            long bytesTransfered = 0;
            while (bytesTransfered < totalBytes) {
                long bufferSize = 65536;
                if (totalBytes - bytesTransfered < bufferSize) {
                    bufferSize = (int)(totalBytes - bytesTransfered);
                    if (bufferSize <= 0)
                        bufferSize = (int)totalBytes;
                }
                long bytesRead = fileChannel.transferTo(bytesTransfered, bufferSize, sChannel);
                if (bytesRead > 0) {
                    bytesTransfered += bytesRead;
                }
            }
            Logger.println(
                "---Took " + (System.currentTimeMillis() - milliInit) + " milliseconds to transfer the checkpoint");
            fileChannel.close();
        } catch (Exception e) {
            Logger.printError("State recover was aborted due to an unexpected exception", e);
        }
    }

    public byte[] getCkpStateHash() {
        return ckpHash;
    }

    public int getCkpLastConsensusId() {
        return ckpLastConsensusId;
    }

    public int getLogLastConsensusId() {
        return logLastConsensusId;
    }

    private RandomAccessFile openLogFile(String file) {
        try {
            return new RandomAccessFile(file, "r");
        } catch (Exception e) {
            Logger.printError(e.getMessage(), e);
        }
        return null;
    }

    private CommandsInfo[] recoverLogState(RandomAccessFile log, int endOffset) {
        try {
            long logLength = log.length();
            ArrayList<CommandsInfo> state = new ArrayList<CommandsInfo>();
            int recoveredBatches = 0;
            boolean mayRead = true;
            Logger.println(
                "filepointer: " + log.getFilePointer() + " loglength " + logLength + " endoffset " + endOffset);
            while (mayRead) {
                try {
                    if (log.getFilePointer() < logLength) {
                        int size = log.readInt();
                        if (size > 0) {
                            byte[] bytes = new byte[size];
                            int read = log.read(bytes);
                            if (read == size) {
                                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                                ObjectInputStream ois = new ObjectInputStream(bis);
                                state.add((CommandsInfo)ois.readObject());
                                if (++recoveredBatches == endOffset) {
                                    Logger.println("read all " + endOffset + " log messages");
                                    return state.toArray(new CommandsInfo[state.size()]);
                                }
                            } else {
                                mayRead = false;
                                Logger.println("STATE CLEAR");
                                state.clear();
                            }
                        } else {
                            logLastConsensusId = log.readInt();
                            Logger.println("ELSE 1. Recovered batches: " + recoveredBatches);
                            Logger.println(", logLastConsensusId: " + logLastConsensusId);
                            return state.toArray(new CommandsInfo[state.size()]);
                        }
                    } else {
                        Logger.println("ELSE 2 " + recoveredBatches);
                        mayRead = false;
                    }
                } catch (Exception e) {
                    Logger.printError(e.getMessage(), e);
                    state.clear();
                    mayRead = false;
                }
            }
        } catch (Exception e) {
            Logger.printError("State recover was aborted due to an unexpected exception", e);
        }

        return null;
    }

    /**
     * Searches the log file and retrieves the portion selected.
     *
     * @param log    The log file
     * @param start  The offset to start retrieving commands
     * @param number The number of commands retrieved
     * @return The commands for the period selected
     */
    private CommandsInfo[] recoverLogState(RandomAccessFile log, long pointer, int startOffset, int number) {
        try {
            long logLength = log.length();
            ArrayList<CommandsInfo> state = new ArrayList<CommandsInfo>();
            int recoveredBatches = 0;
            boolean mayRead = true;

            log.seek(pointer);

            int index = 0;
            while (index < startOffset) {
                int size = log.readInt();
                byte[] bytes = new byte[size];
                log.read(bytes);
                index++;
            }

            while (mayRead) {

                try {
                    if (log.getFilePointer() < logLength) {
                        int size = log.readInt();

                        if (size > 0) {
                            byte[] bytes = new byte[size];
                            int read = log.read(bytes);
                            if (read == size) {
                                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                                ObjectInputStream ois = new ObjectInputStream(bis);

                                state.add((CommandsInfo)ois.readObject());

                                if (++recoveredBatches == number) {
                                    return state.toArray(new CommandsInfo[state.size()]);
                                }
                            } else {
                                Logger.println("recoverLogState (pointer,offset,number) STATE CLEAR");
                                mayRead = false;
                                state.clear();
                            }
                        } else {
                            Logger.println("recoverLogState (pointer,offset,number) ELSE 1");
                            mayRead = false;
                        }
                    } else {
                        Logger.println("recoverLogState (pointer,offset,number) ELSE 2 " + recoveredBatches);
                        mayRead = false;
                    }
                } catch (Exception e) {
                    Logger.printError(e.getMessage(), e);
                    state.clear();
                    mayRead = false;
                }
            }
        } catch (Exception e) {
            Logger.printError("State recover was aborted due to an unexpected exception", e);
        }

        return null;
    }

    public String getLatestFile(String extention) {
        File directory = new File(defaultDir);
        String latestFile = null;
        if (directory.isDirectory()) {
            File[] serverLogs = directory.listFiles(new FileListFilter(replicaId, extention));
            long timestamp = 0;
            for (File f : serverLogs) {
                String[] nameItems = f.getName().split("\\.");
                long filets = new Long(nameItems[1]).longValue();
                if (filets > timestamp) {
                    timestamp = filets;
                    latestFile = f.getAbsolutePath();
                }
            }
        }
        return latestFile;
    }

    private class FileListFilter implements FilenameFilter {

        private int id;
        private String extention;

        public FileListFilter(int id, String extention) {
            this.id = id;
            this.extention = extention;
        }

        public boolean accept(File directory, String filename) {
            boolean fileOK = false;

            if (id >= 0) {
                if (filename.startsWith(id + ".") && filename.endsWith(extention)) {
                    fileOK = true;
                }
            }

            return fileOK;
        }
    }

}