package com.example.group1hrmsapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is used to configure the security settings for the application.
 * It defines the security policies to apply on different URLs and configures user authentication.
 * It also provides a password encoder bean.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * Constructor for SecurityConfig.
     * @param userDetailsService A UserDetailsService instance used for user data retrieval during authentication.
     */
    public SecurityConfig(UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    /**
     * Configures the AuthenticationManagerBuilder with the UserDetailsService and password encoder.
     * @param auth An AuthenticationManagerBuilder instance to configure.
     * @throws Exception If there is an issue with the configuration.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Provides the BCryptPasswordEncoder for password encoding.
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the HttpSecurity instance with the desired security settings.
     * @param http An HttpSecurity instance to configure.
     * @throws Exception If there is an issue with the configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/low/**").hasAuthority("ROLE_LOW")
                .antMatchers("/medium/**").hasAnyAuthority("ROLE_MEDIUM", "ROLE_HIGH")
                .antMatchers("/high/**").hasAuthority("ROLE_HIGH")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Logout URL
                .invalidateHttpSession(true) // Invalidate user session
                .clearAuthentication(true) // Clear user authentication
                .permitAll()
                .and()
                .csrf().disable(); // You may want to enable CSRF protection in a production application.
    }



}

