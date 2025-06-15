package com.commercel.tshirt.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/login")
    public String showLoginPage() {
        log.info("TRACE: Reached AuthController to serve the custom login page at /login");
        return "login"; // This returns the templates/login.html view
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        log.warn("TRACE: Reached AuthController to serve the access-denied page.");
        return "access-denied"; // Assumes you have an access-denied.html
    }
}