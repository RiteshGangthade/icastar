package com.icastar.platform.dto.application;

import com.icastar.platform.entity.JobApplication;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobApplicationDto {
    private Long id;
    private Long jobPostId;
    private String jobTitle;
    private Long artistId;
    private String artistName;
    private String coverLetter;
    private BigDecimal proposedRate;
    private JobApplication.ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
    
    // Artist profile information
    private String artistBio;
    private String artistLocation;
    private String artistExperience;
    private Boolean isArtistVerified;
    
    // Job information
    private String jobDescription;
    private String jobLocation;
    private Boolean isJobRemote;
    private BigDecimal jobBudgetMin;
    private BigDecimal jobBudgetMax;
    
    // Recruiter information
    private Long recruiterId;
    private String recruiterName;
    private String companyName;

    public JobApplicationDto(JobApplication application) {
        this.id = application.getId();
        this.jobPostId = application.getJobPost().getId();
        this.jobTitle = application.getJobPost().getTitle();
        this.artistId = application.getArtist().getId();
        this.coverLetter = application.getCoverLetter();
        this.proposedRate = application.getProposedRate();
        this.status = application.getStatus();
        this.appliedAt = application.getAppliedAt();
        this.updatedAt = application.getUpdatedAt();
        
        if (application.getArtist() != null) {
            this.artistName = application.getArtist().getFirstName() + " " + 
                            application.getArtist().getLastName();
            this.artistBio = application.getArtist().getBio();
            this.artistLocation = application.getArtist().getLocation();
            this.artistExperience = application.getArtist().getExperienceYears() != null ? 
                application.getArtist().getExperienceYears().toString() + " years" : "Not specified";
            this.isArtistVerified = application.getArtist().getUser() != null ? 
                application.getArtist().getUser().getIsVerified() : false;
        }
        
        if (application.getJobPost() != null) {
            this.jobDescription = application.getJobPost().getDescription();
            this.jobLocation = application.getJobPost().getLocation();
            this.isJobRemote = application.getJobPost().getIsRemote();
            this.jobBudgetMin = application.getJobPost().getBudgetMin();
            this.jobBudgetMax = application.getJobPost().getBudgetMax();
            
            if (application.getJobPost().getRecruiter() != null) {
                this.recruiterId = application.getJobPost().getRecruiter().getId();
                this.recruiterName = application.getJobPost().getRecruiter().getContactPersonName();
                this.companyName = application.getJobPost().getRecruiter().getCompanyName();
            }
        }
    }
}
