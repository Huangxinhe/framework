package com.framework.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 和公司返回结构保持一致，新接口使用这个类
 *
 * @param <T>
 */

@Data
@NoArgsConstructor
public class BaseResultMsg<T> {

    private int errno;

    private String errmsg;

    private T data;

    public BaseResultMsg(int errorCode, T data) {
        this.errno = errorCode;
        this.data = data;
    }

    public BaseResultMsg(int errorCode, String errorMessage, T data) {
        this.errno = errorCode;
        this.errmsg = errorMessage;
        this.data = data;
    }

    public static <T> BaseResultMsg ofFail(Integer errno, String errmsg, T data) {
        return new BaseResultMsg(errno, errmsg, data);
    }

    public static <T> BaseResultMsg ofFail(Integer errno, String errmsg) {
        return new BaseResultMsg(errno, errmsg, null);
    }
    public static <T> BaseResultMsg ofSuccess() {
        return new BaseResultMsg();
    }

}
