package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.Categorie;
import com.commercel.tshirt.Entity.Produit;
import com.commercel.tshirt.Repository.CategorieRepository;
import com.commercel.tshirt.Repository.ProduitRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    // Liste des produits
    @GetMapping
    public String listProduits(Model model) {
        model.addAttribute("produits", produitRepository.findAll());
        return "produit/liste";
    }

    @GetMapping("/user")
    public String listProduitsUser(Model model) {
        model.addAttribute("produits", produitRepository.findAll());
        model.addAttribute("categories", categorieRepository.findAll());
        return "produit/userProduit";
    }

    // Formulaire d'ajout
    @GetMapping("/ajouter")
    public String showAddForm(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("categories", categorieRepository.findAll());
        return "produit/form";
    }

    // Enregistrer produit
    @PostMapping("/ajouter")
    public String addProduit(@Valid @ModelAttribute("produit") Produit produit, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categorieRepository.findAll());
            return "produit/form";
        }
        produitRepository.save(produit);
        return "redirect:/produits";
    }

    // Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalide : " + id));
        model.addAttribute("produit", produit);
        model.addAttribute("categories", categorieRepository.findAll());
        return "produit/form";
    }

    // Supprimer
    @GetMapping("/supprimer/{id}")
    public String deleteProduit(@PathVariable Integer id) {
        produitRepository.deleteById(id);
        return "redirect:/produits";
    }

    // Produits par catégorie
    @GetMapping("/categorie/{id}")
    public String produitsParCategorie(@PathVariable Integer id, Model model) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));
        List<Produit> produits = produitRepository.findByCategorieId(id);
        model.addAttribute("categorie", categorie);
        model.addAttribute("produits", produits);
        return "produit/par_categorie";
    }

    @PostMapping("/modifier/{id}")
    public String updateProduit(@PathVariable Integer id,
            @Valid @ModelAttribute("produit") Produit produit,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categorieRepository.findAll());
            return "produit/form";
        }

        // Récupérer l'ancien produit de la base
        Produit ancienProduit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalide : " + id));

        // Mettre à jour les champs
        ancienProduit.setNom(produit.getNom());
        ancienProduit.setThumbnail(produit.getThumbnail());
        ancienProduit.setTaille(produit.getTaille());
        ancienProduit.setCouleur(produit.getCouleur());
        ancienProduit.setPrix(produit.getPrix());
        ancienProduit.setQuantite(produit.getQuantite());
        ancienProduit.setCategorie(produit.getCategorie());

        produitRepository.save(ancienProduit);

        return "redirect:/produits";
    }

}
