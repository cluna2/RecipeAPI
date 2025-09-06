package com.cluna2.RecipeAPI.exceptions;

public class NoSuchReviewException extends RuntimeException {
    public NoSuchReviewException(String message) {
        super(message);
    }

    public NoSuchReviewException(){}
}
