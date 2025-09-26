-- Recruiter Category Fields Data
-- This file contains all the dynamic fields for each recruiter category

USE icastar_db;

-- Get category IDs
SET @production_house_id = (SELECT id FROM recruiter_categories WHERE name = 'PRODUCTION_HOUSE');
SET @casting_director_id = (SELECT id FROM recruiter_categories WHERE name = 'CASTING_DIRECTOR');
SET @individual_id = (SELECT id FROM recruiter_categories WHERE name = 'INDIVIDUAL');

-- PRODUCTION HOUSE FIELDS
-- Basic Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@production_house_id, 'production_house_name', 'Name of Production House', 'TEXT', TRUE, TRUE, 1, 'Enter production house name', 'Official name of your production house or studio'),
(@production_house_id, 'recruiter_name', 'Name of Recruiter', 'TEXT', TRUE, TRUE, 2, 'Enter your full name', 'Your name as the recruiter/contact person'),
(@production_house_id, 'location', 'Location', 'TEXT', TRUE, TRUE, 3, 'Enter city, state, country', 'Where is your production house located?'),
(@production_house_id, 'years_running', 'How long have you been running this production house?', 'NUMBER', TRUE, TRUE, 4, 'Enter number of years', 'How many years has this production house been operational?');

-- Advanced Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@production_house_id, 'mobile_verification', 'Mobile Number Verification', 'BOOLEAN', TRUE, FALSE, 5, NULL, 'Has your mobile number been verified?'),
(@production_house_id, 'email', 'Email ID', 'EMAIL', TRUE, TRUE, 6, 'Enter your email address', 'Your official email address'),
(@production_house_id, 'id_proof', 'ID Proof', 'FILE', TRUE, FALSE, 7, 'Upload ID proof', 'Upload your government issued ID proof'),
(@production_house_id, 'registration_certificate', 'Registration Certificate of Production House', 'FILE', TRUE, FALSE, 8, 'Upload registration certificate', 'Upload the official registration certificate of your production house');

-- CASTING DIRECTOR FIELDS
-- Basic Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@casting_director_id, 'recruiter_name', 'Name of Recruiter', 'TEXT', TRUE, TRUE, 1, 'Enter your full name', 'Your name as the casting director'),
(@casting_director_id, 'location', 'Location', 'TEXT', TRUE, TRUE, 2, 'Enter city, state, country', 'Where are you based?'),
(@casting_director_id, 'production_house_name', 'Name of the Production House', 'TEXT', TRUE, TRUE, 3, 'Enter production house name', 'Name of the production house for whom you are casting');

-- Advanced Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@casting_director_id, 'mobile_verification', 'Mobile Number Verification', 'BOOLEAN', TRUE, FALSE, 4, NULL, 'Has your mobile number been verified?'),
(@casting_director_id, 'id_proof', 'Casting Director Card/ID Proof', 'FILE', TRUE, FALSE, 5, 'Upload casting director card', 'Upload your casting director card or ID proof'),
(@casting_director_id, 'face_verification', 'Face Verification', 'BOOLEAN', TRUE, FALSE, 6, NULL, 'Complete face verification for your profile');

-- INDIVIDUAL RECRUITER FIELDS
-- Basic Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@individual_id, 'name', 'Name', 'TEXT', TRUE, TRUE, 1, 'Enter your full name', 'Your full name'),
(@individual_id, 'location', 'Location', 'TEXT', TRUE, TRUE, 2, 'Enter city, state, country', 'Where are you based?'),
(@individual_id, 'email', 'Email ID', 'EMAIL', TRUE, TRUE, 3, 'Enter your email address', 'Your email address'),
(@individual_id, 'contact_details', 'Contact Details', 'TEXTAREA', TRUE, TRUE, 4, 'Enter additional contact information', 'Any additional contact details or information');

-- Advanced Information
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@individual_id, 'id_proof', 'ID Proof', 'FILE', TRUE, FALSE, 5, 'Upload any ID proof', 'Upload any government issued ID proof'),
(@individual_id, 'face_verification', 'Face Verification', 'BOOLEAN', TRUE, FALSE, 6, NULL, 'Complete face verification for your profile');

-- Add some additional optional fields for all categories
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@production_house_id, 'website', 'Company Website', 'URL', FALSE, TRUE, 9, 'Enter website URL', 'Your production house website'),
(@production_house_id, 'company_description', 'Company Description', 'TEXTAREA', FALSE, TRUE, 10, 'Describe your production house', 'Brief description of your production house and its work'),
(@production_house_id, 'specialization', 'Specialization', 'MULTI_SELECT', FALSE, TRUE, 11, 'Select specializations', 'What type of productions do you specialize in?'),
(@production_house_id, 'team_size', 'Team Size', 'SELECT', FALSE, TRUE, 12, 'Select team size', 'Approximate size of your production team');

-- Casting Director additional fields
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@casting_director_id, 'experience_years', 'Years of Experience', 'NUMBER', FALSE, TRUE, 7, 'Enter years of experience', 'How many years of casting experience do you have?'),
(@casting_director_id, 'specialization', 'Specialization', 'MULTI_SELECT', FALSE, TRUE, 8, 'Select specializations', 'What type of casting do you specialize in?'),
(@casting_director_id, 'portfolio_link', 'Portfolio Link', 'URL', FALSE, TRUE, 9, 'Enter portfolio URL', 'Link to your casting portfolio or previous work');

-- Individual Recruiter additional fields
INSERT INTO recruiter_category_fields (recruiter_category_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, placeholder, help_text) VALUES
(@individual_id, 'business_type', 'Type of Business', 'SELECT', FALSE, TRUE, 7, 'Select business type', 'What type of business or service do you provide?'),
(@individual_id, 'experience_years', 'Years of Experience', 'NUMBER', FALSE, TRUE, 8, 'Enter years of experience', 'How many years of experience do you have?'),
(@individual_id, 'services_offered', 'Services Offered', 'TEXTAREA', FALSE, TRUE, 9, 'Describe your services', 'What services do you offer to artists?');

-- Add options for SELECT and MULTI_SELECT fields
UPDATE recruiter_category_fields 
SET options = JSON_ARRAY('Film', 'Television', 'Web Series', 'Commercial', 'Music Video', 'Documentary', 'Short Film', 'Theater', 'Events', 'Other')
WHERE field_name = 'specialization' AND recruiter_category_id = @production_house_id;

UPDATE recruiter_category_fields 
SET options = JSON_ARRAY('Film', 'Television', 'Web Series', 'Commercial', 'Music Video', 'Documentary', 'Short Film', 'Theater', 'Events', 'Other')
WHERE field_name = 'specialization' AND recruiter_category_id = @casting_director_id;

UPDATE recruiter_category_fields 
SET options = JSON_ARRAY('1-5', '6-10', '11-20', '21-50', '51-100', '100+')
WHERE field_name = 'team_size' AND recruiter_category_id = @production_house_id;

UPDATE recruiter_category_fields 
SET options = JSON_ARRAY('Entrepreneur', 'Event Manager', 'Ad Agency', 'Freelancer', 'Consultant', 'Other')
WHERE field_name = 'business_type' AND recruiter_category_id = @individual_id;
