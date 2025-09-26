package com.icastar.platform.controller;

import com.icastar.platform.dto.admin.JobFilterDto;
import com.icastar.platform.dto.admin.JobManagementDto;
import com.icastar.platform.dto.admin.JobVisibilityToggleDto;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.AdminJobManagementService;
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
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@Slf4j
public class AdminJobManagementController {
    
    private final AdminJobManagementService adminJobManagementService;
    
    /**
     * Get all job posts with filtering and pagination
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(required = false) Long recruiterId,
            @RequestParam(required = false) String recruiterName,
            @RequestParam(required = false) String recruiterEmail,
            @RequestParam(required = false) String recruiterCompany,
            @RequestParam(required = false) String recruiterCategory,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isVisible,
            @RequestParam(required = false) Long subscriptionId,
            @RequestParam(required = false) String subscriptionPlan,
            @RequestParam(required = false) String subscriptionStatus,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String createdFrom,
            @RequestParam(required = false) String createdTo,
            @RequestParam(required = false) String applicationDeadlineFrom,
            @RequestParam(required = false) String applicationDeadlineTo,
            @RequestParam(required = false) Integer minApplications,
            @RequestParam(required = false) Integer maxApplications,
            @RequestParam(required = false) Integer minViews,
            @RequestParam(required = false) Integer maxViews,
            @RequestParam(required = false) Boolean hasBoost,
            @RequestParam(required = false) Boolean isExpired,
            @RequestParam(required = false) Boolean isUrgent,
            Pageable pageable,
            Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            
            // Build filter DTO
            JobFilterDto filter = JobFilterDto.builder()
                    .recruiterId(recruiterId)
                    .recruiterName(recruiterName)
                    .recruiterEmail(recruiterEmail)
                    .recruiterCompany(recruiterCompany)
                    .recruiterCategory(recruiterCategory)
                    .status(status)
                    .isActive(isActive)
                    .isVisible(isVisible)
                    .subscriptionId(subscriptionId)
                    .subscriptionPlan(subscriptionPlan)
                    .subscriptionStatus(subscriptionStatus)
                    .jobType(jobType)
                    .location(location)
                    .title(title)
                    .minApplications(minApplications)
                    .maxApplications(maxApplications)
                    .minViews(minViews)
                    .maxViews(maxViews)
                    .hasBoost(hasBoost)
                    .isExpired(isExpired)
                    .isUrgent(isUrgent)
                    .build();
            
            Page<JobManagementDto> jobs = adminJobManagementService.getAllJobs(filter, pageable, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", jobs.getContent());
            response.put("totalElements", jobs.getTotalElements());
            response.put("totalPages", jobs.getTotalPages());
            response.put("currentPage", jobs.getNumber());
            response.put("size", jobs.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting jobs: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job details by ID
     */
    @GetMapping("/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobById(
            @PathVariable Long jobId,
            Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            JobManagementDto job = adminJobManagementService.getJobById(jobId, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", job);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting job: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Toggle job visibility
     */
    @PostMapping("/{jobId}/toggle-visibility")
    public ResponseEntity<Map<String, Object>> toggleJobVisibility(
            @PathVariable Long jobId,
            @Valid @RequestBody JobVisibilityToggleDto request,
            HttpServletRequest httpRequest,
            Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            request.setJobId(jobId);
            
            JobManagementDto result = adminJobManagementService.toggleJobVisibility(request, admin, 
                    getClientIpAddress(httpRequest), httpRequest.getHeader("User-Agent"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Job visibility updated successfully");
            response.put("data", result);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error toggling job visibility: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to toggle job visibility: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Bulk toggle job visibility
     */
    @PostMapping("/bulk-toggle-visibility")
    public ResponseEntity<Map<String, Object>> bulkToggleJobVisibility(
            @Valid @RequestBody List<JobVisibilityToggleDto> requests,
            HttpServletRequest httpRequest,
            Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            
            List<JobManagementDto> results = adminJobManagementService.bulkToggleJobVisibility(requests, admin,
                    getClientIpAddress(httpRequest), httpRequest.getHeader("User-Agent"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Bulk job visibility updated successfully");
            response.put("data", results);
            response.put("count", results.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error bulk toggling job visibility: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to bulk toggle job visibility: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getJobStatistics(Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            Map<String, Object> statistics = adminJobManagementService.getJobStatistics(admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", statistics);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting job statistics: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get job statistics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job management logs
     */
    @GetMapping("/{jobId}/logs")
    public ResponseEntity<Map<String, Object>> getJobLogs(
            @PathVariable Long jobId,
            Pageable pageable,
            Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            Page<Map<String, Object>> logs = adminJobManagementService.getJobLogs(jobId, pageable, admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", logs.getContent());
            response.put("totalElements", logs.getTotalElements());
            response.put("totalPages", logs.getTotalPages());
            response.put("currentPage", logs.getNumber());
            response.put("size", logs.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting job logs: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get job logs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get recruiters list for filtering
     */
    @GetMapping("/recruiters")
    public ResponseEntity<Map<String, Object>> getRecruiters(Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            List<Map<String, Object>> recruiters = adminJobManagementService.getRecruiters(admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", recruiters);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting recruiters: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get recruiters: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get subscription plans for filtering
     */
    @GetMapping("/subscription-plans")
    public ResponseEntity<Map<String, Object>> getSubscriptionPlans(Authentication authentication) {
        
        try {
            User admin = (User) authentication.getPrincipal();
            List<Map<String, Object>> plans = adminJobManagementService.getSubscriptionPlans(admin);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", plans);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting subscription plans: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get subscription plans: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }
}
