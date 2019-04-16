package com.me.component_base.net.exception;

/**
 *
 * @author asus
 * @date 2017/12/28
 */

public class ServerException extends RuntimeException {

    private int code = 500;

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

    private String message = "服务器忙，请稍后重试";
}
