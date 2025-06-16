package com.commercel.tshirt.dto;

import com.commercel.tshirt.Entity.Role;
import lombok.Data;

import java.util.List;

/**
 * Données exposées à la vue « profil ».
 * Contient l'ID, l'email, le nom, le prénom, le rôle
 * et la(les) adresse(s) de l'utilisateur.
 */
@Data
public class ProfileDto {
    private String email;
    private String nom;
    private String prenom;
    private Role role;
    private List<AdresseDto> adresses; // 0..n adresses
}