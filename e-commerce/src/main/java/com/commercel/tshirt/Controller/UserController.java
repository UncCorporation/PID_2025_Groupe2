package com.commercel.tshirt.Controller;

import com.commercel.tshirt.dto.PasswordChangeDto;
import com.commercel.tshirt.dto.ProfileUpdateDto;
import com.commercel.tshirt.response.ServiceResponse;
import com.commercel.tshirt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users/me")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gère la soumission du formulaire de mise à jour des informations
     * personnelles.
     * Valide les données, appelle le service, et redirige vers la page de profil
     * avec un message de succès ou d'erreur.
     */
    @PostMapping("/update")
    public String updateProfile(@Valid ProfileUpdateDto profileUpdateDto,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Si la validation échoue, on renvoie les messages d'erreur à la vue.
            redirectAttributes.addFlashAttribute("errors_profile",
                    bindingResult.getAllErrors().stream()
                            .map(e -> e.getDefaultMessage())
                            .collect(Collectors.toList()));
            return "redirect:/profil";
        }

        userService.updateProfile(principal.getName(), profileUpdateDto);
        // On ajoute un message de succès pour l'afficher sur la page de profil.
        redirectAttributes.addFlashAttribute("success_profile", "Informations personnelles mises à jour avec succès !");
        return "redirect:/profil";
    }

    /**
     * Gère la soumission du formulaire de changement de mot de passe.
     * Valide les données, vérifie les mots de passe, appelle le service,
     * et redirige vers la page de profil avec un message de succès ou d'erreur.
     */
    @PostMapping("/change-password")
    public String changePassword(@Valid PasswordChangeDto passwordChangeDto,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors_password",
                    bindingResult.getAllErrors().stream()
                            .map(e -> e.getDefaultMessage())
                            .collect(Collectors.toList()));
            return "redirect:/profil";
        }

        // Vérification manuelle de la correspondance des mots de passe.
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("errors_password",
                    List.of("La confirmation ne correspond pas au nouveau mot de passe."));
            return "redirect:/profil";
        }

        ServiceResponse<?> response = userService.changePassword(principal.getName(), passwordChangeDto);
        if (!response.isSuccess()) {
            // Le service a renvoyé une erreur (ex: mauvais mot de passe actuel).
            redirectAttributes.addFlashAttribute("errors_password", response.getErrors().values());
            return "redirect:/profil";
        }

        redirectAttributes.addFlashAttribute("success_password", "Mot de passe changé avec succès !");
        return "redirect:/profil";
    }
}
