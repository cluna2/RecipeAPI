package com.cluna2.RecipeAPI.exceptions;

public class UserReviewException extends RuntimeException {
    public UserReviewException(String message) {
        super(message);
    }

    public UserReviewException() {}
}
