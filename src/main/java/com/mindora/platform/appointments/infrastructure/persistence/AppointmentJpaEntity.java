package com.mindora.platform.appointments.infrastructure.persistence;

import com.mindora.platform.appointments.domain.model.AppointmentStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class AppointmentJpaEntity {

    @Id
    private UUID id;

    private Long psychologistId;

    @Column(length = 200)
    private String psychologistName;

    private Long employeeId;

    private LocalDateTime dateTime;

    private LocalDate date;

    @Column(length = 5)
    private String time;

    @Column(length = 20)
    private String type;

    @Column(length = 500)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Long getPsychologistId() { return psychologistId; }
    public void setPsychologistId(Long psychologistId) { this.psychologistId = psychologistId; }

    public String getPsychologistName() { return psychologistName; }
    public void setPsychologistName(String psychologistName) { this.psychologistName = psychologistName; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}
