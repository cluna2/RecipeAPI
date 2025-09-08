package com.cluna2.RecipeAPI.services;

import com.cluna2.RecipeAPI.exceptions.NoSuchRecipeException;
import com.cluna2.RecipeAPI.exceptions.NoSuchReviewException;
import com.cluna2.RecipeAPI.exceptions.NoSuchUserException;
import com.cluna2.RecipeAPI.exceptions.UserReviewException;
import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.Review;
import com.cluna2.RecipeAPI.models.User;
import com.cluna2.RecipeAPI.repositories.ReviewRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    RecipeService recipeService;

    @Autowired
    UserService userService;

    public Review getReviewById(Long id) throws NoSuchReviewException {
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isEmpty()) {
            throw new NoSuchReviewException(
                    "The review with ID " + id + " could not be found.");
        }
        return review.get();
    }

    public List<Review> getReviewByRecipeId(Long recipeId)
            throws NoSuchRecipeException, NoSuchReviewException {
        Recipe recipe = recipeService.getRecipeById(recipeId);

        List<Review> reviews = recipe.getReviews();

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException(
                    "There are no reviews for this recipe.");
        }
        return reviews;
    }

    public List<Review> getReviewByUsername(String username)
            throws NoSuchReviewException {
        List<Review> reviews = reviewRepo.findByUser_Username(username);

        if (reviews.isEmpty()) {
            throw new NoSuchReviewException(
                    "No reviews could be found for username " + username);
        }
        return reviews;
    }

    public Recipe postNewReview(Review review, Long recipeId)
            throws NoSuchRecipeException, UserReviewException, NoSuchUserException {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        User reviewUser = userService.getUserByUserName(review.getUser().getUsername());
        if (recipe.getUser().getId().equals(reviewUser.getId())) {
            throw new UserReviewException("You cannot post a review for your own posted recipe: " + recipe.getId());
        }
        recipe.getReviews().add(review);
        recipe.updateAverageRating();
        review.setUser(reviewUser);
        recipeService.updateRecipe(recipe, false);
        return recipe;
    }

    public Review deleteReviewById(Long id) throws NoSuchReviewException {
        Review review = getReviewById(id);

        if (null == review) {
            throw new NoSuchReviewException(
                    "The review you are trying to delete does not exist.");
        }
        reviewRepo.deleteById(id);
        return review;
    }

    public Review updateReviewById(Review reviewToUpdate)
            throws NoSuchReviewException {
        try {
            Review review = getReviewById(reviewToUpdate.getId());
        } catch (NoSuchReviewException e) {
            throw new NoSuchReviewException(
                    "The review you are trying to update. " +
                            "Maybe you meant to create one? If not," +
                            "please double-check the ID you passed in.");
        }
        reviewRepo.save(reviewToUpdate);
        return reviewToUpdate;
    }
}

