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

    @Override
    public void saveUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        this.userRepository.save(appUser);
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

    public void createUser(String username, String password) {
        AppUser appUser = new AppUser();
        appUser.setUserName(username);
        appUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(appUser);
    }

}
