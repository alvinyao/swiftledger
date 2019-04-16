package com.higgschain.trust.common.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;

/**
 * response data model
 *
 * @param <T> the type parameter
 * @author liuyu
 */
public class RespData<T> implements java.io.Serializable {
    private static final long serialVersionUID = 4917480918640310535L;

    /**
     * default code is successful
     */
    private String respCode = "000000";
    /**
     * message describe
     */
    private String msg = "success";
    /**
     * data
     */
    private T data;

    /**
     * Instantiates a new Resp data.
     */
    public RespData() {
    }

    /**
     * Instantiates a new Resp data.
     *
     * @param respCode the resp code
     * @param msg      the msg
     */
    public RespData(String respCode, String msg) {
        this.respCode = respCode;
        this.msg = msg;
    }

    /**
     * Success resp data.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the resp data
     */
    public static <T> RespData<T> success(T data) {
        RespData<T> respData = new RespData<T>();
        respData.setCode("000000");
        respData.setData(data);
        return respData;
    }

    /**
     * Error resp data.
     *
     * @param <T>      the type parameter
     * @param respCode the resp code
     * @param message  the message
     * @param data     the data
     * @return the resp data
     */
    public static <T> RespData<T> error(String respCode, String message, T data) {
        RespData<T> respData = new RespData<T>();
        respData.setData(data);
        respData.setCode(respCode);
        respData.setMsg(message);
        return respData;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Gets resp code.
     *
     * @return the resp code
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * Sets code.
     *
     * @param respCode the resp code
     */
    public void setCode(String respCode) {
        this.respCode = respCode;
    }

    /**
     * Sets resp code.
     *
     * @param respCode the resp code
     */
    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() {
        if (msg == null || msg.length() == 0) {
            msg = "unknow error";
        }
        return msg;
    }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * Is success boolean.
     *
     * @return the boolean
     */
    public boolean isSuccess() {
        return StringUtils.equals(respCode, "000000");
    }

    @Override public String toString() {
        try {
            StandardToStringStyle style = new StandardToStringStyle();
            style.setFieldSeparator(" ");
            style.setFieldSeparatorAtStart(true);
            style.setUseShortClassName(true);
            style.setUseIdentityHashCode(false);
            return new ReflectionToStringBuilder(this, style).toString();
        } catch (Exception e) {
            return super.toString();
        }
    }
}
