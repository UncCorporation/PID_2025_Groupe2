package com.commercel.tshirt.service;

import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.Repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * This service is responsible for loading user-specific data.
 * It's a core component of Spring Security's authentication process.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // We use constructor injection, which is a best practice for Spring.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the email. In the Spring Security framework,
     * the "username" is often used as the unique identifier, which we map to our
     * email field.
     *
     * @param email the email identifying the user whose data is required.
     * @return a fully populated user record (never {@code null})
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Find the user entity from the database using the repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur trouvé avec l'email : " + email));

        // 2. Convert our custom User entity into a UserDetails object that Spring
        // Security understands.
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getMotDePasse(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}