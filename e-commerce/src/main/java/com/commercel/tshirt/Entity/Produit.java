package com.commercel.tshirt.Entity;
import jakarta.persistence.*;

@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;
    private String description;
    private Integer prix;
    private String marque;
    private String thumbnail;
    private String couleur;
    private String taille;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    // 🔹 Constructeur par défaut requis par JPA
    public Produit() {}
     // 🔹 Constructeur pratique
    public Produit(String nom, String description, Integer prix, String marque,
                   String thumbnail, String couleur, String taille, Categorie categorie) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.marque = marque;
        this.thumbnail = thumbnail;
        this.couleur = couleur;
        this.taille = taille;
        this.categorie = categorie;
    }

    // 🔹 Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
 public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrix() {
        return prix;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
}
}


