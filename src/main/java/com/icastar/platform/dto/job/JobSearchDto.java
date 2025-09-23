package com.icastar.platform.dto.job;

import com.icastar.platform.entity.JobPost;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class JobSearchDto {
    private String searchTerm;
    private JobPost.JobType jobType;
    private JobPost.ExperienceLevel experienceLevel;
    private BigDecimal minBudget;
    private BigDecimal maxBudget;
    private String location;
    private Boolean isRemote;
    private List<String> skills;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
    private Integer page = 0;
    private Integer size = 20;
}
