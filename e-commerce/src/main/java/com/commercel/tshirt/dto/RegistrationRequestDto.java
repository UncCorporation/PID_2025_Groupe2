package com.commercel.tshirt.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDto {

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email ne peut pas être vide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    @Valid // Déclenche la validation sur l'objet AdresseDto imbriqué
    private AdresseDto adresse;
}