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
    
    // Admin Job Management Methods
    Long countByIsActiveTrue();
    
    Long countByIsActiveFalse();
    
    Long countByApplicationDeadlineBefore(LocalDateTime deadline);
    
    Long countByCreatedAtAfter(LocalDateTime date);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.recruiter.id = :recruiterId")
    Page<JobPost> findByRecruiterIdQuery(@Param("recruiterId") Long recruiterId, Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.recruiter.recruiterCategory.id = :categoryId")
    Page<JobPost> findByRecruiterCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.recruiter.user.id = :userId")
    Page<JobPost> findByRecruiterUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.status = :status AND jp.isActive = :isActive")
    Page<JobPost> findByStatusAndIsActive(@Param("status") JobPost.JobStatus status, 
                                         @Param("isActive") Boolean isActive, 
                                         Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.title LIKE %:search% OR jp.description LIKE %:search%")
    Page<JobPost> findByTitleOrDescriptionContaining(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.createdAt BETWEEN :startDate AND :endDate")
    Page<JobPost> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate, 
                                        Pageable pageable);
    
    @Query("SELECT jp FROM JobPost jp WHERE jp.applicationDeadline BETWEEN :startDate AND :endDate")
    Page<JobPost> findByApplicationDeadlineBetween(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate, 
                                                   Pageable pageable);
}
