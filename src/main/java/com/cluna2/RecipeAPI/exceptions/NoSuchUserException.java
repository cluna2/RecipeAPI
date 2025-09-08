package com.cluna2.RecipeAPI.exceptions;

public class NoSuchUserException extends RuntimeException {
    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException() {}
}
