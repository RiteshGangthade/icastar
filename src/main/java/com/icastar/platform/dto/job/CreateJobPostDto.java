package com.icastar.platform.dto.job;

import com.icastar.platform.entity.JobPost;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateJobPostDto {

    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(max = 5000, message = "Job description must not exceed 5000 characters")
    private String description;

    @NotBlank(message = "Requirements are required")
    @Size(max = 2000, message = "Requirements must not exceed 2000 characters")
    private String requirements;

    @NotNull(message = "Job type is required")
    private JobPost.JobType jobType;

    @NotNull(message = "Experience level is required")
    private JobPost.ExperienceLevel experienceLevel;

    @DecimalMin(value = "0.0", message = "Minimum budget must be non-negative")
    private BigDecimal budgetMin;

    @DecimalMin(value = "0.0", message = "Maximum budget must be non-negative")
    private BigDecimal budgetMax;

    @AssertTrue(message = "Maximum budget must be greater than or equal to minimum budget")
    public boolean isBudgetValid() {
        if (budgetMin == null || budgetMax == null) return true;
        return budgetMax.compareTo(budgetMin) >= 0;
    }

    private String location;

    @NotNull(message = "Remote work option is required")
    private Boolean isRemote;

    private LocalDateTime applicationDeadline;

    @NotEmpty(message = "At least one skill is required")
    private List<String> skillsRequired;

    @Size(max = 1000, message = "Additional notes must not exceed 1000 characters")
    private String additionalNotes;

    private Boolean isVisible = true;
}
