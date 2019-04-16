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
package bftsmart.communication.server;

import bftsmart.communication.SystemMessage;
import bftsmart.reconfiguration.ServerViewController;
import bftsmart.tom.ServiceReplica;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Servers communication layer.
 *
 * @author alysson
 */
public class ServersCommunicationLayer extends Thread {

    private ServerViewController controller;
    private LinkedBlockingQueue<SystemMessage> inQueue;
    private Hashtable<Integer, ServerConnection> connections = new Hashtable<Integer, ServerConnection>();
    private ServerSocket serverSocket;
    private int me;
    private boolean doWork = true;
    private Lock connectionsLock = new ReentrantLock();
    private ReentrantLock waitViewLock = new ReentrantLock();
    //private Condition canConnect = waitViewLock.newCondition();
    private List<PendingConnection> pendingConn = new LinkedList<PendingConnection>();
    private ServiceReplica replica;
    private SecretKey selfPwd;
    private static final String PASSWORD = "commsyst";

    /**
     * Instantiates a new Servers communication layer.
     *
     * @param controller the controller
     * @param inQueue    the in queue
     * @param replica    the replica
     * @throws Exception the exception
     */
    public ServersCommunicationLayer(ServerViewController controller, LinkedBlockingQueue<SystemMessage> inQueue,
        ServiceReplica replica) throws Exception {

        this.controller = controller;
        this.inQueue = inQueue;
        this.me = controller.getStaticConf().getProcessId();
        this.replica = replica;

        //Try connecting if a member of the current view. Otherwise, wait until the Join has been processed!
        if (controller.isInCurrentView()) {
            int[] initialV = controller.getCurrentViewAcceptors();
            for (int i = 0; i < initialV.length; i++) {
                if (initialV[i] != me) {
                    getConnection(initialV[i]);
                }
            }
        }

        serverSocket = new ServerSocket(
            controller.getStaticConf().getServerToServerPort(controller.getStaticConf().getProcessId()));

        SecretKeyFactory fac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        PBEKeySpec spec = new PBEKeySpec(PASSWORD.toCharArray());
        selfPwd = fac.generateSecret(spec);

        serverSocket.setSoTimeout(10000);
        serverSocket.setReuseAddress(true);

        start();
    }

    /**
     * Gets secret key.
     *
     * @param id the id
     * @return the secret key
     */
    public SecretKey getSecretKey(int id) {
        if (id == controller.getStaticConf().getProcessId())
            return selfPwd;
        else
            return connections.get(id).getSecretKey();
    }

    /**
     * Update connections.
     */
    //******* EDUARDO BEGIN **************//
    public void updateConnections() {
        connectionsLock.lock();

        if (this.controller.isInCurrentView()) {

            Iterator<Integer> it = this.connections.keySet().iterator();
            List<Integer> toRemove = new LinkedList<Integer>();
            while (it.hasNext()) {
                int rm = it.next();
                if (!this.controller.isCurrentViewMember(rm)) {
                    toRemove.add(rm);
                }
            }
            for (int i = 0; i < toRemove.size(); i++) {
                this.connections.remove(toRemove.get(i)).shutdown();
            }

            int[] newV = controller.getCurrentViewAcceptors();
            for (int i = 0; i < newV.length; i++) {
                if (newV[i] != me) {
                    getConnection(newV[i]);
                }
            }
        } else {

            Iterator<Integer> it = this.connections.keySet().iterator();
            while (it.hasNext()) {
                this.connections.get(it.next()).shutdown();
            }
        }

        connectionsLock.unlock();
    }

    private ServerConnection getConnection(int remoteId) {
        connectionsLock.lock();
        ServerConnection ret = this.connections.get(remoteId);
        if (ret == null) {
            ret = new ServerConnection(controller, null, remoteId, this.inQueue, this.replica);
            this.connections.put(remoteId, ret);
        }
        connectionsLock.unlock();
        return ret;
    }
    //******* EDUARDO END **************//

    /**
     * Send.
     *
     * @param targets the targets
     * @param sm      the sm
     * @param useMAC  the use mac
     */
    public final void send(int[] targets, SystemMessage sm, boolean useMAC) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(248);
        try {
            new ObjectOutputStream(bOut).writeObject(sm);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] data = bOut.toByteArray();

        for (int i : targets) {
            try {
                if (i == me) {
                    sm.authenticated = true;
                    inQueue.put(sm);
                } else {
                    //Logger.println(("Going to send message to: "+i);
                    //******* EDUARDO BEGIN **************//
                    //connections[i].send(data);
                    getConnection(i).send(data, useMAC);
                    //******* EDUARDO END **************//
                }
            } catch (InterruptedException ex) {
                bftsmart.tom.util.Logger.printError(ex.getMessage(), ex);
            }
        }
    }

    /**
     * Shutdown.
     */
    public void shutdown() {

        bftsmart.tom.util.Logger.println("Shutting down replica sockets");

        doWork = false;

        //******* EDUARDO BEGIN **************//
        int[] activeServers = controller.getCurrentViewAcceptors();

        for (int i = 0; i < activeServers.length; i++) {
            //if (connections[i] != null) {
            //  connections[i].shutdown();
            //}
            if (me != activeServers[i]) {
                getConnection(activeServers[i]).shutdown();
            }
        }
    }

    /**
     * Join view received.
     */
    //******* EDUARDO BEGIN **************//
    public void joinViewReceived() {
        waitViewLock.lock();
        for (int i = 0; i < pendingConn.size(); i++) {
            PendingConnection pc = pendingConn.get(i);
            try {
                establishConnection(pc.s, pc.remoteId);
            } catch (Exception e) {
                bftsmart.tom.util.Logger.printError(e.getMessage(), e);
            }
        }

        pendingConn.clear();

        waitViewLock.unlock();
    }
    //******* EDUARDO END **************//

    @Override public void run() {
        while (doWork) {
            try {

                //Logger.println(("Waiting for server connections");

                Socket newSocket = serverSocket.accept();

                ServersCommunicationLayer.setSocketOptions(newSocket);
                int remoteId = new DataInputStream(newSocket.getInputStream()).readInt();

                //******* EDUARDO BEGIN **************//
                if (!this.controller.isInCurrentView() && (this.controller.getStaticConf().getTTPId() != remoteId)) {
                    waitViewLock.lock();
                    pendingConn.add(new PendingConnection(newSocket, remoteId));
                    waitViewLock.unlock();
                } else {
                    establishConnection(newSocket, remoteId);
                }
                //******* EDUARDO END **************//

            } catch (SocketTimeoutException ex) {
                //timeout on the accept... do nothing
            } catch (IOException ex) {
                Logger.getLogger(ServersCommunicationLayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServersCommunicationLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        Logger.getLogger(ServersCommunicationLayer.class.getName())
            .log(Level.INFO, "ServerCommunicationLayer stopped.");
    }

    //******* EDUARDO BEGIN **************//
    private void establishConnection(Socket newSocket, int remoteId) throws IOException {
        if ((this.controller.getStaticConf().getTTPId() == remoteId) || this.controller.isCurrentViewMember(remoteId)) {
            connectionsLock.lock();
            //Logger.println(("Vai se conectar com: "+remoteId);
            if (this.connections.get(remoteId) == null) { //This must never happen!!!
                //first time that this connection is being established
                //Logger.println(("THIS DOES NOT HAPPEN....."+remoteId);
                this.connections.put(remoteId, new ServerConnection(controller, newSocket, remoteId, inQueue, replica));
            } else {
                //reconnection
                this.connections.get(remoteId).reconnect(newSocket);
            }
            connectionsLock.unlock();

        } else {
            //Logger.println(("Closing connection of: "+remoteId);
            newSocket.close();
        }
    }
    //******* EDUARDO END **************//

    /**
     * Sets socket options.
     *
     * @param socket the socket
     */
    public static void setSocketOptions(Socket socket) {
        try {
            socket.setTcpNoDelay(true);
        } catch (SocketException ex) {
            Logger.getLogger(ServersCommunicationLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override public String toString() {
        String str = "inQueue=" + inQueue.toString();

        int[] activeServers = controller.getCurrentViewAcceptors();

        for (int i = 0; i < activeServers.length; i++) {

            //for(int i=0; i<connections.length; i++) {
            // if(connections[i] != null) {
            if (me != activeServers[i]) {
                str += ", connections[" + activeServers[i] + "]: outQueue=" + getConnection(activeServers[i]).outQueue;
            }
        }

        return str;
    }

    /**
     * The type Pending connection.
     */
    //******* EDUARDO BEGIN: List entry that stores pending connections,
    // as a server may accept connections only after learning the current view,
    // i.e., after receiving the response to the join*************//
    // This is for avoiding that the server accepts connectsion from everywhere
    public class PendingConnection {

        /**
         * The S.
         */
        public Socket s;
        /**
         * The Remote id.
         */
        public int remoteId;

        /**
         * Instantiates a new Pending connection.
         *
         * @param s        the s
         * @param remoteId the remote id
         */
        public PendingConnection(Socket s, int remoteId) {
            this.s = s;
            this.remoteId = remoteId;
        }
    }

    //******* EDUARDO END **************//

    /**
     * Gets connections.
     *
     * @return the connections
     */
    //TODO zyfmodified
    public Hashtable<Integer, ServerConnection> getConnections() {
        return connections;
    }
}
