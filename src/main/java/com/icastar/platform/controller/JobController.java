package com.icastar.platform.controller;

import com.icastar.platform.dto.job.CreateJobPostDto;
import com.icastar.platform.dto.job.JobPostDto;
import com.icastar.platform.dto.job.JobSearchDto;
import com.icastar.platform.dto.job.UpdateJobPostDto;
import com.icastar.platform.entity.JobPost;
import com.icastar.platform.entity.User;
import com.icastar.platform.service.JobService;
import com.icastar.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<JobPostDto>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) JobPost.JobType jobType,
            @RequestParam(required = false) JobPost.ExperienceLevel experienceLevel,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean isRemote) {

        log.info("Fetching jobs - page: {}, size: {}, sortBy: {}, sortDir: {}, search: {}, jobType: {}, experienceLevel: {}, location: {}, isRemote: {}", 
                page, size, sortBy, sortDir, search, jobType, experienceLevel, location, isRemote);

        JobSearchDto searchDto = new JobSearchDto();
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy(sortBy);
        searchDto.setSortDirection(sortDir);
        searchDto.setSearchTerm(search);
        searchDto.setJobType(jobType);
        searchDto.setExperienceLevel(experienceLevel);
        searchDto.setLocation(location);
        searchDto.setIsRemote(isRemote);

        Page<JobPost> jobs = jobService.searchJobs(searchDto);
        Page<JobPostDto> jobDtos = jobs.map(JobPostDto::new);
        
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobPostDto> getJobById(@PathVariable Long jobId) {
        log.info("Fetching job details for job ID: {}", jobId);
        
        JobPost job = jobService.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        
        // Increment view count
        jobService.incrementViewCount(jobId);
        
        JobPostDto jobDto = new JobPostDto(job);
        
        return ResponseEntity.ok(jobDto);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<JobPostDto>> getFeaturedJobs(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Fetching featured jobs with limit: {}", limit);
        
        List<JobPost> featuredJobs = jobService.getFeaturedJobs(limit);
        List<JobPostDto> jobDtos = featuredJobs.stream()
                .map(JobPostDto::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<JobPostDto>> getRecentJobs(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Fetching recent jobs with limit: {}", limit);
        
        List<JobPost> recentJobs = jobService.getRecentJobs(limit);
        List<JobPostDto> jobDtos = recentJobs.stream()
                .map(JobPostDto::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getJobStats() {
        log.info("Fetching job statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalJobs", jobService.getTotalJobsCount());
        stats.put("activeJobs", jobService.getActiveJobsCount());
        
        return ResponseEntity.ok(stats);
    }
}
