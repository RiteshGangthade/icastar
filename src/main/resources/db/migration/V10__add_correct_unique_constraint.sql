-- Add the correct unique constraint: one artist can apply to multiple jobs
-- but cannot apply to the same job multiple times
ALTER TABLE job_applications ADD CONSTRAINT unique_job_artist_application 
    UNIQUE (job_id, artist_id);
