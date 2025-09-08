package com.cluna2.RecipeAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;


    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();


    @Transient
    @JsonIgnore
    private URI locationURI;

    public void generateLocationURI() {
        try {
            locationURI = new URI(
                    ServletUriComponentsBuilder.fromCurrentContextPath()
                                               .path("/users/")
                                               .path(String.valueOf(id))
                                               .toUriString());
        } catch (URISyntaxException e) {
            // exception should stop here.
        }
    }

    public void validate() {
        if (username == null) {
            throw new IllegalStateException("A username is required for this user.");
        }
    }

}
