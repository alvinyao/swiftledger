package com.higgschain.trust.slave.model.bo.manage;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Rs pub key.
 *
 * @author tangfashuang
 */
@Getter
@Setter
public class RsPubKey extends BaseBO {
    /**
     * rs id
     */
    private String rsId;

    /**
     * public key
     */
    private String pubKey;
}
