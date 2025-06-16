package com.commercel.tshirt.service;

import com.commercel.tshirt.Entity.Adresse;
import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.Repository.UserRepository;
import com.commercel.tshirt.dto.PasswordChangeDto;
import com.commercel.tshirt.dto.ProfileUpdateDto;
import com.commercel.tshirt.dto.RegistrationRequestDto;
import com.commercel.tshirt.response.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerNewUser_shouldCreateUser_whenEmailIsNotTaken() {
        RegistrationRequestDto dto = new RegistrationRequestDto();
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setEmail("jean@example.com");
        dto.setMotDePasse("password123");

        var adresseDto = new com.commercel.tshirt.dto.AdresseDto();
        adresseDto.setRue("Rue A");
        adresseDto.setCodePostal("75000");
        adresseDto.setVille("Paris");
        adresseDto.setPays("France");
        adresseDto.setLibelle("Maison");
        dto.setAdresse(adresseDto);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded123");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceResponse<User> response = userService.registerNewUser(dto);

        assertTrue(response.isSuccess());
        assertEquals("jean@example.com", response.getData().getEmail());
        assertEquals("encoded123", response.getData().getMotDePasse());
        assertEquals(1, response.getData().getAdresses().size());
        assertEquals("Rue A", response.getData().getAdresses().get(0).getRue());
    }

    @Test
    void registerNewUser_shouldReturnError_whenEmailExists() {
        RegistrationRequestDto dto = new RegistrationRequestDto();
        dto.setEmail("taken@example.com");

        when(userRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(new User()));

        ServiceResponse<User> response = userService.registerNewUser(dto);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().containsKey("email"));
    }

    @Test
    void updateProfile_shouldUpdateUser_whenEmailExists() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setNom("Old");
        user.setPrenom("Name");

        ProfileUpdateDto dto = new ProfileUpdateDto();
        dto.setNom("New");
        dto.setPrenom("Name");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        ServiceResponse<User> response = userService.updateProfile("user@example.com", dto);

        assertTrue(response.isSuccess());
        assertEquals("New", response.getData().getNom());
    }

    @Test
    void changePassword_shouldSucceed_whenCurrentPasswordIsCorrect() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setMotDePasse("hashedOld");

        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("oldPassword");
        dto.setNewPassword("newPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "hashedOld")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("hashedNew");

        ServiceResponse<?> response = userService.changePassword("user@example.com", dto);

        assertTrue(response.isSuccess());
        verify(userRepository).save(user);
        assertEquals("hashedNew", user.getMotDePasse());
    }

    @Test
    void changePassword_shouldFail_whenCurrentPasswordIsWrong() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setMotDePasse("hashedOld");

        PasswordChangeDto dto = new PasswordChangeDto();
        dto.setCurrentPassword("wrong");
        dto.setNewPassword("newPassword");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashedOld")).thenReturn(false);

        ServiceResponse<?> response = userService.changePassword("user@example.com", dto);

        assertFalse(response.isSuccess());
        assertTrue(response.getErrors().containsKey("currentPassword"));
    }
}

