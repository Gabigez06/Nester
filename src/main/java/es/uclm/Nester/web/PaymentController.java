package es.uclm.Nester.web;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.Nester.domain.Payment;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.PaymentService;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/pagos/{requestId}")
    public String showPayment(
            @PathVariable Long requestId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        User tenant = getTenantFromSession(session);
        if (tenant == null) {
            return "redirect:/login";
        }

        try {
            model.addAttribute(
                    "solicitud",
                    paymentService.getApprovedRequestForTenant(requestId, tenant));
            model.addAttribute("metodosPago", Payment.Method.values());
            model.addAttribute(
                    "pago",
                    paymentService.getPaymentForRequest(requestId).orElse(null));
            return "pago";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/notificaciones";
        }
    }

    @PostMapping("/pagos/{requestId}")
    public String pay(
            @PathVariable Long requestId,
            @RequestParam Payment.Method method,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User tenant = getTenantFromSession(session);
        if (tenant == null) {
            return "redirect:/login";
        }

        try {
            paymentService.pay(requestId, tenant, method);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Pago completado. Tu reserva está confirmada.");
            return "redirect:/notificaciones";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/pagos/" + requestId;
        }
    }

    private User getTenantFromSession(HttpSession session) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");
        if (usuarioSesion instanceof User user
                && user.getRole() == UserRole.INQUILINO) {
            return user;
        }
        return null;
    }
}
