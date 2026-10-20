package es.uclm.Nester.web;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.Nester.domain.Property;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.PropertyService;

@Controller
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/propiedades/nueva")
    public String mostrarFormulario(HttpSession session, Model model) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User usuario)) {
            return "redirect:/login";
        }

        if (usuario.getRole() != UserRole.PROPIETARIO) {
            return "redirect:/";
        }

        model.addAttribute("tipos", Property.PropertyType.values());
        model.addAttribute(
                "complementosDisponibles",
                Property.Complement.values());

        return "publicar";
    }

    @PostMapping("/propiedades/nueva")
    public String publicar(
            @RequestParam String title,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String postalCode,
            @RequestParam String address,
            @RequestParam Property.PropertyType type,
            @RequestParam int bedrooms,
            @RequestParam int beds,
            @RequestParam int bathrooms,
            @RequestParam int guestCapacity,
            @RequestParam(required = false)
                    Set<Property.Complement> complements,
            @RequestParam String description,
            @RequestParam BigDecimal pricePerNight,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User usuario)) {
            return "redirect:/login";
        }

        if (usuario.getRole() != UserRole.PROPIETARIO) {
            return "redirect:/";
        }

        Property property = new Property(
                title,
                country,
                city,
                postalCode,
                address,
                type,
                bedrooms,
                beds,
                bathrooms,
                guestCapacity,
                complements,
                description,
                pricePerNight,
                usuario);

        try {
            propertyService.publish(property);
            return "redirect:/lista";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage());
            return "redirect:/propiedades/nueva";
        }
    }
}