package com.mindora.platform.appointments.application.create;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateAppointmentCommand(
        Long psychologistId,
        String psychologistName,
        Long employeeId,
        LocalDate date,
        String time,
        String type,
        String reason,
        String notes,
        String status
) {
    // Constructor helper para calcular dateTime
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, LocalTime.parse(time));
    }
}
