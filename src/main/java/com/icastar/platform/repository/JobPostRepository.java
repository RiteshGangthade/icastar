package com.icastar.platform.repository;

import com.icastar.platform.entity.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    // Enhanced Job Management Repository Methods

    Page<JobPost> findByRecruiterId(Long recruiterId, Pageable pageable);

    Page<JobPost> findByIsVisibleTrueAndStatus(JobPost.JobStatus status, Pageable pageable);

    Page<JobPost> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title, String description, Pageable pageable);

    Page<JobPost> findByJobTypeAndIsVisibleTrue(JobPost.JobType jobType, Pageable pageable);

    Page<JobPost> findByLocationContainingIgnoreCaseAndIsVisibleTrue(String location, Pageable pageable);

    Page<JobPost> findByIsVisibleTrueAndStatusOrderByTotalViewsDesc(JobPost.JobStatus status, Pageable pageable);

    Page<JobPost> findByIsVisibleTrueAndStatusOrderByCreatedAtDesc(JobPost.JobStatus status, Pageable pageable);

    Long countByStatusAndIsVisibleTrue(JobPost.JobStatus status);

    Long countByRecruiterId(Long recruiterId);
}
