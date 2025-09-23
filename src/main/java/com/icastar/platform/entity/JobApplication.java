package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobApplication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistProfile artist;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "proposed_rate", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal proposedRate;

    @Column(name = "availability_date")
    private LocalDateTime availabilityDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<Audition> auditions;

    public enum ApplicationStatus {
        PENDING, REVIEWED, SHORTLISTED, ACCEPTED, REJECTED, HIRED, WITHDRAWN
    }
}
