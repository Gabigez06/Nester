package es.uclm.Nester.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import es.uclm.Nester.domain.RentalRequest;
import es.uclm.Nester.domain.RentalRequest.Status;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.UserNotification;
import es.uclm.Nester.domain.UserNotification.Type;
import es.uclm.Nester.repository.UserNotificationRepository;

@Service
public class UserNotificationService {

    private final UserNotificationRepository notificationRepository;

    public UserNotificationService(
            UserNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyOwnerOfRequest(RentalRequest request) {
        User owner = request.getProperty().getOwner();

        String message = request.getTenant().getName()
                + " ha solicitado alquilar "
                + request.getProperty().getTitle()
                + ".";

        notificationRepository.save(new UserNotification(
                owner,
                request,
                Type.SOLICITUD_RECIBIDA,
                message));
    }

    public void notifyTenantOfDecision(RentalRequest request) {
        Type type;
        String message;

        if (request.getStatus() == Status.APROBADA) {
            type = Type.SOLICITUD_APROBADA;
            message = "El propietario ha aprobado tu solicitud para "
                    + request.getProperty().getTitle()
                    + ". Completa el pago para confirmar la reserva.";
        } else if (request.getStatus() == Status.RECHAZADA) {
            type = Type.SOLICITUD_RECHAZADA;
            message = "El propietario ha rechazado tu solicitud para "
                    + request.getProperty().getTitle()
                    + ".";
        } else {
            throw new IllegalArgumentException(
                    "La solicitud todavía no tiene una decisión.");
        }

        notificationRepository.save(new UserNotification(
                request.getTenant(),
                request,
                type,
                message));
    }

    public List<UserNotification> getNotificationsFor(User recipient) {
        return notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(recipient.getId());
    }

    public long countUnreadFor(User recipient) {
        return notificationRepository
                .countByRecipientIdAndReadFalse(recipient.getId());
    }

    public void markAsRead(Long notificationId, User recipient) {
        UserNotification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se ha encontrado la notificación."));

        if (!Objects.equals(
                notification.getRecipient().getId(),
                recipient.getId())) {
            throw new IllegalArgumentException(
                    "No puedes modificar la notificación de otro usuario.");
        }

        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
