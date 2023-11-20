package com.pokemonreview.api.exceptions;

public class ResourceNotFoundException extends MyResourceException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
}