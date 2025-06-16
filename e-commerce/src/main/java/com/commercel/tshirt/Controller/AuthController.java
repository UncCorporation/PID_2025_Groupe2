package com.commercel.tshirt.Controller;

import com.commercel.tshirt.Entity.User;
import com.commercel.tshirt.dto.AdresseDto;
import com.commercel.tshirt.dto.ProfileDto;
import com.commercel.tshirt.dto.RegistrationRequestDto;
import com.commercel.tshirt.response.ServiceResponse;
import com.commercel.tshirt.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profil")
    public String showProfilePage(Model model, Principal principal) {
        // Récupérer l'utilisateur connecté
        String email = principal.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Conversion User -> ProfileDto
        ProfileDto profile = new ProfileDto();
        profile.setEmail(user.getEmail());
        profile.setNom(user.getNom());
        profile.setPrenom(user.getPrenom());
        profile.setRole(user.getRole());
        profile.setAdresses(
                user.getAdresses() == null ? List.of() : user.getAdresses().stream().map(a -> {
                    AdresseDto dto = new AdresseDto();
                    dto.setRue(a.getRue());
                    dto.setCodePostal(a.getCodePostal());
                    dto.setVille(a.getVille());
                    dto.setPays(a.getPays());
                    dto.setLibelle(a.getLibelle());
                    return dto;
                }).collect(Collectors.toList()));

        model.addAttribute("profile", profile); // ← on passe le DTO à la vue
        return "profil";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        log.info("TRACE: Reached AuthController to serve the custom login page at /login");
        return "login"; // This returns the templates/login.html view
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register"; // Renvoie templates/register.html
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDto registrationRequest) {
        ServiceResponse<User> response = userService.registerNewUser(registrationRequest);

        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Utilisateur enregistré avec succès !"));
        } else {
            // L'erreur est un conflit (email déjà existant)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response.getErrors());
        }
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        log.warn("TRACE: Reached AuthController to serve the access-denied page.");
        return "access-denied"; // Assumes you have an access-denied.html
    }

    // Ce gestionnaire reste utile pour les erreurs de validation du DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}