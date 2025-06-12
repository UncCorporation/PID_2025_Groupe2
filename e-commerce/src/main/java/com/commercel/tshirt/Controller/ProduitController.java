package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.Produit;
import com.commercel.tshirt.Repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "*")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Tous les produits par catégorie
    @GetMapping("/categorie/{id}")
    public List<Produit> getProduitsByCategorie(@PathVariable Integer id) {
        return produitRepository.findByCategorieId(id);
    }

    // Détails d'un produit
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Integer id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}