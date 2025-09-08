package com.cluna2.RecipeAPI.controllers;

import com.cluna2.RecipeAPI.exceptions.NoSuchUserException;
import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.Review;
import com.cluna2.RecipeAPI.models.User;
import com.cluna2.RecipeAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        try {
            User insertedUser = userService.createNewUser(user);
            return ResponseEntity.created(insertedUser.getLocationURI()).body(insertedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (NoSuchUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "username") String username) {
        try {
            User user = userService.getUserByUserName(username);
            return ResponseEntity.ok(user);
        } catch (NoSuchUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recipes")
    public ResponseEntity<?> getRecipesFromUser(@RequestParam(name = "username") String username) {
        try {
            List<Recipe> recipes = userService.getRecipesByUserName(username);
            return ResponseEntity.ok(recipes);
        } catch (NoSuchUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getReviewsFromUser(@RequestParam(name = "username") String username) {
        try {
            List<Review> reviews = userService.getReviewsByUserName(username);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
