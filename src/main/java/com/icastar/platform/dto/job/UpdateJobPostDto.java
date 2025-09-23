package com.icastar.platform.dto.job;

import com.icastar.platform.entity.JobPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateJobPostDto {
    
    @NotBlank(message = "Job title cannot be empty")
    private String title;

    @NotBlank(message = "Job description cannot be empty")
    private String description;

    @NotNull(message = "Job type cannot be null")
    private JobPost.JobType jobType;

    @NotNull(message = "Experience level cannot be null")
    private JobPost.ExperienceLevel experienceLevel;

    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private String location;
    private Boolean isRemote = false;
    private List<String> skillsRequired;
    private String requirements;
    private LocalDateTime applicationDeadline;
    private Boolean isVisible = true;
    private JobPost.JobStatus status;
}
