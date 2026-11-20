package es.uclm.Nester.web;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.RentalRequestService;

@Controller
public class RentalOwnerController {

    private final RentalRequestService rentalRequestService;

    public RentalOwnerController(
            RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    @GetMapping("/propietario/solicitudes")
    public String showRequests(HttpSession session, Model model) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User owner)) {
            return "redirect:/login";
        }

        if (owner.getRole() != UserRole.PROPIETARIO) {
            return "redirect:/";
        }

        model.addAttribute(
                "solicitudes",
                rentalRequestService.getPendingRequestsForOwner(owner));

        return "solicitudes";
    }

    @PostMapping("/propietario/solicitudes/{requestId}/aprobar")
    public String approve(
            @PathVariable Long requestId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User owner)) {
            return "redirect:/login";
        }

        if (owner.getRole() != UserRole.PROPIETARIO) {
            return "redirect:/";
        }

        try {
            rentalRequestService.approveRental(requestId, owner);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Solicitud aprobada.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage());
        }

        return "redirect:/propietario/solicitudes";
    }

    @PostMapping("/propietario/solicitudes/{requestId}/rechazar")
    public String reject(
            @PathVariable Long requestId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User owner)) {
            return "redirect:/login";
        }

        if (owner.getRole() != UserRole.PROPIETARIO) {
            return "redirect:/";
        }

        try {
            rentalRequestService.rejectRental(requestId, owner);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Solicitud rechazada.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage());
        }

        return "redirect:/propietario/solicitudes";
    }
}