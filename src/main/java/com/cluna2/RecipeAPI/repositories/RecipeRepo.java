package com.cluna2.RecipeAPI.repositories;

import com.cluna2.RecipeAPI.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepo extends JpaRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCase(String name);

    List<Recipe> findByAverageRatingGreaterThanEqual(double averageRating);

    List<Recipe> findByNameContainingIgnoreCaseAndDifficultyRatingLessThanEqual(String name, int difficultyRating);
}
