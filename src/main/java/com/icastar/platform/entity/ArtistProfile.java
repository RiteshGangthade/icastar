package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "artist_profiles")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_type_id", nullable = false)
    private ArtistType artistType;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "stage_name")
    private String stageName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "location")
    private String location;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "portfolio_urls", columnDefinition = "JSON")
    private String portfolioUrls; // JSON array of URLs

    @Column(name = "skills", columnDefinition = "JSON")
    private String skills; // JSON array of skills

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "is_verified_badge", nullable = false)
    private Boolean isVerifiedBadge = false;

    @Column(name = "verification_requested_at")
    private LocalDate verificationRequestedAt;

    @Column(name = "verification_approved_at")
    private LocalDate verificationApprovedAt;

    @Column(name = "total_applications")
    private Integer totalApplications = 0;

    @Column(name = "successful_hires")
    private Integer successfulHires = 0;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobApplication> jobApplications;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookmarkedJob> bookmarkedJobs;

    // Messages are handled at User level, not ArtistProfile level
    // @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Message> sentMessages;

    // @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Message> receivedMessages;

    // Dynamic fields for artist type specific data
    @OneToMany(mappedBy = "artistProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArtistProfileField> dynamicFields;

    public enum Gender {
        MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
    }
}
