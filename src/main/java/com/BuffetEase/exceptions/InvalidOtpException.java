package com.BuffetEase.exceptions;

public class InvalidOtpException extends RuntimeException {
    //public InvalidOtpException(String message) {
    //    super(message);
    //}

    public InvalidOtpException() {
        super("Invalid OTP");
    }

    public InvalidOtpException(String message) {
        super(message);
    }
}
