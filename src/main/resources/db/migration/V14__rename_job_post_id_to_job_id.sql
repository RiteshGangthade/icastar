-- Rename job_post_id to job_id in job_applications table
-- This migration updates the existing job_applications table to use job_id instead of job_post_id

-- First, drop the existing foreign key constraint (if it exists)
-- Note: The constraint name might be different, so we'll handle this carefully
SET @constraint_name = (
    SELECT CONSTRAINT_NAME 
    FROM information_schema.KEY_COLUMN_USAGE 
    WHERE TABLE_SCHEMA = DATABASE() 
    AND TABLE_NAME = 'job_applications' 
    AND COLUMN_NAME = 'job_post_id' 
    AND REFERENCED_TABLE_NAME IS NOT NULL
);

SET @sql = IF(@constraint_name IS NOT NULL, 
    CONCAT('ALTER TABLE job_applications DROP FOREIGN KEY ', @constraint_name), 
    'SELECT "No foreign key constraint found" as message'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Rename the column from job_post_id to job_id
ALTER TABLE job_applications CHANGE COLUMN job_post_id job_id BIGINT NOT NULL;

-- Add the foreign key constraint to reference jobs table
ALTER TABLE job_applications ADD CONSTRAINT fk_job_applications_job_id 
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE;

-- Add the correct unique constraint: one artist can apply to multiple jobs
-- but cannot apply to the same job multiple times
ALTER TABLE job_applications ADD CONSTRAINT unique_job_artist_application 
    UNIQUE (job_id, artist_id);