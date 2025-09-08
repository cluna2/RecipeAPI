package com.cluna2.RecipeAPI.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minutesToMake;

    @Column(nullable = false)
    private Integer difficultyRating;

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;

    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    // set to write-only to prevent infinite regress in json output
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @Builder.Default
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @Builder.Default
    private List<Step> steps = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();


    @Transient
    @JsonIgnore
    private URI locationURI;

    public void setDifficultyRating(int difficultyRating) {
        if (difficultyRating < 0 || difficultyRating > 10) {
            throw new IllegalStateException(
                    "Difficulty rating must be between 0 and 10.");
        }
        this.difficultyRating = difficultyRating;
    }

    public void validate() throws IllegalStateException {
        if (ingredients.size() == 0) {
            throw new IllegalStateException(
                    "You need at least one ingredient for your recipe!");
        } else if (steps.size() == 0) {
            throw new IllegalStateException(
                    "You need at least one step for your recipe!");
        } else if (difficultyRating == null) {
            throw new IllegalStateException(
                    "You need a difficulty rating for your recipe!");
        } else if (user == null) {
            throw new IllegalStateException(
                    "A user must be provided.");
        }
    }

    public void generateLocationURI() {
        try {
            locationURI = new URI(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                                               .path("/recipes/")
                                               .path(String.valueOf(id))
                                               .toUriString());
        } catch (URISyntaxException e) {
            // exception should stop here.
        }
    }


    public void updateAverageRating() {
        if (reviews.size() == 0) {
            return;
        }
        OptionalDouble optionalAverage = reviews.stream().mapToDouble(Review::getRating).average();
        if (optionalAverage.isPresent()) {
            averageRating = optionalAverage.getAsDouble();
        }
    }
}
