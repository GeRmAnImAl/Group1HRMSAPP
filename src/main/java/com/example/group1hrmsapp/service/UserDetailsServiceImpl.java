package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * A service class that implements the UserDetailsService interface from Spring Security to provide core user details functionality.
 * This service is responsible for loading user-specific data and is used by Spring Security for user authentication and authorization.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserDetailsServiceImpl that injects the UserRepository dependency.
     * @param userRepository A UserRepository instance used for user data persistence.
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user entity based on the given username.
     * @param username The username of the user to retrieve.
     * @return A UserDetails object that contains the user's information.
     * @throws UsernameNotFoundException If a user with the given username is not found in the repository.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Map the access level to an authority.
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getAccessLevel().name());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }

}
