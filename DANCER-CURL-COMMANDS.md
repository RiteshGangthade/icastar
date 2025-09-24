# 💃 Dancer Profile - Complete Curl Commands

## 📋 Current Dancer Fields (22 Total)

Based on the system analysis, here are all the dancer fields with their types and requirements:

| Field Name | Display Name | Type | Required | Searchable | Purpose |
|------------|--------------|------|----------|------------|---------|
| `dance_styles` | Dance Styles | MULTI_SELECT | ✅ | ✅ | Core dance specialties |
| `training_background` | Training Background | TEXTAREA | ✅ | ✅ | Formal training details |
| `performance_experience` | Performance Experience | SELECT | ✅ | ✅ | Experience level |
| `choreography_skills` | Choreography Skills | BOOLEAN | ❌ | ✅ | Can create choreography |
| `teaching_experience` | Teaching Experience | BOOLEAN | ❌ | ✅ | Has teaching experience |
| `performance_videos` | Performance Videos | FILE | ✅ | ❌ | Video portfolio |
| `costume_availability` | Costume Availability | BOOLEAN | ❌ | ❌ | Owns costumes |
| `flexibility_level` | Flexibility Level | SELECT | ❌ | ✅ | Physical flexibility |
| `performance_types` | Performance Types | MULTI_SELECT | ❌ | ✅ | Types of performances |
| `awards_recognition` | Awards & Recognition | TEXTAREA | ❌ | ❌ | Achievements |
| `availability` | Availability | SELECT | ❌ | ✅ | Time availability |
| `travel_willingness` | Travel Willingness | SELECT | ❌ | ✅ | Travel preferences |
| `id_proof_file` | ID Proof Document | FILE | ✅ | ❌ | Identity verification |
| `face_verified` | Face Verification | BOOLEAN | ❌ | ✅ | Trust verification |
| `personal_style_approach` | Personal Style & Approach | TEXTAREA | ❌ | ✅ | Artistic philosophy |
| `choreographic_techniques` | Choreographic Techniques | MULTI_SELECT | ❌ | ✅ | Choreography methods |
| `movement_creation_approach` | Movement Creation Approach | TEXTAREA | ❌ | ❌ | Teaching methodology |
| `stage_direction_skills` | Stage Direction & Production Skills | MULTI_SELECT | ❌ | ✅ | Production management |
| `teaching_mentorship_details` | Teaching & Mentorship Experience | TEXTAREA | ❌ | ✅ | Detailed teaching experience |
| `collaboration_preferences` | Collaboration Preferences | MULTI_SELECT | ❌ | ✅ | Work preferences |
| `venue_experience` | Performance Venue Experience | MULTI_SELECT | ❌ | ✅ | Venue experience |
| `social_media_links` | Social Media Presence | JSON | ❌ | ❌ | Online presence |

## 🚀 Complete Dancer Profile Creation Flow

### Step 1: Register Dancer User

```bash
curl --location 'http://localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "priya.dancer@example.com",
    "mobile": "+919876543210",
    "password": "password123",
    "role": "ARTIST",
    "firstName": "Priya",
    "lastName": "Sharma"
}'
```

### Step 2: Login to Get JWT Token

```bash
curl --location 'http://localhost:8080/api/auth/email/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "priya.dancer@example.com",
    "password": "password123"
}'
```

### Step 3: Create Complete Dancer Profile

```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "artistTypeId": 2,
    "firstName": "Priya",
    "lastName": "Sharma",
    "stageName": "Priya the Dancer",
    "bio": "Professional classical and contemporary dancer with 8 years of experience. Specialized in Bharatanatyam and Bollywood dance styles. Performed at various cultural events, weddings, and corporate functions across India.",
    "dateOfBirth": "1995-06-15",
    "gender": "FEMALE",
    "location": "Mumbai, Maharashtra, India",
    "profileImageUrl": "https://example.com/profile-images/priya-sharma.jpg",
    "portfolioUrls": [
        "https://example.com/portfolio/priya-bharatanatyam.mp4",
        "https://example.com/portfolio/priya-bollywood.mp4",
        "https://example.com/portfolio/priya-contemporary.mp4"
    ],
    "skills": [
        "Bharatanatyam",
        "Bollywood Dance",
        "Contemporary",
        "Classical Dance",
        "Choreography",
        "Teaching"
    ],
    "experienceYears": 8,
    "hourlyRate": 3000.00,
    "dynamicFields": [
        {
            "artistTypeFieldId": 1,
            "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop,Bollywood Dance"
        },
        {
            "artistTypeFieldId": 2,
            "fieldValue": "Completed 8 years of formal Bharatanatyam training under Guru Smt. Lakshmi Narayanan. Attended workshops by renowned dancers including Leela Samson and Malavika Sarukkai. Graduated from Kalakshetra Foundation with distinction in classical dance."
        },
        {
            "artistTypeFieldId": 3,
            "fieldValue": "Professional (7+ years)"
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
            "fieldValue": "https://example.com/videos/priya-performance.mp4"
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
            "fieldValue": "Stage Performance,Wedding Performances,Corporate Events,Festivals,Theater"
        },
        {
            "artistTypeFieldId": 10,
            "fieldValue": "Winner of National Dance Competition 2022, Best Performer Award at Mumbai Cultural Festival 2021, Featured in Times of India for outstanding classical dance performance."
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
            "fieldValue": "https://example.com/documents/priya-aadhar.pdf"
        },
        {
            "artistTypeFieldId": 14,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 15,
            "fieldValue": "My unique approach combines classical technique with contemporary expression, focusing on storytelling through movement and emotional connection with the audience. I believe dance is a powerful medium for cultural preservation and artistic innovation."
        },
        {
            "artistTypeFieldId": 16,
            "fieldValue": "Classical Choreography,Contemporary Choreography,Fusion Choreography,Group Choreography,Storytelling Through Dance"
        },
        {
            "artistTypeFieldId": 17,
            "fieldValue": "I approach movement creation by first understanding the music and emotion, then building from basic steps to complex sequences. I work with individual dancers to develop their unique style while maintaining group cohesion and ensuring each performer feels confident and expressive."
        },
        {
            "artistTypeFieldId": 18,
            "fieldValue": "Stage Direction,Performance Management,Rehearsal Organization,Team Leadership,Event Planning"
        },
        {
            "artistTypeFieldId": 19,
            "fieldValue": "Conducted workshops for 200+ students across Mumbai. Specialized in teaching classical dance to beginners and advanced students. Mentored 15 students who went on to perform professionally. Developed a unique teaching methodology that combines traditional techniques with modern approaches."
        },
        {
            "artistTypeFieldId": 20,
            "fieldValue": "Solo Performances,Group Performances,Cross-genre Collaborations,Educational Workshops,Charity Performances,Cultural Exchange Programs"
        },
        {
            "artistTypeFieldId": 21,
            "fieldValue": "Theaters,Concert Halls,Festivals,Wedding Venues,Corporate Events,Television Studios,Cultural Centers"
        },
        {
            "artistTypeFieldId": 22,
            "fieldValue": "{\"instagram\": \"@priya_dancer\", \"youtube\": \"Priya Dance Channel\", \"tiktok\": \"@priya_dances\", \"facebook\": \"Priya Sharma Dancer\"}"
        }
    ]
}'
```

## 🎭 Alternative Dancer Profile (Hip-Hop Dancer)

```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "artistTypeId": 2,
    "firstName": "Rahul",
    "lastName": "Kumar",
    "stageName": "Rahul B-Boy",
    "bio": "Professional hip-hop and breakdance artist with 6 years of experience. Specialized in breaking, popping, and locking. Performed at various street dance competitions and music videos. Passionate about urban dance culture and community building.",
    "dateOfBirth": "1998-03-22",
    "gender": "MALE",
    "location": "Delhi, India",
    "profileImageUrl": "https://example.com/profile-images/rahul-kumar.jpg",
    "portfolioUrls": [
        "https://example.com/portfolio/rahul-breaking.mp4",
        "https://example.com/portfolio/rahul-popping.mp4",
        "https://example.com/portfolio/rahul-battles.mp4"
    ],
    "skills": [
        "Breaking",
        "Popping",
        "Locking",
        "Hip-Hop",
        "Street Dance",
        "Freestyle"
    ],
    "experienceYears": 6,
    "hourlyRate": 2500.00,
    "dynamicFields": [
        {
            "artistTypeFieldId": 1,
            "fieldValue": "Hip-Hop,Breakdance,Popping,Locking,Street Dance"
        },
        {
            "artistTypeFieldId": 2,
            "fieldValue": "Self-taught hip-hop dancer with 6 years of experience. Attended workshops by international B-boys including Lilou and Roxrite. Participated in various street dance battles and competitions across India."
        },
        {
            "artistTypeFieldId": 3,
            "fieldValue": "Advanced (3-7 years)"
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
            "fieldValue": "https://example.com/videos/rahul-breaking.mp4"
        },
        {
            "artistTypeFieldId": 7,
            "fieldValue": "false"
        },
        {
            "artistTypeFieldId": 8,
            "fieldValue": "Extreme"
        },
        {
            "artistTypeFieldId": 9,
            "fieldValue": "Music Videos,Competitions,Festivals,Street Performances,Corporate Events"
        },
        {
            "artistTypeFieldId": 10,
            "fieldValue": "Winner of Delhi Street Dance Championship 2023, Runner-up at National Hip-Hop Competition 2022, Featured in multiple music videos."
        },
        {
            "artistTypeFieldId": 11,
            "fieldValue": "Part-time"
        },
        {
            "artistTypeFieldId": 12,
            "fieldValue": "Within State"
        },
        {
            "artistTypeFieldId": 13,
            "fieldValue": "https://example.com/documents/rahul-pan.pdf"
        },
        {
            "artistTypeFieldId": 14,
            "fieldValue": "true"
        },
        {
            "artistTypeFieldId": 15,
            "fieldValue": "My style is raw, energetic, and authentic. I focus on the fundamentals of hip-hop culture while pushing creative boundaries. I believe in the power of dance to bring communities together and express individual creativity."
        },
        {
            "artistTypeFieldId": 16,
            "fieldValue": "Street Dance Choreography,Competition Choreography,Music Video Choreography,Group Choreography"
        },
        {
            "artistTypeFieldId": 17,
            "fieldValue": "I create movement by freestyling first, then structuring the best elements into choreography. I work with dancers of all levels, from beginners to advanced, focusing on individual expression while maintaining group synchronization."
        },
        {
            "artistTypeFieldId": 18,
            "fieldValue": "Event Planning,Team Leadership,Performance Management"
        },
        {
            "artistTypeFieldId": 19,
            "fieldValue": "Taught hip-hop classes to 100+ students in Delhi. Organized community dance workshops and mentored young dancers. Developed a structured curriculum for street dance education."
        },
        {
            "artistTypeFieldId": 20,
            "fieldValue": "Solo Performances,Group Performances,Cross-genre Collaborations,Educational Workshops,Community Outreach,Competition Participation"
        },
        {
            "artistTypeFieldId": 21,
            "fieldValue": "Street Performances,Competitions,Festivals,Music Videos,Corporate Events,Online Platforms"
        },
        {
            "artistTypeFieldId": 22,
            "fieldValue": "{\"instagram\": \"@rahul_bboy\", \"youtube\": \"Rahul B-Boy Channel\", \"tiktok\": \"@rahul_dances\"}"
        }
    ]
}'
```

## 🔍 Field Value Examples by Type

### **MULTI_SELECT Fields**
```json
{
    "artistTypeFieldId": 1,
    "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop,Bollywood Dance"
}
```

### **TEXTAREA Fields**
```json
{
    "artistTypeFieldId": 2,
    "fieldValue": "Completed 8 years of formal Bharatanatyam training under Guru Smt. Lakshmi Narayanan..."
}
```

### **SELECT Fields**
```json
{
    "artistTypeFieldId": 3,
    "fieldValue": "Professional (7+ years)"
}
```

### **BOOLEAN Fields**
```json
{
    "artistTypeFieldId": 4,
    "fieldValue": "true"
}
```

### **FILE Fields**
```json
{
    "artistTypeFieldId": 6,
    "fieldValue": "https://example.com/videos/performance.mp4"
}
```

### **JSON Fields**
```json
{
    "artistTypeFieldId": 22,
    "fieldValue": "{\"instagram\": \"@dancer\", \"youtube\": \"Dancer Channel\", \"tiktok\": \"@dancer\"}"
}
```

## 🎯 Field ID Reference

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

## 🚀 Quick Test Commands

### **Test 1: Get Dancer Fields**
```bash
curl --location 'http://localhost:8080/api/public/artist-types/2/fields' \
--header 'Content-Type: application/json'
```

### **Test 2: Get Dancer Profile**
```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE'
```

### **Test 3: Update Dancer Profile**
```bash
curl --location --request PUT 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "dynamicFields": [
        {
            "artistTypeFieldId": 15,
            "fieldValue": "Updated personal style description..."
        }
    ]
}'
```

## 📝 Notes

1. **Field IDs**: Use the correct `artistTypeFieldId` for each field
2. **Required Fields**: Make sure to provide values for all required fields
3. **File Uploads**: For FILE fields, provide URLs to uploaded files
4. **JSON Fields**: Use proper JSON format for complex data
5. **Multi-select**: Use comma-separated values for MULTI_SELECT fields
6. **Boolean**: Use "true" or "false" strings for BOOLEAN fields

## 🎉 Success Response

```json
{
  "success": true,
  "message": "Artist profile created successfully",
  "data": {
    "id": 1,
    "firstName": "Priya",
    "lastName": "Sharma",
    "stageName": "Priya the Dancer",
    "bio": "Professional classical and contemporary dancer...",
    "location": "Mumbai, Maharashtra, India",
    "experienceYears": 8,
    "hourlyRate": 3000.00,
    "dynamicFields": [
      {
        "id": 1,
        "artistTypeFieldId": 1,
        "fieldName": "dance_styles",
        "displayName": "Dance Styles",
        "fieldValue": "Bharatanatyam,Contemporary,Hip-Hop,Bollywood Dance"
      }
    ]
  }
}
```

Use these curl commands to create comprehensive dancer profiles with all the dynamic fields! 🎭✨
