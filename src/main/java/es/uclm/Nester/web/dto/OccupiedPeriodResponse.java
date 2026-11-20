package es.uclm.Nester.web.dto;

import java.time.LocalDate;

public record OccupiedPeriodResponse(
        LocalDate checkIn,
        LocalDate checkOut) {
}