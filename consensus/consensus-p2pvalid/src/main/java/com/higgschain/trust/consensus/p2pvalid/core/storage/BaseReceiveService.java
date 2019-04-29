package com.higgschain.trust.consensus.p2pvalid.core.storage;

import com.higgschain.trust.consensus.util.CryptoUtil;
import com.higgschain.trust.consensus.p2pvalid.core.*;
import com.higgschain.trust.consensus.view.ClusterView;
import com.higgschain.trust.consensus.view.IClusterViewManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The type Base receive service.
 */
@Slf4j public abstract class BaseReceiveService {

    /**
     * The Valid consensus.
     */
    @Autowired protected ValidConsensus validConsensus;

    /**
     * The View manager.
     */
    @Autowired protected IClusterViewManager viewManager;

    /**
     * Receive valid response wrap.
     *
     * @param validCommandWrap the valid command wrap
     * @return the valid response wrap
     */
    public ValidResponseWrap<? extends ResponseCommand> receive(ValidCommandWrap validCommandWrap) {
        String messageDigest = validCommandWrap.getValidCommand().getMessageDigestHash();
        long viewId = validCommandWrap.getValidCommand().getView();
        ClusterView view = viewManager.getView(viewId);
        if (view == null) {
            log.warn("the view:{} is not exist, current view will be used", viewId);
            view = viewManager.getCurrentView();
        }
        if (StringUtils.isBlank(view.getPubKey(validCommandWrap.getFromNode()))) {
            throw new RuntimeException(String
                .format("the public key of node:%s not exist at view:%d", validCommandWrap.getFromNode(), viewId));
        }

        String pubKey = view.getPubKey(validCommandWrap.getFromNode());
        if (log.isDebugEnabled()) {
            log.debug("node={},pubKeyForConsensus={}", validCommandWrap.getFromNode(), pubKey);
            log.debug("[BaseReceiveService] user={}", validCommandWrap.getFromNode());
        }

        if (!CryptoUtil.getProtocolCrypto().verify(messageDigest, validCommandWrap.getSign(), pubKey)) {
            throw new RuntimeException(String
                .format("check sign failed for node %s, validCommandWrap %s, pubKeyForConsensus %s",
                    validCommandWrap.getFromNode(), validCommandWrap, pubKey));
        }
        log.debug("verify sign success for node={}", validCommandWrap.getFromNode());
        return receive(validCommandWrap.getValidCommand());
    }

    /**
     * Receive valid response wrap.
     *
     * @param validCommand the valid command
     * @return the valid response wrap
     */
    public abstract ValidResponseWrap<? extends ResponseCommand> receive(ValidCommand<?> validCommand);
}
