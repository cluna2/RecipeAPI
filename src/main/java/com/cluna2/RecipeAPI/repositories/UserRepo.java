package com.cluna2.RecipeAPI.repositories;


import com.cluna2.RecipeAPI.models.Recipe;
import com.cluna2.RecipeAPI.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

}
