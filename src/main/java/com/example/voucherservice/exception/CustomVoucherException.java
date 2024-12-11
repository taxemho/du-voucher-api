package com.example.voucherservice.exception;

@SuppressWarnings("serial")
public class CustomVoucherException extends RuntimeException {
    private final boolean status;

    public CustomVoucherException(String message, boolean status) {
        super(message);
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
