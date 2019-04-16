package com.higgschain.trust.common.mybatis;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;

/**
 * The type Base entity.
 *
 * @param <T> the type parameter
 * @author liuyu
 * @description base entity
 * @date 2018 -03-27
 */
public class BaseEntity<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Override public String toString() {
        try {
            StandardToStringStyle style = new StandardToStringStyle();
            style.setFieldSeparator(" ");
            style.setFieldSeparatorAtStart(true);
            style.setUseShortClassName(true);
            style.setUseIdentityHashCode(false);
            return new ReflectionToStringBuilder(this, style).toString();
        } catch (Throwable e) {
            return super.toString();
        }
    }
}
