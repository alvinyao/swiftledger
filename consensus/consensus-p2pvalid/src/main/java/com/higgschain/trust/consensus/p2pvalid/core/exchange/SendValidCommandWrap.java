package com.higgschain.trust.consensus.p2pvalid.core.exchange;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.higgschain.trust.config.crypto.CryptoUtil;
import com.higgschain.trust.consensus.p2pvalid.core.ValidCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Send valid command wrap.
 *
 * @author cwy
 */
@Getter @Setter @ToString public class SendValidCommandWrap implements Serializable {

    private static final long serialVersionUID = -1L;
    private ValidCommand<?> validCommand;
    private String fromNodeName;
    private String messageDigest;
    private String sign;
    private Class<? extends ValidCommand> commandClass;
    private Set<String> toNodeNames = new HashSet<>();
    private Long traceId;

    /**
     * Instantiates a new Send valid command wrap.
     */
    public SendValidCommandWrap() {
    }

    private SendValidCommandWrap(ValidCommand<?> validCommand) {
        this.validCommand = validCommand;
        this.commandClass = validCommand.getClass();
        HashFunction function = Hashing.sha256();
        String hash = function.hashString(validCommand.messageDigest(), Charsets.UTF_8).toString();
        this.messageDigest = hash;
    }

    /**
     * Of send valid command wrap.
     *
     * @param validCommand the valid command
     * @return the send valid command wrap
     */
    public static SendValidCommandWrap of(ValidCommand<?> validCommand) {
        SendValidCommandWrap validCommandWrap = new SendValidCommandWrap(validCommand);
        return validCommandWrap;
    }

    /**
     * From node name send valid command wrap.
     *
     * @param fromNodeName the from node name
     * @return the send valid command wrap
     */
    public SendValidCommandWrap fromNodeName(String fromNodeName) {
        this.fromNodeName = fromNodeName;
        return this;
    }

    /**
     * Sign send valid command wrap.
     *
     * @param privateKey the private key
     * @return the send valid command wrap
     * @throws Exception the exception
     */
    public SendValidCommandWrap sign(String privateKey) throws Exception {
        this.sign = CryptoUtil.getProtocolCrypto().sign(messageDigest, privateKey);
        return this;
    }

    /**
     * Add to node names send valid command wrap.
     *
     * @param toNodeNames the to node names
     * @return the send valid command wrap
     */
    public SendValidCommandWrap addToNodeNames(Collection<String> toNodeNames) {
        this.toNodeNames.addAll(toNodeNames);
        return this;
    }

    /**
     * Add to node name send valid command wrap.
     *
     * @param toNodeName the to node name
     * @return the send valid command wrap
     */
    public SendValidCommandWrap addToNodeName(String toNodeName) {
        this.toNodeNames.add(toNodeName);
        return this;
    }

    /**
     * Gets trace id.
     *
     * @return the trace id
     */
    public Long getTraceId() {
        return traceId;
    }

    /**
     * Sets trace id.
     *
     * @param traceId the trace id
     */
    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }
}
