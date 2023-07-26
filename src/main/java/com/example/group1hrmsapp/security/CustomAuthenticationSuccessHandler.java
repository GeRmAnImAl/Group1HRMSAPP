package com.example.group1hrmsapp.security;

import com.example.group1hrmsapp.model.AppUser;
import com.example.group1hrmsapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private UserRepository userRepository;

    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findById(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("No user logged in"));
        // Check if it's the first time login
        boolean isFirstTimeLogin = loggedInUser.isFirstTimeLogin();

        if (isFirstTimeLogin) {
            // Redirect to the password change form
            response.sendRedirect(request.getContextPath() + "/showChangePasswordForm");
        } else {
            // Redirect to the default landing page after successful login
            response.sendRedirect(request.getContextPath() + "/"); // Replace "dashboard" with your desired URL.
        }
    }
}

