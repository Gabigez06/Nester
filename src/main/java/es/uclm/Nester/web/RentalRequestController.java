package es.uclm.Nester.web;

import java.time.LocalDate;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.RentalRequestService;

@Controller
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;

    public RentalRequestController(
            RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    @PostMapping("/alquileres/solicitar")
    public String requestRental(
            @RequestParam Long propertyId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut,
            @RequestParam int guests,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User tenant)) {
            return "redirect:/login";
        }

        if (tenant.getRole() != UserRole.INQUILINO) {
            return "redirect:/";
        }

        try {
            rentalRequestService.requestRental(
                    propertyId,
                    tenant,
                    checkIn,
                    checkOut,
                    guests);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Solicitud de alquiler enviada al propietario.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage());
        }

        return "redirect:/";
    }
}
