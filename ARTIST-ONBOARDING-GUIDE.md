# 🎭 iCastar Platform - Complete Artist Onboarding Guide

## 📋 Overview

This comprehensive guide walks you through the complete process of adding an artist to the iCastar platform. The platform supports a dynamic artist type system with custom fields for different types of artists (Actor, Dancer, Singer, Director, etc.).

## 🎯 Prerequisites

Before adding an artist, ensure you have:
- ✅ iCastar platform running (http://localhost:8080)
- ✅ Database properly configured
- ✅ Artist types initialized in the system
- ✅ Valid user credentials for testing

## 🚀 Complete Artist Addition Process

### Step 1: Check Available Artist Types

First, verify what artist types are available in the system:

```bash
# Get all available artist types
curl -X GET "http://localhost:8080/api/public/artist-types" \
  -H "Content-Type: application/json"
```

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "ACTOR",
    "displayName": "Actor",
    "description": "Film, TV, and Theater Actors",
    "iconUrl": "/icons/actor.png",
    "isActive": true,
    "sortOrder": 1,
    "fields": [
      {
        "id": 1,
        "fieldName": "height",
        "displayName": "Height",
        "fieldType": "TEXT",
        "isRequired": true,
        "isSearchable": true,
        "sortOrder": 1,
        "placeholder": "cm",
        "helpText": "Height in centimeters"
      }
    ]
  }
]
```

### Step 2: Get Artist Type Fields (Optional)

To see all fields for a specific artist type:

```bash
# Get fields for Actor type (ID: 1)
curl -X GET "http://localhost:8080/api/public/artist-types/1/fields" \
  -H "Content-Type: application/json"
```

### Step 3: User Registration

Before creating an artist profile, you need to register a user account:

```bash
# Register a new user with ARTIST role
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.actor@example.com",
    "mobile": "+919876543210",
    "password": "password123",
    "role": "ARTIST",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "john.actor@example.com",
    "mobile": "+919876543210",
    "role": "ARTIST",
    "status": "ACTIVE",
    "isVerified": true
  }
}
```

### Step 4: User Authentication

Login to get JWT token for authenticated requests:

```bash
# Option 1: OTP-based login (if implemented)
curl -X POST "http://localhost:8080/api/auth/otp/send" \
  -H "Content-Type: application/json" \
  -d '{
    "mobile": "+919876543210"
  }'

# Then verify OTP
curl -X POST "http://localhost:8080/api/auth/otp/verify" \
  -H "Content-Type: application/json" \
  -d '{
    "mobile": "+919876543210",
    "otp": "123456"
  }'

# Option 2: Email/Password login (if implemented)
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.actor@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "john.actor@example.com",
      "role": "ARTIST"
    }
  }
}
```

### Step 5: Create Artist Profile

Now create the artist profile with the JWT token:

```bash
# Create artist profile
curl -X POST "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "artistTypeId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "stageName": "Johnny Actor",
    "bio": "Professional actor with 5 years of experience in theater and film. Specialized in dramatic roles and character acting.",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE",
    "location": "Mumbai, Maharashtra, India",
    "profileImageUrl": "https://example.com/profile-images/john-doe.jpg",
    "portfolioUrls": [
      "https://example.com/portfolio/john-doe-reel.mp4",
      "https://example.com/portfolio/john-doe-headshots.zip"
    ],
    "skills": [
      "Acting",
      "Dancing",
      "Singing",
      "Character Development",
      "Improvisation"
    ],
    "experienceYears": 5,
    "hourlyRate": 5000.00,
    "dynamicFields": [
      {
        "artistTypeFieldId": 1,
        "fieldValue": "180"
      },
      {
        "artistTypeFieldId": 2,
        "fieldValue": "75"
      }
    ]
  }'
```

**Expected Response:**
```json
{
  "success": true,
  "message": "Artist profile created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "stageName": "Johnny Actor",
    "bio": "Professional actor with 5 years of experience...",
    "dateOfBirth": "1990-05-15",
    "gender": "MALE",
    "location": "Mumbai, Maharashtra, India",
    "profileImageUrl": "https://example.com/profile-images/john-doe.jpg",
    "portfolioUrls": "https://example.com/portfolio/john-doe-reel.mp4,https://example.com/portfolio/john-doe-headshots.zip",
    "skills": "Acting,Dancing,Singing,Character Development,Improvisation",
    "experienceYears": 5,
    "hourlyRate": 5000.00,
    "isVerifiedBadge": false,
    "totalApplications": 0,
    "successfulHires": 0,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Step 6: Verify Artist Profile Creation

Check if the artist profile was created successfully:

```bash
# Get current artist profile
curl -X GET "http://localhost:8080/api/artists/profile" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Step 7: Update Artist Profile (Optional)

You can update the artist profile anytime:

```bash
# Update artist profile
curl -X PUT "http://localhost:8080/api/artists/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "stageName": "Johnny Actor",
    "bio": "Updated bio with more experience and achievements",
    "hourlyRate": 6000.00,
    "skills": [
      "Acting",
      "Dancing",
      "Singing",
      "Character Development",
      "Improvisation",
      "Voice Acting"
    ]
  }'
```

### Step 8: Request Verification Badge (Optional)

Artists can request a verification badge:

```bash
# Request verification badge
curl -X POST "http://localhost:8080/api/artists/profile/verify" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🎨 Artist Types and Their Specific Fields

### 1. 🎬 Actor
**Required Fields:**
- Height (cm)
- Weight (kg)
- Body Type
- Hair Color
- Eye Color
- Skin Tone
- Languages Spoken
- Acting Experience (years)

**Optional Fields:**
- Special Skills
- Demo Reel URL
- Headshots
- Training Background
- Awards/Recognition

### 2. 💃 Dancer
**Required Fields:**
- Dance Styles
- Training Background
- Performance Experience (years)
- Choreography Skills

**Optional Fields:**
- Teaching Experience
- Performance Videos
- Costume Availability
- Flexibility Level

### 3. 🎤 Singer
**Required Fields:**
- Vocal Range
- Music Genres
- Instruments Played
- Recording Experience (years)

**Optional Fields:**
- Live Performance Experience
- Demo Tracks
- Original Compositions
- Language Proficiency

### 4. 🎬 Director
**Required Fields:**
- Directing Experience (years)
- Project Types
- Equipment Owned
- Team Size Managed

**Optional Fields:**
- Portfolio/Showreel
- Awards
- Specialization Areas
- Budget Range Experience

### 5. ✍️ Writer
**Required Fields:**
- Writing Experience (years)
- Writing Types
- Languages
- Published Works

**Optional Fields:**
- Writing Samples
- Awards/Recognition
- Specialization Genres
- Collaboration Experience

### 6. 🎧 DJ/RJ
**Required Fields:**
- Music Genres
- Equipment Owned
- Venue Experience
- Mixing Skills

**Optional Fields:**
- Demo Mix
- Radio Experience
- Event Types
- Technical Skills

### 7. 🎸 Band
**Required Fields:**
- Band Size
- Music Genres
- Instruments
- Performance Experience (years)

**Optional Fields:**
- Original Songs
- Demo Tracks
- Equipment Owned
- Touring Experience

### 8. 👗 Model
**Required Fields:**
- Height (cm)
- Weight (kg)
- Body Measurements
- Hair Color
- Eye Color

**Optional Fields:**
- Modeling Types
- Portfolio
- Comp Cards
- Agency Representation

### 9. 📸 Photographer
**Required Fields:**
- Photography Types
- Equipment Owned
- Editing Software
- Portfolio

**Optional Fields:**
- Studio Available
- Specialization Areas
- Awards/Recognition
- Client Testimonials

### 10. 🎥 Videographer
**Required Fields:**
- Video Types
- Equipment Owned
- Editing Software
- Portfolio

**Optional Fields:**
- Drone License
- Specialization Areas
- Awards/Recognition
- Client Testimonials

## 🔍 Search and Discovery

### Search Artists by Type
```bash
# Get all actors
curl -X GET "http://localhost:8080/api/artists/type/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Search Artists with Filters
```bash
# Search artists with multiple filters
curl -X GET "http://localhost:8080/api/artists/search?location=Mumbai&minExperience=3&maxRate=8000&skills=Acting" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Get Verified Artists
```bash
# Get all verified artists
curl -X GET "http://localhost:8080/api/artists/verified" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 📊 Artist Profile Management

### Profile Statistics
Each artist profile tracks:
- Total Applications Submitted
- Successful Hires
- Verification Status
- Profile Views (if implemented)
- Rating/Reviews (if implemented)

### Dynamic Fields Management
The platform supports custom fields for each artist type:
- Fields are defined in the `artist_type_fields` table
- Values are stored in the `artist_profile_fields` table
- Fields can be required, optional, or searchable
- Support for various field types (text, number, select, etc.)

## 🛠️ Admin Management

### Admin - View All Artists
```bash
# Get all artists (admin only)
curl -X GET "http://localhost:8080/api/admin/artists" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN_HERE"
```

### Admin - Verify Artist
```bash
# Approve verification request (admin only)
curl -X PUT "http://localhost:8080/api/admin/artists/1/verify" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN_HERE"
```

### Admin - Manage Artist Types
```bash
# Create new artist type (admin only)
curl -X POST "http://localhost:8080/api/admin/artist-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN_HERE" \
  -d '{
    "name": "STUNT_PERFORMER",
    "displayName": "Stunt Performer",
    "description": "Professional stunt performers for film and TV",
    "iconUrl": "/icons/stunt.png",
    "sortOrder": 11
  }'
```

## 🚨 Common Issues and Solutions

### Issue 1: "Artist profile already exists"
**Solution:** Each user can only have one artist profile. Check if the user already has a profile.

### Issue 2: "Artist type not found"
**Solution:** Ensure the artist type ID exists. Check available types using the public API.

### Issue 3: "Invalid JWT token"
**Solution:** Make sure you're using a valid JWT token from the authentication step.

### Issue 4: "Required fields missing"
**Solution:** Check the artist type fields to see which fields are required and provide them.

### Issue 5: "User not found"
**Solution:** Ensure the user is properly registered and authenticated.

## 📝 Best Practices

### 1. Profile Completeness
- Fill all required fields
- Add a compelling bio
- Upload high-quality portfolio materials
- Include relevant skills and experience

### 2. Dynamic Fields
- Always provide values for required dynamic fields
- Use appropriate data types for each field
- Keep field values up-to-date

### 3. Portfolio Management
- Use high-quality images and videos
- Organize portfolio materials logically
- Include diverse examples of work
- Keep portfolio URLs accessible

### 4. Verification Process
- Request verification badge for credibility
- Provide authentic information
- Maintain professional standards

## 🔄 Workflow Summary

1. **Check System Status** → Verify artist types are available
2. **User Registration** → Create user account with ARTIST role
3. **Authentication** → Login and get JWT token
4. **Profile Creation** → Create artist profile with all details
5. **Verification** → Request verification badge (optional)
6. **Profile Management** → Update and maintain profile
7. **Discovery** → Artists become searchable and discoverable

## 🎯 Next Steps After Adding Artist

Once an artist is added to the platform, they can:
- ✅ Apply for jobs posted by recruiters
- ✅ Receive messages from recruiters
- ✅ Update their profile and portfolio
- ✅ Search and apply for relevant opportunities
- ✅ Build their professional network
- ✅ Track their application history
- ✅ Manage their subscription and payments

---

## 📞 Support

For technical support or questions:
- Check the API documentation
- Review the database schema
- Contact the development team
- Create an issue in the repository

**Happy Artist Onboarding! 🎭✨**

