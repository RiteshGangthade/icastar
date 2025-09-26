-- Subscription Plans Data for iCastar Platform
-- This file contains all the subscription plans for Artists and Recruiters

USE icastar_db;

-- Clear existing plans and insert new comprehensive plans
DELETE FROM subscription_plans;

-- ARTIST PLANS
INSERT INTO subscription_plans (name, description, plan_type, user_role, price, billing_cycle, max_auditions, max_applications, max_portfolio_items, profile_verification, featured_profile, advanced_analytics, max_file_uploads, max_file_size_mb, sort_order) VALUES
-- Free Artist Plan
('Artist Free', 'Basic plan for new artists to get started', 'FREE', 'ARTIST', 0.00, 'MONTHLY', 5, 10, 5, FALSE, FALSE, FALSE, 10, 10, 1),

-- Basic Artist Plan
('Artist Basic', 'Essential features for growing artists', 'BASIC', 'ARTIST', 299.00, 'MONTHLY', 25, 50, 20, TRUE, FALSE, FALSE, 50, 25, 2),

-- Premium Artist Plan
('Artist Premium', 'Advanced features for professional artists', 'PREMIUM', 'ARTIST', 599.00, 'MONTHLY', 100, 200, 100, TRUE, TRUE, TRUE, 200, 50, 3),

-- Professional Artist Plan
('Artist Professional', 'Complete toolkit for established artists', 'PROFESSIONAL', 'ARTIST', 999.00, 'MONTHLY', 500, 1000, 500, TRUE, TRUE, TRUE, 1000, 100, 4),

-- Enterprise Artist Plan
('Artist Enterprise', 'Unlimited access for top-tier artists', 'ENTERPRISE', 'ARTIST', 1999.00, 'MONTHLY', NULL, NULL, NULL, TRUE, TRUE, TRUE, NULL, 500, 5);

-- Update unlimited fields for Enterprise plan
UPDATE subscription_plans SET 
    unlimited_auditions = TRUE, 
    unlimited_applications = TRUE, 
    unlimited_portfolio = TRUE, 
    unlimited_uploads = TRUE,
    custom_branding = TRUE,
    api_access = TRUE
WHERE name = 'Artist Enterprise';

-- RECRUITER PLANS
INSERT INTO subscription_plans (name, description, plan_type, user_role, price, billing_cycle, max_job_posts, max_messages, max_candidates_view, job_boost_credits, advanced_search, candidate_verification, priority_support, max_file_uploads, max_file_size_mb, sort_order) VALUES
-- Free Recruiter Plan
('Recruiter Free', 'Basic plan for new recruiters', 'FREE', 'RECRUITER', 0.00, 'MONTHLY', 2, 10, 20, 0, FALSE, FALSE, FALSE, 10, 10, 6),

-- Basic Recruiter Plan
('Recruiter Basic', 'Essential features for growing recruiters', 'BASIC', 'RECRUITER', 499.00, 'MONTHLY', 10, 50, 100, 2, FALSE, FALSE, FALSE, 50, 25, 7),

-- Premium Recruiter Plan
('Recruiter Premium', 'Advanced features for professional recruiters', 'PREMIUM', 'RECRUITER', 999.00, 'MONTHLY', 50, 200, 500, 5, TRUE, TRUE, TRUE, 200, 50, 8),

-- Professional Recruiter Plan
('Recruiter Professional', 'Complete toolkit for established recruiters', 'PROFESSIONAL', 'RECRUITER', 1999.00, 'MONTHLY', 200, 1000, 2000, 10, TRUE, TRUE, TRUE, 1000, 100, 9),

-- Enterprise Recruiter Plan
('Recruiter Enterprise', 'Unlimited access for large organizations', 'ENTERPRISE', 'RECRUITER', 4999.00, 'MONTHLY', NULL, NULL, NULL, 25, TRUE, TRUE, TRUE, NULL, 500, 10);

-- Update unlimited fields for Enterprise plan
UPDATE subscription_plans SET 
    unlimited_job_posts = TRUE, 
    unlimited_messages = TRUE, 
    unlimited_candidates = TRUE, 
    unlimited_uploads = TRUE,
    custom_branding = TRUE,
    api_access = TRUE,
    white_label = TRUE
WHERE name = 'Recruiter Enterprise';

-- UNIFIED PLANS (for users who want both artist and recruiter features)
INSERT INTO subscription_plans (name, description, plan_type, user_role, price, billing_cycle, max_auditions, max_applications, max_job_posts, max_messages, max_candidates_view, job_boost_credits, advanced_search, profile_verification, featured_profile, priority_support, max_file_uploads, max_file_size_mb, sort_order) VALUES
-- Unified Basic Plan
('Unified Basic', 'Basic features for both artists and recruiters', 'BASIC', 'BOTH', 699.00, 'MONTHLY', 15, 30, 5, 30, 50, 1, FALSE, FALSE, FALSE, FALSE, 30, 25, 11),

-- Unified Premium Plan
('Unified Premium', 'Advanced features for both roles', 'PREMIUM', 'BOTH', 1299.00, 'MONTHLY', 50, 100, 25, 100, 250, 3, TRUE, TRUE, TRUE, TRUE, 100, 50, 12),

-- Unified Professional Plan
('Unified Professional', 'Complete toolkit for professionals', 'PROFESSIONAL', 'BOTH', 2499.00, 'MONTHLY', 250, 500, 100, 500, 1000, 8, TRUE, TRUE, TRUE, TRUE, 500, 100, 13),

-- Unified Enterprise Plan
('Unified Enterprise', 'Unlimited access for organizations', 'ENTERPRISE', 'BOTH', 6999.00, 'MONTHLY', NULL, NULL, NULL, NULL, NULL, 20, TRUE, TRUE, TRUE, TRUE, NULL, 500, 14);

-- Update unlimited fields for Unified Enterprise plan
UPDATE subscription_plans SET 
    unlimited_auditions = TRUE, 
    unlimited_applications = TRUE, 
    unlimited_job_posts = TRUE, 
    unlimited_messages = TRUE, 
    unlimited_candidates = TRUE, 
    unlimited_uploads = TRUE,
    custom_branding = TRUE,
    api_access = TRUE,
    white_label = TRUE
WHERE name = 'Unified Enterprise';

-- Mark popular plans
UPDATE subscription_plans SET is_popular = TRUE WHERE name IN ('Artist Premium', 'Recruiter Premium', 'Unified Premium');

-- Mark featured plans
UPDATE subscription_plans SET is_featured = TRUE WHERE name IN ('Artist Professional', 'Recruiter Professional', 'Unified Professional');

-- Add detailed features for each plan
INSERT INTO plan_features (subscription_plan_id, feature_name, feature_description, feature_type, feature_value) VALUES
-- Artist Free Plan Features
((SELECT id FROM subscription_plans WHERE name = 'Artist Free'), 'Basic Profile', 'Create and manage basic artist profile', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Free'), 'Limited Applications', 'Apply to up to 10 jobs per month', 'LIMIT', '10'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Free'), 'Basic Search', 'Search and filter job opportunities', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Free'), 'Email Support', 'Basic email support', 'BOOLEAN', 'true'),

-- Artist Premium Plan Features
((SELECT id FROM subscription_plans WHERE name = 'Artist Premium'), 'Advanced Analytics', 'Detailed performance analytics', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Premium'), 'Featured Profile', 'Get featured in search results', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Premium'), 'Priority Support', '24/7 priority customer support', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Premium'), 'Advanced Portfolio', 'Upload up to 100 portfolio items', 'LIMIT', '100'),

-- Recruiter Premium Plan Features
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Premium'), 'Advanced Search', 'Advanced candidate search and filtering', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Premium'), 'Candidate Verification', 'Verify candidate credentials', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Premium'), 'Job Boost', 'Boost job posts for better visibility', 'LIMIT', '5'),
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Premium'), 'Priority Support', '24/7 priority customer support', 'BOOLEAN', 'true'),

-- Enterprise Plan Features
((SELECT id FROM subscription_plans WHERE name = 'Artist Enterprise'), 'Custom Branding', 'Custom branding and white-label options', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Artist Enterprise'), 'API Access', 'Full API access for integrations', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Enterprise'), 'White Label', 'Complete white-label solution', 'BOOLEAN', 'true'),
((SELECT id FROM subscription_plans WHERE name = 'Recruiter Enterprise'), 'Dedicated Manager', 'Dedicated account manager', 'BOOLEAN', 'true');
