package com.mindora.platform.reports.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateReportResource(
        @NotNull(message = "Employee ID is required")
        Long employeeId,

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Content is required")
        @Size(max = 4000, message = "Content must not exceed 4000 characters")
        String content,

        @NotBlank(message = "Summary is required")
        @Size(max = 1000, message = "Summary must not exceed 1000 characters")
        String summary,

        @NotBlank(message = "Type is required")
        String type,

        Integer completedTasks,
        Integer pendingTasks,
        Integer blockers,

        @NotNull(message = "Report date is required")
        LocalDate reportDate
) {
}
