package com.me.component_base.net.exception;

/**
 *
 * @author Pht
 * @date 2017/9/26
 */

public class NetUnauthorizedException extends RuntimeException {
    private int code = 401;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message = "登陆过期，请重新登陆！";
}
