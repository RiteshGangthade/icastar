# 🎭 iCastar Platform - Complete System Guide

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [How Dynamic Artist System Works](#how-dynamic-artist-system-works)
3. [Adding New Artist Types](#adding-new-artist-types)
4. [Adding New Users](#adding-new-users)
5. [Adding New Artist Type Fields](#adding-new-artist-type-fields)
6. [API Endpoints Reference](#api-endpoints-reference)
7. [Database Schema](#database-schema)
8. [Complete Workflow Examples](#complete-workflow-examples)

---

## 🎯 System Overview

The iCastar Platform is a **dynamic artist management system** that allows you to:
- ✅ Add new artist types without code changes
- ✅ Create custom fields for each artist type
- ✅ Manage artist profiles with dynamic data
- ✅ Handle user authentication and authorization
- ✅ Support multiple artist types with different requirements

### 🏗️ Core Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User System   │    │  Artist System  │    │  Dynamic Fields │
│                 │    │                 │    │                 │
│ • Registration  │    │ • Artist Types  │    │ • Field Types   │
│ • Authentication│    │ • Artist Profiles│   │ • Field Values  │
│ • Authorization │    │ • Dynamic Data  │    │ • Validation    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🔄 How Dynamic Artist System Works

### 1. **Three Main Tables**

#### `artist_types` - Defines Artist Categories
```sql
CREATE TABLE artist_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,           -- 'DANCER', 'SINGER', etc.
    display_name VARCHAR(100) NOT NULL,         -- 'Dancer', 'Singer', etc.
    description TEXT,                            -- Description of the artist type
    icon_url VARCHAR(255),                      -- Icon for UI
    is_active BOOLEAN DEFAULT TRUE,             -- Enable/disable
    sort_order INT DEFAULT 0,                   -- Display order
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `artist_type_fields` - Defines Custom Fields for Each Artist Type
```sql
CREATE TABLE artist_type_fields (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    artist_type_id BIGINT NOT NULL,             -- Links to artist_types
    field_name VARCHAR(100) NOT NULL,           -- 'dance_styles', 'vocal_range'
    display_name VARCHAR(100) NOT NULL,         -- 'Dance Styles', 'Vocal Range'
    field_type ENUM('TEXT', 'TEXTAREA', 'NUMBER', 'BOOLEAN', 'SELECT', 'MULTI_SELECT', 'FILE', 'URL', 'DATE') NOT NULL,
    is_required BOOLEAN DEFAULT FALSE,          -- Required field?
    is_searchable BOOLEAN DEFAULT TRUE,         -- Can be searched?
    sort_order INT DEFAULT 0,                  -- Display order
    validation_rules JSON,                      -- Custom validation rules
    options JSON,                               -- Options for SELECT/MULTI_SELECT
    placeholder VARCHAR(255),                   -- Placeholder text
    help_text TEXT,                            -- Help text for users
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_type_id) REFERENCES artist_types(id)
);
```

#### `artist_profile_fields` - Stores Actual Field Values
```sql
CREATE TABLE artist_profile_fields (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    artist_profile_id BIGINT NOT NULL,          -- Links to artist_profiles
    artist_type_field_id BIGINT NOT NULL,       -- Links to artist_type_fields
    field_value TEXT,                           -- The actual value
    file_url VARCHAR(500),                      -- For FILE fields
    file_name VARCHAR(255),                     -- Original filename
    file_size BIGINT,                           -- File size in bytes
    mime_type VARCHAR(100),                     -- MIME type
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (artist_profile_id) REFERENCES artist_profiles(id),
    FOREIGN KEY (artist_type_field_id) REFERENCES artist_type_fields(id)
);
```

### 2. **How It Works - Step by Step**

#### Step 1: Define Artist Type
```sql
INSERT INTO artist_types (name, display_name, description) 
VALUES ('DANCER', 'Dancer', 'Professional dancers specializing in various dance styles');
```

#### Step 2: Define Fields for Artist Type
```sql
INSERT INTO artist_type_fields (artist_type_id, field_name, display_name, field_type, is_required, options) 
VALUES (1, 'dance_styles', 'Dance Styles', 'MULTI_SELECT', TRUE, 
        JSON_ARRAY('Classical Ballet', 'Contemporary', 'Hip-Hop', 'Bollywood'));
```

#### Step 3: User Creates Artist Profile
```json
{
  "artistTypeId": 1,
  "firstName": "Priya",
  "lastName": "Sharma",
  "stageName": "Priya the Dancer",
  "dynamicFields": [
    {
      "artistTypeFieldId": 1,
      "fieldValue": "Classical Ballet,Contemporary,Hip-Hop"
    }
  ]
}
```

#### Step 4: System Saves Dynamic Data
- Creates `artist_profile` record
- Creates `artist_profile_fields` records for each dynamic field
- Links field values to field definitions

---

## ➕ Adding New Artist Types

### Method 1: Using SQL (Recommended)

#### 1. Add Artist Type
```sql
INSERT INTO artist_types (name, display_name, description, icon_url, sort_order) 
VALUES ('PHOTOGRAPHER', 'Photographer', 'Professional photographers for events, portraits, and commercial work', '/icons/photographer.png', 6);
```

#### 2. Add Fields for the Artist Type
```sql
-- Get the artist type ID
SET @photographer_id = LAST_INSERT_ID();

-- Add fields
INSERT INTO artist_type_fields (artist_type_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, options, help_text) VALUES
(@photographer_id, 'photography_style', 'Photography Style', 'MULTI_SELECT', TRUE, TRUE, 1, 
 JSON_ARRAY('Portrait', 'Wedding', 'Event', 'Commercial', 'Fashion', 'Street', 'Nature', 'Sports', 'Documentary'), 
 'Select all photography styles you specialize in'),
 
(@photographer_id, 'equipment', 'Equipment', 'TEXTAREA', FALSE, TRUE, 2, NULL, 
 'List your camera equipment, lenses, lighting, etc.'),
 
(@photographer_id, 'portfolio_link', 'Portfolio Link', 'URL', TRUE, TRUE, 3, NULL, 
 'Share your online portfolio or website'),
 
(@photographer_id, 'experience_years', 'Experience Years', 'NUMBER', TRUE, TRUE, 4, NULL, 
 'Years of professional photography experience'),
 
(@photographer_id, 'sample_photos', 'Sample Photos', 'FILE', TRUE, FALSE, 5, NULL, 
 'Upload your best sample photos');
```

### Method 2: Using Admin API (Future Enhancement)

```bash
# Add new artist type
curl -X POST "http://localhost:8080/api/admin/artist-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "name": "PHOTOGRAPHER",
    "displayName": "Photographer",
    "description": "Professional photographers",
    "iconUrl": "/icons/photographer.png"
  }'

# Add fields for the artist type
curl -X POST "http://localhost:8080/api/admin/artist-types/{id}/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "fieldName": "photography_style",
    "displayName": "Photography Style",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "options": {
      "values": ["Portrait", "Wedding", "Event", "Commercial"]
    }
  }'
```

---

## 👤 Adding New Users

### 1. User Registration

#### Email/Password Registration
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "phoneNumber": "+1234567890"
  }'
```

#### OTP-based Registration
```bash
# Step 1: Send OTP
curl -X POST "http://localhost:8080/api/auth/otp/send" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "+1234567890"
  }'

# Step 2: Verify OTP and register
curl -X POST "http://localhost:8080/api/auth/otp/verify" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "+1234567890",
    "otp": "123456",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. User Login

#### Email/Password Login
```bash
curl -X POST "http://localhost:8080/api/auth/email/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "password": "password123"
  }'
```

#### OTP Login
```bash
# Step 1: Send OTP
curl -X POST "http://localhost:8080/api/auth/otp/send" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "+1234567890"
  }'

# Step 2: Verify OTP
curl -X POST "http://localhost:8080/api/auth/otp/verify" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "+1234567890",
    "otp": "123456"
  }'
```

---

## 🔧 Adding New Artist Type Fields

### Method 1: Direct SQL Insert

```sql
-- Add a new field to existing artist type
INSERT INTO artist_type_fields (
    artist_type_id, 
    field_name, 
    display_name, 
    field_type, 
    is_required, 
    is_searchable, 
    sort_order, 
    options, 
    help_text
) VALUES (
    1,  -- Dancer artist type ID
    'social_media_links', 
    'Social Media Links', 
    'TEXTAREA', 
    FALSE, 
    TRUE, 
    15, 
    NULL, 
    'Share your Instagram, TikTok, YouTube, or other social media profiles'
);
```

### Method 2: Using Admin API (Future Enhancement)

```bash
curl -X POST "http://localhost:8080/api/admin/artist-types/1/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <admin_token>" \
  -d '{
    "fieldName": "social_media_links",
    "displayName": "Social Media Links",
    "fieldType": "TEXTAREA",
    "isRequired": false,
    "isSearchable": true,
    "sortOrder": 15,
    "helpText": "Share your social media profiles"
  }'
```

---

## 🌐 API Endpoints Reference

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/email/login` | Login with email/password |
| POST | `/api/auth/otp/send` | Send OTP to phone |
| POST | `/api/auth/otp/verify` | Verify OTP and login/register |

### Artist Profile Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/artists/profile` | Create artist profile |
| GET | `/api/artists/profile/me` | Get current user's profile |
| GET | `/api/artists/profile/{id}` | Get specific artist profile |
| PUT | `/api/artists/profile/{id}` | Update artist profile |
| DELETE | `/api/artists/profile/{id}` | Delete artist profile |

### Admin Endpoints (Future)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/artist-types` | Create new artist type |
| GET | `/api/admin/artist-types` | List all artist types |
| POST | `/api/admin/artist-types/{id}/fields` | Add field to artist type |
| GET | `/api/admin/artist-types/{id}/fields` | List fields for artist type |

---

## 🗄️ Database Schema

### Core Tables

```sql
-- Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    role ENUM('ARTIST', 'RECRUITER', 'ADMIN') DEFAULT 'ARTIST',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Artist profiles table
CREATE TABLE artist_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    artist_type_id BIGINT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    stage_name VARCHAR(100),
    bio TEXT,
    location VARCHAR(255),
    experience_years INT DEFAULT 0,
    hourly_rate DECIMAL(10,2),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (artist_type_id) REFERENCES artist_types(id)
);
```

---

## 🎬 Complete Workflow Examples

### Example 1: Adding a New "Musician" Artist Type

#### Step 1: Add Artist Type
```sql
INSERT INTO artist_types (name, display_name, description, icon_url, sort_order) 
VALUES ('MUSICIAN', 'Musician', 'Professional musicians and instrumentalists', '/icons/musician.png', 7);
```

#### Step 2: Add Fields
```sql
SET @musician_id = LAST_INSERT_ID();

INSERT INTO artist_type_fields (artist_type_id, field_name, display_name, field_type, is_required, is_searchable, sort_order, options, help_text) VALUES
(@musician_id, 'instruments', 'Instruments', 'MULTI_SELECT', TRUE, TRUE, 1, 
 JSON_ARRAY('Guitar', 'Piano', 'Violin', 'Drums', 'Bass', 'Saxophone', 'Flute', 'Trumpet', 'Cello', 'Other'), 
 'Select all instruments you play'),
 
(@musician_id, 'music_genres', 'Music Genres', 'MULTI_SELECT', TRUE, TRUE, 2, 
 JSON_ARRAY('Classical', 'Jazz', 'Rock', 'Pop', 'Blues', 'Country', 'Electronic', 'Folk', 'R&B', 'Hip-Hop'), 
 'Select all music genres you perform'),
 
(@musician_id, 'performance_experience', 'Performance Experience', 'TEXTAREA', TRUE, TRUE, 3, NULL, 
 'Describe your live performance experience, venues, and notable performances'),
 
(@musician_id, 'audio_samples', 'Audio Samples', 'FILE', TRUE, FALSE, 4, NULL, 
 'Upload your best audio recordings or demos');
```

#### Step 3: User Creates Musician Profile
```bash
curl -X POST "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "artistTypeId": 7,
    "firstName": "Alex",
    "lastName": "Johnson",
    "stageName": "Alex the Guitarist",
    "bio": "Professional guitarist with 10 years of experience in rock and jazz",
    "location": "Los Angeles, CA",
    "experienceYears": 10,
    "hourlyRate": 150.00,
    "dynamicFields": [
      {
        "artistTypeFieldId": 25,
        "fieldValue": "Guitar,Piano"
      },
      {
        "artistTypeFieldId": 26,
        "fieldValue": "Rock,Jazz,Blues"
      },
      {
        "artistTypeFieldId": 27,
        "fieldValue": "Performed at 50+ venues including Madison Square Garden and Hollywood Bowl. Opened for major artists like John Mayer and Eric Clapton."
      }
    ]
  }'
```

### Example 2: Adding Custom Fields to Existing Artist Type

#### Add New Field to Dancer Type
```sql
INSERT INTO artist_type_fields (
    artist_type_id, 
    field_name, 
    display_name, 
    field_type, 
    is_required, 
    is_searchable, 
    sort_order, 
    help_text
) VALUES (
    1,  -- Dancer artist type ID
    'awards_achievements', 
    'Awards & Achievements', 
    'TEXTAREA', 
    FALSE, 
    TRUE, 
    20, 
    'List any awards, competitions won, or notable achievements in dance'
);
```

#### Update Existing Dancer Profile
```bash
curl -X PUT "http://localhost:8080/api/artists/profile/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "firstName": "Priya",
    "lastName": "Sharma",
    "stageName": "Priya the Dancer",
    "bio": "Professional dancer with 8 years of experience",
    "location": "Mumbai, India",
    "experienceYears": 8,
    "hourlyRate": 3000.00,
    "dynamicFields": [
      {
        "artistTypeFieldId": 1,
        "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop"
      },
      {
        "artistTypeFieldId": 2,
        "fieldValue": "Completed 8 years of formal Bharatanatyam training"
      },
      {
        "artistTypeFieldId": 25,
        "fieldValue": "Winner of National Dance Competition 2023, Featured in Bollywood movie 'Dance Dreams'"
      }
    ]
  }'
```

---

## 🚀 Quick Start Commands

### 1. Build and Run Project
```bash
# Set Java 17
export JAVA_HOME=/Users/admin/Library/Java/JavaVirtualMachines/azul-17.0.16/Contents/Home

# Build project
mvn clean compile

# Run project
mvn spring-boot:run
```

### 2. Setup Database
```bash
# Create database
mysql -u root -p
CREATE DATABASE icastar_db;
USE icastar_db;
source database-schema.sql;

# Add basic artist types
source insert-basic-artist-types.sql;

# Add all artist type fields
source artist-types-corrected-data.sql;
```

### 3. Test the System
```bash
# Test registration
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123","firstName":"Test","lastName":"User","phoneNumber":"+1234567890"}'

# Test login
curl -X POST "http://localhost:8080/api/auth/email/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Test artist profile creation
curl -X POST "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "artistTypeId": 1,
    "firstName": "Test",
    "lastName": "Dancer",
    "stageName": "Test Dancer",
    "bio": "Test bio",
    "location": "Test City",
    "experienceYears": 5,
    "hourlyRate": 2000.00,
    "dynamicFields": [
      {
        "artistTypeFieldId": 1,
        "fieldValue": "Contemporary,Hip-Hop"
      }
    ]
  }'
```

---

## 📝 Summary

The iCastar Platform provides a **completely dynamic system** for managing artists:

1. **Add New Artist Types**: Simply insert into `artist_types` table
2. **Add Custom Fields**: Insert into `artist_type_fields` table with any field type
3. **User Registration**: Multiple methods (email/password, OTP)
4. **Dynamic Profiles**: Users can create profiles with custom field data
5. **No Code Changes**: Everything is database-driven

The system is designed to be **infinitely extensible** - you can add any number of artist types and fields without touching the code!

---

## 🔗 Related Documentation

- [README.md](README.md) - Main project overview
- [DATABASE-SETUP-COMPLETE.md](DATABASE-SETUP-COMPLETE.md) - Database setup guide
- [ARTIST-TYPES-AUTO-SETUP-GUIDE.md](ARTIST-TYPES-AUTO-SETUP-GUIDE.md) - Artist types setup
- [TEST-DYNAMIC-FIELDS.md](TEST-DYNAMIC-FIELDS.md) - Testing guide

---

*This guide covers the complete system architecture and workflows. The platform is designed to be flexible and extensible for any type of artist management needs.*
