/*
 * Copyright (c) 2013-2017, suimi
 */
package com.higgschain.trust.consensus.term;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Term info.
 *
 * @author suimi
 * @date 2018 /6/1
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor public class TermInfo {

    /**
     * The constant INIT_END_HEIGHT.
     */
    public static final long INIT_END_HEIGHT = -1;
    private long term;

    private String masterName;

    private long startHeight;

    private long endHeight;

}
