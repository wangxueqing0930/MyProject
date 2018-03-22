package com.wxq.example.exception;

public class FileGenException extends Exception {

    private static final long serialVersionUID = -3830673960198881285L;

    public FileGenException() {
        super();
    }

    public FileGenException(String message) {
        super(message);
    }

    public FileGenException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileGenException(Throwable cause) {
        super(cause);
    }

    protected FileGenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
