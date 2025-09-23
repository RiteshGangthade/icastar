package com.icastar.platform.service;

import com.icastar.platform.dto.job.CreateJobPostDto;
import com.icastar.platform.dto.job.JobPostDto;
import com.icastar.platform.dto.job.JobSearchDto;
import com.icastar.platform.dto.job.UpdateJobPostDto;
import com.icastar.platform.entity.JobPost;
import com.icastar.platform.entity.User;
import com.icastar.platform.exception.BusinessException;
import com.icastar.platform.repository.JobPostRepository;
import com.icastar.platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobService {

    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<JobPost> findById(Long id) {
        return jobPostRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<JobPost> findAll(Pageable pageable) {
        return jobPostRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<JobPost> findByRecruiter(Long recruiterId, Pageable pageable) {
        return jobPostRepository.findByRecruiterId(recruiterId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<JobPost> findVisibleJobs(Pageable pageable) {
        return jobPostRepository.findByIsVisibleTrueAndStatus(JobPost.JobStatus.ACTIVE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<JobPost> searchJobs(JobSearchDto searchDto) {
        Sort sort = Sort.by(
            searchDto.getSortDirection().equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC, 
            searchDto.getSortBy()
        );
        
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);
        
        // Build dynamic query based on search criteria
        if (searchDto.getSearchTerm() != null && !searchDto.getSearchTerm().trim().isEmpty()) {
            return jobPostRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                searchDto.getSearchTerm(), searchDto.getSearchTerm(), pageable);
        }
        
        if (searchDto.getJobType() != null) {
            return jobPostRepository.findByJobTypeAndIsVisibleTrue(searchDto.getJobType(), pageable);
        }
        
        if (searchDto.getLocation() != null && !searchDto.getLocation().trim().isEmpty()) {
            return jobPostRepository.findByLocationContainingIgnoreCaseAndIsVisibleTrue(searchDto.getLocation(), pageable);
        }
        
        return findVisibleJobs(pageable);
    }

    public JobPost createJobPost(Long recruiterId, CreateJobPostDto createDto) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new BusinessException("Recruiter not found with id: " + recruiterId));

        if (recruiter.getRole() != User.UserRole.RECRUITER) {
            throw new BusinessException("User is not a recruiter");
        }

        if (recruiter.getRecruiterProfile() == null) {
            throw new BusinessException("Recruiter profile not found");
        }

        log.info("Creating job post for recruiter {}: {}", recruiterId, createDto.getTitle());

        JobPost jobPost = new JobPost();
        jobPost.setTitle(createDto.getTitle());
        jobPost.setDescription(createDto.getDescription());
        jobPost.setJobType(createDto.getJobType());
        jobPost.setExperienceLevel(createDto.getExperienceLevel());
        jobPost.setBudgetMin(createDto.getBudgetMin());
        jobPost.setBudgetMax(createDto.getBudgetMax());
        jobPost.setLocation(createDto.getLocation());
        jobPost.setIsRemote(createDto.getIsRemote());
        jobPost.setSkillsRequired(createDto.getSkillsRequired());
        jobPost.setRequirements(createDto.getRequirements());
        jobPost.setApplicationDeadline(createDto.getApplicationDeadline());
        jobPost.setIsVisible(createDto.getIsVisible());
        jobPost.setStatus(JobPost.JobStatus.ACTIVE);
        jobPost.setRecruiter(recruiter.getRecruiterProfile());
        jobPost.setTotalApplications(0);
        jobPost.setTotalViews(0);

        JobPost savedJobPost = jobPostRepository.save(jobPost);
        log.info("Job post created successfully with ID: {}", savedJobPost.getId());
        
        return savedJobPost;
    }

    public JobPost updateJobPost(Long jobId, Long recruiterId, UpdateJobPostDto updateDto) {
        JobPost jobPost = findById(jobId)
                .orElseThrow(() -> new BusinessException("Job post not found with id: " + jobId));

        // Verify ownership
        if (!jobPost.getRecruiter().getId().equals(recruiterId)) {
            throw new BusinessException("You don't have permission to update this job post");
        }

        log.info("Updating job post {} for recruiter {}", jobId, recruiterId);

        jobPost.setTitle(updateDto.getTitle());
        jobPost.setDescription(updateDto.getDescription());
        jobPost.setJobType(updateDto.getJobType());
        jobPost.setExperienceLevel(updateDto.getExperienceLevel());
        jobPost.setBudgetMin(updateDto.getBudgetMin());
        jobPost.setBudgetMax(updateDto.getBudgetMax());
        jobPost.setLocation(updateDto.getLocation());
        jobPost.setIsRemote(updateDto.getIsRemote());
        jobPost.setSkillsRequired(updateDto.getSkillsRequired());
        jobPost.setRequirements(updateDto.getRequirements());
        jobPost.setApplicationDeadline(updateDto.getApplicationDeadline());
        jobPost.setIsVisible(updateDto.getIsVisible());
        
        if (updateDto.getStatus() != null) {
            jobPost.setStatus(updateDto.getStatus());
        }

        JobPost updatedJobPost = jobPostRepository.save(jobPost);
        log.info("Job post updated successfully: {}", jobId);
        
        return updatedJobPost;
    }

    public void deleteJobPost(Long jobId, Long recruiterId) {
        JobPost jobPost = findById(jobId)
                .orElseThrow(() -> new BusinessException("Job post not found with id: " + jobId));

        // Verify ownership
        if (!jobPost.getRecruiter().getId().equals(recruiterId)) {
            throw new BusinessException("You don't have permission to delete this job post");
        }

        log.info("Deleting job post {} for recruiter {}", jobId, recruiterId);

        // Soft delete - set as inactive
        jobPost.setIsVisible(false);
        jobPost.setStatus(JobPost.JobStatus.CLOSED);
        
        jobPostRepository.save(jobPost);
        log.info("Job post deleted successfully: {}", jobId);
    }

    public JobPost toggleJobVisibility(Long jobId, Long recruiterId) {
        JobPost jobPost = findById(jobId)
                .orElseThrow(() -> new BusinessException("Job post not found with id: " + jobId));

        // Verify ownership
        if (!jobPost.getRecruiter().getId().equals(recruiterId)) {
            throw new BusinessException("You don't have permission to modify this job post");
        }

        log.info("Toggling visibility for job post {} for recruiter {}", jobId, recruiterId);

        jobPost.setIsVisible(!jobPost.getIsVisible());
        
        JobPost updatedJobPost = jobPostRepository.save(jobPost);
        log.info("Job post visibility toggled successfully: {}", jobId);
        
        return updatedJobPost;
    }

    public JobPost incrementViewCount(Long jobId) {
        JobPost jobPost = findById(jobId)
                .orElseThrow(() -> new BusinessException("Job post not found with id: " + jobId));

        jobPost.setTotalViews(jobPost.getTotalViews() + 1);
        
        return jobPostRepository.save(jobPost);
    }

    @Transactional(readOnly = true)
    public List<JobPost> getFeaturedJobs(int limit) {
        return jobPostRepository.findByIsVisibleTrueAndStatusOrderByTotalViewsDesc(
            JobPost.JobStatus.ACTIVE, PageRequest.of(0, limit)).getContent();
    }

    @Transactional(readOnly = true)
    public List<JobPost> getRecentJobs(int limit) {
        return jobPostRepository.findByIsVisibleTrueAndStatusOrderByCreatedAtDesc(
            JobPost.JobStatus.ACTIVE, PageRequest.of(0, limit)).getContent();
    }

    @Transactional(readOnly = true)
    public Long getTotalJobsCount() {
        return jobPostRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getActiveJobsCount() {
        return jobPostRepository.countByStatusAndIsVisibleTrue(JobPost.JobStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public Long getJobsCountByRecruiter(Long recruiterId) {
        return jobPostRepository.countByRecruiterId(recruiterId);
    }
}
