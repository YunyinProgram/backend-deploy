package com.mindora.platform.reports.interfaces.rest;

import com.mindora.platform.reports.application.internal.commandservices.ReportCommandService;
import com.mindora.platform.reports.application.internal.queryservices.ReportQueryService;
import com.mindora.platform.reports.domain.model.commands.CreateReportCommand;
import com.mindora.platform.reports.domain.model.valueobjects.ReportType;
import com.mindora.platform.reports.interfaces.rest.resources.CreateReportResource;
import com.mindora.platform.reports.interfaces.rest.resources.ReportResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Reports-Controller", description = "Available Report Endpoints")
public class ReportsController {

    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    @GetMapping
    public List<ReportResource> getAllReports() {
        return reportQueryService.getAllReports();
    }

    @GetMapping("/{reportId}")
    public ReportResource getReportById(@PathVariable Long reportId) {
        return reportQueryService.getReportById(reportId);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ReportResource> getReportsByEmployeeId(@PathVariable Long employeeId) {
        return reportQueryService.getReportsByEmployeeId(employeeId);
    }

    @PostMapping
    public ResponseEntity<?> createReport(@Valid @RequestBody CreateReportResource resource) {
        try {
            // Validar que el tipo sea válido
            ReportType reportType;
            try {
                reportType = ReportType.valueOf(resource.type().toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of("message", "Invalid report type: " + resource.type() +
                              ". Valid types are: DAILY, WEEKLY, MONTHLY, PROJECT, INCIDENT"));
            }

            CreateReportCommand command = new CreateReportCommand(
                    resource.employeeId(),
                    resource.title(),
                    resource.content(),
                    resource.summary(),
                    reportType,
                    resource.completedTasks(),
                    resource.pendingTasks(),
                    resource.blockers(),
                    resource.reportDate()
            );

            ReportResource created = reportCommandService.createReport(command);

            return ResponseEntity
                    .created(URI.create("/api/v1/reports/" + created.id()))
                    .body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(java.util.Map.of("message", "Error creating report: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reportCommandService.deleteReport(reportId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
