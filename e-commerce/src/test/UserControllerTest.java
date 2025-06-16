package com.commercel.tshirt.Controller;

import com.commercel.tshirt.dto.PasswordChangeDto;
import com.commercel.tshirt.dto.ProfileUpdateDto;
import com.commercel.tshirt.response.ServiceResponse;
import com.commercel.tshirt.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Test mise à jour du profil avec données valides
    @Test
    @WithMockUser(username = "test@example.com")
    void updateProfile_validData_redirectsWithSuccess() throws Exception {
        mockMvc.perform(post("/users/me/update")
                        .param("nom", "Doe")
                        .param("prenom", "John"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("success_profile"));

        verify(userService).updateProfile("test@example.com", any(ProfileUpdateDto.class));
    }

    // Test mise à jour du profil avec champ vide → erreur
    @Test
    @WithMockUser(username = "test@example.com")
    void updateProfile_invalidData_redirectsWithErrors() throws Exception {
        mockMvc.perform(post("/users/me/update")
                        .param("nom", "") // nom vide
                        .param("prenom", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("errors_profile"));
    }

    // Test changement mot de passe → succès
    @Test
    @WithMockUser(username = "test@example.com")
    void changePassword_validData_redirectsWithSuccess() throws Exception {
        when(userService.changePassword(any(), any()))
                .thenReturn(new ServiceResponse<>(true, null));

        mockMvc.perform(post("/users/me/change-password")
                        .param("currentPassword", "oldpass")
                        .param("newPassword", "newpass123")
                        .param("confirmPassword", "newpass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("success_password"));

        verify(userService).changePassword(Mockito.eq("test@example.com"), any(PasswordChangeDto.class));
    }

    // Test mot de passe : non concordants
    @Test
    @WithMockUser(username = "test@example.com")
    void changePassword_mismatchPasswords_redirectsWithError() throws Exception {
        mockMvc.perform(post("/users/me/change-password")
                        .param("currentPassword", "oldpass")
                        .param("newPassword", "pass123")
                        .param("confirmPassword", "differentPass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("errors_password"));
    }

    // Test mot de passe : mauvais mot de passe actuel
    @Test
    @WithMockUser(username = "test@example.com")
    void changePassword_wrongCurrentPassword_redirectsWithServiceError() throws Exception {
        HashMap<String, String> errors = new HashMap<>();
        errors.put("currentPassword", "Mot de passe actuel incorrect");

        when(userService.changePassword(any(), any()))
                .thenReturn(new ServiceResponse<>(false, errors));

        mockMvc.perform(post("/users/me/change-password")
                        .param("currentPassword", "wrongpass")
                        .param("newPassword", "pass123")
                        .param("confirmPassword", "pass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attribute("errors_password", errors.values()));
    }
}

