package com.commercel.tshirt.runner;

import com.commercel.tshirt.Entity.Adresse;
import com.commercel.tshirt.Entity.Categorie;
import com.commercel.tshirt.Entity.Produit;
import com.commercel.tshirt.Entity.Role;
import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.Repository.CategorieRepository;
import com.commercel.tshirt.Repository.ProduitRepository;
import com.commercel.tshirt.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DataInitializationRunner implements CommandLineRunner {

        @Autowired
        private ProduitRepository produitRepository;

        @Autowired
        private CategorieRepository categorieRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        private static class TempProduitData {
                Integer categorieId, id, prix, miniatureImageIndex;
                String nom, description, marque, thumbnail, couleur, taille;

                public TempProduitData(Integer categorieId, Integer id, String nom, String description, Integer prix,
                                String marque, String thumbnail, String couleur, String taille,
                                Integer miniatureImageIndex) {
                        this.categorieId = categorieId;
                        this.id = id;
                        this.nom = nom;
                        this.description = description;
                        this.prix = prix;
                        this.marque = marque;
                        this.thumbnail = thumbnail;
                        this.couleur = couleur;
                        this.taille = taille;
                        this.miniatureImageIndex = miniatureImageIndex;
                }
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                System.out.println("Executing DataInitializationRunner...");

                createInitialUsers();
                createInitialProducts();
        }

        private void createInitialUsers() {
                if (userRepository.count() == 0) {
                        System.out.println("No users found, creating initial admin and test user...");

                        // Admin User
                        User admin = new User();
                        admin.setNom("Admin");
                        admin.setPrenom("Super");
                        admin.setEmail("admin@tshirt.com");
                        admin.setMotDePasse(passwordEncoder.encode("adminPsw123!"));
                        admin.setRole(Role.ROLE_ADMIN);
                        userRepository.save(admin);

                        // Test User
                        User testUser = new User();
                        testUser.setNom("User");
                        testUser.setPrenom("Test");
                        testUser.setEmail("user@tshirt.com");
                        testUser.setMotDePasse(passwordEncoder.encode("sUperUserPsw123"));
                        testUser.setRole(Role.ROLE_CLIENT);

                        // Create and set address for test user
                        Adresse adresse = new Adresse();
                        adresse.setRue("12 rue de Paris");
                        adresse.setCodePostal("75001");
                        adresse.setVille("Paris");
                        adresse.setPays("France");
                        adresse.setLibelle("Résidence");
                        adresse.setUser(testUser);

                        testUser.setAdresses(Collections.singletonList(adresse));
                        userRepository.save(testUser);

                        System.out.println("Initial users created.");
                } else {
                        System.out.println("Users already exist. Skipping creation.");
                }
        }

        private void createInitialProducts() {
                if (produitRepository.count() == 0) {
                        System.out.println("No products found, populating initial data...");

                        Map<Integer, Categorie> processedCategories = new HashMap<>();
                        Map<Integer, String[]> categoryDetails = Map.of(
                                        1,
                                        new String[] { "T-shirts Homme", "cat_1_thumb.jpg",
                                                        "Collection de t-shirts pour hommes" },
                                        2,
                                        new String[] { "T-shirts Femme", "cat_2_thumb.jpg",
                                                        "Collection de t-shirts pour femmes" },
                                        3,
                                        new String[] { "T-shirts Enfants", "cat_3_thumb.jpg",
                                                        "Collection de t-shirts pour enfants" },
                                        4, new String[] { "T-shirts Unisexe", "cat_4_thumb.jpg",
                                                        "Collection de t-shirts unisexes" });

                        // --- FULL LIST OF 10 PRODUCTS ---
                        List<TempProduitData> initialProduitData = Arrays.asList(
                                        new TempProduitData(2, 1, "T-shirt Unisexe",
                                                        "T-shirt confortable et polyvalent pour tous.", 20,
                                                        "Nike", "https://nobero.com/cdn/shop/files/og.jpg?v=1722234051",
                                                        "Blanc", "L", 0),
                                        new TempProduitData(1, 2, "T-shirt Noir", "Un t-shirt en coton doux", 25,
                                                        "Adidas",
                                                        "https://i.etsystatic.com/42116567/r/il/4cecee/5125476409/il_570xN.5125476409_g83c.jpg",
                                                        "Noir", "M", 0),
                                        new TempProduitData(4, 3, "T-shirt Rouge", "Un t-shirt en polyester léger", 22,
                                                        "Puma",
                                                        "https://assets.hermes.com/is/image/hermesproduct/t-shirt-broderie-h--072025HA01-worn-4-0-0-800-800_g.jpg",
                                                        "Rouge", "s", 0),
                                        new TempProduitData(2, 4, "T-shirt rob", "T-shirt parfait pour le sport", 22,
                                                        "Reebok",
                                                        "https://trans-shirt.fr/cdn/shop/products/SS031_Burgundy_FT_600x600.jpg",
                                                        "Rouge", "s", 0),
                                        new TempProduitData(2, 5, "T-shirt Vert",
                                                        "T-shirt écologique fabriqué à partir de matériaux recyclés",
                                                        30, "Patagonia",
                                                        "https://ker-crea.fr/5804-large_default/tee-shirt-homme-noir-personnalisable.jpg",
                                                        "vert",
                                                        "L", 0),
                                        new TempProduitData(3, 6, "T-shirt Gris",
                                                        "Un t-shirt polyvalent au design minimaliste", 19,
                                                        "Under Armour",
                                                        "https://esi.link/gray_shirt", "Gris", "M",
                                                        0),
                                        new TempProduitData(3, 7, "T-shirt jaune",
                                                        "T-shirt vibrant pour un style estival", 21, "H&M",
                                                        "https://cdn3.brentinyparis.com/146898-zoom_image/t-shirt-large-jaune-uni.jpg",
                                                        "Jaune",
                                                        "L", 0),
                                        new TempProduitData(1, 8, "T-shirt Violet",
                                                        "Un t-shirt doux et léger pour un confort optimal", 23,
                                                        "Uniqlo",
                                                        "https://m.media-amazon.com/images/I/61gn3DFLlWL._AC_UY1000_.jpg",
                                                        "Violet", "S",
                                                        0),
                                        new TempProduitData(2, 9, "T-shirt Orange",
                                                        "Idéal pour les amateurs de couleurs éclatantes", 24,
                                                        "Zara",
                                                        "https://remote.max.maxhat.com/images/files/11941-31039-adpart.jpg",
                                                        "Orange", "XL",
                                                        0),
                                        new TempProduitData(4, 10, "T-shirt Rose", "Un t-shirt tendance et moderne", 20,
                                                        "Levi's",
                                                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSS2OZOd0xZpbvN0ghPSEnAOa6fUbUqqmp3QA&s",
                                                        "rose", "M", 0));

                        for (TempProduitData data : initialProduitData) {
                                Integer categorieId = data.categorieId;
                                Categorie categorie = processedCategories.computeIfAbsent(categorieId, id -> {
                                        Optional<Categorie> optCategorie = categorieRepository.findById(id);
                                        if (optCategorie.isPresent()) {
                                                return optCategorie.get();
                                        }
                                        Categorie newCategorie = new Categorie();
                                        newCategorie.setId(id);
                                        String[] catInfo = categoryDetails.getOrDefault(id,
                                                        new String[] { "Default", "default.png", "Default desc." });
                                        newCategorie.setNomCategorie(catInfo[0]);
                                        newCategorie.setMiniature(catInfo[1]);
                                        newCategorie.setDescription(catInfo[2]);
                                        return categorieRepository.save(newCategorie);
                                });

                                Produit produit = new Produit();
                                produit.setId(data.id);
                                produit.setNom(data.nom);
                                produit.setDescription(data.description);
                                produit.setPrix(data.prix);
                                produit.setMarque(data.marque);
                                produit.setMiniatureImageIndex(data.miniatureImageIndex);
                                produit.setCouleur(data.couleur);
                                produit.setTaille(data.taille);
                                produit.setCategorie(categorie);

                                List<String> images = new ArrayList<>();
                                if (data.thumbnail != null && !data.thumbnail.isEmpty()) {
                                        images.add(data.thumbnail);
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
}
