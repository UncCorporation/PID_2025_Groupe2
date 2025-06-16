package com.commercel.tshirt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDto {
    @NotBlank(message = "Le mot de passe actuel est requis")
    private String currentPassword;
    @NotBlank(message = "Le nouveau mot de passe est requis")
    @Size(min = 8, message = "Le nouveau mot de passe doit contenir au moins 8 caractères")
    private String newPassword;
    @NotBlank(message = "La confirmation du mot de passe est requise")
    private String confirmPassword;
}