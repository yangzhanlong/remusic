package com.me.component_base.net.exception;

/**
 *
 * @author Pht
 * @date 2017/9/26
 */

public class NetNotFoundException extends RuntimeException {
    private int code=404;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message+",code:"+code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message="找不到异常";
}