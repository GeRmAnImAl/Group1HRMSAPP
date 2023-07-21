package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for AppUser entity. This extends JpaRepository to provide standard database operation methods
 * and could be used to add more complex, custom operations related to AppUsers in the future.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    /**
     * Retrieves an AppUser by a specific username.
     * @param userName String representing the AppUser in the database.
     * @return an AppUser Object.
     */
    @Override
    Optional<AppUser> findById(String userName);
}
