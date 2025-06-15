package com.commercel.tshirt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        /**
         * Defines a bean for the password encoder.
         * We use BCrypt, which is the industry standard for hashing passwords.
         * 
         * @return A BCryptPasswordEncoder instance.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        /**
         * Configures the security filter chain that applies to all HTTP requests.
         * This is the central place to define security rules.
         * 
         * @param http The HttpSecurity to configure.
         * @return The configured SecurityFilterChain.
         * @throws Exception If an error occurs during configuration.
         */
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                // Configure authorization rules
                                .authorizeHttpRequests(authz -> authz
                                                // Public resources that everyone can access
                                                .requestMatchers(
                                                                "/js/**",
                                                                "/css/**",
                                                                "/images/**",
                                                                "/webjars/**")
                                                .permitAll()
                                                // Public pages AND the public API endpoints they call
                                                .requestMatchers("/", "/produits/**", "/categories/**",
                                                                "/api/produits/**", "/api/categories/**")
                                                .permitAll()
                                                // Registration and login endpoints must be public
                                                .requestMatchers("/register", "/login").permitAll()
                                                // Secure admin area
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                // All other requests must be authenticated
                                                .anyRequest().authenticated())

                                // Configure form-based login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/profil", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                // Configure logout using the recommended POST method
                                .logout(logout -> logout
                                                .logoutUrl("/logout") // The URL that triggers logout
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())

                                // Configure exception handling for access denied
                                .exceptionHandling(exceptions -> exceptions
                                                .accessDeniedPage("/access-denied"));

                return http.build();
        }
}