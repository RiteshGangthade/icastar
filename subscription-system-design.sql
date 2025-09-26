-- Enhanced Subscription System Design for iCastar Platform
-- This file creates a comprehensive subscription system for both Artists and Recruiters

USE icastar_db;

-- Enhanced Subscription Plans table with role-specific plans
CREATE TABLE IF NOT EXISTS subscription_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    plan_type ENUM('FREE', 'BASIC', 'PREMIUM', 'PROFESSIONAL', 'ENTERPRISE') NOT NULL,
    user_role ENUM('ARTIST', 'RECRUITER', 'BOTH') NOT NULL DEFAULT 'BOTH',
    price DECIMAL(10,2) NOT NULL,
    billing_cycle ENUM('MONTHLY', 'YEARLY', 'ONE_TIME') NOT NULL,
    
    -- Artist-specific features
    max_auditions INT DEFAULT 0,
    unlimited_auditions BOOLEAN NOT NULL DEFAULT FALSE,
    max_applications INT DEFAULT 0,
    unlimited_applications BOOLEAN NOT NULL DEFAULT FALSE,
    max_portfolio_items INT DEFAULT 0,
    unlimited_portfolio BOOLEAN NOT NULL DEFAULT FALSE,
    profile_verification BOOLEAN NOT NULL DEFAULT FALSE,
    priority_verification BOOLEAN NOT NULL DEFAULT FALSE,
    featured_profile BOOLEAN NOT NULL DEFAULT FALSE,
    advanced_analytics BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Recruiter-specific features
    max_job_posts INT DEFAULT 0,
    unlimited_job_posts BOOLEAN NOT NULL DEFAULT FALSE,
    max_messages INT DEFAULT 0,
    unlimited_messages BOOLEAN NOT NULL DEFAULT FALSE,
    max_candidates_view INT DEFAULT 0,
    unlimited_candidates BOOLEAN NOT NULL DEFAULT FALSE,
    job_boost_credits INT DEFAULT 0,
    advanced_search BOOLEAN NOT NULL DEFAULT FALSE,
    candidate_verification BOOLEAN NOT NULL DEFAULT FALSE,
    priority_support BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Common features
    max_file_uploads INT DEFAULT 0,
    unlimited_uploads BOOLEAN NOT NULL DEFAULT FALSE,
    max_file_size_mb INT DEFAULT 10,
    custom_branding BOOLEAN NOT NULL DEFAULT FALSE,
    api_access BOOLEAN NOT NULL DEFAULT FALSE,
    white_label BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Plan metadata
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    is_popular BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Enhanced Subscriptions table
CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_plan_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED', 'TRIAL') NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    auto_renew BOOLEAN NOT NULL DEFAULT FALSE,
    amount_paid DECIMAL(10,2),
    payment_reference VARCHAR(255),
    
    -- Usage tracking
    auditions_used INT DEFAULT 0,
    applications_used INT DEFAULT 0,
    job_posts_used INT DEFAULT 0,
    messages_used INT DEFAULT 0,
    candidates_viewed INT DEFAULT 0,
    job_boosts_used INT DEFAULT 0,
    file_uploads_used INT DEFAULT 0,
    
    -- Trial information
    is_trial BOOLEAN NOT NULL DEFAULT FALSE,
    trial_end_date DATETIME,
    
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plans(id)
);

-- Plan Features table for detailed feature management
CREATE TABLE IF NOT EXISTS plan_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_plan_id BIGINT NOT NULL,
    feature_name VARCHAR(100) NOT NULL,
    feature_description TEXT,
    feature_type ENUM('LIMIT', 'BOOLEAN', 'NUMBER', 'TEXT') NOT NULL,
    feature_value VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plans(id) ON DELETE CASCADE
);

-- Usage Tracking table for detailed analytics
CREATE TABLE IF NOT EXISTS usage_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    feature_name VARCHAR(100) NOT NULL,
    usage_count INT DEFAULT 1,
    usage_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSON,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
);

-- Plan Upgrades/Downgrades tracking
CREATE TABLE IF NOT EXISTS subscription_changes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    from_plan_id BIGINT,
    to_plan_id BIGINT NOT NULL,
    change_type ENUM('UPGRADE', 'DOWNGRADE', 'RENEWAL', 'CANCELLATION') NOT NULL,
    change_reason TEXT,
    prorated_amount DECIMAL(10,2),
    effective_date DATETIME NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (from_plan_id) REFERENCES subscription_plans(id),
    FOREIGN KEY (to_plan_id) REFERENCES subscription_plans(id)
);
