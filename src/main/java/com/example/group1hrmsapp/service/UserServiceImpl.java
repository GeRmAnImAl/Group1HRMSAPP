package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for handling user-related operations in the HRMS application.
 * This service contains methods for retrieving, creating, updating, and deleting AppUser entities.
 * It also provides functionality for encoding passwords and authenticating users.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Retrieves an AppUser entity by the given username.
     * @param userName The username of the AppUser to retrieve.
     * @return An AppUser object if a user with the given username is found, otherwise a RuntimeException is thrown.
     * @throws RuntimeException If a user with the given username is not found in the repository.
     */
    @Override
    public AppUser getUserById(String userName) {
        Optional<AppUser> optional = userRepository.findByUserName(userName);
        AppUser appUser = null;
        if(optional.isPresent()){
            appUser = optional.get();
        }
        else{
            throw new RuntimeException("Username '" + userName + "' not found.");
        }
        return appUser;
    }

    /**
     * Saves the provided AppUser entity to the repository. The user's password will be encoded before saving.
     * @param appUser The AppUser entity to be saved. The user's password in this object should be in plain text, as this method will handle the password encoding.
     */
    @Override
    public void saveUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        this.userRepository.save(appUser);
    }

    /**
     * Deletes an AppUser entity from the repository based on the provided username.
     * @param userName The username of the AppUser to be deleted.
     */
    @Override
    public void deleteUserById(String userName) {
        this.userRepository.deleteById(userName);
    }

    /**
     * Retrieves all AppUser entities from the repository.
     * @return A list of all AppUser entities stored in the repository.
     */
    @Override
    public Object getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates and saves a new AppUser entity to the repository. The password will be encoded before the user is saved.
     * @param username The username for the new AppUser.
     * @param password The password for the new AppUser. This should be in plain text, as the method will handle password encoding.
     */
    public void createUser(String username, String password) {
        AppUser appUser = new AppUser();
        appUser.setUserName(username);
        appUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(appUser);
    }

    /**
     * Changes the password of the AppUser with the given username.
     * Verifies the current password before updating to the new password.
     * @param username The username of the AppUser whose password needs to be changed.
     * @param currentPassword The current password of the AppUser.
     * @param newPassword The new password for the AppUser.
     * @return true if the password is successfully changed, false otherwise.
     * @throws RuntimeException If no user is currently logged in.
     */
    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));

        // Verify that the current password matches the user's actual password
        if (!passwordEncoder.matches(currentPassword, loggedInUser.getPassword())) {
            return false; // Passwords don't match
        }

        // Update the user's password with the new one
        loggedInUser.setPassword(passwordEncoder.encode(newPassword));
        loggedInUser.setFirstTimeLogin(false);
        userRepository.save(loggedInUser);

        return true;
    }

}
