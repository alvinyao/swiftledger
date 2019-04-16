package com.higgschain.trust.consensus.bftsmartcustom.started.custom;

import java.util.Map;

/**
 * The interface Number name mapping.
 *
 * @author: zhouyafeng
 * @create: 2018 /07/15 09:58
 * @description:
 */
public interface NumberNameMapping {
    /**
     * Gets mapping.
     *
     * @return the mapping
     */
    //获取映射对象
    Map<String, String> getMapping();

    /**
     * Add mapping boolean.
     *
     * @param map the map
     * @return the boolean
     */
    //添加映射对
    boolean addMapping(Map<String, String> map);
}