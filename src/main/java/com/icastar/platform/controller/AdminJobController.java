package com.icastar.platform.controller;

import com.icastar.platform.dto.job.JobPostDto;
import com.icastar.platform.dto.job.UpdateJobPostDto;
import com.icastar.platform.entity.JobPost;
import com.icastar.platform.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminJobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Page<JobPostDto>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) JobPost.JobStatus status,
            @RequestParam(required = false) Long recruiterId,
            @RequestParam(required = false) Boolean isVisible) {

        log.info("Admin fetching jobs - page: {}, size: {}, sortBy: {}, sortDir: {}, status: {}, recruiterId: {}, isVisible: {}", 
                page, size, sortBy, sortDir, status, recruiterId, isVisible);

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<JobPost> jobs = jobService.findAll(pageable);
        Page<JobPostDto> jobDtos = jobs.map(JobPostDto::new);
        
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobPostDto> getJobById(@PathVariable Long jobId) {
        log.info("Admin fetching job details for job ID: {}", jobId);
        
        JobPost job = jobService.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        
        JobPostDto jobDto = new JobPostDto(job);
        
        return ResponseEntity.ok(jobDto);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobPostDto> updateJobPost(
            @PathVariable Long jobId,
            @RequestBody UpdateJobPostDto updateDto) {
        
        log.info("Admin updating job post: {}", jobId);
        
        // Admin can update any job post, so we pass null as recruiterId
        JobPost jobPost = jobService.updateJobPost(jobId, null, updateDto);
        JobPostDto jobDto = new JobPostDto(jobPost);
        
        return ResponseEntity.ok(jobDto);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> deleteJobPost(@PathVariable Long jobId) {
        log.info("Admin deleting job post: {}", jobId);
        
        // Admin can delete any job post, so we pass null as recruiterId
        jobService.deleteJobPost(jobId, null);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Job post deleted successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getJobStats() {
        log.info("Admin fetching job statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalJobs", jobService.getTotalJobsCount());
        stats.put("activeJobs", jobService.getActiveJobsCount());
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<JobPostDto>> getFeaturedJobs(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Admin fetching featured jobs with limit: {}", limit);
        
        List<JobPost> featuredJobs = jobService.getFeaturedJobs(limit);
        List<JobPostDto> jobDtos = featuredJobs.stream()
                .map(JobPostDto::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(jobDtos);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<JobPostDto>> getRecentJobs(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Admin fetching recent jobs with limit: {}", limit);
        
        List<JobPost> recentJobs = jobService.getRecentJobs(limit);
        List<JobPostDto> jobDtos = recentJobs.stream()
                .map(JobPostDto::new)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(jobDtos);
    }
}
