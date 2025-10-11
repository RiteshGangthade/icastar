package com.icastar.platform.controller;

import com.icastar.platform.dto.job.CreateJobApplicationDto;
import com.icastar.platform.dto.job.JobApplicationDto;
import com.icastar.platform.dto.job.UpdateJobApplicationDto;
import com.icastar.platform.entity.JobApplication;
import com.icastar.platform.entity.ArtistProfile;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.JobApplicationService;
import com.icastar.platform.service.ArtistService;
import com.icastar.platform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job Application Management", description = "APIs for job applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final ArtistService artistService;
    private final UserService userService;

    @Operation(summary = "Apply for a job", description = "Submit an application for a job")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<Map<String, Object>> applyForJob(
            @Parameter(description = "Job application details", required = true)
            @Valid @RequestBody CreateJobApplicationDto createDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            JobApplication application = jobApplicationService.createApplication(artistProfile.getId(), createDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application submitted successfully");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error applying for job", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to submit application");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get my applications", description = "Get all applications for the current user with search and filtering")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my-applications")
    public ResponseEntity<Map<String, Object>> getMyApplications(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Status filter") @RequestParam(required = false) JobApplication.ApplicationStatus status,
            @Parameter(description = "Search by job title") @RequestParam(required = false) String jobTitle,
            @Parameter(description = "Search by company name") @RequestParam(required = false) String companyName,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "appliedAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<JobApplication> applications;

            if (status != null) {
                applications = jobApplicationService.findByArtistAndStatus(artistProfile, status, pageable);
            } else {
                applications = jobApplicationService.findByArtist(artistProfile, pageable);
            }

            // Apply search filters if provided
            if (jobTitle != null || companyName != null) {
                List<JobApplication> filteredApplications = applications.getContent().stream()
                        .filter(application -> {
                            boolean matchesJobTitle = jobTitle == null || 
                                    application.getJob().getTitle().toLowerCase().contains(jobTitle.toLowerCase());
                            boolean matchesCompanyName = companyName == null || 
                                    (application.getJob().getRecruiter().getRecruiterProfile() != null && 
                                     application.getJob().getRecruiter().getRecruiterProfile().getCompanyName().toLowerCase().contains(companyName.toLowerCase()));
                            return matchesJobTitle && matchesCompanyName;
                        })
                        .collect(java.util.stream.Collectors.toList());
                
                // Create a new Page with filtered content
                applications = new org.springframework.data.domain.PageImpl<>(
                        filteredApplications, 
                        pageable, 
                        filteredApplications.size()
                );
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", applications.getContent());
            response.put("totalElements", applications.getTotalElements());
            response.put("totalPages", applications.getTotalPages());
            response.put("currentPage", applications.getNumber());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving applications", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve applications");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get application by ID", description = "Get a specific application by its ID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getApplicationById(@PathVariable Long id) {
        try {
            Optional<JobApplication> application = jobApplicationService.findById(id);
            if (application.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", application.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Application not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving application", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve application");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Update application status", description = "Update the status of an application (for recruiters)")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobApplicationDto updateDto) {
        try {
            JobApplication application = jobApplicationService.updateApplication(id, updateDto);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application status updated successfully");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating application status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update application status");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Schedule interview", description = "Schedule an interview for an application")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/schedule-interview")
    public ResponseEntity<Map<String, Object>> scheduleInterview(
            @PathVariable Long id,
            @Parameter(description = "Interview date and time") @RequestParam LocalDateTime interviewTime,
            @Parameter(description = "Interview notes") @RequestParam(required = false) String notes) {
        try {
            JobApplication application = jobApplicationService.scheduleInterview(id, interviewTime, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Interview scheduled successfully");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error scheduling interview", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to schedule interview");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Shortlist application", description = "Shortlist an application")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/shortlist")
    public ResponseEntity<Map<String, Object>> shortlistApplication(
            @PathVariable Long id,
            @Parameter(description = "Shortlist notes") @RequestParam(required = false) String notes) {
        try {
            JobApplication application = jobApplicationService.shortlistApplication(id, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application shortlisted successfully");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error shortlisting application", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to shortlist application");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Reject application", description = "Reject an application")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectApplication(
            @PathVariable Long id,
            @Parameter(description = "Rejection reason") @RequestParam String rejectionReason) {
        try {
            JobApplication application = jobApplicationService.rejectApplication(id, rejectionReason);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application rejected");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error rejecting application", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to reject application");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Hire applicant", description = "Hire an applicant")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/hire")
    public ResponseEntity<Map<String, Object>> hireApplicant(
            @PathVariable Long id,
            @Parameter(description = "Contract URL") @RequestParam(required = false) String contractUrl,
            @Parameter(description = "Hire notes") @RequestParam(required = false) String notes) {
        try {
            JobApplication application = jobApplicationService.hireApplication(id, contractUrl, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Applicant hired successfully");
            response.put("data", application);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error hiring applicant", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to hire applicant");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Withdraw application", description = "Withdraw an application")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Map<String, Object>> withdrawApplication(@PathVariable Long id) {
        try {
            jobApplicationService.withdrawApplication(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Application withdrawn successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error withdrawing application", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to withdraw application");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Check if applied for job", description = "Check if user has already applied for a specific job")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/check/{jobId}")
    public ResponseEntity<Map<String, Object>> checkApplicationStatus(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ArtistProfile artistProfile = artistService.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Artist profile not found"));

            boolean hasApplied = jobApplicationService.hasAppliedForJob(artistProfile.getId(), jobId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("hasApplied", hasApplied);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking application status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to check application status");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}