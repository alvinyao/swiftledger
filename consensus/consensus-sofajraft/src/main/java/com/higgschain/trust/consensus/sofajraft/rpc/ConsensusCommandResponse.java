package com.higgschain.trust.consensus.sofajraft.rpc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ConsensusCommandResponse implements Serializable {
    private static final long serialVersionUID = -4220017686727146773L;

    private boolean           success;

    /**
     * redirect peer id
     */
    private String            redirect;

    private String            errorMsg;

    public ConsensusCommandResponse(boolean success, String redirect, String errorMsg) {
        super();
        this.success = success;
        this.redirect = redirect;
        this.errorMsg = errorMsg;
    }

    public ConsensusCommandResponse() {
        super();
    }

    @Override
    public String toString() {
        return "ValueResponse [success=" + this.success + ", redirect=" + this.redirect
                + ", errorMsg=" + this.errorMsg + "]";
    }
}
