package es.uclm.Nester.web;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.service.UserNotificationService;

@Controller
public class UserNotificationController {

    private final UserNotificationService notificationService;

    public UserNotificationController(
            UserNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notificaciones")
    public String showNotifications(
            HttpSession session,
            Model model) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User user)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "notificaciones",
                notificationService.getNotificationsFor(user));

        return "notificaciones";
    }

    @PostMapping("/notificaciones/{notificationId}/leer")
    public String markAsRead(
            @PathVariable Long notificationId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (!(usuarioSesion instanceof User user)) {
            return "redirect:/login";
        }

        try {
            notificationService.markAsRead(notificationId, user);
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    exception.getMessage());
        }

        return "redirect:/notificaciones";
    }
}