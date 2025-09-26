-- Job Posting Dynamic Fields System
-- This extends the existing job posting system to support dynamic fields

USE icastar_db;

-- Job Posting Fields table (similar to artist_type_fields and recruiter_category_fields)
CREATE TABLE job_posting_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    field_name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    field_type ENUM('TEXT', 'TEXTAREA', 'NUMBER', 'EMAIL', 'PHONE', 'URL', 'DATE', 'BOOLEAN', 'SELECT', 'MULTI_SELECT', 'CHECKBOX', 'RADIO', 'FILE', 'JSON') NOT NULL,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    is_searchable BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INT DEFAULT 0,
    validation_rules JSON,
    options JSON,
    placeholder VARCHAR(255),
    help_text VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Job Posting Field Values table (similar to artist_profile_fields and recruiter_profile_fields)
CREATE TABLE job_posting_field_values (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_post_id BIGINT NOT NULL,
    job_posting_field_id BIGINT NOT NULL,
    field_value TEXT,
    file_url VARCHAR(500),
    file_name VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_post_id) REFERENCES job_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (job_posting_field_id) REFERENCES job_posting_fields(id) ON DELETE CASCADE
);

-- Insert the job posting fields as requested
INSERT INTO job_posting_fields (field_name, display_name, field_type, is_required, is_searchable, sort_order, options, placeholder, help_text) VALUES
('artist_category', 'Category of Artist', 'MULTI_SELECT', TRUE, TRUE, 1, 
 JSON_ARRAY('Actor', 'Singer', 'Musician', 'Writer', 'Dancer', 'Makeup Artist', 'Standup Comedian', 'Photographer', 'Videographer', 'Director', 'Producer', 'Other'), 
 'Select artist categories', 
 'What type of artists are you looking for?'),

('project_type', 'Type of Your Project', 'SELECT', TRUE, TRUE, 2, 
 JSON_ARRAY('Film', 'Television', 'Web Series', 'Commercial', 'Music Video', 'Documentary', 'Short Film', 'Theater', 'Event', 'Photoshoot', 'Other'), 
 'Select project type', 
 'What type of project is this?'),

('director_name', 'Name of Director', 'TEXT', FALSE, TRUE, 3, 
 NULL, 
 'Enter director name', 
 'Name of the director for this project'),

('synopsis_concept', 'Synopsis/Concept', 'TEXTAREA', TRUE, TRUE, 4, 
 NULL, 
 'Describe the project concept...', 
 'Provide a brief synopsis or concept of your project'),

('role_responsibilities', 'Role and Responsibilities', 'TEXTAREA', TRUE, TRUE, 5, 
 NULL, 
 'Describe the role and responsibilities...', 
 'What will the artist be responsible for in this project?'),

('project_timeline', 'Project Timeline and Deadlines', 'TEXTAREA', TRUE, TRUE, 6, 
 NULL, 
 'Describe project timeline...', 
 'When does the project start and what are the key deadlines?'),

('conditions', 'Conditions', 'TEXTAREA', TRUE, FALSE, 7, 
 JSON_OBJECT('maxLength', 500), 
 'Enter project conditions (max 500 words)...', 
 'Any specific conditions, requirements, or terms for this project (maximum 500 words)');

-- Add some additional useful fields
INSERT INTO job_posting_fields (field_name, display_name, field_type, is_required, is_searchable, sort_order, options, placeholder, help_text) VALUES
('budget_range', 'Budget Range', 'SELECT', FALSE, TRUE, 8, 
 JSON_ARRAY('Under ₹10,000', '₹10,000 - ₹25,000', '₹25,000 - ₹50,000', '₹50,000 - ₹1,00,000', '₹1,00,000 - ₹2,50,000', '₹2,50,000 - ₹5,00,000', '₹5,00,000+', 'Negotiable'), 
 'Select budget range', 
 'What is the budget range for this project?'),

('location_requirements', 'Location Requirements', 'TEXT', FALSE, TRUE, 9, 
 NULL, 
 'Enter location requirements', 
 'Where will the work be performed? (City, Studio, On-location, etc.)'),

('experience_level', 'Experience Level Required', 'SELECT', FALSE, TRUE, 10, 
 JSON_ARRAY('Beginner', 'Intermediate', 'Experienced', 'Professional', 'Expert', 'Any Level'), 
 'Select experience level', 
 'What level of experience is required?'),

('equipment_provided', 'Equipment Provided', 'BOOLEAN', FALSE, FALSE, 11, 
 NULL, 
 NULL, 
 'Will you provide necessary equipment?'),

('travel_required', 'Travel Required', 'BOOLEAN', FALSE, TRUE, 12, 
 NULL, 
 NULL, 
 'Does this project require travel?'),

('audition_required', 'Audition Required', 'BOOLEAN', FALSE, TRUE, 13, 
 NULL, 
 NULL, 
 'Will there be an audition process?');
