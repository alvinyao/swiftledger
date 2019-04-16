package com.higgschain.trust.slave.api.vo;

import com.higgschain.trust.slave.model.bo.BaseBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Page vo.
 *
 * @param <T> the type parameter
 */
@Setter
@Getter
public class PageVO<T> extends BaseBO {
    private Long total;

    private Integer pageNo;

    private Integer pageSize;

    private List<T> data;
}
