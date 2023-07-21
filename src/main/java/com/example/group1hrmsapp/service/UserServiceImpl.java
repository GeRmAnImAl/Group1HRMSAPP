package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AccessLevel;
import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
        Optional<AppUser> optional = userRepository.findById(userName);
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
     * Generates a JWT token for the provided username. The token will expire one hour after its creation time.
     * @param username The username for which to generate a JWT token.
     * @return A JWT token as a string, signed with HS512 algorithm and the provided secret key.
     */
    @Override
    public String generateJwtToken(String username) {
        long expirationMillis = System.currentTimeMillis() + (60 * 60 * 1000);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationMillis))
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
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

}
