package com.upgrad.bookmyconsultation.exception;


public class SlotUnavailableException extends RuntimeException {

    private String message;

    public SlotUnavailableException(String message)
    {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
