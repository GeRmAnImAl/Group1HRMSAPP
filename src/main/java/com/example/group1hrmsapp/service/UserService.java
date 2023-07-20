package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.User;

public interface UserService {
    User getUserById(String userName);
    void saveUser(User user);
    void deleteUserById(String userName);

    Object getAllUsers();
}
