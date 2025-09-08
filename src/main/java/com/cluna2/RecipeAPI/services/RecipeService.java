package com.cluna2.RecipeAPI.services;

import com.cluna2.RecipeAPI.exceptions.NoSuchRecipeException;
import com.cluna2.RecipeAPI.exceptions.NoSuchUserException;
import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.User;
import com.cluna2.RecipeAPI.repositories.RecipeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    RecipeRepo recipeRepo;

    @Autowired
    UserService userService;

    @Transactional
    public Recipe createNewRecipe(Recipe recipe) throws IllegalStateException, NoSuchUserException {
        recipe.validate();
        User user = userService.getUserByUserName(recipe.getUser().getUsername());
        recipe.setUser(user);
        recipe = recipeRepo.save(recipe);
        recipe.generateLocationURI();
        return recipe;
    }

    public Recipe getRecipeById(Long id) throws NoSuchRecipeException {
        Optional<Recipe> recipeOptional = recipeRepo.findById(id);

        if (recipeOptional.isEmpty()) {
            throw new NoSuchRecipeException(
                    "No recipe with ID " + id + " could be found.");
        }
        Recipe recipe = recipeOptional.get();
        recipe.generateLocationURI();
        return recipe;
    }

    public List<Recipe> getRecipesByName(String name)
            throws NoSuchRecipeException {
        List<Recipe> matchingRecipes =
                recipeRepo.findByNameContainingIgnoreCase(name);

        if (matchingRecipes.isEmpty()) {
            throw new NoSuchRecipeException(
                    "No recipes could be found with that name.");
        }

        return matchingRecipes;
    }

    public List<Recipe> getAllRecipes() throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findAll();

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException(
                    "There are no recipes yet :( feel free to add one.");
        }
        return recipes;
    }

    @Transactional
    public Recipe deleteRecipeById(Long id) throws NoSuchRecipeException {
        try {
            Recipe recipe = getRecipeById(id);
            recipeRepo.deleteById(id);
            return recipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException(
                    e.getMessage() + " Could not delete.");
        }
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe, boolean forceIdCheck)
            throws NoSuchRecipeException {
        try {
            if (forceIdCheck) {
                getRecipeById(recipe.getId());
            }
            recipe.validate();
            Recipe savedRecipe = recipeRepo.save(recipe);
            savedRecipe.generateLocationURI();
            return savedRecipe;
        } catch (NoSuchRecipeException e) {
            throw new NoSuchRecipeException(
                    "The recipe you passed in did not have an ID found " +
                            "in the database. Double check that it is correct. " +
                            "Or maybe you meant to POST a recipe not PATCH one.");
        }
    }

    public List<Recipe> getRecipesByMinimumAverageRating(double minAverageRating)
            throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.findByAverageRatingGreaterThanEqual(minAverageRating);

        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes can be found with that minimum average rating.");
        }

        return recipes;
    }


    public List<Recipe> getRecipesByNameAndMaxDifficultyRating(String name, int maxDifficulty)
        throws NoSuchRecipeException {
        List<Recipe> recipes = recipeRepo.
                findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual(name, maxDifficulty);
        if (recipes.isEmpty()) {
            throw new NoSuchRecipeException("No recipes can be found with that name and that difficulty rating.");
        }
        return recipes;
    }

    public List<Recipe> getRecipesByUsername(String username)
        throws NoSuchRecipeException {
        List<Recipe> recipesFromUserName = recipeRepo.findByUser_Username(username);

        if (recipesFromUserName.isEmpty()) {
            throw new NoSuchRecipeException(String.format("No recipes can be found from user: %s", username));
        }

        return recipesFromUserName;
    }
}
