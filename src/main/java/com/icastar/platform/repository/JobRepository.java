package com.icastar.platform.repository;

import com.icastar.platform.entity.Job;
import com.icastar.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // Find jobs by recruiter
    List<Job> findByRecruiter(User recruiter);
    Page<Job> findByRecruiter(User recruiter, Pageable pageable);

    // Find jobs by status
    List<Job> findByStatus(Job.JobStatus status);
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    // Find active jobs
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.applicationDeadline >= :currentDate")
    List<Job> findActiveJobs(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.applicationDeadline >= :currentDate")
    Page<Job> findActiveJobs(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    // Find jobs by location
    List<Job> findByLocationContainingIgnoreCase(String location);
    Page<Job> findByLocationContainingIgnoreCase(String location, Pageable pageable);

    // Find jobs by job type
    List<Job> findByJobType(Job.JobType jobType);
    Page<Job> findByJobType(Job.JobType jobType, Pageable pageable);

    // Find jobs by experience level
    List<Job> findByExperienceLevel(Job.ExperienceLevel experienceLevel);
    Page<Job> findByExperienceLevel(Job.ExperienceLevel experienceLevel, Pageable pageable);

    // Find remote jobs
    List<Job> findByIsRemoteTrue();
    Page<Job> findByIsRemoteTrue(Pageable pageable);

    // Find featured jobs
    List<Job> findByIsFeaturedTrue();
    Page<Job> findByIsFeaturedTrue(Pageable pageable);

    // Find urgent jobs
    List<Job> findByIsUrgentTrue();
    Page<Job> findByIsUrgentTrue(Pageable pageable);

    // Search jobs by title and description
    @Query("SELECT j FROM Job j WHERE " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.requirements) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Job> findBySearchTerm(@Param("searchTerm") String searchTerm);

    @Query("SELECT j FROM Job j WHERE " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.requirements) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Job> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Find jobs by budget range
    @Query("SELECT j FROM Job j WHERE j.budgetMin >= :minBudget AND j.budgetMax <= :maxBudget")
    List<Job> findByBudgetRange(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget);

    @Query("SELECT j FROM Job j WHERE j.budgetMin >= :minBudget AND j.budgetMax <= :maxBudget")
    Page<Job> findByBudgetRange(@Param("minBudget") BigDecimal minBudget, @Param("maxBudget") BigDecimal maxBudget, Pageable pageable);

    // Find jobs by skills (JSON search)
    @Query("SELECT j FROM Job j WHERE j.skillsRequired LIKE %:skill%")
    List<Job> findByRequiredSkill(@Param("skill") String skill);

    @Query("SELECT j FROM Job j WHERE j.skillsRequired LIKE %:skill%")
    Page<Job> findByRequiredSkill(@Param("skill") String skill, Pageable pageable);

    // Find jobs by tags
    @Query("SELECT j FROM Job j WHERE j.tags LIKE %:tag%")
    List<Job> findByTag(@Param("tag") String tag);

    @Query("SELECT j FROM Job j WHERE j.tags LIKE %:tag%")
    Page<Job> findByTag(@Param("tag") String tag, Pageable pageable);

    // Find most popular jobs (by applications count)
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.applicationsCount DESC")
    List<Job> findMostPopularJobs(Pageable pageable);

    // Find recently posted jobs
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.publishedAt DESC")
    List<Job> findRecentlyPostedJobs(Pageable pageable);

    // Find jobs expiring soon
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.applicationDeadline BETWEEN :startDate AND :endDate ORDER BY j.applicationDeadline ASC")
    List<Job> findJobsExpiringSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Count jobs by recruiter
    Long countByRecruiter(User recruiter);

    // Count jobs by status
    Long countByStatus(Job.JobStatus status);

    // Find jobs with comprehensive filters
    @Query("SELECT j FROM Job j WHERE " +
           "(:searchTerm IS NULL OR " +
           "LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(j.requirements) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:jobType IS NULL OR j.jobType = :jobType) AND " +
           "(:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:minPay IS NULL OR j.budgetMin >= :minPay) AND " +
           "(:maxPay IS NULL OR j.budgetMax <= :maxPay) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:isRemote IS NULL OR j.isRemote = :isRemote) AND " +
           "(:isUrgent IS NULL OR j.isUrgent = :isUrgent) AND " +
           "(:isFeatured IS NULL OR j.isFeatured = :isFeatured)")
    Page<Job> findJobsWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("jobType") Job.JobType jobType,
            @Param("experienceLevel") Job.ExperienceLevel experienceLevel,
            @Param("status") Job.JobStatus status,
            @Param("minPay") BigDecimal minPay,
            @Param("maxPay") BigDecimal maxPay,
            @Param("location") String location,
            @Param("isRemote") Boolean isRemote,
            @Param("isUrgent") Boolean isUrgent,
            @Param("isFeatured") Boolean isFeatured,
            Pageable pageable);
}
