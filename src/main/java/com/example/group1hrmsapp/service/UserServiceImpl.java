package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.User;
import com.example.group1hrmsapp.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Override
    public User getUserById(String userName) {
        Optional<User> optional = userRepository.findById(userName);
        User user = null;
        if(optional.isPresent()){
            user = optional.get();
        }
        else{
            throw new RuntimeException("Username '" + userName + "' not found.");
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Override
    public void deleteUserById(String userName) {
        this.userRepository.deleteById(userName);
    }

    @Override
    public Object getAllUsers() {
        return userRepository.findAll();
    }

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
}
