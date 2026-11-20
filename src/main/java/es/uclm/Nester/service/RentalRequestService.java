package es.uclm.Nester.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.Nester.domain.Property;
import es.uclm.Nester.domain.RentalRequest;
import es.uclm.Nester.domain.RentalRequest.Status;
import es.uclm.Nester.domain.User;
import es.uclm.Nester.domain.User.UserRole;
import es.uclm.Nester.repository.PropertyRepository;
import es.uclm.Nester.repository.RentalRequestRepository;
import es.uclm.Nester.web.dto.OccupiedPeriodResponse;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final PropertyRepository propertyRepository;
    private final UserNotificationService userNotificationService;

    public RentalRequestService(
            RentalRequestRepository rentalRequestRepository,
            PropertyRepository propertyRepository, UserNotificationService userNotificationService) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.propertyRepository = propertyRepository;
        this.userNotificationService = userNotificationService;
    }

    @Transactional 
    public RentalRequest requestRental(
            Long propertyId,
            User tenant,
            LocalDate checkIn,
            LocalDate checkOut,
            int guests) {

        if (tenant == null || tenant.getRole() != UserRole.INQUILINO) {
            throw new IllegalArgumentException(
                    "Solo un inquilino puede solicitar un alquiler.");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se ha encontrado el inmueble."));

        if (Objects.equals(property.getOwner().getId(), tenant.getId())) {
            throw new IllegalArgumentException(
                    "No puedes solicitar tu propio inmueble.");
        }

        if (checkIn == null || checkOut == null
                || checkIn.isBefore(LocalDate.now())
                || !checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Selecciona fechas válidas de entrada y salida.");
        }

        if (guests < 1 || guests > property.getGuestCapacity()) {
            throw new IllegalArgumentException(
                    "El número de huéspedes supera la capacidad del inmueble.");
        }

        boolean datesOccupied = !rentalRequestRepository
                .findByPropertyIdAndStatusAndCheckInBeforeAndCheckOutAfter(
                        propertyId,
                        Status.APROBADA,
                        checkOut,
                        checkIn)
                .isEmpty();

        if (datesOccupied) {
            throw new IllegalArgumentException(
                    "El inmueble ya está ocupado en esas fechas.");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice = property.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));

        RentalRequest request = new RentalRequest(
                property,
                tenant,
                checkIn,
                checkOut,
                guests,
                totalPrice);

        RentalRequest savedRequest = rentalRequestRepository.save(request);
        userNotificationService.notifyOwnerOfRequest(savedRequest);
        return savedRequest;
    }

    public List<OccupiedPeriodResponse> getOccupiedPeriods(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            throw new IllegalArgumentException("No se ha encontrado el inmueble.");
        }

        return rentalRequestRepository
                .findByPropertyIdAndStatus(propertyId, Status.APROBADA)
                .stream()
                .map(request -> new OccupiedPeriodResponse(
                        request.getCheckIn(),
                        request.getCheckOut()))
                .toList();
    }

    public List<RentalRequest> getPendingRequestsForOwner(User owner) {
        if (owner == null || owner.getRole() != UserRole.PROPIETARIO) {
            throw new IllegalArgumentException(
                    "Solo un propietario puede consultar estas solicitudes.");
        }

        return rentalRequestRepository
                .findByProperty_Owner_IdAndStatusOrderByRequestedAtDesc(
                        owner.getId(),
                        Status.PENDIENTE);
    }

    @Transactional
    public RentalRequest approveRental(Long requestId, User owner) {
        RentalRequest request = getPendingRequestForOwner(requestId, owner);

        boolean datesOccupied = !rentalRequestRepository
                .findByPropertyIdAndStatusAndCheckInBeforeAndCheckOutAfter(
                        request.getProperty().getId(),
                        Status.APROBADA,
                        request.getCheckOut(),
                        request.getCheckIn())
                .isEmpty();

        if (datesOccupied) {
            throw new IllegalArgumentException(
                    "Las fechas ya se han aprobado para otra solicitud.");
        }

        request.setStatus(Status.APROBADA);
        RentalRequest savedRequest = rentalRequestRepository.save(request);
        userNotificationService.notifyTenantOfDecision(savedRequest);

        return savedRequest;
    }

    @Transactional
    public RentalRequest rejectRental(Long requestId, User owner) {
        RentalRequest request = getPendingRequestForOwner(requestId, owner);
        request.setStatus(Status.RECHAZADA);

        RentalRequest savedRequest = rentalRequestRepository.save(request);
        userNotificationService.notifyTenantOfDecision(savedRequest);

        return savedRequest;
    }

    private RentalRequest getPendingRequestForOwner(
            Long requestId,
            User owner) {
        if (owner == null || owner.getRole() != UserRole.PROPIETARIO) {
            throw new IllegalArgumentException(
                    "Solo un propietario puede gestionar solicitudes.");
        }

        RentalRequest request = rentalRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se ha encontrado la solicitud."));

        if (!Objects.equals(
                request.getProperty().getOwner().getId(),
                owner.getId())) {
            throw new IllegalArgumentException(
                    "Esta solicitud no pertenece a tus inmuebles.");
        }

        if (request.getStatus() != Status.PENDIENTE) {
            throw new IllegalArgumentException(
                    "La solicitud ya ha sido gestionada.");
        }

        return request;
    }
}
