package com.higgschain.trust.consensus.p2pvalid.core;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * The type Response command.
 *
 * @param <T> the type parameter
 * @author cwy
 */
@Setter @Getter @ToString public abstract class ResponseCommand<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -1L;

    private T t;
    private String cmdName;

    /**
     * Instantiates a new Response command.
     */
    public ResponseCommand() {
        this.setCmdName(this.getClass().getSimpleName());
    }

    /**
     * Instantiates a new Response command.
     *
     * @param t the t
     */
    public ResponseCommand(T t) {
        this();
        this.t = t;
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
        return Hashing.sha256()
            .hashString(this.getClass().getName().concat("_").concat(messageDigest()), Charsets.UTF_8).toString();
    }

}
