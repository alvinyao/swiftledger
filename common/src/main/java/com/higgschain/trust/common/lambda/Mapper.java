package com.higgschain.trust.common.lambda;

/**
 * The interface Mapper.
 *
 * @param <FROM> the type parameter
 * @param <TO>   the type parameter
 * @author duhongming
 * @date 2018 /8/2
 */
public interface Mapper<FROM, TO> {

    /**
     * map
     *
     * @param src the src
     * @return to
     */
    TO mapping(FROM src);
}
