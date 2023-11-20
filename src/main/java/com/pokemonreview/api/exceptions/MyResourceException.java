package com.pokemonreview.api.exceptions;

public abstract class MyResourceException extends RuntimeException{
    public MyResourceException(String message) {
        super(message);
    }

    public MyResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}