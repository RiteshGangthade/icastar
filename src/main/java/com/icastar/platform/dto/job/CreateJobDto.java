package com.icastar.platform.dto.job;

import com.icastar.platform.entity.Job;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateJobDto {

    @NotBlank(message = "Job title is required")
    @Size(max = 200, message = "Job title must not exceed 200 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(max = 5000, message = "Job description must not exceed 5000 characters")
    private String description;

    @Size(max = 3000, message = "Requirements must not exceed 3000 characters")
    private String requirements;

    private String location;

    @NotNull(message = "Job type is required")
    private Job.JobType jobType;

    @NotNull(message = "Experience level is required")
    private Job.ExperienceLevel experienceLevel;

    @Positive(message = "Minimum budget must be positive")
    private BigDecimal budgetMin;

    @Positive(message = "Maximum budget must be positive")
    private BigDecimal budgetMax;

    private String currency = "INR";

    @Positive(message = "Duration must be positive")
    private Integer durationDays;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate applicationDeadline;

    private Boolean isRemote = false;
    private Boolean isUrgent = false;
    private Boolean isFeatured = false;

    private List<String> tags;
    private List<String> skillsRequired;
    private String benefits;
    private String contactEmail;
    private String contactPhone;
}
