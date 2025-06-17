package com.commercel.tshirt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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
                                // Le CSRF est maintenant ACTIVÉ par défaut.
                                // C'est crucial pour la sécurité de notre application stateful.

                                .authorizeHttpRequests(authz -> authz
                                                // Définition des endpoints publics.
                                                // Nous devons autoriser explicitement les ressources statiques.
                                                .requestMatchers("/", "/login", "/logout", "/auth/register",
                                                                "/access-denied",
                                                                "/api/produits/**", "/api/categories/**",
                                                                "/css/**", "/js/**", "/images/**",
                                                                "/register", "/conditions-generales")
                                                .permitAll()

                                                // Les membres (clients ou admins) peuvent accéder à leur profil.
                                                .requestMatchers("/users/me/**", "/profil")
                                                .hasAnyAuthority("ROLE_CLIENT", "ROLE_ADMIN")

                                                // Toute autre requête doit être authentifiée.
                                                .anyRequest().authenticated())

                                // Configuration du processus de connexion.
                                .formLogin(form -> form
                                                .loginPage("/login") // Utilise notre page de login personnalisée servie
                                                                     // par AuthController.
                                                .loginProcessingUrl("/login") // L'URL à laquelle le formulaire doit
                                                                              // être soumis.
                                                .defaultSuccessUrl("/profil", true)
                                                // Redirige vers /login?error=true en cas d'échec.
                                                // C'est la méthode standard pour les formulaires web.
                                                .failureUrl("/login?error=true"))

                                // Configuration du processus de déconnexion.
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID"))

                                // Configuration de la gestion des exceptions.
                                .exceptionHandling(exceptions -> exceptions
                                                // Pour les accès non autorisés, on renvoie un statut 403 Forbidden.
                                                // Le point d'entrée d'authentification par défaut (redirection vers la
                                                // page de login)
                                                // est maintenant utilisé, ce qui corrige le flux de navigation.
                                                .accessDeniedPage("/access-denied"));

                return http.build();
        }
}