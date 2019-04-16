package com.me.component_base.net.exception;

/**
 *
 * @author SEELE
 * @date 2017/9/26
 */

public class BIZexception extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public BIZexception(String desc) {
        this.message=desc;
    }
}
