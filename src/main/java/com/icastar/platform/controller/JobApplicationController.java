package com.icastar.platform.controller;

import com.icastar.platform.dto.application.CreateJobApplicationDto;
import com.icastar.platform.dto.application.JobApplicationDto;
import com.icastar.platform.entity.JobApplication;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.JobApplicationService;
import com.icastar.platform.service.UserService;
import jakarta.validation.Valid;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
@Slf4j
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<JobApplicationDto> createApplication(@Valid @RequestBody CreateJobApplicationDto createDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User artist = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (artist.getRole() != User.UserRole.ARTIST) {
                throw new RuntimeException("Only artists can apply for jobs");
            }

            log.info("Creating job application for artist: {}", email);

            JobApplication application = jobApplicationService.createApplication(artist.getId(), createDto);
            JobApplicationDto applicationDto = new JobApplicationDto(application);

            return ResponseEntity.ok(applicationDto);
        } catch (Exception e) {
            log.error("Error creating job application", e);
            throw new RuntimeException("Failed to create job application: " + e.getMessage());
        }
    }

    @GetMapping("/my-applications")
    public ResponseEntity<Page<JobApplicationDto>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) JobApplication.ApplicationStatus status) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User artist = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (artist.getRole() != User.UserRole.ARTIST) {
                throw new RuntimeException("Only artists can view applications");
            }

            log.info("Fetching applications for artist: {}", email);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<JobApplication> applications;

            if (status != null) {
                applications = jobApplicationService.findByArtistAndStatus(artist.getId(), status, pageable);
            } else {
                applications = jobApplicationService.findByArtist(artist.getId(), pageable);
            }

            Page<JobApplicationDto> applicationDtos = applications.map(JobApplicationDto::new);

            return ResponseEntity.ok(applicationDtos);
        } catch (Exception e) {
            log.error("Error fetching applications", e);
            throw new RuntimeException("Failed to fetch applications: " + e.getMessage());
        }
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<JobApplicationDto> getApplication(@PathVariable Long applicationId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            log.info("Fetching application {} for user: {}", applicationId, email);

            JobApplication application = jobApplicationService.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

            // Check if user has permission to view this application
            boolean canView = false;
            if (user.getRole() == User.UserRole.ARTIST && 
                application.getArtist().getId().equals(user.getId())) {
                canView = true;
            } else if (user.getRole() == User.UserRole.RECRUITER && 
                       application.getJobPost().getRecruiter().getId().equals(user.getId())) {
                canView = true;
            }

            if (!canView) {
                throw new RuntimeException("You don't have permission to view this application");
            }

            JobApplicationDto applicationDto = new JobApplicationDto(application);

            return ResponseEntity.ok(applicationDto);
        } catch (Exception e) {
            log.error("Error fetching application", e);
            throw new RuntimeException("Failed to fetch application: " + e.getMessage());
        }
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Map<String, String>> deleteApplication(@PathVariable Long applicationId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User artist = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (artist.getRole() != User.UserRole.ARTIST) {
                throw new RuntimeException("Only artists can delete applications");
            }

            log.info("Deleting application {} for artist: {}", applicationId, email);

            jobApplicationService.deleteApplication(applicationId, artist.getId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Application deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting application", e);
            throw new RuntimeException("Failed to delete application: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMyApplicationStats() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User artist = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (artist.getRole() != User.UserRole.ARTIST) {
                throw new RuntimeException("Only artists can view application stats");
            }

            log.info("Fetching application stats for artist: {}", email);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalApplications", jobApplicationService.getApplicationsCountByArtist(artist.getId()));
            stats.put("pendingApplications", jobApplicationService.getApplicationsCountByStatus(JobApplication.ApplicationStatus.PENDING));
            stats.put("acceptedApplications", jobApplicationService.getApplicationsCountByStatus(JobApplication.ApplicationStatus.ACCEPTED));
            stats.put("rejectedApplications", jobApplicationService.getApplicationsCountByStatus(JobApplication.ApplicationStatus.REJECTED));

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching application stats", e);
            throw new RuntimeException("Failed to fetch application stats: " + e.getMessage());
        }
    }
}
