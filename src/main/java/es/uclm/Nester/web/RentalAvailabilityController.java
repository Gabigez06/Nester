package es.uclm.Nester.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import es.uclm.Nester.service.RentalRequestService;
import es.uclm.Nester.web.dto.OccupiedPeriodResponse;

@RestController
public class RentalAvailabilityController {

    private final RentalRequestService rentalRequestService;

    public RentalAvailabilityController(
            RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    @GetMapping("/api/propiedades/{propertyId}/fechas-ocupadas")
    public List<OccupiedPeriodResponse> getOccupiedPeriods(
            @PathVariable Long propertyId) {
        return rentalRequestService.getOccupiedPeriods(propertyId);
    }
}
