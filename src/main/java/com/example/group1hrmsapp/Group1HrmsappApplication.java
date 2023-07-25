package com.example.group1hrmsapp;

import com.example.group1hrmsapp.model.AccessLevel;
import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Group1HrmsappApplication {

    public static void main(String[] args) {
        SpringApplication.run(Group1HrmsappApplication.class, args);


    }
}
