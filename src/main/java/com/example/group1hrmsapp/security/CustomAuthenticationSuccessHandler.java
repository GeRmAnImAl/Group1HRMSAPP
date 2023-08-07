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

/**
 * Customizes the behavior of successful authentication in the application.
 * It checks whether a user logs in for the first time and redirects to a
 * password change form if true, or to the main dashboard otherwise.
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private UserRepository userRepository;

    /**
     * Constructs a new CustomAuthenticationSuccessHandler.
     * @param userRepository the user repository used to fetch user details.
     */
    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles the behavior after a successful authentication. Redirects users
     * to specific pages based on whether it's their first time logging in.
     * @param request the servlet request.
     * @param response the servlet response.
     * @param authentication the current authentication object.
     * @throws IOException in case of IO errors.
     * @throws ServletException in case of servlet errors.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        // Fetch the AppUser entity associated with the username
        AppUser loggedInUser = userRepository.findByUserName(loggedInUsername)
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

