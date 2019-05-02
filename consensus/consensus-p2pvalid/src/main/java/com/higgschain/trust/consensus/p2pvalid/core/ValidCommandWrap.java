package com.higgschain.trust.consensus.p2pvalid.core;

import com.higgschain.trust.consensus.config.NodeState;
import com.higgschain.trust.consensus.util.CryptoUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * The type Valid command wrap.
 *
 * @author cwy
 */
@Getter @Setter @ToString @NoArgsConstructor public class ValidCommandWrap implements Serializable {
    private static final long serialVersionUID = -1L;
    private ValidCommand<?> validCommand;
    private String fromNode;
    private String sign;
    private Class<?> commandClass;

    /**
     * Builder builder.
     *
     * @return the builder
     */
    public static Builder builder(NodeState nodeState) {
        return new Builder(nodeState);
    }

    /**
     * The type Builder.
     *
     * @param <T> the type parameter
     */
    public static class Builder<T extends Serializable> {
        private ValidCommand<T> validCommand;
        private NodeState nodeState;

        public Builder(NodeState nodeState) {
            this.nodeState = nodeState;
        }

        /**
         * With command builder.
         *
         * @param command the command
         * @return the builder
         */
        public Builder withCommand(ValidCommand<T> command) {
            this.validCommand = command;
            return this;
        }

        /**
         * Build valid command wrap.
         *
         * @return the valid command wrap
         */
        public ValidCommandWrap build() {
            ValidCommandWrap wrap = new ValidCommandWrap();
            wrap.setValidCommand(validCommand);
            wrap.setCommandClass(validCommand.getClass());
            wrap.setFromNode(nodeState.getNodeName());
            wrap.setSign(CryptoUtil.getProtocolCrypto()
                .sign(validCommand.getMessageDigestHash(), nodeState.getConsensusPrivateKey()));
            return wrap;
        }
    }
}
