# 🗄️ iCastar Platform - Database Setup Guide

## 📋 Overview

This guide provides step-by-step instructions for setting up the iCastar platform database with default data, specifically focusing on the Dancer artist type with comprehensive fields and sample data.

## 🎯 Prerequisites

Before setting up the database, ensure you have:
- ✅ MySQL 8.0+ installed and running
- ✅ Database access credentials
- ✅ iCastar platform codebase
- ✅ Maven installed (for Java application)

## 🚀 Complete Database Setup Process

### Step 1: Create Database and Basic Schema

First, create the database and basic schema:

```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE IF NOT EXISTS icastar_db;
USE icastar_db;

# Run the main schema
source /path/to/icastar/database-schema.sql;
```

### Step 2: Add Dancer Default Data

Run the comprehensive dancer data script:

```bash
# Run the dancer default data script
source /path/to/icastar/dancer-default-data.sql;
```

This script will:
- ✅ Create the Dancer artist type
- ✅ Add 12 comprehensive fields for dancers
- ✅ Create 2 sample dancer profiles
- ✅ Add dynamic field values for testing

### Step 3: Enable Java Data Initializer

The Java application has a data initializer that will automatically create all artist types when the application starts. To enable it:

1. **Uncomment the @Component annotation** in `ArtistTypeDataInitializer.java`:
   ```java
   @Component  // This line should be uncommented
   @RequiredArgsConstructor
   @Slf4j
   public class ArtistTypeDataInitializer implements CommandLineRunner {
   ```

2. **Start the application**:
   ```bash
   mvn spring-boot:run
   ```

The application will automatically:
- ✅ Check if artist types exist
- ✅ Create all 10 default artist types if they don't exist
- ✅ Add all required fields for each type
- ✅ Log the initialization process

## 🎭 Dancer Artist Type Details

### Artist Type Information
- **Name**: `DANCER`
- **Display Name**: `Dancer`
- **Description**: Professional Dancers specializing in various dance styles
- **Icon**: `/icons/dancer.png`
- **Sort Order**: `2`

### Required Fields (Must be filled)

1. **Dance Styles** (MULTI_SELECT)
   - Options: Classical Ballet, Contemporary, Hip-Hop, Jazz, Bharatanatyam, Kathak, Kuchipudi, Odissi, Mohiniyattam, Manipuri, Salsa, Bachata, Tango, Waltz, Bollywood, Folk Dance, Modern Dance, Lyrical, Tap Dance, Breakdance, Street Dance, Ballroom, Latin Dance, Flamenco, Belly Dance, Acrobatic Dance, Aerial Dance, Pole Dance, Other
   - Searchable: Yes
   - Required: Yes

2. **Training Background** (TEXTAREA)
   - Max Length: 1000 characters
   - Searchable: Yes
   - Required: Yes

3. **Performance Experience** (SELECT)
   - Options: Beginner (0-1 years), Intermediate (1-3 years), Advanced (3-7 years), Professional (7+ years), Expert (10+ years)
   - Searchable: Yes
   - Required: Yes

4. **Performance Videos** (FILE)
   - Max Size: 50MB
   - Allowed Types: mp4, mov, avi, mkv
   - Required: Yes

### Optional Fields (Can be filled)

5. **Choreography Skills** (BOOLEAN)
   - Searchable: Yes
   - Required: No

6. **Teaching Experience** (BOOLEAN)
   - Searchable: Yes
   - Required: No

7. **Costume Availability** (BOOLEAN)
   - Searchable: No
   - Required: No

8. **Flexibility Level** (SELECT)
   - Options: Basic, Intermediate, Advanced, Extreme
   - Searchable: Yes
   - Required: No

9. **Performance Types** (MULTI_SELECT)
   - Options: Stage Performance, Music Videos, Wedding Performances, Corporate Events, Fashion Shows, Theater, Film/TV, Competitions, Festivals, Private Events, Online Performances, Street Performances, Other
   - Searchable: Yes
   - Required: No

10. **Awards & Recognition** (TEXTAREA)
    - Max Length: 500 characters
    - Searchable: No
    - Required: No

11. **Availability** (SELECT)
    - Options: Full-time, Part-time, Weekends Only, Evenings Only, Flexible, Project-based, On-demand
    - Searchable: Yes
    - Required: No

12. **Travel Willingness** (SELECT)
    - Options: Local Only, Within State, Within Country, International, Anywhere
    - Searchable: Yes
    - Required: No

## 👥 Sample Dancer Profiles

### Profile 1: Priya Sharma (Classical Dancer)
- **Email**: test.dancer@example.com
- **Password**: password123
- **Stage Name**: Priya the Dancer
- **Location**: Mumbai, Maharashtra, India
- **Experience**: 8 years
- **Hourly Rate**: ₹3,000
- **Specialties**: Bharatanatyam, Bollywood Dance, Contemporary
- **Training**: Kalakshetra Foundation graduate
- **Awards**: National Dance Competition 2022 winner

### Profile 2: Rahul Kumar (Hip-Hop Dancer)
- **Email**: hiphop.dancer@example.com
- **Password**: password123
- **Stage Name**: Rahul B-Boy
- **Location**: Delhi, India
- **Experience**: 6 years
- **Hourly Rate**: ₹2,500
- **Specialties**: Breaking, Popping, Locking, Hip-Hop
- **Training**: Self-taught with international workshops
- **Awards**: Delhi Street Dance Championship 2023 winner

## 🔍 Testing the Setup

### 1. Verify Artist Type Creation
```bash
# Check if Dancer type exists
curl -X GET "http://localhost:8080/api/public/artist-types" \
  -H "Content-Type: application/json"
```

### 2. Check Dancer Fields
```bash
# Get Dancer type fields
curl -X GET "http://localhost:8080/api/public/artist-types/2/fields" \
  -H "Content-Type: application/json"
```

### 3. Test Sample Profiles
```bash
# Login with sample dancer
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test.dancer@example.com",
    "password": "password123"
  }'

# Get dancer profile
curl -X GET "http://localhost:8080/api/artists/profile" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Search Dancers
```bash
# Search dancers by location
curl -X GET "http://localhost:8080/api/artists/search?location=Mumbai" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Search dancers by dance style
curl -X GET "http://localhost:8080/api/artists/search?skills=Bharatanatyam" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🛠️ Database Verification Queries

### Check Artist Types
```sql
SELECT id, name, display_name, description, is_active, sort_order 
FROM artist_types 
ORDER BY sort_order;
```

### Check Dancer Fields
```sql
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
JOIN artist_types at ON atf.artist_type_id = at.id
WHERE at.name = 'DANCER'
ORDER BY atf.sort_order;
```

### Check Sample Profiles
```sql
SELECT 
    ap.id,
    ap.first_name,
    ap.last_name,
    ap.stage_name,
    ap.location,
    ap.experience_years,
    ap.hourly_rate,
    u.email,
    at.display_name as artist_type
FROM artist_profiles ap
JOIN users u ON ap.user_id = u.id
JOIN artist_types at ON ap.artist_type_id = at.id
WHERE at.name = 'DANCER';
```

### Check Dynamic Field Values
```sql
SELECT 
    apf.id,
    ap.first_name,
    ap.last_name,
    atf.field_name,
    atf.display_name,
    apf.field_value
FROM artist_profile_fields apf
JOIN artist_profiles ap ON apf.artist_profile_id = ap.id
JOIN artist_type_fields atf ON apf.artist_type_field_id = atf.id
JOIN artist_types at ON atf.artist_type_id = at.id
WHERE at.name = 'DANCER'
ORDER BY ap.id, atf.sort_order;
```

## 🚨 Troubleshooting

### Issue 1: "Table doesn't exist"
**Solution**: Run the database schema first:
```bash
mysql -u root -p icastar_db < database-schema.sql
```

### Issue 2: "Artist type already exists"
**Solution**: The script checks for existing data. If you want to reset:
```sql
DELETE FROM artist_profile_fields WHERE artist_profile_id IN (
    SELECT id FROM artist_profiles WHERE artist_type_id = (
        SELECT id FROM artist_types WHERE name = 'DANCER'
    )
);
DELETE FROM artist_profiles WHERE artist_type_id = (
    SELECT id FROM artist_types WHERE name = 'DANCER'
);
DELETE FROM artist_type_fields WHERE artist_type_id = (
    SELECT id FROM artist_types WHERE name = 'DANCER'
);
DELETE FROM artist_types WHERE name = 'DANCER';
```

### Issue 3: "Java initializer not running"
**Solution**: 
1. Ensure `@Component` is uncommented in `ArtistTypeDataInitializer.java`
2. Restart the application
3. Check logs for initialization messages

### Issue 4: "Sample users can't login"
**Solution**: Check if users were created:
```sql
SELECT email, role, status, is_verified FROM users WHERE email LIKE '%dancer%';
```

## 📊 Expected Results

After successful setup, you should have:

### Database Tables
- ✅ `artist_types` - 1 Dancer type record
- ✅ `artist_type_fields` - 12 Dancer field records
- ✅ `users` - 2 test user records
- ✅ `artist_profiles` - 2 dancer profile records
- ✅ `artist_profile_fields` - 20 dynamic field value records

### API Endpoints Working
- ✅ `GET /api/public/artist-types` - Returns Dancer type
- ✅ `GET /api/public/artist-types/2/fields` - Returns 12 fields
- ✅ `POST /api/auth/login` - Works with sample users
- ✅ `GET /api/artists/profile` - Returns dancer profiles
- ✅ `GET /api/artists/search` - Search functionality works

## 🎯 Next Steps

After successful setup:

1. **Test the API endpoints** using the provided curl commands
2. **Create additional dancer profiles** using the artist onboarding guide
3. **Add more artist types** using the new artist types guide
4. **Configure the frontend** to display dancer profiles
5. **Set up job posting functionality** for recruiters to hire dancers

## 📝 Notes

- The sample data includes realistic Indian dance styles and locations
- All passwords are set to `password123` for testing
- The data includes both classical and contemporary dance examples
- Field validation rules are included for proper data entry
- Search functionality is enabled for relevant fields

---

## 📞 Support

For issues with database setup:
1. Check MySQL logs for errors
2. Verify database permissions
3. Ensure all SQL scripts run without errors
4. Check application logs for Java initialization
5. Contact the development team for complex issues

**Happy Database Setup! 🗄️✨**

