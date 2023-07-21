package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.AppUser;

public interface UserService {
    AppUser getUserById(String userName);
    void saveUser(AppUser appUser);
    void deleteUserById(String userName);

    Object getAllUsers();
    String generateJwtToken(String username);
}
