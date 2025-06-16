package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.Categorie;
import com.commercel.tshirt.Repository.CategorieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategorieController.class)
public class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorieRepository categorieRepository;

    @Test
    void getAllCategories_returnsJsonList() throws Exception {
        // Mock données de test
        Categorie cat1 = new Categorie();
        cat1.setId(1L);
        cat1.setNom("Homme");

        Categorie cat2 = new Categorie();
        cat2.setId(2L);
        cat2.setNom("Femme");

        List<Categorie> mockCategories = List.of(cat1, cat2);

        // Simulation du comportement du repo
        when(categorieRepository.findAll()).thenReturn(mockCategories);

        // Vérification du comportement de l'API
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nom").value("Homme"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nom").value("Femme"));
    }
}
