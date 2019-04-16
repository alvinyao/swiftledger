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
import com.higgschain.trust.consensus.bftsmartcustom.started.custom.SpringUtil;
import com.higgschain.trust.consensus.bftsmartcustom.started.custom.config.SmartConfig;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Map;

/**
 * The type Configuration.
 */
public class Configuration {

    /**
     * The Process id.
     */
    protected int processId;
    /**
     * The Channels blocking.
     */
    protected boolean channelsBlocking;
    /**
     * The Dh p.
     */
    protected BigInteger DH_P;
    /**
     * The Dh g.
     */
    protected BigInteger DH_G;
    /**
     * The Auto connect limit.
     */
    protected int autoConnectLimit;
    /**
     * The Configs.
     */
    protected Map<String, String> configs;
    /**
     * The Hosts.
     */
    protected HostsConfig hosts;

    private String hmacAlgorithm = "HmacSha1";
    private int hmacSize = 160;

    /**
     * The constant configHome.
     */
    protected static String configHome = "";

    /**
     * The constant hostsFileName.
     */
    protected static String hostsFileName = "";

    /**
     * The Default keys.
     */
    protected boolean defaultKeys = false;

    /**
     * Instantiates a new Configuration.
     *
     * @param procId the proc id
     */
    public Configuration(int procId) {
        processId = procId;
        init();
    }

    /**
     * Instantiates a new Configuration.
     *
     * @param processId       the process id
     * @param configHomeParam the config home param
     */
    public Configuration(int processId, String configHomeParam) {
        this.processId = processId;
        configHome = configHomeParam;
        init();
    }

    /**
     * Instantiates a new Configuration.
     *
     * @param processId          the process id
     * @param configHomeParam    the config home param
     * @param hostsFileNameParam the hosts file name param
     */
    public Configuration(int processId, String configHomeParam, String hostsFileNameParam) {
        this.processId = processId;
        configHome = configHomeParam;
        hostsFileName = hostsFileNameParam;
        init();
    }

    /**
     * Init.
     */
    protected void init() {
        try {
            hosts = new HostsConfig(configHome, hostsFileName);

            loadConfig();

            String s = (String)configs.remove("system.autoconnect");
            if (s == null) {
                autoConnectLimit = -1;
            } else {
                autoConnectLimit = Integer.parseInt(s);
            }

            s = (String)configs.remove("system.channels.blocking");
            if (s == null) {
                channelsBlocking = false;
            } else {
                channelsBlocking = (s.equalsIgnoreCase("true")) ? true : false;
            }

            s = (String)configs.remove("system.communication.defaultkeys");
            if (s == null) {
                defaultKeys = false;
            } else {
                defaultKeys = (s.equalsIgnoreCase("true")) ? true : false;
            }

            s = (String)configs.remove("system.diffie-hellman.p");
            if (s == null) {
                String pHexString = "FFFFFFFF FFFFFFFF C90FDAA2 2168C234 C4C6628B 80DC1CD1"
                    + "29024E08 8A67CC74 020BBEA6 3B139B22 514A0879 8E3404DD"
                    + "EF9519B3 CD3A431B 302B0A6D F25F1437 4FE1356D 6D51C245"
                    + "E485B576 625E7EC6 F44C42E9 A637ED6B 0BFF5CB6 F406B7ED"
                    + "EE386BFB 5A899FA5 AE9F2411 7C4B1FE6 49286651 ECE65381" + "FFFFFFFF FFFFFFFF";
                DH_P = new BigInteger(pHexString.replaceAll(" ", ""), 16);
            } else {
                DH_P = new BigInteger(s, 16);
            }
            s = (String)configs.remove("system.diffie-hellman.g");
            if (s == null) {
                DH_G = new BigInteger("2");
            } else {
                DH_G = new BigInteger(s);
            }

        } catch (Exception e) {
            Logger.printError("Wrong system.config file format.", e);
        }
    }

    /**
     * Use default keys boolean.
     *
     * @return the boolean
     */
    public boolean useDefaultKeys() {
        return defaultKeys;
    }

    /**
     * Is host setted boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public final boolean isHostSetted(int id) {
        if (hosts.getHost(id) == null) {
            return false;
        }
        return true;
    }

    /**
     * Use blocking channels boolean.
     *
     * @return the boolean
     */
    public final boolean useBlockingChannels() {
        return this.channelsBlocking;
    }

    /**
     * Gets auto connect limit.
     *
     * @return the auto connect limit
     */
    public final int getAutoConnectLimit() {
        return this.autoConnectLimit;
    }

    /**
     * Gets dhp.
     *
     * @return the dhp
     */
    public final BigInteger getDHP() {
        return DH_P;
    }

    /**
     * Gets dhg.
     *
     * @return the dhg
     */
    public final BigInteger getDHG() {
        return DH_G;
    }

    /**
     * Gets hmac algorithm.
     *
     * @return the hmac algorithm
     */
    public final String getHmacAlgorithm() {
        return hmacAlgorithm;
    }

    /**
     * Gets hmac size.
     *
     * @return the hmac size
     */
    public final int getHmacSize() {
        return hmacSize;
    }

    /**
     * Gets property.
     *
     * @param key the key
     * @return the property
     */
    public final String getProperty(String key) {
        Object o = configs.get(key);
        if (o != null) {
            return o.toString();
        }
        return null;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public final Map<String, String> getProperties() {
        return configs;
    }

    /**
     * Gets remote address.
     *
     * @param id the id
     * @return the remote address
     */
    public final InetSocketAddress getRemoteAddress(int id) {
        return hosts.getRemoteAddress(id);
    }

    /**
     * Gets server to server remote address.
     *
     * @param id the id
     * @return the server to server remote address
     */
    public final InetSocketAddress getServerToServerRemoteAddress(int id) {
        return hosts.getServerToServerRemoteAddress(id);
    }

    /**
     * Gets local address.
     *
     * @param id the id
     * @return the local address
     */
    public final InetSocketAddress getLocalAddress(int id) {
        return hosts.getLocalAddress(id);
    }

    /**
     * Gets host.
     *
     * @param id the id
     * @return the host
     */
    public final String getHost(int id) {
        return hosts.getHost(id);
    }

    /**
     * Gets port.
     *
     * @param id the id
     * @return the port
     */
    public final int getPort(int id) {
        return hosts.getPort(id);
    }

    /**
     * Gets server to server port.
     *
     * @param id the id
     * @return the server to server port
     */
    public final int getServerToServerPort(int id) {
        return hosts.getServerToServerPort(id);
    }

    /**
     * Gets process id.
     *
     * @return the process id
     */
    public final int getProcessId() {
        return processId;
    }

    /**
     * Sets process id.
     *
     * @param processId the process id
     */
    public final void setProcessId(int processId) {
        this.processId = processId;
    }

    /**
     * Add host info.
     *
     * @param id   the id
     * @param host the host
     * @param port the port
     */
    public final void addHostInfo(int id, String host, int port) {
        this.hosts.add(id, host, port);
    }

    private void loadConfig() {
        Map<String, String> systemConfigs = SpringUtil.getBean(SmartConfig.class).getConfigs();
        configs = new Hashtable<>();
        //此处使用引用传递会影响后续运行，所以对map的每项单独赋值
        if (!systemConfigs.isEmpty()) {
            for (Map.Entry<String, String> entry : systemConfigs.entrySet()) {
                configs.put(entry.getKey(), entry.getValue());
            }
        }
        //        try{
        //            if(configHome == null || configHome.equals("")){
        //                configHome="config";
        //            }
        //            String sep = System.getProperty("file.separator");
        //            String path =  configHome+sep+"system.config";
        //            FileReader fr = new FileReader(path);
        //            BufferedReader rd = new BufferedReader(fr);
        //            String line = null;
        //            while((line = rd.readLine()) != null){
        //                if(!line.startsWith("#")){
        //                    StringTokenizer str = new StringTokenizer(line,"=");
        //                    if(str.countTokens() > 1){
        //                        configs.put(str.nextToken().trim(),str.nextToken().trim());
        //                    }
        //                }
        //            }
        //            fr.close();
        //            rd.close();
        //        }catch(Exception e){
        //            Logger.printError(e.getMessage(),e);
        //        }
    }
}
