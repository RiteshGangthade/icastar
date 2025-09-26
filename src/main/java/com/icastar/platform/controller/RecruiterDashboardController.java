package com.icastar.platform.controller;

import com.icastar.platform.dto.recruiter.*;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.RecruiterDashboardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiter/dashboard")
@RequiredArgsConstructor
@Slf4j
public class RecruiterDashboardController {
    
    private final RecruiterDashboardService recruiterDashboardService;
    
    /**
     * Get recruiter dashboard overview
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            RecruiterDashboardDto dashboard = recruiterDashboardService.getDashboard(recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", dashboard);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting recruiter dashboard: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get dashboard: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Post a new job (requires subscription)
     */
    @PostMapping("/jobs")
    public ResponseEntity<Map<String, Object>> postJob(
            @Valid @RequestBody JobPostingDto jobPosting,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> result = recruiterDashboardService.postJob(jobPosting, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Job posted successfully");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error posting job: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to post job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all jobs posted by recruiter
     */
    @GetMapping("/jobs")
    public ResponseEntity<Map<String, Object>> getMyJobs(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            Pageable pageable,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Page<RecentJobDto> jobs = recruiterDashboardService.getMyJobs(
                    status, isActive, jobType, location, title, pageable, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs.getContent());
            response.put("totalElements", jobs.getTotalElements());
            response.put("totalPages", jobs.getTotalPages());
            response.put("currentPage", jobs.getNumber());
            response.put("size", jobs.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting recruiter jobs: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job details by ID
     */
    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobDetails(
            @PathVariable Long jobId,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> jobDetails = recruiterDashboardService.getJobDetails(jobId, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobDetails);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting job details: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get job details: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * View artists who applied to a job
     */
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<Map<String, Object>> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String artistCategory,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel,
            Pageable pageable,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Page<RecentApplicationDto> applications = recruiterDashboardService.getJobApplications(
                    jobId, status, artistCategory, location, experienceLevel, pageable, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", applications.getContent());
            response.put("totalElements", applications.getTotalElements());
            response.put("totalPages", applications.getTotalPages());
            response.put("currentPage", applications.getNumber());
            response.put("size", applications.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting job applications: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get applications: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Browse artist profiles
     */
    @GetMapping("/artists")
    public ResponseEntity<Map<String, Object>> browseArtists(
            @RequestParam(required = false) String artistCategory,
            @RequestParam(required = false) String artistType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(required = false) Boolean isPremium,
            Pageable pageable,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Page<ArtistSuggestionDto> artists = recruiterDashboardService.browseArtists(
                    artistCategory, artistType, location, skills, genres, experienceLevel, 
                    availability, isVerified, isPremium, pageable, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", artists.getContent());
            response.put("totalElements", artists.getTotalElements());
            response.put("totalPages", artists.getTotalPages());
            response.put("currentPage", artists.getNumber());
            response.put("size", artists.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error browsing artists: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to browse artists: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get artist suggestions based on job criteria
     */
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, Object>> getArtistSuggestions(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String artistCategory,
            @RequestParam(required = false) String artistType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String genres,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) String availability,
            @RequestParam(required = false) Boolean isVerified,
            @RequestParam(required = false) Boolean isPremium,
            @RequestParam(defaultValue = "10") Integer limit,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            List<ArtistSuggestionDto> suggestions = recruiterDashboardService.getArtistSuggestions(
                    jobId, artistCategory, artistType, location, skills, genres, 
                    experienceLevel, availability, isVerified, isPremium, limit, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", suggestions);
            response.put("count", suggestions.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting artist suggestions: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get suggestions: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Track hires and past jobs
     */
    @GetMapping("/hires")
    public ResponseEntity<Map<String, Object>> getHires(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String artistCategory,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String performanceRating,
            @RequestParam(required = false) Boolean isCompleted,
            @RequestParam(required = false) Boolean isRecommended,
            Pageable pageable,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Page<RecentHireDto> hires = recruiterDashboardService.getHires(
                    status, artistCategory, jobType, performanceRating, isCompleted, 
                    isRecommended, pageable, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hires.getContent());
            response.put("totalElements", hires.getTotalElements());
            response.put("totalPages", hires.getTotalPages());
            response.put("currentPage", hires.getNumber());
            response.put("size", hires.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting hires: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get hires: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get hire details by ID
     */
    @GetMapping("/hires/{hireId}")
    public ResponseEntity<Map<String, Object>> getHireDetails(
            @PathVariable Long hireId,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> hireDetails = recruiterDashboardService.getHireDetails(hireId, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", hireDetails);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting hire details: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get hire details: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Rate and provide feedback for a hire
     */
    @PostMapping("/hires/{hireId}/rate")
    public ResponseEntity<Map<String, Object>> rateHire(
            @PathVariable Long hireId,
            @RequestParam String rating,
            @RequestParam(required = false) String feedback,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> result = recruiterDashboardService.rateHire(hireId, rating, feedback, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rating submitted successfully");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error rating hire: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to rate hire: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get recruiter statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics(Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> statistics = recruiterDashboardService.getStatistics(recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", statistics);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting recruiter statistics: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get statistics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get subscription status and features
     */
    @GetMapping("/subscription")
    public ResponseEntity<Map<String, Object>> getSubscriptionStatus(Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> subscription = recruiterDashboardService.getSubscriptionStatus(recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", subscription);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting subscription status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get subscription status: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update job status
     */
    @PutMapping("/jobs/{jobId}/status")
    public ResponseEntity<Map<String, Object>> updateJobStatus(
            @PathVariable Long jobId,
            @RequestParam String status,
            @RequestParam(required = false) String reason,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> result = recruiterDashboardService.updateJobStatus(jobId, status, reason, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Job status updated successfully");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating job status: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update job status: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Boost job visibility
     */
    @PostMapping("/jobs/{jobId}/boost")
    public ResponseEntity<Map<String, Object>> boostJob(
            @PathVariable Long jobId,
            @RequestParam(required = false) Integer days,
            Authentication authentication) {
        try {
            User recruiter = (User) authentication.getPrincipal();
            Map<String, Object> result = recruiterDashboardService.boostJob(jobId, days, recruiter);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Job boosted successfully");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error boosting job: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to boost job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
