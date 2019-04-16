package com.higgschain.trust.slave.core.service.ca;

import java.io.IOException;

/**
 * The interface Ca init service.
 *
 * @author WangQuanzhou
 * @desc TODO
 * @date 2018 /6/5 15:40
 */
public interface CaInitService {

    /**
     * Init key pair.
     *
     * @throws IOException the io exception
     */
    void initKeyPair() throws IOException;

}
