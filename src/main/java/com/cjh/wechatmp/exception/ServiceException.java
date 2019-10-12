package com.cjh.wechatmp.exception;

import com.cjh.wechatmp.enums.ErrorEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 功能：业务异常
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code = ErrorEnum.FAIL.getCode();
    private String msg;

    public ServiceException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(ErrorEnum statusCode) {
        super(statusCode.getName());
        this.code = statusCode.getCode();
        this.msg = statusCode.getName();
    }

    public ServiceException(int code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg = msg;
    }

}
