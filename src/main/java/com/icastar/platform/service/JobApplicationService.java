package com.icastar.platform.service;

import com.icastar.platform.dto.application.CreateJobApplicationDto;
import com.icastar.platform.dto.application.JobApplicationDto;
import com.icastar.platform.dto.application.UpdateApplicationStatusDto;
import com.icastar.platform.entity.JobApplication;
import com.icastar.platform.entity.JobPost;
import com.icastar.platform.entity.User;
import com.icastar.platform.exception.BusinessException;
import com.icastar.platform.repository.JobApplicationRepository;
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
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> findByJobPost(Long jobPostId, Pageable pageable) {
        return jobApplicationRepository.findByJobPostId(jobPostId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> findByArtist(Long artistId, Pageable pageable) {
        return jobApplicationRepository.findByArtistId(artistId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> findByRecruiter(Long recruiterId, Pageable pageable) {
        return jobApplicationRepository.findByJobPostRecruiterId(recruiterId, pageable);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> findByJobPostAndStatus(Long jobPostId, JobApplication.ApplicationStatus status) {
        return jobApplicationRepository.findByJobPostIdAndStatus(jobPostId, status);
    }

    @Transactional(readOnly = true)
    public boolean hasArtistAppliedToJob(Long artistId, Long jobPostId) {
        return jobApplicationRepository.existsByArtistIdAndJobPostId(artistId, jobPostId);
    }

    public JobApplication createApplication(Long artistId, CreateJobApplicationDto createDto) {
        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new BusinessException("Artist not found with id: " + artistId));

        if (artist.getRole() != User.UserRole.ARTIST) {
            throw new BusinessException("User is not an artist");
        }

        if (artist.getArtistProfile() == null) {
            throw new BusinessException("Artist profile not found");
        }

        JobPost jobPost = jobPostRepository.findById(createDto.getJobPostId())
                .orElseThrow(() -> new BusinessException("Job post not found with id: " + createDto.getJobPostId()));

        // Check if job is still active and visible
        if (!jobPost.getIsVisible() || jobPost.getStatus() != JobPost.JobStatus.ACTIVE) {
            throw new BusinessException("Job post is not available for applications");
        }

        // Check if application deadline has passed
        if (jobPost.getApplicationDeadline() != null && 
            jobPost.getApplicationDeadline().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Application deadline has passed");
        }

        // Check if artist has already applied
        if (hasArtistAppliedToJob(artistId, createDto.getJobPostId())) {
            throw new BusinessException("You have already applied to this job");
        }

        log.info("Creating job application for artist {} to job {}", artistId, createDto.getJobPostId());

        JobApplication application = new JobApplication();
        application.setJobPost(jobPost);
        application.setArtist(artist.getArtistProfile());
        application.setCoverLetter(createDto.getCoverLetter());
        application.setProposedRate(createDto.getProposedRate());
        application.setStatus(JobApplication.ApplicationStatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Update job post application count
        jobPost.setTotalApplications(jobPost.getTotalApplications() + 1);
        jobPostRepository.save(jobPost);

        log.info("Job application created successfully with ID: {}", savedApplication.getId());
        
        return savedApplication;
    }

    public JobApplication updateApplicationStatus(Long applicationId, Long recruiterId, UpdateApplicationStatusDto updateDto) {
        JobApplication application = findById(applicationId)
                .orElseThrow(() -> new BusinessException("Application not found with id: " + applicationId));

        // Verify that the recruiter owns the job post
        if (!application.getJobPost().getRecruiter().getId().equals(recruiterId)) {
            throw new BusinessException("You don't have permission to update this application");
        }

        log.info("Updating application status for application {} to {}", applicationId, updateDto.getStatus());

        application.setStatus(updateDto.getStatus());
        application.setUpdatedAt(LocalDateTime.now());

        // If accepted, update artist's successful hires count
        if (updateDto.getStatus() == JobApplication.ApplicationStatus.ACCEPTED) {
            application.getArtist().setSuccessfulHires(application.getArtist().getSuccessfulHires() + 1);
        }

        JobApplication updatedApplication = jobApplicationRepository.save(application);
        log.info("Application status updated successfully: {}", applicationId);
        
        return updatedApplication;
    }


    @Transactional(readOnly = true)
    public Long getTotalApplicationsCount() {
        return jobApplicationRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getApplicationsCountByStatus(JobApplication.ApplicationStatus status) {
        return jobApplicationRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long getApplicationsCountByArtist(Long artistId) {
        return jobApplicationRepository.countByArtistId(artistId);
    }

    @Transactional(readOnly = true)
    public Long getApplicationsCountByJobPost(Long jobPostId) {
        return jobApplicationRepository.countByJobPostId(jobPostId);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getRecentApplications(int limit) {
        return jobApplicationRepository.findAll(
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "appliedAt"))
        ).getContent();
    }

    @Transactional(readOnly = true)
    public List<JobApplication> getApplicationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return jobApplicationRepository.findByAppliedAtBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<JobApplication> findByRecruiterAndStatus(Long recruiterId, JobApplication.ApplicationStatus status) {
        return jobApplicationRepository.findByRecruiterAndStatus(recruiterId, status);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> findByArtistAndStatus(Long artistId, JobApplication.ApplicationStatus status, Pageable pageable) {
        // Filter by status after getting the page
        Page<JobApplication> applications = jobApplicationRepository.findByArtistId(artistId, pageable);
        return applications.map(app -> app.getStatus() == status ? app : null)
                .map(app -> app != null ? app : null);
    }

    @Transactional(readOnly = true)
    public Long getApplicationsCountByRecruiter(Long recruiterId) {
        return jobApplicationRepository.countByRecruiterId(recruiterId);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> findAll(Pageable pageable) {
        return jobApplicationRepository.findAll(pageable);
    }

    public void deleteApplication(Long applicationId, Long userId) {
        JobApplication application = findById(applicationId)
                .orElseThrow(() -> new BusinessException("Application not found with id: " + applicationId));

        // If userId is null, it means admin is deleting (no permission check)
        if (userId != null) {
            // Verify ownership
            if (!application.getArtist().getId().equals(userId)) {
                throw new BusinessException("You don't have permission to delete this application");
            }

            // Only allow deletion if status is PENDING
            if (application.getStatus() != JobApplication.ApplicationStatus.PENDING) {
                throw new BusinessException("Cannot delete application that has been processed");
            }
        }

        log.info("Deleting application {} for user {}", applicationId, userId);

        // Update job post application count
        JobPost jobPost = application.getJobPost();
        jobPost.setTotalApplications(jobPost.getTotalApplications() - 1);
        jobPostRepository.save(jobPost);

        jobApplicationRepository.delete(application);
        log.info("Application deleted successfully: {}", applicationId);
    }
}
