package com.icastar.platform.service;

import com.icastar.platform.dto.job.CreateJobDto;
import com.icastar.platform.dto.job.JobDto;
import com.icastar.platform.entity.Job;
import com.icastar.platform.entity.User;
import com.icastar.platform.repository.JobRepository;
import com.icastar.platform.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Job> findActiveJobs() {
        return jobRepository.findActiveJobs(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Page<Job> findActiveJobs(Pageable pageable) {
        return jobRepository.findActiveJobs(LocalDate.now(), pageable);
    }

    @Transactional(readOnly = true)
    public List<Job> findByRecruiter(User recruiter) {
        return jobRepository.findByRecruiter(recruiter);
    }

    @Transactional(readOnly = true)
    public Page<Job> findByRecruiter(User recruiter, Pageable pageable) {
        return jobRepository.findByRecruiter(recruiter, pageable);
    }

    public Page<Job> findByRecruiter(Long recruiterId, Pageable pageable) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobRepository.findByRecruiter(recruiter, pageable);
    }

    @Transactional(readOnly = true)
    public List<Job> searchJobs(String searchTerm) {
        return jobRepository.findBySearchTerm(searchTerm);
    }

    @Transactional(readOnly = true)
    public Page<Job> searchJobs(String searchTerm, Pageable pageable) {
        return jobRepository.findBySearchTerm(searchTerm, pageable);
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsByLocation(String location) {
        return jobRepository.findByLocationContainingIgnoreCase(location);
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsByType(Job.JobType jobType) {
        return jobRepository.findByJobType(jobType);
    }

    @Transactional(readOnly = true)
    public List<Job> findRemoteJobs() {
        return jobRepository.findByIsRemoteTrue();
    }

    @Transactional(readOnly = true)
    public List<Job> findFeaturedJobs() {
        return jobRepository.findByIsFeaturedTrue();
    }

    @Transactional(readOnly = true)
    public List<Job> findUrgentJobs() {
        return jobRepository.findByIsUrgentTrue();
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsBySkill(String skill) {
        return jobRepository.findByRequiredSkill(skill);
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsByTag(String tag) {
        return jobRepository.findByTag(tag);
    }

    @Transactional(readOnly = true)
    public List<Job> findMostPopularJobs(Pageable pageable) {
        return jobRepository.findMostPopularJobs(pageable);
    }

    @Transactional(readOnly = true)
    public List<Job> findRecentlyPostedJobs(Pageable pageable) {
        return jobRepository.findRecentlyPostedJobs(pageable);
    }

    @Transactional(readOnly = true)
    public List<Job> findJobsExpiringSoon(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return jobRepository.findJobsExpiringSoon(startDate, endDate);
    }

    public Job createJob(Long recruiterId, CreateJobDto createJobDto) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setTitle(createJobDto.getTitle());
        job.setDescription(createJobDto.getDescription());
        job.setRequirements(createJobDto.getRequirements());
        job.setLocation(createJobDto.getLocation());
        job.setJobType(createJobDto.getJobType());
        job.setExperienceLevel(createJobDto.getExperienceLevel());
        job.setBudgetMin(createJobDto.getBudgetMin());
        job.setBudgetMax(createJobDto.getBudgetMax());
        job.setCurrency(createJobDto.getCurrency());
        job.setDurationDays(createJobDto.getDurationDays());
        job.setStartDate(createJobDto.getStartDate());
        job.setEndDate(createJobDto.getEndDate());
        job.setApplicationDeadline(createJobDto.getApplicationDeadline());
        job.setIsRemote(createJobDto.getIsRemote());
        job.setIsUrgent(createJobDto.getIsUrgent());
        job.setIsFeatured(createJobDto.getIsFeatured());
        job.setContactEmail(createJobDto.getContactEmail());
        job.setContactPhone(createJobDto.getContactPhone());
        job.setBenefits(createJobDto.getBenefits());

        // Convert lists to JSON strings
        try {
            if (createJobDto.getTags() != null) {
                job.setTags(objectMapper.writeValueAsString(createJobDto.getTags()));
            }
            if (createJobDto.getSkillsRequired() != null) {
                job.setSkillsRequired(objectMapper.writeValueAsString(createJobDto.getSkillsRequired()));
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting lists to JSON", e);
            throw new RuntimeException("Error processing job data");
        }

        job.setStatus(Job.JobStatus.ACTIVE);
        job.setPublishedAt(LocalDateTime.now());

        return jobRepository.save(job);
    }

    public Job updateJob(Long jobId, CreateJobDto updateJobDto) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(updateJobDto.getTitle());
        job.setDescription(updateJobDto.getDescription());
        job.setRequirements(updateJobDto.getRequirements());
        job.setLocation(updateJobDto.getLocation());
        job.setJobType(updateJobDto.getJobType());
        job.setExperienceLevel(updateJobDto.getExperienceLevel());
        job.setBudgetMin(updateJobDto.getBudgetMin());
        job.setBudgetMax(updateJobDto.getBudgetMax());
        job.setCurrency(updateJobDto.getCurrency());
        job.setDurationDays(updateJobDto.getDurationDays());
        job.setStartDate(updateJobDto.getStartDate());
        job.setEndDate(updateJobDto.getEndDate());
        job.setApplicationDeadline(updateJobDto.getApplicationDeadline());
        job.setIsRemote(updateJobDto.getIsRemote());
        job.setIsUrgent(updateJobDto.getIsUrgent());
        job.setIsFeatured(updateJobDto.getIsFeatured());
        job.setContactEmail(updateJobDto.getContactEmail());
        job.setContactPhone(updateJobDto.getContactPhone());
        job.setBenefits(updateJobDto.getBenefits());

        // Convert lists to JSON strings
        try {
            if (updateJobDto.getTags() != null) {
                job.setTags(objectMapper.writeValueAsString(updateJobDto.getTags()));
            }
            if (updateJobDto.getSkillsRequired() != null) {
                job.setSkillsRequired(objectMapper.writeValueAsString(updateJobDto.getSkillsRequired()));
            }
        } catch (JsonProcessingException e) {
            log.error("Error converting lists to JSON", e);
            throw new RuntimeException("Error processing job data");
        }

        return jobRepository.save(job);
    }

    public Job updateJobStatus(Long jobId, Job.JobStatus status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(status);
        if (status == Job.JobStatus.CLOSED) {
            job.setClosedAt(LocalDateTime.now());
        }

        return jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(Job.JobStatus.CANCELLED);
        job.setClosedAt(LocalDateTime.now());
        jobRepository.save(job);
    }

    public void incrementViews(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setViewsCount(job.getViewsCount() + 1);
        jobRepository.save(job);
    }

    public void incrementApplications(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setApplicationsCount(job.getApplicationsCount() + 1);
        jobRepository.save(job);
    }

    @Transactional(readOnly = true)
    public Long countJobsByRecruiter(User recruiter) {
        return jobRepository.countByRecruiter(recruiter);
    }

    @Transactional(readOnly = true)
    public Long countJobsByStatus(Job.JobStatus status) {
        return jobRepository.countByStatus(status);
    }

    // JobPost related methods (for compatibility with controllers)
    public Job createJobPost(Long recruiterId, com.icastar.platform.dto.job.CreateJobPostDto createJobPostDto) {
        // Convert CreateJobPostDto to CreateJobDto and delegate to existing method
        CreateJobDto createJobDto = new CreateJobDto();
        // Note: CreateJobPostDto doesn't have artistTypeId, using default or null
        createJobDto.setTitle(createJobPostDto.getTitle());
        createJobDto.setDescription(createJobPostDto.getDescription());
        createJobDto.setLocation(createJobPostDto.getLocation());
        createJobDto.setBudgetMin(createJobPostDto.getBudgetMin());
        createJobDto.setBudgetMax(createJobPostDto.getBudgetMax());
        createJobDto.setApplicationDeadline(createJobPostDto.getApplicationDeadline() != null ? 
            createJobPostDto.getApplicationDeadline().toLocalDate() : null);
        createJobDto.setSkillsRequired(createJobPostDto.getSkillsRequired());
        // Note: CreateJobPostDto doesn't have experienceYearsMin/Max, using null
        createJobDto.setIsFeatured(false); // Default value
        
        return createJob(recruiterId, createJobDto);
    }

    public Job updateJobPost(Long jobId, Long recruiterId, com.icastar.platform.dto.job.UpdateJobPostDto updateJobPostDto) {
        // Convert UpdateJobPostDto to CreateJobDto and delegate to existing method
        CreateJobDto updateJobDto = new CreateJobDto();
        // Note: UpdateJobPostDto doesn't have artistTypeId, using null
        updateJobDto.setTitle(updateJobPostDto.getTitle());
        updateJobDto.setDescription(updateJobPostDto.getDescription());
        updateJobDto.setLocation(updateJobPostDto.getLocation());
        updateJobDto.setBudgetMin(updateJobPostDto.getBudgetMin());
        updateJobDto.setBudgetMax(updateJobPostDto.getBudgetMax());
        updateJobDto.setApplicationDeadline(updateJobPostDto.getApplicationDeadline() != null ? 
            updateJobPostDto.getApplicationDeadline().toLocalDate() : null);
        updateJobDto.setSkillsRequired(updateJobPostDto.getSkillsRequired());
        // Note: UpdateJobPostDto doesn't have experienceYearsMin/Max, using null
        updateJobDto.setIsFeatured(false); // Default value
        
        return updateJob(jobId, updateJobDto);
    }

    public void deleteJobPost(Long jobId, Long recruiterId) {
        deleteJob(jobId);
    }

    public Job toggleJobVisibility(Long jobId, Long recruiterId) {
        Job job = findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(job.getStatus() == Job.JobStatus.ACTIVE ? Job.JobStatus.CLOSED : Job.JobStatus.ACTIVE);
        return jobRepository.save(job);
    }

    public Long getJobsCountByRecruiter(Long recruiterId) {
        User recruiter = userRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return countJobsByRecruiter(recruiter);
    }

    public List<Job> getFeaturedJobs(int limit) {
        return jobRepository.findByIsFeaturedTrue()
                .stream()
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Job> getRecentJobs(int limit) {
        return jobRepository.findAll()
                .stream()
                .sorted((j1, j2) -> j2.getPublishedAt().compareTo(j1.getPublishedAt()))
                .limit(limit)
                .collect(java.util.stream.Collectors.toList());
    }

    public Long getTotalJobsCount() {
        return jobRepository.count();
    }

    public Long getActiveJobsCount() {
        return countJobsByStatus(Job.JobStatus.ACTIVE);
    }
}