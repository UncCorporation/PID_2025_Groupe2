package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.Categorie;
import com.commercel.tshirt.Repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CategorieController {

    @Autowired
    private CategorieRepository categorieRepository;

    /**
     * This method handles a request for data and returns a JSON response.
     * The @ResponseBody annotation is the key.
     * It behaves like the methods in your old @RestController.
     */
    @GetMapping("/api/categories")
    @CrossOrigin(origins = "*") // You might apply this only to API methods
    @ResponseBody
    public List<Categorie> getAllCategories() {
        // @ResponseBody tells Spring to serialize this List into JSON
        return categorieRepository.findAll();
    }
}
