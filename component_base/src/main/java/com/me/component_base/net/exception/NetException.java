package com.me.component_base.net.exception;

/**
 *
 * @author Pht
 * @date 2016/12/22
 */

public class NetException extends RuntimeException {
    public int code;
    public String message;

    public NetException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
