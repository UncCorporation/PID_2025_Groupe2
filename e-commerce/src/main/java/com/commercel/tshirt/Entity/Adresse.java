package com.commercel.tshirt.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rue;

    @Column(nullable = false)
    private String codePostal;
    @Column(nullable = false)
    private String ville;
    @Column(nullable = false)
    private String pays;

    private String libelle; // e.g., "Facturation", "Livraison"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}