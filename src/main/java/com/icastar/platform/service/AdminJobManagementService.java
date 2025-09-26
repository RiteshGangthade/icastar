package com.icastar.platform.service;

import com.icastar.platform.dto.admin.JobFilterDto;
import com.icastar.platform.dto.admin.JobManagementDto;
import com.icastar.platform.dto.admin.JobVisibilityToggleDto;
import com.icastar.platform.entity.*;
import com.icastar.platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminJobManagementService {
    
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final AdminPermissionRepository adminPermissionRepository;
    private final JobApplicationRepository jobApplicationRepository;
    
    /**
     * Check if user has job management permissions
     */
    private void checkJobManagementPermission(User admin) {
        Optional<AdminPermission> permission = adminPermissionRepository.findByUserIdAndPermissionTypeAndPermissionLevelAndIsActiveTrue(
                admin.getId(), AdminPermission.PermissionType.USER_MANAGEMENT, AdminPermission.PermissionLevel.READ);
        if (permission.isEmpty()) {
            throw new RuntimeException("Insufficient permissions for job management");
        }
    }
    
    /**
     * Get all jobs with filtering and pagination
     */
    @Transactional(readOnly = true)
    public Page<JobManagementDto> getAllJobs(JobFilterDto filter, Pageable pageable, User admin) {
        checkJobManagementPermission(admin);
        
        // This would need to be implemented in JobPostRepository with custom query
        // For now, returning all jobs - implement filtering in repository
        Page<JobPost> jobs = jobPostRepository.findAll(pageable);
        
        return jobs.map(this::convertToJobManagementDto);
    }
    
    /**
     * Get job by ID
     */
    @Transactional(readOnly = true)
    public JobManagementDto getJobById(Long jobId, User admin) {
        checkJobManagementPermission(admin);
        
        JobPost job = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        return convertToJobManagementDto(job);
    }
    
    /**
     * Toggle job visibility
     */
    @Transactional
    public JobManagementDto toggleJobVisibility(JobVisibilityToggleDto request, User admin, 
                                               String ipAddress, String userAgent) {
        checkJobManagementPermission(admin);
        
        JobPost job = jobPostRepository.findById(request.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        boolean previousVisibility = job.getIsActive();
        job.setIsActive(request.getIsVisible());
        job.setUpdatedAt(LocalDateTime.now());
        
        JobPost savedJob = jobPostRepository.save(job);
        
        // Log the action
        logJobAction(job, admin, "TOGGLE_VISIBILITY", 
                    previousVisibility ? "VISIBLE" : "HIDDEN", 
                    request.getIsVisible() ? "VISIBLE" : "HIDDEN", 
                    request.getReason(), request.getAdminNotes(), ipAddress, userAgent);
        
        return convertToJobManagementDto(savedJob);
    }
    
    /**
     * Bulk toggle job visibility
     */
    @Transactional
    public List<JobManagementDto> bulkToggleJobVisibility(List<JobVisibilityToggleDto> requests, User admin,
                                                         String ipAddress, String userAgent) {
        checkJobManagementPermission(admin);
        
        List<JobManagementDto> results = requests.stream()
                .map(request -> {
                    try {
                        return toggleJobVisibility(request, admin, ipAddress, userAgent);
                    } catch (Exception e) {
                        log.error("Error toggling visibility for job {}: {}", request.getJobId(), e.getMessage());
                        return null;
                    }
                })
                .filter(job -> job != null)
                .collect(Collectors.toList());
        
        log.info("Bulk visibility toggle completed: {} jobs processed", results.size());
        return results;
    }
    
    /**
     * Get job statistics
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getJobStatistics(User admin) {
        checkJobManagementPermission(admin);
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Count jobs by status
        long totalJobs = jobPostRepository.count();
        long activeJobs = jobPostRepository.countByIsActiveTrue();
        long inactiveJobs = jobPostRepository.countByIsActiveFalse();
        long expiredJobs = jobPostRepository.countByApplicationDeadlineBefore(LocalDateTime.now());
        
        statistics.put("totalJobs", totalJobs);
        statistics.put("activeJobs", activeJobs);
        statistics.put("inactiveJobs", inactiveJobs);
        statistics.put("expiredJobs", expiredJobs);
        
        // Count by job type
        Map<String, Long> jobsByType = jobPostRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        job -> job.getJobType().name(),
                        Collectors.counting()
                ));
        statistics.put("jobsByType", jobsByType);
        
        // Count by recruiter category
        Map<String, Long> jobsByCategory = jobPostRepository.findAll().stream()
                .filter(job -> job.getRecruiter() != null && job.getRecruiter().getRecruiterCategory() != null)
                .collect(Collectors.groupingBy(
                        job -> job.getRecruiter().getRecruiterCategory().getDisplayName(),
                        Collectors.counting()
                ));
        statistics.put("jobsByCategory", jobsByCategory);
        
        // Recent jobs
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        long recentJobs = jobPostRepository.countByCreatedAtAfter(last24Hours);
        statistics.put("recentJobs", recentJobs);
        
        // Average applications per job
        double avgApplications = jobPostRepository.findAll().stream()
                .mapToInt(job -> job.getApplications() != null ? job.getApplications().size() : 0)
                .average()
                .orElse(0.0);
        statistics.put("avgApplicationsPerJob", avgApplications);
        
        return statistics;
    }
    
    /**
     * Get job management logs
     */
    @Transactional(readOnly = true)
    public Page<Map<String, Object>> getJobLogs(Long jobId, Pageable pageable, User admin) {
        checkJobManagementPermission(admin);
        
        // This would need to be implemented with a job management log table
        // For now, returning empty page
        return Page.empty(pageable);
    }
    
    /**
     * Get recruiters for filtering
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRecruiters(User admin) {
        checkJobManagementPermission(admin);
        
        return recruiterProfileRepository.findAll().stream()
                .map(recruiter -> {
                    Map<String, Object> recruiterMap = new HashMap<>();
                    recruiterMap.put("id", recruiter.getId());
                    recruiterMap.put("name", recruiter.getContactPersonName());
                    recruiterMap.put("email", recruiter.getUser().getEmail());
                    recruiterMap.put("company", recruiter.getCompanyName());
                    recruiterMap.put("category", recruiter.getRecruiterCategory() != null ? 
                            recruiter.getRecruiterCategory().getDisplayName() : "Unknown");
                    recruiterMap.put("isActive", recruiter.getIsActive());
                    return recruiterMap;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Get subscription plans for filtering
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSubscriptionPlans(User admin) {
        checkJobManagementPermission(admin);
        
        return subscriptionPlanRepository.findAll().stream()
                .map(plan -> {
                    Map<String, Object> planMap = new HashMap<>();
                    planMap.put("id", plan.getId());
                    planMap.put("name", plan.getName());
                    planMap.put("planType", plan.getPlanType().name());
                    planMap.put("userRole", plan.getUserRole().name());
                    planMap.put("price", plan.getPrice());
                    planMap.put("isActive", plan.getIsActive());
                    return planMap;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Convert JobPost to JobManagementDto
     */
    private JobManagementDto convertToJobManagementDto(JobPost job) {
        RecruiterProfile recruiter = job.getRecruiter();
        User recruiterUser = recruiter != null ? recruiter.getUser() : null;
        
        // Get subscription information
        String subscriptionPlan = "Unknown";
        String subscriptionStatus = "Unknown";
        if (recruiterUser != null) {
            Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(recruiterUser.getId(), Subscription.SubscriptionStatus.ACTIVE);
            if (subscription.isPresent()) {
                subscriptionPlan = subscription.get().getSubscriptionPlan().getName();
                subscriptionStatus = subscription.get().getStatus().name();
            }
        }
        
        // Get application count
        int applicationCount = job.getApplications() != null ? job.getApplications().size() : 0;
        
        return JobManagementDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
                .jobType(job.getJobType().name())
                .location(job.getLocation())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .currency(job.getCurrency())
                .status(job.getStatus().name())
                .isActive(job.getIsActive())
                .isVisible(job.getIsActive())
                .applicationDeadline(job.getApplicationDeadline())
                .startDate(job.getStartDate())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .recruiterId(recruiter != null ? recruiter.getId() : null)
                .recruiterName(recruiter != null ? recruiter.getContactPersonName() : "Unknown")
                .recruiterEmail(recruiterUser != null ? recruiterUser.getEmail() : "Unknown")
                .recruiterCompany(recruiter != null ? recruiter.getCompanyName() : "Unknown")
                .recruiterCategory(recruiter != null && recruiter.getRecruiterCategory() != null ? 
                        recruiter.getRecruiterCategory().getDisplayName() : "Unknown")
                .subscriptionPlan(subscriptionPlan)
                .subscriptionStatus(subscriptionStatus)
                .applicationCount(applicationCount)
                .viewCount(0) // This would need to be tracked separately
                .boostCount(0) // This would need to be tracked separately
                .build();
    }
    
    /**
     * Log job action
     */
    private void logJobAction(JobPost job, User admin, String action, String previousStatus, 
                             String newStatus, String reason, String adminNotes, 
                             String ipAddress, String userAgent) {
        
        // This would need to be implemented with a job management log table
        log.info("Job action logged: {} by admin {} for job {} - {} -> {}", 
                action, admin.getId(), job.getId(), previousStatus, newStatus);
    }
}
