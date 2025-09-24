-- iCastar Platform - Default Data for Dancer Artist Type
-- This script adds the Dancer artist type with all its fields to the database

USE icastar_db;

-- Insert Dancer Artist Type
INSERT INTO artist_types (name, display_name, description, icon_url, is_active, sort_order, created_at, updated_at)
VALUES (
    'DANCER',
    'Dancer', 
    'Professional Dancers specializing in various dance styles including classical, contemporary, hip-hop, ballet, and more. Perfect for stage performances, music videos, events, and entertainment productions.',
    '/icons/dancer.png',
    TRUE,
    2,
    NOW(),
    NOW()
);

-- Get the Dancer artist type ID (assuming it's ID 2, but we'll use a variable)
SET @dancer_type_id = LAST_INSERT_ID();

-- Insert Dancer-specific fields
INSERT INTO artist_type_fields (
    artist_type_id, 
    field_name, 
    display_name, 
    field_type, 
    is_required, 
    is_searchable, 
    sort_order, 
    validation_rules, 
    options, 
    placeholder, 
    help_text, 
    is_active, 
    created_at, 
    updated_at
) VALUES 
-- Dance Styles (Required, Searchable)
(
    @dancer_type_id,
    'dance_styles',
    'Dance Styles',
    'MULTI_SELECT',
    TRUE,
    TRUE,
    1,
    NULL,
    JSON_ARRAY(
        'Classical Ballet',
        'Contemporary',
        'Hip-Hop',
        'Jazz',
        'Bharatanatyam',
        'Kathak',
        'Kuchipudi',
        'Odissi',
        'Mohiniyattam',
        'Manipuri',
        'Salsa',
        'Bachata',
        'Tango',
        'Waltz',
        'Bollywood',
        'Folk Dance',
        'Modern Dance',
        'Lyrical',
        'Tap Dance',
        'Breakdance',
        'Street Dance',
        'Ballroom',
        'Latin Dance',
        'Flamenco',
        'Belly Dance',
        'Acrobatic Dance',
        'Aerial Dance',
        'Pole Dance',
        'Other'
    ),
    NULL,
    'Select all dance styles you specialize in. This helps recruiters find the right dancer for their project.',
    TRUE,
    NOW(),
    NOW()
),

-- Training Background (Required, Searchable)
(
    @dancer_type_id,
    'training_background',
    'Training Background',
    'TEXTAREA',
    TRUE,
    TRUE,
    2,
    JSON_OBJECT('maxLength', 1000),
    NULL,
    'Describe your dance training, education, and background...',
    'Provide details about your formal dance training, workshops, masterclasses, and educational background.',
    TRUE,
    NOW(),
    NOW()
),

-- Performance Experience (Required, Searchable)
(
    @dancer_type_id,
    'performance_experience',
    'Performance Experience',
    'SELECT',
    TRUE,
    TRUE,
    3,
    NULL,
    JSON_ARRAY(
        'Beginner (0-1 years)',
        'Intermediate (1-3 years)',
        'Advanced (3-7 years)',
        'Professional (7+ years)',
        'Expert (10+ years)'
    ),
    NULL,
    'Select your level of performance experience in years.',
    TRUE,
    NOW(),
    NOW()
),

-- Choreography Skills (Optional, Searchable)
(
    @dancer_type_id,
    'choreography_skills',
    'Choreography Skills',
    'BOOLEAN',
    FALSE,
    TRUE,
    4,
    NULL,
    NULL,
    NULL,
    'Can you create and teach choreography to other dancers?',
    TRUE,
    NOW(),
    NOW()
),

-- Teaching Experience (Optional, Searchable)
(
    @dancer_type_id,
    'teaching_experience',
    'Teaching Experience',
    'BOOLEAN',
    FALSE,
    TRUE,
    5,
    NULL,
    NULL,
    NULL,
    'Do you have experience teaching dance to others?',
    TRUE,
    NOW(),
    NOW()
),

-- Performance Videos (Required, Not Searchable)
(
    @dancer_type_id,
    'performance_videos',
    'Performance Videos',
    'FILE',
    TRUE,
    FALSE,
    6,
    JSON_OBJECT('maxSize', '50MB', 'allowedTypes', JSON_ARRAY('mp4', 'mov', 'avi', 'mkv')),
    NULL,
    NULL,
    'Upload videos showcasing your dance performances. This is essential for recruiters to evaluate your skills.',
    TRUE,
    NOW(),
    NOW()
),

-- Costume Availability (Optional, Not Searchable)
(
    @dancer_type_id,
    'costume_availability',
    'Costume Availability',
    'BOOLEAN',
    FALSE,
    FALSE,
    7,
    NULL,
    NULL,
    NULL,
    'Do you have your own dance costumes and accessories?',
    TRUE,
    NOW(),
    NOW()
),

-- Flexibility Level (Optional, Searchable)
(
    @dancer_type_id,
    'flexibility_level',
    'Flexibility Level',
    'SELECT',
    FALSE,
    TRUE,
    8,
    NULL,
    JSON_ARRAY(
        'Basic',
        'Intermediate',
        'Advanced',
        'Extreme'
    ),
    NULL,
    'What is your current flexibility level?',
    TRUE,
    NOW(),
    NOW()
),

-- Performance Types (Optional, Searchable)
(
    @dancer_type_id,
    'performance_types',
    'Performance Types',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    9,
    NULL,
    JSON_ARRAY(
        'Stage Performance',
        'Music Videos',
        'Wedding Performances',
        'Corporate Events',
        'Fashion Shows',
        'Theater',
        'Film/TV',
        'Competitions',
        'Festivals',
        'Private Events',
        'Online Performances',
        'Street Performances',
        'Other'
    ),
    NULL,
    'What types of performances have you done or are interested in?',
    TRUE,
    NOW(),
    NOW()
),

-- Awards and Recognition (Optional, Not Searchable)
(
    @dancer_type_id,
    'awards_recognition',
    'Awards & Recognition',
    'TEXTAREA',
    FALSE,
    FALSE,
    10,
    JSON_OBJECT('maxLength', 500),
    NULL,
    'List any awards, recognition, or notable achievements...',
    'Mention any dance competitions won, awards received, or special recognition in your dance career.',
    TRUE,
    NOW(),
    NOW()
),

-- Availability (Optional, Searchable)
(
    @dancer_type_id,
    'availability',
    'Availability',
    'SELECT',
    FALSE,
    TRUE,
    11,
    NULL,
    JSON_ARRAY(
        'Full-time',
        'Part-time',
        'Weekends Only',
        'Evenings Only',
        'Flexible',
        'Project-based',
        'On-demand'
    ),
    NULL,
    'What is your general availability for dance projects?',
    TRUE,
    NOW(),
    NOW()
),

-- Travel Willingness (Optional, Searchable)
(
    @dancer_type_id,
    'travel_willingness',
    'Travel Willingness',
    'SELECT',
    FALSE,
    TRUE,
    12,
    NULL,
    JSON_ARRAY(
        'Local Only',
        'Within State',
        'Within Country',
        'International',
        'Anywhere'
    ),
    NULL,
    'How far are you willing to travel for dance projects?',
    TRUE,
    NOW(),
    NOW()
);

-- Insert some sample dancer profiles for testing
-- First, create a test user
INSERT INTO users (email, mobile, password, role, status, is_verified, is_active, created_at, updated_at)
VALUES (
    'test.dancer@example.com',
    '+919876543210',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- password: password123
    'ARTIST',
    'ACTIVE',
    TRUE,
    TRUE,
    NOW(),
    NOW()
);

SET @test_user_id = LAST_INSERT_ID();

-- Create a sample dancer profile
INSERT INTO artist_profiles (
    user_id,
    artist_type_id,
    first_name,
    last_name,
    stage_name,
    bio,
    date_of_birth,
    gender,
    location,
    profile_image_url,
    portfolio_urls,
    skills,
    experience_years,
    hourly_rate,
    is_verified_badge,
    is_active,
    created_at,
    updated_at
) VALUES (
    @test_user_id,
    @dancer_type_id,
    'Priya',
    'Sharma',
    'Priya the Dancer',
    'Professional classical and contemporary dancer with 8 years of experience. Specialized in Bharatanatyam and Bollywood dance styles. Performed at various cultural events, weddings, and corporate functions across India.',
    '1995-06-15',
    'FEMALE',
    'Mumbai, Maharashtra, India',
    'https://example.com/profile-images/priya-sharma.jpg',
    JSON_ARRAY(
        'https://example.com/portfolio/priya-bharatanatyam.mp4',
        'https://example.com/portfolio/priya-bollywood.mp4',
        'https://example.com/portfolio/priya-contemporary.mp4'
    ),
    JSON_ARRAY(
        'Bharatanatyam',
        'Bollywood Dance',
        'Contemporary',
        'Classical Dance',
        'Choreography',
        'Teaching'
    ),
    8,
    3000.00,
    FALSE,
    TRUE,
    NOW(),
    NOW()
);

SET @test_artist_id = LAST_INSERT_ID();

-- Insert dynamic field values for the sample dancer
-- Get field IDs for the dancer type
SET @dance_styles_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'dance_styles');
SET @training_background_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'training_background');
SET @performance_experience_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'performance_experience');
SET @choreography_skills_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'choreography_skills');
SET @teaching_experience_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'teaching_experience');
SET @flexibility_level_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'flexibility_level');
SET @performance_types_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'performance_types');
SET @awards_recognition_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'awards_recognition');
SET @availability_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'availability');
SET @travel_willingness_field_id = (SELECT id FROM artist_type_fields WHERE artist_type_id = @dancer_type_id AND field_name = 'travel_willingness');

-- Insert dynamic field values
INSERT INTO artist_profile_fields (
    artist_profile_id,
    artist_type_field_id,
    field_value,
    created_at,
    updated_at
) VALUES 
(
    @test_artist_id,
    @dance_styles_field_id,
    'Bharatanatyam,Bollywood Dance,Contemporary,Classical Ballet',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @training_background_field_id,
    'Completed 8 years of formal Bharatanatyam training under Guru Smt. Lakshmi Narayanan. Attended workshops by renowned dancers including Leela Samson and Malavika Sarukkai. Graduated from Kalakshetra Foundation with distinction in classical dance.',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @performance_experience_field_id,
    'Professional (7+ years)',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @choreography_skills_field_id,
    'true',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @teaching_experience_field_id,
    'true',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @flexibility_level_field_id,
    'Advanced',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @performance_types_field_id,
    'Stage Performance,Wedding Performances,Corporate Events,Festivals,Theater',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @awards_recognition_field_id,
    'Winner of National Dance Competition 2022, Best Performer Award at Mumbai Cultural Festival 2021, Featured in Times of India for outstanding classical dance performance.',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @availability_field_id,
    'Flexible',
    NOW(),
    NOW()
),
(
    @test_artist_id,
    @travel_willingness_field_id,
    'Within Country',
    NOW(),
    NOW()
);

-- Create another sample dancer profile
INSERT INTO users (email, mobile, password, role, status, is_verified, is_active, created_at, updated_at)
VALUES (
    'hiphop.dancer@example.com',
    '+919876543211',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- password: password123
    'ARTIST',
    'ACTIVE',
    TRUE,
    TRUE,
    NOW(),
    NOW()
);

SET @test_user_id_2 = LAST_INSERT_ID();

INSERT INTO artist_profiles (
    user_id,
    artist_type_id,
    first_name,
    last_name,
    stage_name,
    bio,
    date_of_birth,
    gender,
    location,
    profile_image_url,
    portfolio_urls,
    skills,
    experience_years,
    hourly_rate,
    is_verified_badge,
    is_active,
    created_at,
    updated_at
) VALUES (
    @test_user_id_2,
    @dancer_type_id,
    'Rahul',
    'Kumar',
    'Rahul B-Boy',
    'Professional hip-hop and breakdance artist with 6 years of experience. Specialized in breaking, popping, and locking. Performed at various street dance competitions and music videos. Passionate about urban dance culture.',
    '1998-03-22',
    'MALE',
    'Delhi, India',
    'https://example.com/profile-images/rahul-kumar.jpg',
    JSON_ARRAY(
        'https://example.com/portfolio/rahul-breaking.mp4',
        'https://example.com/portfolio/rahul-popping.mp4',
        'https://example.com/portfolio/rahul-battles.mp4'
    ),
    JSON_ARRAY(
        'Breaking',
        'Popping',
        'Locking',
        'Hip-Hop',
        'Street Dance',
        'Freestyle'
    ),
    6,
    2500.00,
    FALSE,
    TRUE,
    NOW(),
    NOW()
);

SET @test_artist_id_2 = LAST_INSERT_ID();

-- Insert dynamic field values for the second sample dancer
INSERT INTO artist_profile_fields (
    artist_profile_id,
    artist_type_field_id,
    field_value,
    created_at,
    updated_at
) VALUES 
(
    @test_artist_id_2,
    @dance_styles_field_id,
    'Hip-Hop,Breakdance,Popping,Locking,Street Dance',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @training_background_field_id,
    'Self-taught hip-hop dancer with 6 years of experience. Attended workshops by international B-boys including Lilou and Roxrite. Participated in various street dance battles and competitions across India.',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @performance_experience_field_id,
    'Advanced (3-7 years)',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @choreography_skills_field_id,
    'true',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @teaching_experience_field_id,
    'true',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @flexibility_level_field_id,
    'Extreme',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @performance_types_field_id,
    'Music Videos,Competitions,Festivals,Street Performances,Corporate Events',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @awards_recognition_field_id,
    'Winner of Delhi Street Dance Championship 2023, Runner-up at National Hip-Hop Competition 2022, Featured in multiple music videos.',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @availability_field_id,
    'Part-time',
    NOW(),
    NOW()
),
(
    @test_artist_id_2,
    @travel_willingness_field_id,
    'Within State',
    NOW(),
    NOW()
);

-- Display success message
SELECT 'Dancer artist type and sample data inserted successfully!' as message;

-- Display the created artist type
SELECT 
    at.id,
    at.name,
    at.display_name,
    at.description,
    at.is_active,
    at.sort_order,
    COUNT(atf.id) as field_count
FROM artist_types at
LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id
WHERE at.name = 'DANCER'
GROUP BY at.id, at.name, at.display_name, at.description, at.is_active, at.sort_order;

-- Display the fields created
SELECT 
    atf.id,
    atf.field_name,
    atf.display_name,
    atf.field_type,
    atf.is_required,
    atf.is_searchable,
    atf.sort_order,
    atf.help_text
FROM artist_type_fields atf
JOIN artist_types at ON atf.artist_type_id = at.id
WHERE at.name = 'DANCER'
ORDER BY atf.sort_order;

-- Display sample profiles created
SELECT 
    ap.id,
    ap.first_name,
    ap.last_name,
    ap.stage_name,
    ap.location,
    ap.experience_years,
    ap.hourly_rate,
    u.email
FROM artist_profiles ap
JOIN users u ON ap.user_id = u.id
JOIN artist_types at ON ap.artist_type_id = at.id
WHERE at.name = 'DANCER';

