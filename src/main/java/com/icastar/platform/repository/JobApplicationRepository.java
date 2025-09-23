package com.icastar.platform.repository;

import com.icastar.platform.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobPostId(Long jobPostId);

    List<JobApplication> findByArtistId(Long artistId);

    List<JobApplication> findByStatus(JobApplication.ApplicationStatus status);

    List<JobApplication> findByJobPostIdAndStatus(Long jobPostId, JobApplication.ApplicationStatus status);

    List<JobApplication> findByArtistIdAndStatus(Long artistId, JobApplication.ApplicationStatus status);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.recruiter.id = :recruiterId")
    Page<JobApplication> findByJobPostRecruiterId(@Param("recruiterId") Long recruiterId, Pageable pageable);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.id = :jobPostId")
    Page<JobApplication> findByJobPostId(@Param("jobPostId") Long jobPostId, Pageable pageable);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId")
    Page<JobApplication> findByArtistId(@Param("artistId") Long artistId, Pageable pageable);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId AND ja.jobPost.id = :jobPostId")
    boolean existsByArtistIdAndJobPostId(@Param("artistId") Long artistId, @Param("jobPostId") Long jobPostId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.appliedAt BETWEEN :startDate AND :endDate")
    List<JobApplication> findByAppliedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                               @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.recruiter.id = :recruiterId AND ja.status = :status")
    List<JobApplication> findByRecruiterAndStatus(@Param("recruiterId") Long recruiterId, 
                                                  @Param("status") JobApplication.ApplicationStatus status);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId AND ja.status = :status")
    List<JobApplication> findByArtistAndStatus(@Param("artistId") Long artistId, 
                                               @Param("status") JobApplication.ApplicationStatus status);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.id = :jobPostId ORDER BY ja.appliedAt DESC")
    List<JobApplication> findByJobPostOrderByAppliedAtDesc(@Param("jobPostId") Long jobPostId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId ORDER BY ja.appliedAt DESC")
    List<JobApplication> findByArtistOrderByAppliedAtDesc(@Param("artistId") Long artistId);

    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.status = :status")
    Long countByStatus(@Param("status") JobApplication.ApplicationStatus status);

    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.artist.id = :artistId")
    Long countByArtistId(@Param("artistId") Long artistId);

    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.jobPost.id = :jobPostId")
    Long countByJobPostId(@Param("jobPostId") Long jobPostId);

    @Query("SELECT COUNT(ja) FROM JobApplication ja WHERE ja.jobPost.recruiter.id = :recruiterId")
    Long countByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.recruiter.id = :recruiterId AND ja.status = 'PENDING'")
    List<JobApplication> findPendingApplicationsByRecruiter(@Param("recruiterId") Long recruiterId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId AND ja.status = 'PENDING'")
    List<JobApplication> findPendingApplicationsByArtist(@Param("artistId") Long artistId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.jobPost.recruiter.id = :recruiterId AND ja.status = 'ACCEPTED'")
    List<JobApplication> findAcceptedApplicationsByRecruiter(@Param("recruiterId") Long recruiterId);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.artist.id = :artistId AND ja.status = 'ACCEPTED'")
    List<JobApplication> findAcceptedApplicationsByArtist(@Param("artistId") Long artistId);
}
