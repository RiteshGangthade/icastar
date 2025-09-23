package com.icastar.platform.dto.job;

import com.icastar.platform.entity.JobPost;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class JobPostDto {
    private Long id;
    private String title;
    private String description;
    private JobPost.JobType jobType;
    private JobPost.ExperienceLevel experienceLevel;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String location;
    private Boolean isRemote;
    private List<String> skillsRequired;
    private String requirements;
    private LocalDateTime applicationDeadline;
    private Boolean isVisible;
    private JobPost.JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Recruiter information
    private Long recruiterId;
    private String recruiterName;
    private String companyName;
    private String companyLogoUrl;
    
    // Statistics
    private Long totalApplications;
    private Long totalViews;
    private Boolean isBookmarked;

    public JobPostDto(JobPost jobPost) {
        this.id = jobPost.getId();
        this.title = jobPost.getTitle();
        this.description = jobPost.getDescription();
        this.jobType = jobPost.getJobType();
        this.experienceLevel = jobPost.getExperienceLevel();
        this.budgetMin = jobPost.getBudgetMin();
        this.budgetMax = jobPost.getBudgetMax();
        this.location = jobPost.getLocation();
        this.isRemote = jobPost.getIsRemote();
        this.skillsRequired = jobPost.getSkillsRequired();
        this.requirements = jobPost.getRequirements();
        this.applicationDeadline = jobPost.getApplicationDeadline();
        this.isVisible = jobPost.getIsVisible();
        this.status = jobPost.getStatus();
        this.createdAt = jobPost.getCreatedAt();
        this.updatedAt = jobPost.getUpdatedAt();
        
        if (jobPost.getRecruiter() != null) {
            this.recruiterId = jobPost.getRecruiter().getId();
            this.recruiterName = jobPost.getRecruiter().getContactPersonName();
            this.companyName = jobPost.getRecruiter().getCompanyName();
            this.companyLogoUrl = jobPost.getRecruiter().getCompanyLogoUrl();
        }
        
        this.totalApplications = jobPost.getTotalApplications().longValue();
        this.totalViews = jobPost.getTotalViews().longValue();
    }
}
