package es.uclm.Nester.web;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.service.UserNotificationService;

@ControllerAdvice
public class NotificationModelAdvice {

    private final UserNotificationService notificationService;

    public NotificationModelAdvice(
            UserNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ModelAttribute
    public void addNotificationData(Model model, HttpSession session) {
        Object usuarioSesion = session.getAttribute("usuarioLogueado");

        if (usuarioSesion instanceof User user) {
            model.addAttribute("hayUsuarioLogueado", true);
            model.addAttribute(
                    "notificacionesNoLeidas",
                    notificationService.countUnreadFor(user));
            model.addAttribute(
                    "esPropietario",
                    user.getRole() == UserRole.PROPIETARIO);
        } else {
            model.addAttribute("hayUsuarioLogueado", false);
            model.addAttribute("notificacionesNoLeidas", 0);
            model.addAttribute("esPropietario", false);
        }
    }
}