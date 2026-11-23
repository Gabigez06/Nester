package es.uclm.Nester.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.Nester.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRentalRequestId(Long rentalRequestId);
}
