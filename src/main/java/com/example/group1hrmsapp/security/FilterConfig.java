package com.example.group1hrmsapp.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilter() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtTokenFilter("yourSecretKey")); // Replace with your actual secret key
        registrationBean.addUrlPatterns("/employeeList/*"); // Add URL patterns for your protected endpoints in EmployeeController
        registrationBean.addUrlPatterns("/showNewEmployeeForm/*");
        registrationBean.addUrlPatterns("/saveEmployee/*");
        registrationBean.addUrlPatterns("/showUpdateEmployeeForm/*");
        registrationBean.addUrlPatterns("/deleteEmployee/*");
        registrationBean.addUrlPatterns("/page/*");
        registrationBean.setOrder(1); // Set the order to ensure it runs before other filters
        return registrationBean;
    }
}

