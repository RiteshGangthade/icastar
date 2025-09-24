# 🔧 iCastar Platform - Dynamic Fields System Guide

## 📋 Overview

The iCastar platform uses a sophisticated dynamic field system that allows you to add custom fields to artist types without modifying the core code. This guide explains how the `ArtistProfileField` table works and the complete flow for adding new fields.

## 🏗️ System Architecture

### Database Tables Structure

```
artist_types (1) ──→ (many) artist_type_fields (1) ──→ (many) artist_profile_fields
     │                        │                              │
     │                        │                              │
   Defines                Field Definition              Actual Field Values
 Artist Types            (Template)                    (User Data)
```

### 1. **artist_types** Table
- Stores different artist types (Actor, Dancer, Singer, etc.)
- Each type can have multiple custom fields

### 2. **artist_type_fields** Table  
- Defines the field templates for each artist type
- Contains field metadata (name, type, validation, options)
- Acts as a blueprint for dynamic fields

### 3. **artist_profile_fields** Table
- Stores actual field values for each artist profile
- Links to both artist profile and field definition
- Contains the user's specific data

## 🔄 Complete Flow: How Dynamic Fields Work

### Step 1: Define Field Template (artist_type_fields)

When you add a new field to an artist type, you create a **field template**:

```sql
-- Example: Adding "Height" field to Actor type
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
    help_text
) VALUES (
    1, -- Actor type ID
    'height',
    'Height',
    'NUMBER',
    TRUE,
    TRUE,
    1,
    '{"min": 150, "max": 220}',
    NULL,
    'cm',
    'Height in centimeters'
);
```

### Step 2: Artist Fills Field Value (artist_profile_fields)

When an artist creates/updates their profile, the system creates **field value records**:

```sql
-- Example: Actor sets their height
INSERT INTO artist_profile_fields (
    artist_profile_id,
    artist_type_field_id,
    field_value
) VALUES (
    1, -- Artist profile ID
    1, -- Height field definition ID
    '180' -- Actual height value
);
```

### Step 3: System Retrieves and Displays Data

The system joins the tables to get complete field information:

```sql
-- Get all fields for an artist profile
SELECT 
    apf.id,
    apf.field_value,
    atf.field_name,
    atf.display_name,
    atf.field_type,
    atf.is_required,
    atf.help_text
FROM artist_profile_fields apf
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
WHERE apf.artist_profile_id = 1
ORDER BY atf.sort_order;
```

## 🎯 How to Add New Fields: Complete Process

### Method 1: Using Admin API (Recommended)

#### Step 1: Add New Field Definition
```bash
# Add a new field to existing artist type
curl -X POST "http://localhost:8080/api/admin/artist-types/1/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "special_skills",
    "displayName": "Special Skills",
    "fieldType": "MULTI_SELECT",
    "isRequired": false,
    "isSearchable": true,
    "sortOrder": 15,
    "options": {
      "values": [
        "Martial Arts",
        "Horse Riding",
        "Swimming",
        "Driving",
        "Languages",
        "Music Instruments"
      ]
    },
    "helpText": "Select your special skills and abilities"
  }'
```

#### Step 2: Artists Can Now Use the Field
When artists create/update their profiles, they can include the new field:

```bash
# Artist updates profile with new field
curl -X PUT "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ARTIST_JWT_TOKEN" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dynamicFields": [
      {
        "artistTypeFieldId": 15,
        "fieldValue": "Martial Arts,Swimming,Languages"
      }
    ]
  }'
```

### Method 2: Direct Database Insert

#### Step 1: Add Field Definition
```sql
-- Add new field to Actor type
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
    1, -- Actor type ID
    'special_skills',
    'Special Skills',
    'MULTI_SELECT',
    FALSE,
    TRUE,
    15,
    NULL,
    JSON_ARRAY(
        'Martial Arts',
        'Horse Riding',
        'Swimming',
        'Driving',
        'Languages',
        'Music Instruments'
    ),
    NULL,
    'Select your special skills and abilities',
    TRUE,
    NOW(),
    NOW()
);
```

#### Step 2: Artists Add Values
```sql
-- Artist adds their special skills
INSERT INTO artist_profile_fields (
    artist_profile_id,
    artist_type_field_id,
    field_value,
    created_at,
    updated_at
) VALUES (
    1, -- Artist profile ID
    15, -- Special skills field ID
    'Martial Arts,Swimming,Languages',
    NOW(),
    NOW()
);
```

## 🔍 Field Types and Their Storage

### 1. TEXT Fields
```sql
-- Field Definition
field_type = 'TEXT'
validation_rules = '{"maxLength": 100}'

-- Field Value
field_value = 'John Doe'
```

### 2. NUMBER Fields
```sql
-- Field Definition
field_type = 'NUMBER'
validation_rules = '{"min": 0, "max": 100}'

-- Field Value
field_value = '25'
```

### 3. SELECT Fields
```sql
-- Field Definition
field_type = 'SELECT'
options = '["Option 1", "Option 2", "Option 3"]'

-- Field Value
field_value = 'Option 2'
```

### 4. MULTI_SELECT Fields
```sql
-- Field Definition
field_type = 'MULTI_SELECT'
options = '["Skill 1", "Skill 2", "Skill 3"]'

-- Field Value (comma-separated)
field_value = 'Skill 1,Skill 3'
```

### 5. BOOLEAN Fields
```sql
-- Field Definition
field_type = 'BOOLEAN'

-- Field Value
field_value = 'true' -- or 'false'
```

### 6. FILE Fields
```sql
-- Field Definition
field_type = 'FILE'
validation_rules = '{"maxSize": "10MB", "allowedTypes": ["jpg", "png", "pdf"]}'

-- Field Value
field_value = 'https://example.com/file.pdf'
file_url = 'https://example.com/file.pdf'
file_name = 'resume.pdf'
file_size = 1024000
mime_type = 'application/pdf'
```

### 7. DATE Fields
```sql
-- Field Definition
field_type = 'DATE'

-- Field Value
field_value = '2024-01-15'
```

### 8. JSON Fields
```sql
-- Field Definition
field_type = 'JSON'

-- Field Value
field_value = '{"key1": "value1", "key2": "value2"}'
```

## 🎨 Real-World Example: Adding "Social Media Links" Field

### Step 1: Add Field Definition
```bash
curl -X POST "http://localhost:8080/api/admin/artist-types/1/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "social_media_links",
    "displayName": "Social Media Links",
    "fieldType": "JSON",
    "isRequired": false,
    "isSearchable": false,
    "sortOrder": 20,
    "helpText": "Add your social media profiles (Instagram, Twitter, etc.)"
  }'
```

### Step 2: Artist Adds Social Media Links
```bash
curl -X PUT "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ARTIST_JWT_TOKEN" \
  -d '{
    "dynamicFields": [
      {
        "artistTypeFieldId": 20,
        "fieldValue": "{\"instagram\": \"@johnactor\", \"twitter\": \"@johnactor\", \"youtube\": \"John Actor Channel\"}"
      }
    ]
  }'
```

### Step 3: System Retrieves and Displays
```sql
-- Get social media links for an artist
SELECT 
    apf.field_value as social_media_json,
    atf.display_name
FROM artist_profile_fields apf
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
WHERE apf.artist_profile_id = 1 
AND atf.field_name = 'social_media_links';
```

## 🔧 API Endpoints for Dynamic Fields

### Get Artist Profile with Dynamic Fields
```bash
curl -X GET "http://localhost:8080/api/artists/profile" \
  -H "Authorization: Bearer JWT_TOKEN"
```

**Response includes dynamic fields:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "dynamicFields": [
      {
        "id": 1,
        "artistTypeFieldId": 1,
        "fieldName": "height",
        "displayName": "Height",
        "fieldValue": "180"
      },
      {
        "id": 2,
        "artistTypeFieldId": 15,
        "fieldName": "special_skills",
        "displayName": "Special Skills",
        "fieldValue": "Martial Arts,Swimming"
      }
    ]
  }
}
```

### Update Dynamic Fields
```bash
curl -X PUT "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer JWT_TOKEN" \
  -d '{
    "dynamicFields": [
      {
        "id": 1,
        "fieldValue": "185"
      },
      {
        "artistTypeFieldId": 15,
        "fieldValue": "Martial Arts,Swimming,Languages"
      }
    ]
  }'
```

## 🔍 Search and Filtering with Dynamic Fields

### Search by Dynamic Field Values
```bash
# Search actors with specific height
curl -X GET "http://localhost:8080/api/artists/search?field=height&value=180" \
  -H "Authorization: Bearer JWT_TOKEN"

# Search by special skills
curl -X GET "http://localhost:8080/api/artists/search?field=special_skills&value=Martial Arts" \
  -H "Authorization: Bearer JWT_TOKEN"
```

### Database Query for Search
```sql
-- Find artists with specific field values
SELECT DISTINCT ap.*
FROM artist_profiles ap
JOIN artist_profile_fields apf ON ap.id = apf.artist_profile_id
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
WHERE atf.field_name = 'height'
AND apf.field_value = '180'
AND atf.is_searchable = TRUE;
```

## 🛠️ Admin Management of Dynamic Fields

### List All Fields for Artist Type
```bash
curl -X GET "http://localhost:8080/api/admin/artist-types/1/fields" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### Update Field Definition
```bash
curl -X PUT "http://localhost:8080/api/admin/artist-types/1/fields/15" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "displayName": "Updated Special Skills",
    "isRequired": true,
    "helpText": "Updated help text"
  }'
```

### Delete Field Definition
```bash
curl -X DELETE "http://localhost:8080/api/admin/artist-types/1/fields/15" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

## 📊 Data Flow Diagram

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────────┐
│   Admin User    │    │   Artist User    │    │   Recruiter User    │
└─────────┬───────┘    └─────────┬────────┘    └─────────┬───────────┘
          │                      │                       │
          │ 1. Create Field      │ 2. Fill Field Value   │ 3. Search/Filter
          │    Definition        │                       │
          ▼                      ▼                       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    iCastar Platform API                             │
└─────────────────────┬───────────────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        Database Layer                               │
│                                                                     │
│  ┌─────────────────┐  ┌──────────────────┐  ┌─────────────────────┐ │
│  │ artist_types    │  │ artist_type_     │  │ artist_profile_     │ │
│  │                 │  │ fields           │  │ fields              │ │
│  │ - id            │  │ - id             │  │ - id                │ │
│  │ - name          │  │ - artist_type_id │  │ - artist_profile_id │ │
│  │ - display_name  │  │ - field_name     │  │ - artist_type_      │ │
│  │ - description   │  │ - field_type     │  │   field_id          │ │
│  │                 │  │ - is_required    │  │ - field_value       │ │
│  │                 │  │ - options        │  │ - file_url          │ │
│  └─────────────────┘  └──────────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

## 🚨 Important Considerations

### 1. Field Validation
- Always validate field values against field definitions
- Check required fields are provided
- Validate data types and formats
- Enforce validation rules (min/max, patterns, etc.)

### 2. Data Integrity
- Use foreign key constraints
- Handle cascade deletes properly
- Maintain referential integrity
- Backup data before major changes

### 3. Performance
- Index searchable fields
- Use pagination for large datasets
- Optimize queries with proper joins
- Cache frequently accessed field definitions

### 4. Security
- Validate all input data
- Sanitize field values
- Check user permissions
- Prevent SQL injection

## 🎯 Best Practices

### 1. Field Naming
- Use snake_case for field names
- Be descriptive but concise
- Avoid special characters
- Use consistent naming conventions

### 2. Field Types
- Choose appropriate field types
- Use validation rules effectively
- Provide helpful placeholders
- Include clear help text

### 3. Data Storage
- Store values as strings (flexibility)
- Use JSON for complex data
- Handle file uploads properly
- Maintain data consistency

### 4. User Experience
- Order fields logically
- Group related fields
- Provide clear instructions
- Show validation errors

## 🔄 Complete Example: Adding "Languages" Field to Dancer

### Step 1: Admin Adds Field Definition
```bash
curl -X POST "http://localhost:8080/api/admin/artist-types/2/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "languages",
    "displayName": "Languages Known",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 13,
    "options": {
      "values": [
        "English",
        "Hindi",
        "Tamil",
        "Telugu",
        "Bengali",
        "Marathi",
        "Gujarati",
        "Punjabi",
        "Kannada",
        "Malayalam",
        "Other"
      ]
    },
    "helpText": "Select all languages you can perform in"
  }'
```

### Step 2: Dancer Updates Profile
```bash
curl -X PUT "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer DANCER_JWT_TOKEN" \
  -d '{
    "dynamicFields": [
      {
        "artistTypeFieldId": 13,
        "fieldValue": "English,Hindi,Tamil"
      }
    ]
  }'
```

### Step 3: Recruiter Searches Dancers
```bash
# Search dancers who know Hindi
curl -X GET "http://localhost:8080/api/artists/search?artistType=DANCER&field=languages&value=Hindi" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

### Step 4: System Processes Request
```sql
-- Database query executed by the system
SELECT ap.*, apf.field_value as languages
FROM artist_profiles ap
JOIN artist_profile_fields apf ON ap.id = apf.artist_profile_id
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
JOIN artist_types at ON ap.artist_type_id = at.id
WHERE at.name = 'DANCER'
AND atf.field_name = 'languages'
AND apf.field_value LIKE '%Hindi%'
AND atf.is_searchable = TRUE;
```

## 📝 Summary

The dynamic field system in iCastar provides:

1. **Flexibility** - Add new fields without code changes
2. **Scalability** - Support unlimited custom fields
3. **Type Safety** - Strong field type definitions
4. **Searchability** - Advanced search and filtering
5. **Validation** - Built-in data validation
6. **User Experience** - Intuitive field management

This system allows the platform to adapt to different artist types and requirements while maintaining data integrity and performance.

---

## 📞 Support

For questions about the dynamic field system:
1. Check the API documentation
2. Review the database schema
3. Test with the provided examples
4. Contact the development team

**Happy Field Management! 🔧✨**

