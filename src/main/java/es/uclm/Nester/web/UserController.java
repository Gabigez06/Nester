package es.uclm.Nester.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import es.uclm.Nester.domain.User;

import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registro")
    public String showRegistrationForm() {
        return "registro";
    }

    @PostMapping("/registro")
    public String register(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam UserRole role,
            RedirectAttributes redirectAttributes) {

        try {
            userService.register(name, surname, email, phone, password, role);
            return "redirect:/";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/registro";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
        @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userService.login(email, password);
            
            // Guardamos el usuario en la sesión para saber que está logueado
            session.setAttribute("usuarioLogueado", user);
            
            return "redirect:/";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/login";
        }
    }
}