package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_posts")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobPost extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private RecruiterProfile recruiter;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_post_id"))
    @Column(name = "skill")
    private List<String> skillsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level", nullable = false)
    private ExperienceLevel experienceLevel;

    @Column(name = "budget_min", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal budgetMin;

    @Column(name = "budget_max", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal budgetMax;

    @Column(name = "location")
    private String location;

    @Column(name = "is_remote", nullable = false)
    private Boolean isRemote = false;

    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status = JobStatus.ACTIVE;

    @Column(name = "is_boosted", nullable = false)
    private Boolean isBoosted = false;

    @Column(name = "boost_expires_at")
    private LocalDateTime boostExpiresAt;

    @Column(name = "total_applications")
    private Integer totalApplications = 0;

    @Column(name = "total_views")
    private Integer totalViews = 0;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> jobApplications;

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookmarkedJob> bookmarkedJobs;

    public enum JobType {
        FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP
    }

    public enum ExperienceLevel {
        ENTRY, JUNIOR, MID, SENIOR, EXPERT
    }

    public enum JobStatus {
        ACTIVE, CLOSED, EXPIRED, DRAFT
    }
}
