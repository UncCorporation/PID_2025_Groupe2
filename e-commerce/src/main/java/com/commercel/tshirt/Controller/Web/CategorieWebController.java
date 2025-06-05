package com.commercel.tshirt.Controller.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategorieWebController {

    @GetMapping("/categorie")
    public String cotegoriePage() {
        return "categorie";
    }
}
