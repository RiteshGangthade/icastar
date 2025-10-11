-- Simple fix: Add default value to job_post_id column to prevent the error
ALTER TABLE job_applications MODIFY COLUMN job_post_id BIGINT DEFAULT 1;
