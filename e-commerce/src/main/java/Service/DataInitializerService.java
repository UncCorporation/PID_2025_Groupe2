package com.commercel.tshirt.service; // Or your chosen package

import com.commercel.tshirt.Entity.Categorie;
import com.commercel.tshirt.Entity.Produit;
import com.commercel.tshirt.Repository.CategorieRepository;
import com.commercel.tshirt.Repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DataInitializerService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @PostConstruct
    @Transactional
    public void populateData() {
        // Using a map to keep track of categories processed in this run to minimize DB
        // hits
        Map<Integer, Categorie> processedCategories = new HashMap<>();

        // Populate Categories first if needed, or handle them on-the-fly
        // For simplicity, we handle them on-the-fly with products.

        if (produitRepository.count() == 0) { // Check if products need to be populated
            List<ProduitData> initialProduitData = Arrays.asList(
                    new ProduitData(2, 1, "T-shirt Unisexe", "T-shirt confortable et polyvalent pour tous.", 20, "Nike",
                            "https://nobero.com/cdn/shop/files/og.jpg?v=1722234051", "Blanc", "L"),
                    new ProduitData(1, 2, "T-shirt Noir", "Un t-shirt en coton doux", 25, "Adidas",
                            "https://i.etsystatic.com/42116567/r/il/4cecee/5125476409/il_570xN.5125476409_g83c.jpg",
                            "Noir", "M"),
                    new ProduitData(4, 3, "T-shirt Rouge", "Un t-shirt en polyester léger", 22, "Puma",
                            "https://assets.hermes.com/is/image/hermesproduct/t-shirt-broderie-h--072025HA01-worn-4-0-0-800-800_g.jpg",
                            "Rouge", "s"),
                    new ProduitData(2, 4, "T-shirt rob", "T-shirt parfait pour le sport", 22, "Reebok",
                            "https://trans-shirt.fr/cdn/shop/products/SS031_Burgundy_FT_600x600.jpg", "Rouge", "s"),
                    new ProduitData(2, 5, "T-shirt Vert", "T-shirt écologique fabriqué à partir de matériaux recyclés",
                            30, "Patagonia",
                            "https://ker-crea.fr/5804-large_default/tee-shirt-homme-noir-personnalisable.jpg", "vert",
                            "L"),
                    new ProduitData(3, 6, "T-shirt Gris", "Un t-shirt polyvalent au design minimaliste", 19,
                            "Under Armour",
                            "https://sport-clique.fr/2773-tm_large_default/t-shirt-sport-gris-clair.jpg", "Gris", "M"),
                    new ProduitData(3, 7, "T-shirt jaune", "T-shirt vibrant pour un style estival", 21, "H&M",
                            "https://cdn3.brentinyparis.com/146898-zoom_image/t-shirt-large-jaune-uni.jpg", "Jaune",
                            "L"),
                    new ProduitData(1, 8, "T-shirt Violet", "Un t-shirt doux et léger pour un confort optimal", 23,
                            "Uniqlo", "https://m.media-amazon.com/images/I/61gn3DFLlWL._AC_UY1000_.jpg", "Violet", "S"),
                    new ProduitData(2, 9, "T-shirt Orange", "Idéal pour les amateurs de couleurs éclatantes", 24,
                            "Zara", "https://remote.max.maxhat.com/images/files/11941-31039-adpart.jpg", "Orange",
                            "XL"),
                    new ProduitData(4, 10, "T-shirt Rose", "Un t-shirt tendance et moderne", 20, "Levi's",
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSS2OZOd0xZpbvN0ghPSEnAOa6fUbUqqmp3QA&s",
                            "rose", "M"));

            for (ProduitData data : initialProduitData) {
                Integer categorieId = data.getCategorieId();
                Categorie categorie = processedCategories.get(categorieId);

                if (categorie == null) {
                    Optional<Categorie> optCategorie = categorieRepository.findById(categorieId);
                    if (optCategorie.isPresent()) {
                        categorie = optCategorie.get();
                    } else {
                        // Categorie doesn't exist, create it
                        categorie = new Categorie();
                        categorie.setId(categorieId); // Using the ID from your data
                        // Your data doesn't specify category names, so using a placeholder.
                        // You might want to add a 'nom' to your ProduitData or have a predefined map.
                        categorie.setNom("Category " + categorieId);
                        categorie = categorieRepository.save(categorie);
                    }
                    processedCategories.put(categorieId, categorie);
                }

                Produit produit = new Produit();
                produit.setId(data.getId()); // Assuming IDs from data are used. If DB generated, remove this.
                produit.setNom(data.getNom());
                produit.setDescription(data.getDescription());
                produit.setPrix(data.getPrix());
                produit.setMarque(data.getMarque());
                produit.setCouleur(data.getCouleur());
                produit.setTaille(data.getTaille());
                produit.setCategorie(categorie); // Set the fetched or newly created category

                // Populate imagesVitrine. The first image added will be at order 0 (our
                // "thumbnail").
                List<String> images = new ArrayList<>();
                if (data.getThumbnail() != null && !data.getThumbnail().isEmpty()) {
                    images.add(data.getThumbnail()); // This will be the first image, hence the thumbnail
                    // If you have more images for a product in your source data, add them here.
                }
                produit.setImagesVitrine(images);

                produitRepository.save(produit);
            }
            System.out.println("Initial data populated: " + categorieRepository.count() + " categories and "
                    + produitRepository.count() + " produits.");
        } else {
            System.out.println("Data already exists. Skipping population.");
        }
    }

    // Helper class for ProduitData (same as before)
    private static class ProduitData {
        private Integer categorieId;
        private Integer id;
        private String nom;
        private String description;
        private Integer prix;
        private String marque;
        private String thumbnail;
        private String couleur;
        private String taille;

        public ProduitData(Integer categorieId, Integer id, String nom, String description, Integer prix, String marque,
                String thumbnail, String couleur, String taille) {
            this.categorieId = categorieId;
            this.id = id;
            this.nom = nom;
            this.description = description;
            this.prix = prix;
            this.marque = marque;
            this.thumbnail = thumbnail;
            this.couleur = couleur;
            this.taille = taille;
        }

        // Getters
        public Integer getCategorieId() {
            return categorieId;
        }

        public Integer getId() {
            return id;
        }

        public String getNom() {
            return nom;
        }

        public String getDescription() {
            return description;
        }

        public Integer getPrix() {
            return prix;
        }

        public String getMarque() {
            return marque;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public String getCouleur() {
            return couleur;
        }

        public String getTaille() {
            return taille;
        }
    }
}
