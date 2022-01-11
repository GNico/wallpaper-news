package com.gnico.feed;

public class CouldNotFetchException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public CouldNotFetchException(String message) {
        super(message);
    }
    
    public CouldNotFetchException(String message, Throwable cause) {
        super(message, cause);
    }

}
