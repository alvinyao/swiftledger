package com.higgschain.trust.consensus.p2pvalid.core;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Valid command.
 *
 * @param <T> the type parameter
 * @author cwy
 */
@Slf4j @Setter @Getter @ToString public abstract class ValidCommand<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1L;
    private static ConcurrentHashMap cmdTypeMap = new ConcurrentHashMap();

    private T t;

    private long view;

    private String cmdName;

    /**
     * Instantiates a new Valid command.
     */
    public ValidCommand() {
    }

    /**
     * Instantiates a new Valid command.
     *
     * @param t    the t
     * @param view the view
     */
    public ValidCommand(T t, long view) {
        this.t = t;
        this.view = view;
    }

    /**
     * Get t.
     *
     * @return the t
     */
    public T get() {
        return t;
    }

    /**
     * Gets cmd name.
     *
     * @return the cmd name
     */
    public String getCmdName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Type class.
     *
     * @return the class
     */
    public Class<?> type() {
        return t.getClass();
    }

    /**
     * Message digest string.
     *
     * @return the string
     */
    public abstract String messageDigest();

    /**
     * Gets message digest hash.
     *
     * @return the message digest hash
     */
    public String getMessageDigestHash() {
        String messageDigest = messageDigest();
        String digest = this.getClass().getName().concat("_").concat(messageDigest);
        log.trace("message digest:{}, hash digest:{}", messageDigest, digest);
        return Hashing.sha256().hashString(digest, Charsets.UTF_8).toString();
    }
}
