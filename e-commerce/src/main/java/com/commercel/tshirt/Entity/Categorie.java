package com.commercel.tshirt.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Categorie {

    @Id
    private Integer id;

    private String nomCategorie;
    private String miniature;
    private String description;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Produit> produits;
}
