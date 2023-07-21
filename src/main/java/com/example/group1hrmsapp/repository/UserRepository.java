package com.example.group1hrmsapp.repository;

import com.example.group1hrmsapp.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    @Override
    Optional<AppUser> findById(String userName);
}
