package com.commercel.tshirt.service;

import com.commercel.tshirt.Entity.Adresse;
import com.commercel.tshirt.Entity.Role;
import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.Repository.UserRepository;
import com.commercel.tshirt.dto.PasswordChangeDto;
import com.commercel.tshirt.dto.ProfileUpdateDto;
import com.commercel.tshirt.dto.RegistrationRequestDto;
import com.commercel.tshirt.response.ServiceResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public ServiceResponse<User> registerNewUser(RegistrationRequestDto registrationRequest) {
        Optional<User> existingUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (existingUser.isPresent()) {
            Map<String, String> error = Map.of("email",
                    "Un compte avec l'email " + registrationRequest.getEmail() + " existe déjà.");
            return new ServiceResponse<>(error);
        }

        User newUser = new User();
        newUser.setNom(registrationRequest.getNom());
        newUser.setPrenom(registrationRequest.getPrenom());
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setMotDePasse(passwordEncoder.encode(registrationRequest.getMotDePasse()));
        newUser.setRole(Role.ROLE_CLIENT);

        Adresse newAdresse = new Adresse();
        newAdresse.setRue(registrationRequest.getAdresse().getRue());
        newAdresse.setCodePostal(registrationRequest.getAdresse().getCodePostal());
        newAdresse.setVille(registrationRequest.getAdresse().getVille());
        newAdresse.setPays(registrationRequest.getAdresse().getPays());
        newAdresse.setLibelle(registrationRequest.getAdresse().getLibelle());

        newAdresse.setUser(newUser);

        newUser.setAdresses(Collections.singletonList(newAdresse));

        User savedUser = userRepository.save(newUser);

        return new ServiceResponse<>(savedUser);
    }

    @Transactional
    public ServiceResponse<User> updateProfile(String email, ProfileUpdateDto profileUpdateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        user.setNom(profileUpdateDto.getNom());
        user.setPrenom(profileUpdateDto.getPrenom());

        User updatedUser = userRepository.save(user);
        return new ServiceResponse<>(updatedUser);
    }

    @Transactional
    public ServiceResponse<?> changePassword(String email, PasswordChangeDto passwordChangeDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        if (!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getMotDePasse())) {
            return new ServiceResponse<>(Map.of("currentPassword", "Le mot de passe actuel est incorrect."));
        }

        user.setMotDePasse(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);

        return new ServiceResponse<>(Map.of("message", "Mot de passe changé avec succès."));
    }
}