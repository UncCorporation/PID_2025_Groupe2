package com.commercel.tshirt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdresseDto {
    @NotBlank(message = "La rue ne peut pas être vide")
    private String rue;

    @NotBlank(message = "Le code postal ne peut pas être vide")
    private String codePostal;

    @NotBlank(message = "La ville ne peut pas être vide")
    private String ville;

    @NotBlank(message = "Le pays ne peut pas être vide")
    private String pays;

    private String libelle; // "Maison", "Travail", etc. Optionnel.
}