package es.uclm.Nester.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.Nester.domain.UserNotification;

public interface UserNotificationRepository
        extends JpaRepository<UserNotification, Long> {

    List<UserNotification> findByRecipientIdOrderByCreatedAtDesc(
            Long recipientId);

    List<UserNotification> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(
            Long recipientId);

    long countByRecipientIdAndReadFalse(Long recipientId);
}