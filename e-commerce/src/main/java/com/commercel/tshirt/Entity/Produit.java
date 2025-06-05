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
    private Integer id;

    private String nom;
    private String description;
    private Integer prix;
    private String marque;
    private Integer miniatureImageIndex;
    private String couleur;
    private String taille;

    @ElementCollection
    @CollectionTable(name = "produit_images_vitrine", joinColumns = @JoinColumn(name = "produit_id"))
    @Column(name = "url_image", nullable = false)
    @OrderColumn(name = "image_order")
    private List<String> imagesVitrine;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @Transient
    public String getMiniatureUrl() {
        if (imagesVitrine != null && !imagesVitrine.isEmpty() && miniatureImageIndex != null &&
                miniatureImageIndex >= 0 && miniatureImageIndex < imagesVitrine.size()) {
            return imagesVitrine.get(miniatureImageIndex);
        }
        // Consider returning a default placeholder URL or throwing an exception
        // if a valid miniature cannot be determined based on business rules.
        return null;
    }
}
