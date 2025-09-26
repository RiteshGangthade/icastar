-- Recruiter Categories Dynamic System
-- This extends the existing recruiter system to support different recruiter types

USE icastar_db;

-- Recruiter Categories table (similar to artist_types)
CREATE TABLE recruiter_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Recruiter Category Fields table (similar to artist_type_fields)
CREATE TABLE recruiter_category_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_category_id BIGINT NOT NULL,
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
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_category_id) REFERENCES recruiter_categories(id) ON DELETE CASCADE
);

-- Recruiter Profile Fields table (similar to artist_profile_fields)
CREATE TABLE recruiter_profile_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_profile_id BIGINT NOT NULL,
    recruiter_category_field_id BIGINT NOT NULL,
    field_value TEXT,
    file_url VARCHAR(500),
    file_name VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_profile_id) REFERENCES recruiter_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (recruiter_category_field_id) REFERENCES recruiter_category_fields(id) ON DELETE CASCADE
);

-- Add recruiter_category_id to existing recruiter_profiles table
ALTER TABLE recruiter_profiles 
ADD COLUMN recruiter_category_id BIGINT,
ADD FOREIGN KEY (recruiter_category_id) REFERENCES recruiter_categories(id) ON DELETE SET NULL;

-- Insert the three recruiter categories
INSERT INTO recruiter_categories (name, display_name, description, icon_url, sort_order) VALUES
('PRODUCTION_HOUSE', 'Production House', 'Film and TV production companies, studios, and production houses', '/icons/production-house.png', 1),
('CASTING_DIRECTOR', 'Casting Director', 'Professional casting directors working for production houses', '/icons/casting-director.png', 2),
('INDIVIDUAL', 'Individual Recruiter', 'Entrepreneurs, event managers, ad agencies, and individual recruiters', '/icons/individual-recruiter.png', 3);
