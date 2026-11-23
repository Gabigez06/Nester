package es.uclm.Nester.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.Nester.domain.Payment;
import es.uclm.Nester.domain.Payment.Method;
import es.uclm.Nester.domain.Payment.Status;
import es.uclm.Nester.domain.RentalRequest;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.PaymentRepository;
import es.uclm.Nester.repository.RentalRequestRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRequestRepository rentalRequestRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            RentalRequestRepository rentalRequestRepository) {
        this.paymentRepository = paymentRepository;
        this.rentalRequestRepository = rentalRequestRepository;
    }

    @Transactional(readOnly = true)
    public RentalRequest getApprovedRequestForTenant(
            Long requestId,
            User tenant) {
        return findApprovedRequestForTenant(requestId, tenant);
    }

    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentForRequest(Long requestId) {
        return paymentRepository.findByRentalRequestId(requestId);
    }

    @Transactional
    public Payment pay(Long requestId, User tenant, Method method) {
        if (method == null) {
            throw new IllegalArgumentException("Selecciona un método de pago.");
        }

        RentalRequest request = findApprovedRequestForTenant(requestId, tenant);
        Payment payment = paymentRepository
                .findByRentalRequestId(requestId)
                .orElseGet(() -> new Payment(request, method));

        if (payment.getStatus() == Status.COMPLETADO) {
            throw new IllegalArgumentException(
                    "Esta solicitud ya está pagada.");
        }

        if (payment.getId() != null) {
            payment.startRetry(method);
        }

        paymentRepository.save(payment);

        // Pago simulado: la integración real con una pasarela queda fuera del alcance.
        payment.complete();
        return paymentRepository.save(payment);
    }

    private RentalRequest findApprovedRequestForTenant(
            Long requestId,
            User tenant) {
        if (tenant == null || tenant.getRole() != UserRole.INQUILINO) {
            throw new IllegalArgumentException(
                    "Solo un inquilino puede realizar este pago.");
        }

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se ha encontrado la solicitud."));

        if (!Objects.equals(request.getTenant().getId(), tenant.getId())) {
            throw new IllegalArgumentException(
                    "Esta solicitud no pertenece a tu cuenta.");
        }

        if (request.getStatus() != RentalRequest.Status.APROBADA) {
            throw new IllegalArgumentException(
                    "El propietario todavía no ha aprobado esta solicitud.");
        }

        return request;
    }
}
