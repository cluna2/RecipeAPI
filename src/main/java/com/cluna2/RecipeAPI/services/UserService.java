package com.cluna2.RecipeAPI.services;


import com.cluna2.RecipeAPI.exceptions.NoSuchUserException;
import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.Review;
import com.cluna2.RecipeAPI.models.User;
import com.cluna2.RecipeAPI.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    public User createNewUser(User user) throws IllegalStateException {
        user.validate();
        user = userRepo.save(user);
        user.generateLocationURI();
        return user;
    }

    public List<Recipe> getRecipesByUserName(String username) throws NoSuchUserException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new NoSuchUserException("User does not exist.");
        }
        if (user.getRecipes().isEmpty()) {
            throw new NoSuchUserException("No recipes found from user: " + username);
        }
        return user.getRecipes();
    }

    public List<Review> getReviewsByUserName(String username) throws NoSuchUserException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new NoSuchUserException("User does not exist.");
        }

        if (user.getReviews().isEmpty()) {
            throw new NoSuchUserException("No reviews found from user: " + username);
        }
        return user.getReviews();
    }

    public User getUserByUserName(String username) throws NoSuchUserException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new NoSuchUserException("No user with username: " + username + " could be found.");
        }
        user.generateLocationURI();
        return user;
    }

    public User getUserById(Long id) throws NoSuchUserException {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserException("No user with ID: " + id + " could be found.");
        }

        User user = userOptional.get();
        user.generateLocationURI();
        return user;
    }
}
