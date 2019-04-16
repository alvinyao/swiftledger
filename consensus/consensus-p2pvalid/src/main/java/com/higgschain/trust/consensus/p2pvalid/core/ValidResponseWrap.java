package com.higgschain.trust.consensus.p2pvalid.core;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * The type Valid response wrap.
 *
 * @param <T> the type parameter
 */
@NoArgsConstructor
@Data
@Slf4j
public class ValidResponseWrap<T extends ResponseCommand> implements Serializable {

    private static final long serialVersionUID = 5358908034044478066L;
    /**
     * The constant SUCCESS_CODE.
     */
    public static final String SUCCESS_CODE = "SUCCESS";
    /**
     * The constant FAILED_CODE.
     */
    public static final String FAILED_CODE = "FAILED";
    private String code;
    private String message;
    private T resultOne;
    private List<T> resultList;

    private String sign;

    /**
     * Success response valid response wrap.
     *
     * @param <T>    the type parameter
     * @param result the result
     * @return the valid response wrap
     */
    public static <T extends ResponseCommand> ValidResponseWrap<T> successResponse(Object result) {
        ValidResponseWrap<T> tValidResponseWrap = new ValidResponseWrap<>();
        tValidResponseWrap.setCode(SUCCESS_CODE);
        if (result == null) {
            return tValidResponseWrap;
        }
        if (result instanceof ResponseCommand) {
            tValidResponseWrap.setResultOne((T) result);
            return tValidResponseWrap;
        }
        if (result instanceof List) {
            tValidResponseWrap.setResultList((List) result);
            return tValidResponseWrap;
        }
        
        log.error("result({}) is not ResponseCommand type", result.getClass().getName());
        return tValidResponseWrap;
    }

    /**
     * Failed response valid response wrap.
     *
     * @return the valid response wrap
     */
    public static ValidResponseWrap<?> failedResponse() {
        return failedResponse("");
    }

    /**
     * Failed response valid response wrap.
     *
     * @param message the message
     * @return the valid response wrap
     */
    public static ValidResponseWrap<?> failedResponse(String message) {
        return failedResponse(FAILED_CODE, message);
    }

    /**
     * Failed response valid response wrap.
     *
     * @param code    the code
     * @param message the message
     * @return the valid response wrap
     */
    public static ValidResponseWrap<?> failedResponse(String code, String message) {
        ValidResponseWrap<ResponseCommand> response = new ValidResponseWrap<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    /**
     * Is sucess boolean.
     *
     * @return the boolean
     */
    public boolean isSucess() {
        return SUCCESS_CODE.equals(code);
    }

    /**
     * Result object.
     *
     * @return the object
     */
    public Object result() {
        return resultOne == null ? resultList : resultOne;
    }
}
