package com.example.group1hrmsapp;

import com.example.group1hrmsapp.service.UserServiceImpl;
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
