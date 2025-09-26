package com.icastar.platform.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobFilterDto {
    
    private Long recruiterId;
    private String recruiterName;
    private String recruiterEmail;
    private String recruiterCompany;
    private String recruiterCategory;
    
    private String status;
    private Boolean isActive;
    private Boolean isVisible;
    
    private Long subscriptionId;
    private String subscriptionPlan;
    private String subscriptionStatus;
    
    private String jobType;
    private String location;
    private String title;
    
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime applicationDeadlineFrom;
    private LocalDateTime applicationDeadlineTo;
    
    private Integer minApplications;
    private Integer maxApplications;
    private Integer minViews;
    private Integer maxViews;
    
    private Boolean hasBoost;
    private Boolean isExpired;
    private Boolean isUrgent;
}
