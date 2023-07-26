package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AppUser;

/**
 *
 */
public interface UserService {
    /**
     * Retrieves an AppUser entity by the given username.
     * @param userName The username of the AppUser to retrieve.
     * @return An AppUser object if a user with the given username is found, otherwise a RuntimeException is thrown.
     * @throws RuntimeException If a user with the given username is not found in the repository.
     */
    AppUser getUserById(String userName);

    /**
     * Saves the provided AppUser entity to the repository. The user's password will be encoded before saving.
     * @param appUser The AppUser entity to be saved. The user's password in this object should be in plain text, as this method will handle the password encoding.
     */
    void saveUser(AppUser appUser);

    /**
     * Deletes an AppUser entity from the repository based on the provided username.
     * @param userName The username of the AppUser to be deleted.
     */
    void deleteUserById(String userName);

    /**
     * Retrieves all AppUser entities from the repository.
     * @return A list of all AppUser entities stored in the repository.
     */
    Object getAllUsers();

    /**
     * Generates a JWT token for the provided username. The token will expire one hour after its creation time.
     * @param username The username for which to generate a JWT token.
     * @return A JWT token as a string, signed with HS512 algorithm and the provided secret key.
     */
    String generateJwtToken(String username);

    boolean changePassword(String username, String currentPassword, String newPassword);
}
