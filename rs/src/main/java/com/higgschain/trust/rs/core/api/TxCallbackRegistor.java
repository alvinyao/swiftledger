package com.higgschain.trust.rs.core.api;

import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.callback.TxBatchCallbackHandler;
import com.higgschain.trust.rs.core.callback.TxCallbackHandler;
import com.higgschain.trust.rs.core.vo.VotingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * The type Tx callback registor.
 *
 * @author liuyu
 * @description
 * @date 2018 -05-12
 */
@Repository
@Slf4j
public class TxCallbackRegistor {
    private TxCallbackHandler coreTxCallback;
    private TxBatchCallbackHandler txBatchCallbackHandler;

    /**
     * Regist callback.
     *
     * @param callback the callback
     */
    public void registCallback(TxCallbackHandler callback) {
        this.coreTxCallback = callback;
    }

    /**
     * Regist batch callback.
     *
     * @param callback the callback
     */
    public void registBatchCallback(TxBatchCallbackHandler callback) {
        this.txBatchCallbackHandler = callback;
    }

    /**
     * Gets core tx callback.
     *
     * @return the core tx callback
     */
    public TxCallbackHandler getCoreTxCallback() {
        return coreTxCallback;
    }

    /**
     * Gets core tx batch callback.
     *
     * @return the core tx batch callback
     */
    public TxBatchCallbackHandler getCoreTxBatchCallback() {
        return txBatchCallbackHandler;
    }

    /**
     * vote for custom
     *
     * @param votingRequest the voting request
     */
    public void onVote(VotingRequest votingRequest) {
        if (coreTxCallback != null) {
            coreTxCallback.onVote(votingRequest);
        } else if (txBatchCallbackHandler != null) {
            txBatchCallbackHandler.onVote(votingRequest);
        } else {
            log.error("[onVote] callback handler is not register");
            throw new RsCoreException(RsCoreErrorEnum.RS_CORE_TX_CORE_TX_CALLBACK_NOT_SET);
        }
    }
}
