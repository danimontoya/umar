package com.uma.umar.exception;

/**
 * Created by danieh on 7/20/17.
 */

public class UmARRuntimeException extends RuntimeException {

    public UmARRuntimeException(Exception e) {
        super(e);
    }

    public UmARRuntimeException(String message) {
        super(message);
    }
}
