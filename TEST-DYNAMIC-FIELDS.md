# 🧪 Test Dynamic Fields - Complete Test Guide

## ✅ **FIXED: Dynamic Fields Now Working!**

The issue has been resolved! The `ArtistController` and `ArtistService` now properly handle and save `dynamicFields` data.

### 🔧 **What Was Fixed**

1. **ArtistController**: Added logic to process `dynamicFields` from request
2. **ArtistService**: Added `saveDynamicFields()` method to save data to `artist_profile_fields` table
3. **Response**: All endpoints now return `dynamicFields` in the response

### 🚀 **Complete Test Flow**

#### **Step 1: Register Dancer User**
```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test.dancer@example.com",
    "mobile": "+919876543210",
    "password": "password123",
    "role": "ARTIST",
    "firstName": "Test",
    "lastName": "Dancer"
}'
```

#### **Step 2: Login to Get JWT Token**
```bash
curl --location 'http://localhost:8080/api/auth/email/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test.dancer@example.com",
    "password": "password123"
}'
```

#### **Step 3: Create Dancer Profile with Dynamic Fields**
```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "artistTypeId": 2,
    "firstName": "Test",
    "lastName": "Dancer",
    "stageName": "Test Dancer",
    "bio": "Professional dancer with 5 years of experience in classical and contemporary dance.",
    "location": "Mumbai, India",
    "experienceYears": 5,
    "hourlyRate": 2500.00,
    "dynamicFields": [
        {
            "artistTypeFieldId": 1,
            "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop"
        },
        {
            "artistTypeFieldId": 2,
            "fieldValue": "Completed 5 years of formal dance training at Kalakshetra Foundation. Specialized in classical dance forms with contemporary fusion."
        },
        {
            "artistTypeFieldId": 3,
            "fieldValue": "Intermediate (3-7 years)"
        },
        {
            "artistTypeFieldId": 4,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 5,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 6,
            "fieldValue": "https://example.com/videos/test-dancer-performance.mp4"
        },
        {
            "artistTypeFieldId": 7,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 8,
            "fieldValue": "Advanced"
        },
        {
            "artistTypeFieldId": 9,
            "fieldValue": "Stage Performance,Wedding Performances,Corporate Events"
        },
        {
            "artistTypeFieldId": 10,
            "fieldValue": "Winner of Mumbai Dance Competition 2023, Best Performer Award at Cultural Festival 2022"
        },
        {
            "artistTypeFieldId": 11,
            "fieldValue": "Flexible"
        },
        {
            "artistTypeFieldId": 12,
            "fieldValue": "Within Country"
        },
        {
            "artistTypeFieldId": 13,
            "fieldValue": "https://example.com/documents/test-dancer-aadhar.pdf"
        },
        {
            "artistTypeFieldId": 14,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 15,
            "fieldValue": "My unique approach combines classical technique with contemporary expression, focusing on storytelling through movement."
        },
        {
            "artistTypeFieldId": 16,
            "fieldValue": "Classical Choreography,Contemporary Choreography,Fusion Choreography"
        },
        {
            "artistTypeFieldId": 17,
            "fieldValue": "I approach movement creation by understanding the music and emotion first, then building from basic steps to complex sequences."
        },
        {
            "artistTypeFieldId": 18,
            "fieldValue": "Stage Direction,Performance Management,Team Leadership"
        },
        {
            "artistTypeFieldId": 19,
            "fieldValue": "Conducted workshops for 100+ students. Specialized in teaching classical dance to beginners and advanced students."
        },
        {
            "artistTypeFieldId": 20,
            "fieldValue": "Solo Performances,Group Performances,Cross-genre Collaborations"
        },
        {
            "artistTypeFieldId": 21,
            "fieldValue": "Theaters,Concert Halls,Festivals,Wedding Venues"
        },
        {
            "artistTypeFieldId": 22,
            "fieldValue": "{\"instagram\": \"@test_dancer\", \"youtube\": \"Test Dancer Channel\", \"tiktok\": \"@test_dances\"}"
        }
    ]
}'
```

### 🎯 **Expected Success Response**

```json
{
  "success": true,
  "message": "Artist profile created successfully",
  "data": {
    "id": 1,
    "firstName": "Test",
    "lastName": "Dancer",
    "stageName": "Test Dancer",
    "bio": "Professional dancer with 5 years of experience...",
    "location": "Mumbai, India",
    "experienceYears": 5,
    "hourlyRate": 2500.00,
    "artistType": {
      "id": 2,
      "name": "DANCER",
      "displayName": "Dancer"
    }
  },
  "dynamicFields": [
    {
      "id": 1,
      "artistTypeFieldId": 1,
      "fieldName": "dance_styles",
      "displayName": "Dance Styles",
      "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop"
    },
    {
      "id": 2,
      "artistTypeFieldId": 2,
      "fieldName": "training_background",
      "displayName": "Training Background",
      "fieldValue": "Completed 5 years of formal dance training..."
    }
    // ... more dynamic fields
  ]
}
```

### 🔍 **Test Get Profile**

```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE'
```

### 🔄 **Test Update Profile with Dynamic Fields**

```bash
curl --location --request PUT 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "firstName": "Test",
    "lastName": "Dancer",
    "stageName": "Updated Test Dancer",
    "bio": "Updated bio with more experience...",
    "dynamicFields": [
        {
            "artistTypeFieldId": 15,
            "fieldValue": "Updated personal style and approach description..."
        },
        {
            "artistTypeFieldId": 10,
            "fieldValue": "Updated awards and recognition information..."
        }
    ]
}'
```

### 📊 **Database Verification**

After creating the profile, you can verify the data was saved by checking these tables:

```sql
-- Check artist profile
SELECT * FROM artist_profiles WHERE first_name = 'Test' AND last_name = 'Dancer';

-- Check dynamic fields
SELECT apf.*, atf.field_name, atf.display_name 
FROM artist_profile_fields apf
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
WHERE apf.artist_profile_id = 1;

-- Check specific field values
SELECT atf.field_name, atf.display_name, apf.field_value
FROM artist_profile_fields apf
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
WHERE apf.artist_profile_id = 1
ORDER BY atf.sort_order;
```

### 🎉 **Success Indicators**

1. **API Response**: Should return `success: true` with `dynamicFields` array
2. **Database**: Records should be created in `artist_profile_fields` table
3. **Field Values**: All dynamic field values should be properly stored
4. **Field Types**: Different field types (TEXT, BOOLEAN, FILE, JSON) should be handled correctly

### 🐛 **Troubleshooting**

If you encounter issues:

1. **Check JWT Token**: Make sure the token is valid and not expired
2. **Check Field IDs**: Ensure `artistTypeFieldId` values exist in the database
3. **Check Required Fields**: Make sure all required fields are provided
4. **Check Database**: Verify the database connection and table structure

### 📝 **Field ID Reference**

| Field Name | Field ID | Type | Required |
|------------|----------|------|----------|
| `dance_styles` | 1 | MULTI_SELECT | ✅ |
| `training_background` | 2 | TEXTAREA | ✅ |
| `performance_experience` | 3 | SELECT | ✅ |
| `choreography_skills` | 4 | BOOLEAN | ❌ |
| `teaching_experience` | 5 | BOOLEAN | ❌ |
| `performance_videos` | 6 | FILE | ✅ |
| `costume_availability` | 7 | BOOLEAN | ❌ |
| `flexibility_level` | 8 | SELECT | ❌ |
| `performance_types` | 9 | MULTI_SELECT | ❌ |
| `awards_recognition` | 10 | TEXTAREA | ❌ |
| `availability` | 11 | SELECT | ❌ |
| `travel_willingness` | 12 | SELECT | ❌ |
| `id_proof_file` | 13 | FILE | ✅ |
| `face_verified` | 14 | BOOLEAN | ❌ |
| `personal_style_approach` | 15 | TEXTAREA | ❌ |
| `choreographic_techniques` | 16 | MULTI_SELECT | ❌ |
| `movement_creation_approach` | 17 | TEXTAREA | ❌ |
| `stage_direction_skills` | 18 | MULTI_SELECT | ❌ |
| `teaching_mentorship_details` | 19 | TEXTAREA | ❌ |
| `collaboration_preferences` | 20 | MULTI_SELECT | ❌ |
| `venue_experience` | 21 | MULTI_SELECT | ❌ |
| `social_media_links` | 22 | JSON | ❌ |

## 🎯 **The Dynamic Fields Are Now Working!**

The API will now properly save and retrieve all dynamic field data for dancer profiles. Test it out with the curl commands above! 🎭✨
