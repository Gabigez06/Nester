package es.uclm.Nester.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

    public enum Method {
        TARJETA_CREDITO,
        TARJETA_DEBITO,
        PAYPAL
    }

    public enum Status {
        PENDIENTE,
        COMPLETADO,
        FALLIDO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "rental_request_id", nullable = false, unique = true)
    private RentalRequest rentalRequest;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Method method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    protected Payment() {
    }

    public Payment(RentalRequest rentalRequest, Method method) {
        this.rentalRequest = rentalRequest;
        this.amount = rentalRequest.getTotalPrice();
        this.method = method;
        this.status = Status.PENDIENTE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void startRetry(Method method) {
        if (status == Status.COMPLETADO) {
            throw new IllegalStateException("El pago ya se ha completado.");
        }

        this.method = method;
        this.status = Status.PENDIENTE;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = Status.COMPLETADO;
        this.updatedAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = Status.FALLIDO;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public RentalRequest getRentalRequest() {
        return rentalRequest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Method getMethod() {
        return method;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
