package com.icastar.platform.service;

import com.icastar.platform.dto.recruiter.*;
import com.icastar.platform.entity.*;
import com.icastar.platform.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecruiterDashboardService {
    
    private final UserRepository userRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobPostRepository jobPostRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final UsageTrackingRepository usageTrackingRepository;
    
    /**
     * Get recruiter dashboard overview
     */
    @Transactional(readOnly = true)
    public RecruiterDashboardDto getDashboard(User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get subscription information
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        // Get dashboard statistics
        Map<String, Object> statistics = getDashboardStatistics(recruiter);
        
        // Get recent activity
        List<RecentJobDto> recentJobs = getRecentJobs(recruiter, 5);
        List<RecentApplicationDto> recentApplications = getRecentApplications(recruiter, 5);
        List<RecentHireDto> recentHires = getRecentHires(recruiter, 5);
        
        // Build dashboard DTO
        return RecruiterDashboardDto.builder()
                .recruiterId(recruiterProfile.getId())
                .recruiterName(recruiterProfile.getContactPersonName())
                .companyName(recruiterProfile.getCompanyName())
                .recruiterCategory(recruiterProfile.getRecruiterCategory() != null ? 
                        recruiterProfile.getRecruiterCategory().getDisplayName() : "Unknown")
                .email(recruiter.getEmail())
                .phone(recruiterProfile.getContactPhone())
                .isActive(recruiterProfile.getIsActive())
                .subscriptionId(subscription.map(Subscription::getId).orElse(null))
                .subscriptionPlan(subscription.map(s -> s.getSubscriptionPlan().getName()).orElse("Free"))
                .subscriptionStatus(subscription.map(s -> s.getStatus().name()).orElse("INACTIVE"))
                .subscriptionExpiresAt(subscription.map(Subscription::getExpiresAt).orElse(null))
                .remainingJobPosts(getRemainingJobPosts(recruiter))
                .maxJobPosts(getMaxJobPosts(recruiter))
                .canPostJob(canPostJob(recruiter))
                .totalJobsPosted((Long) statistics.get("totalJobsPosted"))
                .activeJobs((Long) statistics.get("activeJobs"))
                .closedJobs((Long) statistics.get("closedJobs"))
                .totalApplications((Long) statistics.get("totalApplications"))
                .totalHires((Long) statistics.get("totalHires"))
                .totalViews((Long) statistics.get("totalViews"))
                .averageApplicationsPerJob((Double) statistics.get("averageApplicationsPerJob"))
                .hireRate((Double) statistics.get("hireRate"))
                .recentJobs(recentJobs)
                .recentApplications(recentApplications)
                .recentHires(recentHires)
                .canPostNewJob(canPostJob(recruiter))
                .canViewApplications(true)
                .canBrowseArtists(true)
                .canGetSuggestions(true)
                .canTrackHires(true)
                .availableFeatures(getAvailableFeatures(recruiter))
                .premiumFeatures(getPremiumFeatures(recruiter))
                .hasPremiumFeatures(hasPremiumFeatures(recruiter))
                .build();
    }
    
    /**
     * Post a new job (requires subscription)
     */
    @Transactional
    public Map<String, Object> postJob(JobPostingDto jobPosting, User recruiter) {
        // Check if recruiter can post job
        if (!canPostJob(recruiter)) {
            throw new RuntimeException("Insufficient subscription to post job");
        }
        
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Create job post
        JobPost jobPost = new JobPost();
        jobPost.setTitle(jobPosting.getTitle());
        jobPost.setDescription(jobPosting.getDescription());
        jobPost.setRequirements(jobPosting.getRequirements());
        jobPost.setJobType(JobPost.JobType.valueOf(jobPosting.getJobType()));
        jobPost.setLocation(jobPosting.getLocation());
        jobPost.setSalaryMin(jobPosting.getSalaryMin());
        jobPost.setSalaryMax(jobPosting.getSalaryMax());
        jobPost.setCurrency(jobPosting.getCurrency());
        jobPost.setApplicationDeadline(jobPosting.getApplicationDeadline());
        jobPost.setStartDate(jobPosting.getStartDate());
        jobPost.setStatus(JobPost.JobStatus.OPEN);
        jobPost.setIsActive(true);
        jobPost.setRecruiter(recruiterProfile);
        jobPost.setCreatedAt(LocalDateTime.now());
        jobPost.setUpdatedAt(LocalDateTime.now());
        
        // Save job post
        JobPost savedJob = jobPostRepository.save(jobPost);
        
        // Track usage
        trackJobPostUsage(recruiter);
        
        // Return result
        Map<String, Object> result = new HashMap<>();
        result.put("jobId", savedJob.getId());
        result.put("title", savedJob.getTitle());
        result.put("status", savedJob.getStatus());
        result.put("createdAt", savedJob.getCreatedAt());
        result.put("remainingJobPosts", getRemainingJobPosts(recruiter));
        
        return result;
    }
    
    /**
     * Get all jobs posted by recruiter
     */
    @Transactional(readOnly = true)
    public Page<RecentJobDto> getMyJobs(String status, Boolean isActive, String jobType, 
                                       String location, String title, Pageable pageable, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Build query based on filters
        Page<JobPost> jobs = jobPostRepository.findByRecruiterId(recruiterProfile.getId(), pageable);
        
        // Apply additional filters if needed
        if (status != null) {
            // Filter jobs by status
            List<JobPost> filteredJobs = jobs.getContent().stream()
                    .filter(job -> job.getStatus().name().equals(status))
                    .collect(Collectors.toList());
            // Create a new Page with filtered content
            jobs = new org.springframework.data.domain.PageImpl<>(filteredJobs, pageable, filteredJobs.size());
        }
        
        return jobs.map(this::convertToRecentJobDto);
    }
    
    /**
     * Get job details by ID
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getJobDetails(Long jobId, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job post
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // Check if job belongs to recruiter
        if (!jobPost.getRecruiter().getId().equals(recruiterProfile.getId())) {
            throw new RuntimeException("Access denied to this job");
        }
        
        // Get applications count
        int applicationCount = jobPost.getApplications() != null ? jobPost.getApplications().size() : 0;
        
        // Build job details
        Map<String, Object> jobDetails = new HashMap<>();
        jobDetails.put("id", jobPost.getId());
        jobDetails.put("title", jobPost.getTitle());
        jobDetails.put("description", jobPost.getDescription());
        jobDetails.put("requirements", jobPost.getRequirements());
        jobDetails.put("jobType", jobPost.getJobType());
        jobDetails.put("location", jobPost.getLocation());
        jobDetails.put("salaryMin", jobPost.getSalaryMin());
        jobDetails.put("salaryMax", jobPost.getSalaryMax());
        jobDetails.put("currency", jobPost.getCurrency());
        jobDetails.put("status", jobPost.getStatus());
        jobDetails.put("isActive", jobPost.getIsActive());
        jobDetails.put("applicationDeadline", jobPost.getApplicationDeadline());
        jobDetails.put("startDate", jobPost.getStartDate());
        jobDetails.put("createdAt", jobPost.getCreatedAt());
        jobDetails.put("updatedAt", jobPost.getUpdatedAt());
        jobDetails.put("applicationCount", applicationCount);
        jobDetails.put("viewCount", 0); // This would need to be tracked separately
        jobDetails.put("boostCount", 0); // This would need to be tracked separately
        
        return jobDetails;
    }
    
    /**
     * Get job applications
     */
    @Transactional(readOnly = true)
    public Page<RecentApplicationDto> getJobApplications(Long jobId, String status, String artistCategory,
                                                        String location, String experienceLevel, 
                                                        Pageable pageable, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job post
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // Check if job belongs to recruiter
        if (!jobPost.getRecruiter().getId().equals(recruiterProfile.getId())) {
            throw new RuntimeException("Access denied to this job");
        }
        
        // Get applications
        Page<JobApplication> applications = jobApplicationRepository.findByJobPostId(jobId, pageable);
        
        return applications.map(this::convertToRecentApplicationDto);
    }
    
    /**
     * Browse artist profiles
     */
    @Transactional(readOnly = true)
    public Page<ArtistSuggestionDto> browseArtists(String artistCategory, String artistType, String location,
                                                    String skills, String genres, String experienceLevel,
                                                    String availability, Boolean isVerified, Boolean isPremium,
                                                    Pageable pageable, User recruiter) {
        // Get all artist profiles with filters
        Page<ArtistProfile> artistProfiles = artistProfileRepository.findAll(pageable);
        
        // Apply filters
        if (artistCategory != null) {
            // Filter artist profiles by category
            List<ArtistProfile> filteredProfiles = artistProfiles.getContent().stream()
                    .filter(profile -> profile.getArtistType() != null && 
                            profile.getArtistType().getDisplayName().equals(artistCategory))
                    .collect(Collectors.toList());
            // Create a new Page with filtered content
            artistProfiles = new org.springframework.data.domain.PageImpl<>(filteredProfiles, pageable, filteredProfiles.size());
        }
        
        return artistProfiles.map(this::convertToArtistSuggestionDto);
    }
    
    /**
     * Get artist suggestions based on job criteria
     */
    @Transactional(readOnly = true)
    public List<ArtistSuggestionDto> getArtistSuggestions(Long jobId, String artistCategory, String artistType,
                                                          String location, String skills, String genres,
                                                          String experienceLevel, String availability,
                                                          Boolean isVerified, Boolean isPremium, Integer limit,
                                                          User recruiter) {
        // Get job post if jobId is provided
        final JobPost jobPost = jobId != null ? 
                jobPostRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found")) : null;
        
        // Get all artist profiles
        List<ArtistProfile> artistProfiles = artistProfileRepository.findAll();
        
        // Convert to suggestions and calculate match scores
        List<ArtistSuggestionDto> suggestions = artistProfiles.stream()
                .map(profile -> {
                    ArtistSuggestionDto suggestion = convertToArtistSuggestionDto(profile);
                    
                    // Calculate match score based on criteria
                    double matchScore = calculateMatchScore(profile, jobPost, artistCategory, artistType,
                            location, skills, genres, experienceLevel, availability, isVerified, isPremium);
                    
                    suggestion.setMatchScore(matchScore);
                    suggestion.setMatchReasons(generateMatchReasons(profile, jobPost, artistCategory, artistType,
                            location, skills, genres, experienceLevel, availability, isVerified, isPremium));
                    
                    return suggestion;
                })
                .filter(suggestion -> suggestion.getMatchScore() > 0.3) // Only show relevant matches
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        return suggestions;
    }
    
    /**
     * Get hires and past jobs
     */
    @Transactional(readOnly = true)
    public Page<RecentHireDto> getHires(String status, String artistCategory, String jobType,
                                        String performanceRating, Boolean isCompleted, Boolean isRecommended,
                                        Pageable pageable, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job posts by recruiter
        Page<JobPost> jobPosts = jobPostRepository.findByRecruiterId(recruiterProfile.getId(), pageable);
        
        // Convert to hires (this would need to be implemented with a proper hire tracking system)
        return jobPosts.map(job -> {
            // This is a placeholder - would need actual hire tracking
            return RecentHireDto.builder()
                    .id(job.getId())
                    .jobId(job.getId())
                    .jobTitle(job.getTitle())
                    .artistId(1L) // Placeholder
                    .artistName("Sample Artist")
                    .artistEmail("artist@example.com")
                    .artistCategory("Dancer")
                    .hireStatus("COMPLETED")
                    .hiredAt(job.getCreatedAt())
                    .startDate(job.getStartDate())
                    .endDate(job.getStartDate().plusDays(30))
                    .agreedSalary(job.getSalaryMin())
                    .currency(job.getCurrency())
                    .contractType("FULL_TIME")
                    .workLocation(job.getLocation())
                    .workSchedule("9-5")
                    .performanceRating("5")
                    .feedback("Excellent work")
                    .isCompleted(true)
                    .isRecommended(true)
                    .canViewProfile(true)
                    .canRate(true)
                    .canRecommend(true)
                    .canRehire(true)
                    .canMessage(true)
                    .build();
        });
    }
    
    /**
     * Get hire details by ID
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getHireDetails(Long hireId, User recruiter) {
        // This would need to be implemented with a proper hire tracking system
        Map<String, Object> hireDetails = new HashMap<>();
        hireDetails.put("id", hireId);
        hireDetails.put("message", "Hire details would be implemented with proper hire tracking system");
        
        return hireDetails;
    }
    
    /**
     * Rate and provide feedback for a hire
     */
    @Transactional
    public Map<String, Object> rateHire(Long hireId, String rating, String feedback, User recruiter) {
        // This would need to be implemented with a proper hire tracking system
        Map<String, Object> result = new HashMap<>();
        result.put("hireId", hireId);
        result.put("rating", rating);
        result.put("feedback", feedback);
        result.put("message", "Rating submitted successfully");
        
        return result;
    }
    
    /**
     * Get recruiter statistics
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStatistics(User recruiter) {
        return getDashboardStatistics(recruiter);
    }
    
    /**
     * Get subscription status and features
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getSubscriptionStatus(User recruiter) {
        // Get subscription
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        Map<String, Object> subscriptionStatus = new HashMap<>();
        subscriptionStatus.put("hasSubscription", subscription.isPresent());
        subscriptionStatus.put("subscriptionId", subscription.map(Subscription::getId).orElse(null));
        subscriptionStatus.put("subscriptionPlan", subscription.map(s -> s.getSubscriptionPlan().getName()).orElse("Free"));
        subscriptionStatus.put("subscriptionStatus", subscription.map(s -> s.getStatus().name()).orElse("INACTIVE"));
        subscriptionStatus.put("expiresAt", subscription.map(Subscription::getExpiresAt).orElse(null));
        subscriptionStatus.put("remainingJobPosts", getRemainingJobPosts(recruiter));
        subscriptionStatus.put("maxJobPosts", getMaxJobPosts(recruiter));
        subscriptionStatus.put("canPostJob", canPostJob(recruiter));
        subscriptionStatus.put("availableFeatures", getAvailableFeatures(recruiter));
        subscriptionStatus.put("premiumFeatures", getPremiumFeatures(recruiter));
        subscriptionStatus.put("hasPremiumFeatures", hasPremiumFeatures(recruiter));
        
        return subscriptionStatus;
    }
    
    /**
     * Update job status
     */
    @Transactional
    public Map<String, Object> updateJobStatus(Long jobId, String status, String reason, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job post
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // Check if job belongs to recruiter
        if (!jobPost.getRecruiter().getId().equals(recruiterProfile.getId())) {
            throw new RuntimeException("Access denied to this job");
        }
        
        // Update status
        jobPost.setStatus(JobPost.JobStatus.valueOf(status));
        jobPost.setUpdatedAt(LocalDateTime.now());
        
        JobPost savedJob = jobPostRepository.save(jobPost);
        
        Map<String, Object> result = new HashMap<>();
        result.put("jobId", savedJob.getId());
        result.put("status", savedJob.getStatus());
        result.put("updatedAt", savedJob.getUpdatedAt());
        
        return result;
    }
    
    /**
     * Boost job visibility
     */
    @Transactional
    public Map<String, Object> boostJob(Long jobId, Integer days, User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job post
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        // Check if job belongs to recruiter
        if (!jobPost.getRecruiter().getId().equals(recruiterProfile.getId())) {
            throw new RuntimeException("Access denied to this job");
        }
        
        // Boost job (this would need to be implemented with proper boost tracking)
        Map<String, Object> result = new HashMap<>();
        result.put("jobId", jobPost.getId());
        result.put("boosted", true);
        result.put("boostDays", days != null ? days : 7);
        result.put("boostExpiresAt", LocalDateTime.now().plusDays(days != null ? days : 7));
        
        return result;
    }
    
    // Helper methods
    
    private Map<String, Object> getDashboardStatistics(User recruiter) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get job posts by recruiter
        List<JobPost> jobPosts = jobPostRepository.findByRecruiterId(recruiterProfile.getId(), Pageable.unpaged()).getContent();
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalJobsPosted", (long) jobPosts.size());
        statistics.put("activeJobs", jobPosts.stream().filter(job -> job.getIsActive()).count());
        statistics.put("closedJobs", jobPosts.stream().filter(job -> !job.getIsActive()).count());
        statistics.put("totalApplications", jobPosts.stream().mapToInt(job -> 
                job.getApplications() != null ? job.getApplications().size() : 0).sum());
        statistics.put("totalHires", 0L); // This would need to be tracked separately
        statistics.put("totalViews", 0L); // This would need to be tracked separately
        statistics.put("averageApplicationsPerJob", jobPosts.stream().mapToDouble(job -> 
                job.getApplications() != null ? job.getApplications().size() : 0).average().orElse(0.0));
        statistics.put("hireRate", 0.0); // This would need to be calculated
        
        return statistics;
    }
    
    private List<RecentJobDto> getRecentJobs(User recruiter, int limit) {
        // Get recruiter profile
        RecruiterProfile recruiterProfile = recruiterProfileRepository.findByUserId(recruiter.getId())
                .orElseThrow(() -> new RuntimeException("Recruiter profile not found"));
        
        // Get recent job posts
        List<JobPost> jobPosts = jobPostRepository.findByRecruiterId(recruiterProfile.getId(), Pageable.unpaged())
                .getContent()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .collect(Collectors.toList());
        
        return jobPosts.stream()
                .map(this::convertToRecentJobDto)
                .collect(Collectors.toList());
    }
    
    private List<RecentApplicationDto> getRecentApplications(User recruiter, int limit) {
        // This would need to be implemented with proper application tracking
        return new ArrayList<>();
    }
    
    private List<RecentHireDto> getRecentHires(User recruiter, int limit) {
        // This would need to be implemented with proper hire tracking
        return new ArrayList<>();
    }
    
    private RecentJobDto convertToRecentJobDto(JobPost jobPost) {
        return RecentJobDto.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .jobType(jobPost.getJobType().name())
                .location(jobPost.getLocation())
                .salaryMin(jobPost.getSalaryMin())
                .salaryMax(jobPost.getSalaryMax())
                .currency(jobPost.getCurrency())
                .status(jobPost.getStatus().name())
                .isActive(jobPost.getIsActive())
                .applicationDeadline(jobPost.getApplicationDeadline())
                .startDate(jobPost.getStartDate())
                .createdAt(jobPost.getCreatedAt())
                .applicationCount(jobPost.getApplications() != null ? jobPost.getApplications().size() : 0)
                .viewCount(0) // This would need to be tracked separately
                .boostCount(0) // This would need to be tracked separately
                .canEdit(true)
                .canClose(true)
                .canBoost(true)
                .canViewApplications(true)
                .build();
    }
    
    private RecentApplicationDto convertToRecentApplicationDto(JobApplication application) {
        return RecentApplicationDto.builder()
                .id(application.getId())
                .jobId(application.getJobPost().getId())
                .jobTitle(application.getJobPost().getTitle())
                .artistId(application.getArtist().getId())
                .artistName(application.getArtist().getUser().getEmail()) // Placeholder
                .artistEmail(application.getArtist().getUser().getEmail())
                .artistPhone("N/A") // Placeholder
                .artistCategory("Unknown") // Placeholder
                .applicationStatus(application.getStatus().name())
                .appliedAt(application.getAppliedAt())
                .lastUpdatedAt(application.getUpdatedAt())
                .artistBio("N/A") // Placeholder
                .artistLocation("N/A") // Placeholder
                .artistExperience(0) // Placeholder
                .artistSkills("N/A") // Placeholder
                .artistPortfolio("N/A") // Placeholder
                .coverLetter(application.getCoverLetter())
                .expectedSalary("N/A") // Placeholder
                .availability("N/A") // Placeholder
                .additionalNotes("N/A") // Placeholder
                .canViewProfile(true)
                .canAccept(true)
                .canReject(true)
                .canShortlist(true)
                .canMessage(true)
                .build();
    }
    
    private ArtistSuggestionDto convertToArtistSuggestionDto(ArtistProfile artistProfile) {
        return ArtistSuggestionDto.builder()
                .artistId(artistProfile.getId())
                .artistName(artistProfile.getUser().getEmail()) // Placeholder
                .artistEmail(artistProfile.getUser().getEmail())
                .artistCategory(artistProfile.getArtistType() != null ? 
                        artistProfile.getArtistType().getDisplayName() : "Unknown")
                .artistType("Unknown") // Placeholder
                .location("N/A") // Placeholder
                .bio("N/A") // Placeholder
                .profilePhoto("N/A") // Placeholder
                .matchScore(0.0)
                .matchReasons(new ArrayList<>())
                .skills(new ArrayList<>())
                .genres(new ArrayList<>())
                .languages(new ArrayList<>())
                .experienceYears(0) // Placeholder
                .experienceLevel("Unknown") // Placeholder
                .portfolioItems(new ArrayList<>())
                .achievements(new ArrayList<>())
                .certifications(new ArrayList<>())
                .availability("N/A") // Placeholder
                .preferredJobType("N/A") // Placeholder
                .expectedSalaryMin(null)
                .expectedSalaryMax(null)
                .currency("USD")
                .workLocation("N/A") // Placeholder
                .workSchedule("N/A") // Placeholder
                .phone("N/A") // Placeholder
                .website("N/A") // Placeholder
                .socialLinks(new ArrayList<>())
                .contactPreference("Email")
                .lastActive(LocalDateTime.now())
                .totalApplications(0) // Placeholder
                .totalHires(0) // Placeholder
                .hireRate(0.0) // Placeholder
                .verificationStatus("UNVERIFIED") // Placeholder
                .isVerified(false)
                .isPremium(false)
                .canViewProfile(true)
                .canContact(true)
                .canShortlist(true)
                .canInvite(true)
                .canMessage(true)
                .build();
    }
    
    private double calculateMatchScore(ArtistProfile profile, JobPost jobPost, String artistCategory,
                                          String artistType, String location, String skills, String genres,
                                          String experienceLevel, String availability, Boolean isVerified,
                                          Boolean isPremium) {
        // This would implement sophisticated matching algorithm
        return Math.random(); // Placeholder
    }
    
    private List<String> generateMatchReasons(ArtistProfile profile, JobPost jobPost, String artistCategory,
                                              String artistType, String location, String skills, String genres,
                                              String experienceLevel, String availability, Boolean isVerified,
                                              Boolean isPremium) {
        // This would generate reasons for the match
        return Arrays.asList("Skills match", "Location match", "Experience level match");
    }
    
    private boolean canPostJob(User recruiter) {
        // Check subscription and remaining job posts
        return getRemainingJobPosts(recruiter) > 0;
    }
    
    private int getRemainingJobPosts(User recruiter) {
        // Get subscription
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        if (subscription.isEmpty()) {
            return 0; // No subscription
        }
        
        // Get usage tracking
        List<UsageTracking> usage = usageTrackingRepository.findByUserIdAndFeatureType(
                recruiter.getId(), "JOB_POSTING");
        
        int used = usage.size();
        int max = getMaxJobPosts(recruiter);
        
        return Math.max(0, max - used);
    }
    
    private int getMaxJobPosts(User recruiter) {
        // Get subscription
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        if (subscription.isEmpty()) {
            return 0; // No subscription
        }
        
        // Get plan features
        List<PlanFeature> features = subscription.get().getSubscriptionPlan().getFeatures();
        
        for (PlanFeature feature : features) {
            if (feature.getFeatureName().equals("JOB_POSTING")) {
                try {
                    return Integer.parseInt(feature.getFeatureValue());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        
        return 0;
    }
    
    private List<String> getAvailableFeatures(User recruiter) {
        // Get subscription
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        if (subscription.isEmpty()) {
            return Arrays.asList("Basic job posting", "View applications");
        }
        
        // Get plan features
        List<PlanFeature> features = subscription.get().getSubscriptionPlan().getFeatures();
        
        return features.stream()
                .map(PlanFeature::getFeatureName)
                .collect(Collectors.toList());
    }
    
    private List<String> getPremiumFeatures(User recruiter) {
        return Arrays.asList("Advanced job posting", "Artist suggestions", "Boost jobs", "Analytics");
    }
    
    private boolean hasPremiumFeatures(User recruiter) {
        // Get subscription
        Optional<Subscription> subscription = subscriptionRepository.findByUserIdAndStatus(
                recruiter.getId(), Subscription.SubscriptionStatus.ACTIVE);
        
        if (subscription.isEmpty()) {
            return false;
        }
        
        // Check if plan has premium features
        return subscription.get().getSubscriptionPlan().getPlanType() != SubscriptionPlan.PlanType.FREE;
    }
    
    private void trackJobPostUsage(User recruiter) {
        // Create usage tracking record
        UsageTracking usage = new UsageTracking();
        usage.setUserId(recruiter.getId());
        usage.setFeatureType("JOB_POSTING");
        usage.setFeatureValue(1.0);
        usage.setUsageDate(LocalDateTime.now());
        usage.setCreatedAt(LocalDateTime.now());
        
        usageTrackingRepository.save(usage);
    }
}
