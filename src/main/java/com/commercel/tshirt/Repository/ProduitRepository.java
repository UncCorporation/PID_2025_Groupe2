package com.commercel.tshirt.Repository;
import com.commercel.tshirt.Entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    List<Produit> findByCategorieId(Integer categorieId);
}
