-- Insert Basic Artist Types
USE icastar_db;

-- Insert basic artist types
INSERT IGNORE INTO artist_types (name, display_name, description, icon_url, is_active, sort_order, created_at, updated_at) VALUES
('DANCER', 'Dancer', 'Professional Dancers specializing in various dance styles including classical, contemporary, hip-hop, ballet, and more. Perfect for stage performances, music videos, events, and entertainment productions.', '/icons/dancer.png', TRUE, 2, NOW(), NOW()),
('SINGER', 'Singer', 'Professional Singers specializing in various music genres including classical, pop, rock, jazz, folk, and more. Perfect for live performances, recordings, and entertainment productions.', '/icons/singer.png', TRUE, 3, NOW(), NOW()),
('DIRECTOR', 'Director', 'Professional Directors specializing in various film and video production including commercial, independent, documentary, and more. Perfect for film production, music videos, and creative projects.', '/icons/director.png', TRUE, 4, NOW(), NOW()),
('WRITER', 'Writer', 'Professional Writers specializing in various writing forms including screenplays, lyrics, content, and more. Perfect for creative writing, content creation, and storytelling projects.', '/icons/writer.png', TRUE, 5, NOW(), NOW());

-- Verify the insertion
SELECT * FROM artist_types ORDER BY sort_order;
