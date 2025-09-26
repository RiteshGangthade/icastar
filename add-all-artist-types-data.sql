-- iCastar Platform - Add All Artist Types Default Data
-- This script adds default fields for all artist types: Singer, Director, Writer, Dancer

USE icastar_db;

-- First, let's check existing artist types
SELECT * FROM artist_types;

-- ==================== SINGER FIELDS (Artist Type ID: 3) ====================

-- Genre (Multi Select)
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Select your Genre', 'genre', 'MULTI_SELECT',
  'Choose one or more genres you perform', 1, 1,
  JSON_ARRAY('Classical','Pop','Rock','Jazz','Hip-Hop','Folk','Country','R&B','Devotional','Bollywood','Indie','Alternative','Sufi','Qawwali','Ghazal','Playback','Opera'),
  'Select genres', 1, NULL, 3
);

-- Vocal Range (Single Select)
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Select your Vocal Range', 'vocal_range', 'SELECT',
  'Choose your vocal range', 1, 1,
  JSON_ARRAY('Tenor','Baritone','Bass','Soprano','Mezzo-soprano','Alto','Countertenor','Contralto'),
  'Select vocal range', 2, NULL, 3
);

-- Languages you can sing (Multi Select)
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Languages you can sing', 'languages', 'MULTI_SELECT',
  'Select the languages you can sing in', 1, 1,
  JSON_ARRAY('Hindi','English','Marathi','Tamil','Telugu','Punjabi','Gujarati','Kannada','Bengali','Malayalam','Assamese','Odia','Bhojpuri','Rajasthani','Urdu','Sanskrit'),
  'Select languages', 3, NULL, 3
);

-- Brief about you
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Brief about you', 'about', 'TEXTAREA',
  'Tell us what makes you unique', 0, 0,
  NULL, 'Write something about yourself', 4, NULL, 3
);

-- Experience Years
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Your Experience', 'experience_years', 'NUMBER',
  'Enter your total singing experience in years', 0, 1,
  NULL, 'e.g., 5', 5, NULL, 3
);

-- Achievements
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Your Achievements', 'achievements', 'TEXTAREA',
  'Mention any awards or recognitions you have received', 0, 1,
  NULL, 'Write your achievements', 6, NULL, 3
);

-- Short Video
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload your singing short video', 'short_video', 'FILE',
  'Upload a short video clip of your singing', 0, 0,
  NULL, 'Upload video', 7, NULL, 3
);

-- Long Audio
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload your mp3 singing long file', 'long_audio', 'FILE',
  'Upload a long mp3 audio of your singing', 0, 0,
  NULL, 'Upload audio', 8, NULL, 3
);

-- Profile Picture
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload your profile picture', 'profile_picture', 'FILE',
  'Upload a clear profile picture', 1, 0,
  NULL, 'Upload image', 9, NULL, 3
);

-- Face Verification
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Face Verification', 'face_verification', 'BOOLEAN',
  'Enable face verification for your profile', 0, 0,
  NULL, NULL, 10, NULL, 3
);

-- ==================== DIRECTOR FIELDS (Artist Type ID: 4) ====================

-- Genre
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Select your genre', 'genre', 'MULTI_SELECT',
  'Choose one or more genres you specialise in', 1, 1,
  JSON_ARRAY(
    'Auteur Directors','Commercial Directors','Independent filmmaker',
    'Documentary Director','Action Director','Romantic Director','Comedy Director',
    'Historical/Period Director','Animation Director','Music Video Director',
    'Art director','Multi genre director','Web Series Director','Short Film Director'
  ),
  'Select genres', 1, NULL, 4
);

-- Full Name
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Full Name', 'full_name', 'TEXT',
  'Enter your original full name', 1, 1,
  NULL, 'Enter full name', 2, NULL, 4
);

-- Username
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Username', 'username', 'TEXT',
  'If you have a professional username or handle', 0, 1,
  NULL, 'Enter username', 3, NULL, 4
);

-- City
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'City', 'city', 'TEXT',
  'Enter the city you work in', 1, 1,
  NULL, 'Enter city', 4, NULL, 4
);

-- Training
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Training', 'training', 'TEXTAREA',
  'Name of institution or director you assisted', 0, 1,
  NULL, 'Describe your training or mentorship', 5, NULL, 4
);

-- Description
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Describe Yourself', 'description', 'TEXTAREA',
  'Information for producers or production houses', 0, 1,
  NULL, 'Write a short bio', 6, NULL, 4
);

-- Experience Years
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Experience', 'experience_years', 'NUMBER',
  'Enter your total directing experience in years', 0, 1,
  NULL, 'e.g., 5', 7, NULL, 4
);

-- Profile Photo
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Profile Photo', 'profile_photo', 'FILE',
  'Upload a clear profile picture', 1, 0,
  NULL, 'Upload image', 8, NULL, 4
);

-- ID Proof
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload ID Proof', 'id_proof', 'FILE',
  'Upload Aadhar / PAN / Voter ID', 1, 0,
  NULL, 'Upload ID', 9, NULL, 4
);

-- Face Verification
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Face Verification', 'face_verification', 'BOOLEAN',
  'Enable face verification for your profile', 0, 0,
  NULL, NULL, 10, NULL, 4
);

-- Short Video
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Directed Short Cropped Video', 'short_video', 'FILE',
  'Upload best shots so producers can hire you', 0, 0,
  NULL, 'Upload video', 11, NULL, 4
);

-- Drive Link
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Drive Link', 'drive_link', 'URL',
  'Share your drive link if you have more videos', 0, 0,
  NULL, 'Paste Google Drive link', 12, NULL, 4
);

-- ==================== WRITER FIELDS (Artist Type ID: 5) ====================

-- Genre
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Select your genre', 'genre', 'MULTI_SELECT',
  'Pick one or more writing genres', 1, 1,
  JSON_ARRAY(
    'Story writer','Screenplay writer','Lyrics writer',
    'Dialog specialist','Content writer','Public relationship script writer',
    'Multigenre writer','Technical writer','Copywriter','Blog writer',
    'Social media writer','Marketing writer','Creative writer'
  ),
  'Select genres', 1, NULL, 5
);

-- Education/Training
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Education / Training', 'education_training', 'TEXTAREA',
  'Name of institution or courses you attended', 0, 1,
  NULL, 'Describe your education or training', 2, NULL, 5
);

-- Demo
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Paste your demo here', 'demo', 'TEXTAREA',
  'Paste your demo script or content here', 0, 0,
  NULL, 'Enter your demo', 3, NULL, 5
);

-- Experience Years
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Experience', 'experience_years', 'NUMBER',
  'Enter your total writing experience in years', 0, 1,
  NULL, 'e.g., 3', 4, NULL, 5
);

-- Languages
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Languages you can write in', 'languages', 'MULTI_SELECT',
  'Select the languages you can write in', 1, 1,
  JSON_ARRAY('Hindi','English','Marathi','Tamil','Telugu','Punjabi','Gujarati','Kannada','Bengali','Urdu','Sanskrit'),
  'Select languages', 5, NULL, 5
);

-- Achievements
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Achievements', 'achievements', 'TEXTAREA',
  'Mention any awards or recognitions', 0, 1,
  NULL, 'Enter your achievements', 6, NULL, 5
);

-- Portfolio
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Portfolio Link', 'portfolio_link', 'URL',
  'Share your portfolio or website link', 0, 0,
  NULL, 'Enter portfolio URL', 7, NULL, 5
);

-- ==================== DANCER FIELDS (Artist Type ID: 2) ====================

-- Brief Yourself
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Brief Yourself', 'bio', 'TEXTAREA',
  'Describe yourself briefly', 0, 1,
  NULL, 'Enter about yourself', 1, NULL, 2
);

-- Dance Style
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Your Unique Dance Style', 'dance_style', 'TEXT',
  'Describe your unique dance style', 1, 1,
  NULL, 'e.g., Contemporary Fusion', 2, NULL, 2
);

-- Choreography Experience
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Choreography Experience', 'experience_years', 'NUMBER',
  'Total years of choreography experience', 0, 1,
  NULL, 'e.g., 5', 3, NULL, 2
);

-- Training
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Training', 'training', 'TEXTAREA',
  'Any formal dance training, schools, workshops, or programs attended', 0, 1,
  NULL, 'Enter your training details', 4, NULL, 2
);

-- Achievements
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Achievements', 'achievements', 'TEXTAREA',
  'Mention any awards or achievements', 0, 1,
  NULL, 'Enter your achievements', 5, NULL, 2
);

-- Personal Style
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Personal Style / Approach', 'personal_style', 'TEXTAREA',
  'Describe your unique style or philosophy in dance and choreography', 0, 1,
  NULL, 'Enter your style/philosophy', 6, NULL, 2
);

-- Skills and Strengths
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Skills and Strengths', 'skills_strengths', 'TEXTAREA',
  'Include choreographic techniques, movement creation, stage direction & production, teaching & mentorship', 0, 1,
  NULL, 'Describe your skills and strengths', 7, NULL, 2
);

-- Dancing Video
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Dancing Video', 'dancing_video', 'FILE',
  'Upload your dance video', 0, 0,
  NULL, 'Upload video file', 8, NULL, 2
);

-- Drive Link
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Drive Link', 'drive_link', 'URL',
  'Share Google Drive link for more videos', 0, 0,
  NULL, 'Paste Google Drive link', 9, NULL, 2
);

-- Face Verification
INSERT INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Face Verification', 'face_verification', 'BOOLEAN',
  'Enable face verification for your profile', 0, 0,
  NULL, NULL, 10, NULL, 2
);

-- ==================== VERIFICATION ====================

-- Check all inserted fields
SELECT 
    at.name as artist_type,
    atf.field_name,
    atf.display_name,
    atf.field_type,
    atf.is_required,
    atf.is_searchable,
    atf.sort_order
FROM artist_type_fields atf
JOIN artist_types at ON atf.artist_type_id = at.id
ORDER BY at.name, atf.sort_order;

-- Count fields per artist type
SELECT 
    at.name as artist_type,
    COUNT(atf.id) as field_count
FROM artist_types at
LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id
GROUP BY at.id, at.name
ORDER BY at.name;
