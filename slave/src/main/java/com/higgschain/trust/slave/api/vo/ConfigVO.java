package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Config vo.
 *
 * @author WangQuanzhou
 * @desc node configuration
 * @date 2018 /6/5 10:27
 */
@Getter @Setter public class ConfigVO extends BaseBO {
    private String reqNo;
    private String version;

    private String valid;

    private String pubKey;

    private String priKey;

    private String usage;

    private String tmpPubKey;

    private String tmpPriKey;

    private String nodeName;
}
