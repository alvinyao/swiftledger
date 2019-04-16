package com.higgschain.trust.slave.model.bo.ca;

import com.higgschain.trust.slave.model.bo.action.Action;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Ca action.
 *
 * @param <T> the type parameter
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 16:15
 */
@Getter @Setter public class CaAction<T> extends Action {
    private String version;

    private Date period;

    private T data;

    private boolean valid;

    private String pubKey;

    private String user;

    private String usage;
}
