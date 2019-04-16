package com.me.component_base.net.exception;

/**
 *
 * @author asus
 * @date 2017/12/1
 * 网络异常
 */

public class NetworkException extends RuntimeException {

    private int code = 404;

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

    private String message = "网络不给力，请检查网络设置。";
}
