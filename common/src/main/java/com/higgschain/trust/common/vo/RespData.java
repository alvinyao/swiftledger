package com.higgschain.trust.common.vo;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.StandardToStringStyle;

/**
 * response data model
 *
 * @param <T>
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

    public RespData() {
    }

    public RespData(String respCode, String msg) {
        this.respCode = respCode;
        this.msg = msg;
    }

    public static <T> RespData<T> success(T data) {
        RespData<T> respData = new RespData<T>();
        respData.setCode("000000");
        respData.setData(data);
        return respData;
    }

    public static <T> RespData<T> error(String respCode, String message, T data) {
        RespData<T> respData = new RespData<T>();
        respData.setData(data);
        respData.setCode(respCode);
        respData.setMsg(message);
        return respData;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setCode(String respCode) {
        this.respCode = respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMsg() {
        if (msg == null || msg.length() == 0) {
            msg = "unknow error";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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
