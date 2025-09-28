package com.icastar.platform.controller;

import com.icastar.platform.dto.job.CreateJobPostDto;
import com.icastar.platform.dto.job.JobPostDto;
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
import java.util.Map;

@RestController
@RequestMapping("/recruiter/jobs")
@RequiredArgsConstructor
@Slf4j
public class RecruiterJobController {

    private final JobService jobService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<JobPostDto> createJobPost(@Valid @RequestBody CreateJobPostDto createDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can create job posts");
            }

            log.info("Creating job post for recruiter: {}", email);

            JobPost jobPost = jobService.createJobPost(recruiter.getId(), createDto);
            JobPostDto jobDto = new JobPostDto(jobPost);

            return ResponseEntity.ok(jobDto);
        } catch (Exception e) {
            log.error("Error creating job post", e);
            throw new RuntimeException("Failed to create job post: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<JobPostDto>> getMyJobPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can view job posts");
            }

            log.info("Fetching job posts for recruiter: {}", email);

            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<JobPost> jobs = jobService.findByRecruiter(recruiter.getId(), pageable);
            Page<JobPostDto> jobDtos = jobs.map(JobPostDto::new);

            return ResponseEntity.ok(jobDtos);
        } catch (Exception e) {
            log.error("Error fetching job posts", e);
            throw new RuntimeException("Failed to fetch job posts: " + e.getMessage());
        }
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobPostDto> getMyJobPost(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can view job posts");
            }

            log.info("Fetching job post {} for recruiter: {}", jobId, email);

            JobPost job = jobService.findById(jobId)
                    .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

            // Verify ownership
            if (!job.getRecruiter().getId().equals(recruiter.getId())) {
                throw new RuntimeException("You don't have permission to view this job post");
            }

            JobPostDto jobDto = new JobPostDto(job);

            return ResponseEntity.ok(jobDto);
        } catch (Exception e) {
            log.error("Error fetching job post", e);
            throw new RuntimeException("Failed to fetch job post: " + e.getMessage());
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<JobPostDto> updateJobPost(
            @PathVariable Long jobId,
            @Valid @RequestBody UpdateJobPostDto updateDto) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can update job posts");
            }

            log.info("Updating job post {} for recruiter: {}", jobId, email);

            JobPost jobPost = jobService.updateJobPost(jobId, recruiter.getId(), updateDto);
            JobPostDto jobDto = new JobPostDto(jobPost);

            return ResponseEntity.ok(jobDto);
        } catch (Exception e) {
            log.error("Error updating job post", e);
            throw new RuntimeException("Failed to update job post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Map<String, String>> deleteJobPost(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can delete job posts");
            }

            log.info("Deleting job post {} for recruiter: {}", jobId, email);

            jobService.deleteJobPost(jobId, recruiter.getId());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Job post deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting job post", e);
            throw new RuntimeException("Failed to delete job post: " + e.getMessage());
        }
    }

    @PostMapping("/{jobId}/toggle-visibility")
    public ResponseEntity<JobPostDto> toggleJobVisibility(@PathVariable Long jobId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can modify job posts");
            }

            log.info("Toggling visibility for job post {} for recruiter: {}", jobId, email);

            JobPost jobPost = jobService.toggleJobVisibility(jobId, recruiter.getId());
            JobPostDto jobDto = new JobPostDto(jobPost);

            return ResponseEntity.ok(jobDto);
        } catch (Exception e) {
            log.error("Error toggling job visibility", e);
            throw new RuntimeException("Failed to toggle job visibility: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMyJobStats() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            
            User recruiter = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (recruiter.getRole() != User.UserRole.RECRUITER) {
                throw new RuntimeException("Only recruiters can view job stats");
            }

            log.info("Fetching job stats for recruiter: {}", email);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalJobs", jobService.getJobsCountByRecruiter(recruiter.getId()));

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching job stats", e);
            throw new RuntimeException("Failed to fetch job stats: " + e.getMessage());
        }
    }
}
