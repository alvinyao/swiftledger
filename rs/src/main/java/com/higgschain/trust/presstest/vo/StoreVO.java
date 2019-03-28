package com.higgschain.trust.presstest.vo;

import com.higgschain.trust.rs.core.vo.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyu
 * @description
 * @date 2018-09-11
 */
@Getter
@Setter
public class StoreVO extends BaseVO{
    private String reqNo;
    private String[] values;
}
