-- iCastar Platform - Corrected Artist Types Data
-- This script adds default fields for all artist types with correct IDs

-- ==================== DANCER FIELDS (Artist Type ID: 1) ====================

-- Brief Yourself
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Brief Yourself', 'bio', 'TEXTAREA',
  'Describe yourself briefly', 0, 1,
  NULL, 'Enter about yourself', 1, NULL, 1
);

-- Dance Style
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Your Unique Dance Style', 'dance_style', 'TEXT',
  'Describe your unique dance style', 1, 1,
  NULL, 'e.g., Contemporary Fusion', 2, NULL, 1
);

-- Choreography Experience
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Choreography Experience', 'experience_years', 'NUMBER',
  'Total years of choreography experience', 0, 1,
  NULL, 'e.g., 5', 3, NULL, 1
);

-- Training
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Training', 'training', 'TEXTAREA',
  'Any formal dance training, schools, workshops, or programs attended', 0, 1,
  NULL, 'Enter your training details', 4, NULL, 1
);

-- Achievements
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Achievements', 'achievements', 'TEXTAREA',
  'Mention any awards or achievements', 0, 1,
  NULL, 'Enter your achievements', 5, NULL, 1
);

-- Personal Style
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Personal Style / Approach', 'personal_style', 'TEXTAREA',
  'Describe your unique style or philosophy in dance and choreography', 0, 1,
  NULL, 'Enter your style/philosophy', 6, NULL, 1
);

-- Skills and Strengths
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Skills and Strengths', 'skills_strengths', 'TEXTAREA',
  'Include choreographic techniques, movement creation, stage direction & production, teaching & mentorship', 0, 1,
  NULL, 'Describe your skills and strengths', 7, NULL, 1
);

-- Dancing Video
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Dancing Video', 'dancing_video', 'FILE',
  'Upload your dance video', 0, 0,
  NULL, 'Upload video file', 8, NULL, 1
);

-- Drive Link
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Upload Drive Link', 'drive_link', 'URL',
  'Share Google Drive link for more videos', 0, 0,
  NULL, 'Paste Google Drive link', 9, NULL, 1
);

-- Face Verification
INSERT IGNORE INTO artist_type_fields (
  created_at, is_active, display_name, field_name, field_type,
  help_text, is_required, is_searchable, options, placeholder,
  sort_order, validation_rules, artist_type_id
) VALUES (
  NOW(), 1, 'Face Verification', 'face_verification', 'BOOLEAN',
  'Enable face verification for your profile', 0, 0,
  NULL, NULL, 10, NULL, 1
);
