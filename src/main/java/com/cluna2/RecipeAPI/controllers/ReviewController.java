package com.cluna2.RecipeAPI.controllers;

import com.cluna2.RecipeAPI.exceptions.NoSuchRecipeException;
import com.cluna2.RecipeAPI.exceptions.NoSuchReviewException;
import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.Review;
import com.cluna2.RecipeAPI.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable("id") Long id) {
        try {
            Review review = reviewService.getReviewById(id);
            return ResponseEntity.ok(review);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<?> postNewReview(
            @RequestBody Review review, @PathVariable("recipeId") Long recipeId) {
        try {
            Recipe recipe = reviewService.postNewReview(review, recipeId);
            return ResponseEntity.created(recipe.getLocationURI()).body(recipe);
        } catch (NoSuchRecipeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@PathVariable("id") Long id) {
        try {
            Review deletedReview = reviewService.deleteReviewById(id);
            return ResponseEntity.ok("The review with id: " + deletedReview.getId() +
                    " by username: " + deletedReview.getUsername() +
                    " is deleted. ");
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateReviewById(@RequestBody Review updatedReview) {
        try {
            Review returnedUpdatedReview  = reviewService.updateReviewById(updatedReview);
            return ResponseEntity.ok(returnedUpdatedReview);
        } catch (NoSuchReviewException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
