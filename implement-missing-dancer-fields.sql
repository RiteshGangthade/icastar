-- iCastar Platform - Implement Missing Dancer Fields
-- This script adds the missing critical fields for Dancer artist type

USE icastar_db;

-- Get the Dancer artist type ID
SET @dancer_type_id = (SELECT id FROM artist_types WHERE name = 'DANCER');

-- Check if Dancer type exists
SELECT 
    CASE 
        WHEN @dancer_type_id IS NOT NULL THEN 'Dancer type found'
        ELSE 'Dancer type not found - please run dancer-default-data.sql first'
    END as status;

-- Add Missing Critical Fields for Dancer Type

-- 1. ID Proof Upload (Critical)
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
) VALUES (
    @dancer_type_id,
    'id_proof_file',
    'ID Proof Document',
    'FILE',
    TRUE,
    FALSE,
    13,
    JSON_OBJECT(
        'maxSize', '5MB',
        'allowedTypes', JSON_ARRAY('pdf', 'jpg', 'jpeg', 'png'),
        'required', true
    ),
    NULL,
    NULL,
    'Upload your Aadhar Card, PAN Card, or Voter ID for identity verification. This helps build trust with recruiters and clients.',
    TRUE,
    NOW(),
    NOW()
);

-- 2. Face Verification (Important)
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
) VALUES (
    @dancer_type_id,
    'face_verified',
    'Face Verification',
    'BOOLEAN',
    FALSE,
    TRUE,
    14,
    NULL,
    NULL,
    NULL,
    'Complete face verification to enhance your profile visibility and build trust with recruiters. This is optional but recommended.',
    TRUE,
    NOW(),
    NOW()
);

-- 3. Enhanced Personal Style (Nice to have)
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
) VALUES (
    @dancer_type_id,
    'personal_style_approach',
    'Personal Style & Approach',
    'TEXTAREA',
    FALSE,
    TRUE,
    15,
    JSON_OBJECT(
        'maxLength', 1000,
        'minLength', 50
    ),
    NULL,
    'Describe your unique style and philosophy in dance...',
    'Share your personal approach to dance, your artistic philosophy, and what makes your style unique. This helps recruiters understand your artistic vision.',
    TRUE,
    NOW(),
    NOW()
);

-- 4. Detailed Choreographic Techniques (Professional enhancement)
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
) VALUES (
    @dancer_type_id,
    'choreographic_techniques',
    'Choreographic Techniques',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    16,
    NULL,
    JSON_ARRAY(
        'Classical Choreography',
        'Contemporary Choreography',
        'Fusion Choreography',
        'Group Choreography',
        'Solo Choreography',
        'Storytelling Through Dance',
        'Abstract Movement',
        'Traditional Folk Choreography',
        'Modern Interpretive',
        'Commercial Choreography',
        'Theatrical Choreography',
        'Cultural Fusion',
        'Experimental Movement',
        'Collaborative Choreography',
        'Educational Choreography',
        'Competition Choreography',
        'Wedding Choreography',
        'Film/TV Choreography',
        'Stage Production',
        'Event Choreography'
    ),
    NULL,
    'Select all choreographic techniques and methods you specialize in. This helps recruiters find the right choreographer for their project.',
    TRUE,
    NOW(),
    NOW()
);

-- 5. Movement Creation Approach (Professional detail)
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
) VALUES (
    @dancer_type_id,
    'movement_creation_approach',
    'Movement Creation Approach',
    'TEXTAREA',
    FALSE,
    FALSE,
    17,
    JSON_OBJECT(
        'maxLength', 800,
        'minLength', 30
    ),
    NULL,
    'Describe how you approach creating movement for individuals and groups...',
    'Explain your methodology for creating choreography, how you work with different skill levels, and your approach to teaching movement to others.',
    TRUE,
    NOW(),
    NOW()
);

-- 6. Stage Direction & Production Skills (Leadership skills)
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
) VALUES (
    @dancer_type_id,
    'stage_direction_skills',
    'Stage Direction & Production Skills',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    18,
    NULL,
    JSON_ARRAY(
        'Stage Direction',
        'Performance Management',
        'Rehearsal Organization',
        'Production Coordination',
        'Team Leadership',
        'Event Planning',
        'Costume Coordination',
        'Lighting Design',
        'Sound Coordination',
        'Venue Management',
        'Crew Management',
        'Timeline Planning',
        'Budget Management',
        'Client Communication',
        'Technical Coordination',
        'Safety Management',
        'Quality Control',
        'Crisis Management',
        'Multi-location Coordination',
        'International Productions'
    ),
    NULL,
    'Select your stage direction and production management skills. These skills are valuable for larger productions and events.',
    TRUE,
    NOW(),
    NOW()
);

-- 7. Teaching & Mentorship Experience (Enhanced)
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
) VALUES (
    @dancer_type_id,
    'teaching_mentorship_details',
    'Teaching & Mentorship Experience',
    'TEXTAREA',
    FALSE,
    TRUE,
    19,
    JSON_OBJECT(
        'maxLength', 1200,
        'minLength', 50
    ),
    NULL,
    'Describe your teaching and mentorship experience...',
    'Share details about workshops, classes, or mentorship programs you have conducted. Include information about your teaching methodology and student success stories.',
    TRUE,
    NOW(),
    NOW()
);

-- 8. Collaboration Preferences (Professional networking)
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
) VALUES (
    @dancer_type_id,
    'collaboration_preferences',
    'Collaboration Preferences',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    20,
    NULL,
    JSON_ARRAY(
        'Solo Performances',
        'Group Performances',
        'Duet Performances',
        'Cross-genre Collaborations',
        'International Collaborations',
        'Cultural Exchange Programs',
        'Educational Workshops',
        'Community Outreach',
        'Charity Performances',
        'Corporate Events',
        'Wedding Performances',
        'Festival Performances',
        'Competition Participation',
        'Mentorship Programs',
        'Online Collaborations',
        'Film/TV Projects',
        'Theater Productions',
        'Music Video Projects',
        'Fashion Shows',
        'Brand Collaborations'
    ),
    NULL,
    'Select the types of collaborations and performances you are interested in. This helps match you with relevant opportunities.',
    TRUE,
    NOW(),
    NOW()
);

-- 9. Performance Venue Experience (Professional experience)
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
) VALUES (
    @dancer_type_id,
    'venue_experience',
    'Performance Venue Experience',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    21,
    NULL,
    JSON_ARRAY(
        'Theaters',
        'Concert Halls',
        'Stadiums',
        'Outdoor Venues',
        'Wedding Venues',
        'Corporate Events',
        'Festivals',
        'Television Studios',
        'Film Sets',
        'Dance Studios',
        'Community Centers',
        'Schools & Colleges',
        'Hotels & Resorts',
        'Shopping Malls',
        'Cultural Centers',
        'International Venues',
        'Online Platforms',
        'Private Events',
        'Government Functions',
        'Charity Events'
    ),
    NULL,
    'Select the types of venues where you have performed. This demonstrates your versatility and experience.',
    TRUE,
    NOW(),
    NOW()
);

-- 10. Social Media Presence (Modern requirement)
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
) VALUES (
    @dancer_type_id,
    'social_media_links',
    'Social Media Presence',
    'JSON',
    FALSE,
    FALSE,
    22,
    JSON_OBJECT(
        'schema', JSON_OBJECT(
            'type', 'object',
            'properties', JSON_OBJECT(
                'instagram', JSON_OBJECT('type', 'string', 'format', 'url'),
                'youtube', JSON_OBJECT('type', 'string', 'format', 'url'),
                'tiktok', JSON_OBJECT('type', 'string', 'format', 'url'),
                'facebook', JSON_OBJECT('type', 'string', 'format', 'url'),
                'twitter', JSON_OBJECT('type', 'string', 'format', 'url'),
                'linkedin', JSON_OBJECT('type', 'string', 'format', 'url')
            )
        )
    ),
    NULL,
    '{"instagram": "https://instagram.com/yourhandle", "youtube": "https://youtube.com/yourchannel"}',
    'Add your social media profiles to showcase your work and build your online presence. This helps recruiters discover your content.',
    TRUE,
    NOW(),
    NOW()
);

-- Display success message
SELECT 'Missing Dancer fields implemented successfully!' as message;

-- Display the new fields count
SELECT 
    COUNT(*) as new_fields_added,
    'New fields added to Dancer artist type' as description
FROM artist_type_fields 
WHERE artist_type_id = @dancer_type_id 
AND field_name IN (
    'id_proof_file',
    'face_verified', 
    'personal_style_approach',
    'choreographic_techniques',
    'movement_creation_approach',
    'stage_direction_skills',
    'teaching_mentorship_details',
    'collaboration_preferences',
    'venue_experience',
    'social_media_links'
);

-- Display all Dancer fields
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
WHERE atf.artist_type_id = @dancer_type_id
ORDER BY atf.sort_order;

-- Display field statistics
SELECT 
    atf.field_type,
    COUNT(*) as field_count,
    GROUP_CONCAT(atf.field_name ORDER BY atf.sort_order) as field_names
FROM artist_type_fields atf
WHERE atf.artist_type_id = @dancer_type_id
GROUP BY atf.field_type
ORDER BY field_count DESC;
