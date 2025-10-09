package com.icastar.platform.controller;

import com.icastar.platform.dto.job.CreateJobDto;
import com.icastar.platform.dto.job.JobDto;
import com.icastar.platform.entity.Job;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.JobService;
import com.icastar.platform.service.UserService;
import com.icastar.platform.service.BookmarkedJobService;
import com.icastar.platform.service.JobApplicationService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job Management", description = "APIs for job browsing, searching, and management")
public class JobController {

    private final JobService jobService;
    private final UserService userService;
    private final BookmarkedJobService bookmarkedJobService;
    private final JobApplicationService jobApplicationService;

    @Operation(summary = "Get all active jobs", description = "Retrieve a paginated list of all active jobs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(name = "Success Response",
                                    value = "{\"success\": true, \"data\": [{\"id\": 1, \"title\": \"Actor for TV Commercial\", \"description\": \"...\", \"location\": \"Mumbai\"}]}")))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "publishedAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Job> jobs = jobService.findActiveJobs(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs.getContent());
            response.put("totalElements", jobs.getTotalElements());
            response.put("totalPages", jobs.getTotalPages());
            response.put("currentPage", jobs.getNumber());
            response.put("size", jobs.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get job by ID", description = "Retrieve a specific job by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getJobById(@PathVariable Long id) {
        try {
            Optional<Job> job = jobService.findById(id);
            if (job.isPresent()) {
                // Increment view count
                jobService.incrementViews(id);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", job.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Job not found");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error retrieving job", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve job");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Search jobs", description = "Search jobs by title, description, or requirements")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchJobs(
            @Parameter(description = "Search term") @RequestParam String q,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Job> jobs = jobService.searchJobs(q, pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs.getContent());
            response.put("totalElements", jobs.getTotalElements());
            response.put("totalPages", jobs.getTotalPages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error searching jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to search jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get jobs by location", description = "Filter jobs by location")
    @GetMapping("/location/{location}")
    public ResponseEntity<Map<String, Object>> getJobsByLocation(@PathVariable String location) {
        try {
            List<Job> jobs = jobService.findJobsByLocation(location);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving jobs by location", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve jobs by location");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get jobs by type", description = "Filter jobs by job type")
    @GetMapping("/type/{jobType}")
    public ResponseEntity<Map<String, Object>> getJobsByType(@PathVariable Job.JobType jobType) {
        try {
            List<Job> jobs = jobService.findJobsByType(jobType);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving jobs by type", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve jobs by type");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get remote jobs", description = "Get all remote jobs")
    @GetMapping("/remote")
    public ResponseEntity<Map<String, Object>> getRemoteJobs() {
        try {
            List<Job> jobs = jobService.findRemoteJobs();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving remote jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve remote jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get featured jobs", description = "Get all featured jobs")
    @GetMapping("/featured")
    public ResponseEntity<Map<String, Object>> getFeaturedJobs() {
        try {
            List<Job> jobs = jobService.findFeaturedJobs();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving featured jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve featured jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get urgent jobs", description = "Get all urgent jobs")
    @GetMapping("/urgent")
    public ResponseEntity<Map<String, Object>> getUrgentJobs() {
        try {
            List<Job> jobs = jobService.findUrgentJobs();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving urgent jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve urgent jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get jobs by skill", description = "Filter jobs by required skill")
    @GetMapping("/skill/{skill}")
    public ResponseEntity<Map<String, Object>> getJobsBySkill(@PathVariable String skill) {
        try {
            List<Job> jobs = jobService.findJobsBySkill(skill);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving jobs by skill", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve jobs by skill");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get most popular jobs", description = "Get jobs sorted by application count")
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getMostPopularJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Job> jobs = jobService.findMostPopularJobs(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving popular jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve popular jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get recently posted jobs", description = "Get jobs sorted by publication date")
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentlyPostedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Job> jobs = jobService.findRecentlyPostedJobs(pageable);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving recent jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve recent jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(summary = "Get jobs expiring soon", description = "Get jobs with application deadline approaching")
    @GetMapping("/expiring")
    public ResponseEntity<Map<String, Object>> getJobsExpiringSoon(
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<Job> jobs = jobService.findJobsExpiringSoon(days);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving expiring jobs", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve expiring jobs");
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}