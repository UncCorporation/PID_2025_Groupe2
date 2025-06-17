package com.commercel.tshirt.Controller;

import com.commercel.tshirt.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            userService.getUserByEmail(principal.getName())
                    .ifPresent(user -> model.addAttribute("currentPrenom", user.getPrenom()));
        }
        return "index";
    }

    @GetMapping("/conditions-generales")
    public String conditions() {
        return "conditions_generales";
    }
}