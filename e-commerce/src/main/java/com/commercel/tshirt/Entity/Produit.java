package com.commercel.tshirt.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String description;
    private Integer prix;
    private String marque;
    private String miniature;

    @ElementCollection
    @CollectionTable(name = "produit_images_vitrine", joinColumns = @JoinColumn(name = "produit_id"))
    @Column(name = "url_image")
    private List<String> imagesVitrine;

    private String couleur;
    private String taille;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

}
