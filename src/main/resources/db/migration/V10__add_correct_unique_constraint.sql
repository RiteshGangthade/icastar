-- Add the correct unique constraint: one artist can apply to multiple jobs
-- but cannot apply to the same job multiple times
-- Note: Using job_post_id as per V1 schema
ALTER TABLE job_applications ADD CONSTRAINT unique_job_artist_application 
    UNIQUE (job_post_id, artist_id);
