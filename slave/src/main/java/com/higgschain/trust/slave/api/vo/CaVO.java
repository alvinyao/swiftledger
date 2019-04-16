package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The type Ca vo.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 16:15
 */
@Getter @Setter public class CaVO extends BaseBO {
    private String reqNo;

    private String version;

    private Date period;

    private String valid;

    private String pubKey;

    private String user;

    private String usage;
}
