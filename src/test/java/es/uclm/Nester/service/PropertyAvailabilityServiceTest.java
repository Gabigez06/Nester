package es.uclm.Nester.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class PropertyAvailabilityServiceTest {
    @Test
void shouldRejectAvailabilityWhenEndDateIsBeforeStartDate() {
    // Arrange
    LocalDate startDate = LocalDate.of(2026, 11, 10);
    LocalDate endDate = LocalDate.of(2026, 11, 9);

    // Act + Assert
    assertThrows(
            InvalidAvailabilityPeriodException.class,
            () -> propertyAvailabilityService.create(
                    1L,
                    startDate,
                    endDate
            )
    );
}
}
