# 🎨 iCastar Platform - Adding New Artist Types Guide

## 📋 Overview

The iCastar platform features a dynamic artist type system that allows you to add new types of artists without modifying the core code. This guide walks you through the complete process of adding a new artist type with custom fields.

## 🎯 Prerequisites

Before adding a new artist type, ensure you have:
- ✅ iCastar platform running (http://localhost:8080)
- ✅ Admin access with proper JWT token
- ✅ Understanding of the artist type requirements
- ✅ Database access for verification

## 🚀 Complete Process for Adding New Artist Types

### Step 1: Plan Your New Artist Type

Before implementing, plan your new artist type:

**Example: Adding "STUNT_PERFORMER"**

**Basic Information:**
- Name: `STUNT_PERFORMER`
- Display Name: `Stunt Performer`
- Description: `Professional stunt performers for film, TV, and live events`
- Icon URL: `/icons/stunt-performer.png`
- Sort Order: `11` (after existing types)

**Required Fields:**
- Height (cm)
- Weight (kg)
- Body Type
- Specialization Areas
- Safety Certifications
- Experience Level
- Physical Abilities

**Optional Fields:**
- Training Background
- Awards/Recognition
- Demo Reel
- Insurance Coverage
- Equipment Owned

### Step 2: Create the Artist Type

Use the admin API to create the new artist type:

```bash
# Create new artist type
curl -X POST "http://localhost:8080/api/admin/artist-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "name": "STUNT_PERFORMER",
    "displayName": "Stunt Performer",
    "description": "Professional stunt performers for film, TV, and live events. Specialized in high-risk action sequences, fight choreography, and physical performance.",
    "iconUrl": "/icons/stunt-performer.png",
    "sortOrder": 11
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Artist type created successfully",
  "data": {
    "id": 11,
    "name": "STUNT_PERFORMER",
    "displayName": "Stunt Performer",
    "description": "Professional stunt performers for film, TV, and live events...",
    "iconUrl": "/icons/stunt-performer.png",
    "isActive": true,
    "sortOrder": 11,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Step 3: Add Required Fields

Add the required fields for the new artist type:

```bash
# Add Height field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "height",
    "displayName": "Height",
    "fieldType": "NUMBER",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 1,
    "placeholder": "cm",
    "helpText": "Height in centimeters",
    "validationRules": {
      "min": 150,
      "max": 220
    }
  }'

# Add Weight field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "weight",
    "displayName": "Weight",
    "fieldType": "NUMBER",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 2,
    "placeholder": "kg",
    "helpText": "Weight in kilograms",
    "validationRules": {
      "min": 40,
      "max": 150
    }
  }'

# Add Body Type field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "body_type",
    "displayName": "Body Type",
    "fieldType": "SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 3,
    "helpText": "Select your body type",
    "options": {
      "values": [
        "Athletic",
        "Muscular",
        "Lean",
        "Average",
        "Stocky",
        "Flexible"
      ]
    }
  }'

# Add Specialization Areas field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "specialization_areas",
    "displayName": "Specialization Areas",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 4,
    "helpText": "Select your areas of specialization",
    "options": {
      "values": [
        "Fight Choreography",
        "High Falls",
        "Vehicle Stunts",
        "Fire Stunts",
        "Water Stunts",
        "Acrobatics",
        "Martial Arts",
        "Weapons Handling",
        "Explosive Stunts",
        "Climbing/Rappelling"
      ]
    }
  }'

# Add Safety Certifications field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "safety_certifications",
    "displayName": "Safety Certifications",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 5,
    "helpText": "Select your safety certifications",
    "options": {
      "values": [
        "First Aid/CPR",
        "Fire Safety",
        "Water Safety",
        "High Altitude Safety",
        "Explosive Safety",
        "Vehicle Safety",
        "Rigging Safety",
        "Stunt Coordinator Certification"
      ]
    }
  }'

# Add Experience Level field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "experience_level",
    "displayName": "Experience Level",
    "fieldType": "SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 6,
    "helpText": "Select your experience level",
    "options": {
      "values": [
        "Beginner (0-2 years)",
        "Intermediate (2-5 years)",
        "Advanced (5-10 years)",
        "Expert (10+ years)"
      ]
    }
  }'

# Add Physical Abilities field (Required)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "physical_abilities",
    "displayName": "Physical Abilities",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 7,
    "helpText": "Select your physical abilities",
    "options": {
      "values": [
        "High Pain Tolerance",
        "Excellent Balance",
        "Quick Reflexes",
        "Physical Strength",
        "Flexibility",
        "Endurance",
        "Fearlessness",
        "Coordination"
      ]
    }
  }'
```

### Step 4: Add Optional Fields

Add optional fields for enhanced profile information:

```bash
# Add Training Background field (Optional)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "training_background",
    "displayName": "Training Background",
    "fieldType": "TEXTAREA",
    "isRequired": false,
    "isSearchable": false,
    "sortOrder": 8,
    "placeholder": "Describe your training background...",
    "helpText": "Describe your formal training, workshops, and certifications"
  }'

# Add Awards/Recognition field (Optional)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "awards_recognition",
    "displayName": "Awards & Recognition",
    "fieldType": "TEXTAREA",
    "isRequired": false,
    "isSearchable": false,
    "sortOrder": 9,
    "placeholder": "List your awards and recognition...",
    "helpText": "List any awards, recognition, or notable achievements"
  }'

# Add Demo Reel field (Optional)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "demo_reel_url",
    "displayName": "Demo Reel URL",
    "fieldType": "URL",
    "isRequired": false,
    "isSearchable": false,
    "sortOrder": 10,
    "placeholder": "https://example.com/demo-reel",
    "helpText": "Link to your stunt demo reel or showreel"
  }'

# Add Insurance Coverage field (Optional)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "insurance_coverage",
    "displayName": "Insurance Coverage",
    "fieldType": "SELECT",
    "isRequired": false,
    "isSearchable": true,
    "sortOrder": 11,
    "helpText": "Select your insurance coverage level",
    "options": {
      "values": [
        "Basic Coverage",
        "Standard Coverage",
        "Premium Coverage",
        "Custom Coverage",
        "No Coverage"
      ]
    }
  }'

# Add Equipment Owned field (Optional)
curl -X POST "http://localhost:8080/api/admin/artist-types/11/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN" \
  -d '{
    "fieldName": "equipment_owned",
    "displayName": "Equipment Owned",
    "fieldType": "MULTI_SELECT",
    "isRequired": false,
    "isSearchable": true,
    "sortOrder": 12,
    "helpText": "Select equipment you own",
    "options": {
      "values": [
        "Safety Gear",
        "Protective Equipment",
        "Rigging Equipment",
        "Weapons (Props)",
        "Costumes",
        "Makeup/Prosthetics",
        "Transportation",
        "Communication Devices"
      ]
    }
  }'
```

### Step 5: Verify the New Artist Type

Check that your new artist type was created successfully:

```bash
# Get the new artist type
curl -X GET "http://localhost:8080/api/public/artist-types/11" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
{
  "id": 11,
  "name": "STUNT_PERFORMER",
  "displayName": "Stunt Performer",
  "description": "Professional stunt performers for film, TV, and live events...",
  "iconUrl": "/icons/stunt-performer.png",
  "isActive": true,
  "sortOrder": 11,
  "fields": [
    {
      "id": 101,
      "fieldName": "height",
      "displayName": "Height",
      "fieldType": "NUMBER",
      "isRequired": true,
      "isSearchable": true,
      "sortOrder": 1,
      "placeholder": "cm",
      "helpText": "Height in centimeters"
    },
    {
      "id": 102,
      "fieldName": "weight",
      "displayName": "Weight",
      "fieldType": "NUMBER",
      "isRequired": true,
      "isSearchable": true,
      "sortOrder": 2,
      "placeholder": "kg",
      "helpText": "Weight in kilograms"
    }
    // ... other fields
  ]
}
```

### Step 6: Test Creating a Stunt Performer Profile

Now test creating an artist profile with the new type:

```bash
# First, register a user
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "stunt.performer@example.com",
    "mobile": "+919876543211",
    "password": "password123",
    "role": "ARTIST",
    "firstName": "Mike",
    "lastName": "Stunt"
  }'

# Login to get JWT token
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "stunt.performer@example.com",
    "password": "password123"
  }'

# Create stunt performer profile
curl -X POST "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "artistTypeId": 11,
    "firstName": "Mike",
    "lastName": "Stunt",
    "stageName": "Mike the Stuntman",
    "bio": "Professional stunt performer with 8 years of experience in high-risk action sequences. Specialized in fight choreography and high falls.",
    "dateOfBirth": "1985-03-20",
    "gender": "MALE",
    "location": "Los Angeles, CA, USA",
    "experienceYears": 8,
    "hourlyRate": 8000.00,
    "skills": [
      "Fight Choreography",
      "High Falls",
      "Vehicle Stunts",
      "Martial Arts",
      "Safety Coordination"
    ],
    "dynamicFields": [
      {
        "artistTypeFieldId": 101,
        "fieldValue": "185"
      },
      {
        "artistTypeFieldId": 102,
        "fieldValue": "85"
      },
      {
        "artistTypeFieldId": 103,
        "fieldValue": "Athletic"
      },
      {
        "artistTypeFieldId": 104,
        "fieldValue": "Fight Choreography,High Falls,Vehicle Stunts"
      },
      {
        "artistTypeFieldId": 105,
        "fieldValue": "First Aid/CPR,Fire Safety,Stunt Coordinator Certification"
      },
      {
        "artistTypeFieldId": 106,
        "fieldValue": "Advanced (5-10 years)"
      },
      {
        "artistTypeFieldId": 107,
        "fieldValue": "High Pain Tolerance,Excellent Balance,Quick Reflexes,Physical Strength"
      }
    ]
  }'
```

## 🎨 Field Types Available

### 1. TEXT
Single-line text input
```json
{
  "fieldType": "TEXT",
  "placeholder": "Enter text...",
  "validationRules": {
    "maxLength": 100
  }
}
```

### 2. TEXTAREA
Multi-line text input
```json
{
  "fieldType": "TEXTAREA",
  "placeholder": "Enter description...",
  "validationRules": {
    "maxLength": 1000
  }
}
```

### 3. NUMBER
Numeric input
```json
{
  "fieldType": "NUMBER",
  "placeholder": "0",
  "validationRules": {
    "min": 0,
    "max": 1000
  }
}
```

### 4. EMAIL
Email validation
```json
{
  "fieldType": "EMAIL",
  "placeholder": "email@example.com"
}
```

### 5. PHONE
Phone number validation
```json
{
  "fieldType": "PHONE",
  "placeholder": "+1234567890"
}
```

### 6. URL
URL validation
```json
{
  "fieldType": "URL",
  "placeholder": "https://example.com"
}
```

### 7. DATE
Date picker
```json
{
  "fieldType": "DATE",
  "placeholder": "YYYY-MM-DD"
}
```

### 8. BOOLEAN
Checkbox
```json
{
  "fieldType": "BOOLEAN",
  "helpText": "Check if applicable"
}
```

### 9. SELECT
Single selection dropdown
```json
{
  "fieldType": "SELECT",
  "options": {
    "values": ["Option 1", "Option 2", "Option 3"]
  }
}
```

### 10. MULTI_SELECT
Multiple selection
```json
{
  "fieldType": "MULTI_SELECT",
  "options": {
    "values": ["Option 1", "Option 2", "Option 3"]
  }
}
```

### 11. CHECKBOX
Multiple checkboxes
```json
{
  "fieldType": "CHECKBOX",
  "options": {
    "values": ["Checkbox 1", "Checkbox 2", "Checkbox 3"]
  }
}
```

### 12. RADIO
Radio buttons
```json
{
  "fieldType": "RADIO",
  "options": {
    "values": ["Radio 1", "Radio 2", "Radio 3"]
  }
}
```

### 13. FILE
File upload
```json
{
  "fieldType": "FILE",
  "helpText": "Upload your file (max 10MB)"
}
```

### 14. JSON
JSON data
```json
{
  "fieldType": "JSON",
  "helpText": "Enter valid JSON data"
}
```

## 🔧 Advanced Configuration

### Validation Rules
```json
{
  "validationRules": {
    "min": 0,
    "max": 100,
    "minLength": 2,
    "maxLength": 50,
    "pattern": "^[A-Za-z\\s]+$",
    "required": true
  }
}
```

### Field Options
```json
{
  "options": {
    "values": ["Option 1", "Option 2"],
    "allowCustom": true,
    "multiple": true,
    "defaultValue": "Option 1"
  }
}
```

### Search Configuration
```json
{
  "isSearchable": true,
  "searchWeight": 1.0,
  "searchType": "EXACT" // EXACT, PARTIAL, FUZZY
}
```

## 🎯 Real-World Examples

### Example 1: Adding "MUSIC_PRODUCER"
```bash
# Create Music Producer type
curl -X POST "http://localhost:8080/api/admin/artist-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "name": "MUSIC_PRODUCER",
    "displayName": "Music Producer",
    "description": "Professional music producers and sound engineers",
    "iconUrl": "/icons/music-producer.png",
    "sortOrder": 12
  }'

# Add required fields
curl -X POST "http://localhost:8080/api/admin/artist-types/12/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "fieldName": "music_genres",
    "displayName": "Music Genres",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 1,
    "options": {
      "values": ["Pop", "Rock", "Hip-Hop", "Electronic", "Classical", "Jazz", "Country", "R&B"]
    }
  }'
```

### Example 2: Adding "VOICE_ARTIST"
```bash
# Create Voice Artist type
curl -X POST "http://localhost:8080/api/admin/artist-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "name": "VOICE_ARTIST",
    "displayName": "Voice Artist",
    "description": "Professional voice artists for commercials, animations, and audiobooks",
    "iconUrl": "/icons/voice-artist.png",
    "sortOrder": 13
  }'

# Add voice-specific fields
curl -X POST "http://localhost:8080/api/admin/artist-types/13/fields" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "fieldName": "voice_types",
    "displayName": "Voice Types",
    "fieldType": "MULTI_SELECT",
    "isRequired": true,
    "isSearchable": true,
    "sortOrder": 1,
    "options": {
      "values": ["Male", "Female", "Child", "Elderly", "Character Voice", "Narrator", "Commercial"]
    }
  }'
```

## 🛠️ Admin Management Commands

### List All Artist Types
```bash
curl -X GET "http://localhost:8080/api/admin/artist-types" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Update Artist Type
```bash
curl -X PUT "http://localhost:8080/api/admin/artist-types/11" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "displayName": "Updated Stunt Performer",
    "description": "Updated description",
    "isActive": true
  }'
```

### Deactivate Artist Type
```bash
curl -X PUT "http://localhost:8080/api/admin/artist-types/11" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "isActive": false
  }'
```

### Update Field
```bash
curl -X PUT "http://localhost:8080/api/admin/artist-types/11/fields/101" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -d '{
    "displayName": "Updated Height",
    "isRequired": false,
    "helpText": "Updated help text"
  }'
```

### Delete Field
```bash
curl -X DELETE "http://localhost:8080/api/admin/artist-types/11/fields/101" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

## 🚨 Common Issues and Solutions

### Issue 1: "Artist type name already exists"
**Solution:** Use a unique name. Check existing types first.

### Issue 2: "Field name already exists for this artist type"
**Solution:** Use unique field names within the same artist type.

### Issue 3: "Invalid field type"
**Solution:** Use only supported field types (TEXT, NUMBER, SELECT, etc.).

### Issue 4: "Validation rules format invalid"
**Solution:** Ensure validation rules are in proper JSON format.

### Issue 5: "Options required for SELECT field"
**Solution:** Provide options array for SELECT, MULTI_SELECT, CHECKBOX, and RADIO fields.

## 📊 Best Practices

### 1. Naming Conventions
- Use UPPER_SNAKE_CASE for artist type names
- Use descriptive display names
- Keep field names short but meaningful

### 2. Field Design
- Make essential fields required
- Use appropriate field types
- Provide helpful placeholder text
- Add validation rules where needed

### 3. Search Optimization
- Mark important fields as searchable
- Use appropriate search weights
- Consider search performance

### 4. User Experience
- Order fields logically
- Group related fields together
- Provide clear help text
- Use consistent terminology

## 🔄 Workflow Summary

1. **Plan** → Define artist type requirements and fields
2. **Create Type** → Add the new artist type via admin API
3. **Add Fields** → Create all required and optional fields
4. **Test** → Verify the new type works correctly
5. **Deploy** → Make the new type available to users
6. **Monitor** → Track usage and gather feedback

## 🎯 Next Steps After Adding New Artist Type

Once a new artist type is added:
- ✅ Artists can select it during profile creation
- ✅ The type appears in public artist type listings
- ✅ Search and filtering work with the new type
- ✅ Admin can manage artists of the new type
- ✅ Reports and analytics include the new type

## 📝 Database Schema Impact

Adding new artist types affects these tables:
- `artist_types` - New artist type record
- `artist_type_fields` - New field definitions
- `artist_profile_fields` - Dynamic field values (when artists use the type)

No code changes are required - the system is fully dynamic!

---

## 📞 Support

For technical support:
- Check the API documentation
- Review existing artist types for reference
- Test thoroughly before deploying
- Contact the development team for complex requirements

**Happy Artist Type Creation! 🎨✨**

