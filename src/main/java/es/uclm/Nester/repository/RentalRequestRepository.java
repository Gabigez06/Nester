package es.uclm.Nester.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.Nester.domain.RentalRequest;
import es.uclm.Nester.domain.RentalRequest.Status;

public interface RentalRequestRepository
        extends JpaRepository<RentalRequest, Long> {

    List<RentalRequest> findByPropertyIdAndStatus(
            Long propertyId,
            Status status);

    List<RentalRequest> findByPropertyIdAndStatusAndCheckInBeforeAndCheckOutAfter(
            Long propertyId,
            Status status,
            LocalDate requestedCheckOut,
            LocalDate requestedCheckIn);

    List<RentalRequest> findByProperty_Owner_IdAndStatusOrderByRequestedAtDesc(
            Long ownerId,
            Status status);
}
