# 🎭 Artist Types Auto-Setup Guide

## 📋 Overview

This guide explains how to automatically add all artist types data to your database when the application starts, with duplicate prevention.

## 🚀 **Option 1: Manual Setup (Recommended)**

Since there are compilation issues with the Java approach, use the manual setup:

### **Step 1: Run the Setup Script**
```bash
# Make the script executable (already done)
chmod +x setup-artist-types-manual.sh

# Run the setup
./setup-artist-types-manual.sh
```

### **Step 2: Verify Data**
```bash
# Check all artist types and their field counts
mysql -u root -p -e "USE icastar_db; SELECT at.name, COUNT(atf.id) as field_count FROM artist_types at LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id GROUP BY at.id, at.name;"
```

## 🔧 **Option 2: Automatic Setup (Java Approach)**

If you want automatic setup, you can use the Java initializer:

### **Step 1: Remove Compilation Issues**
The current Java compilation has issues. You can either:

1. **Use the manual setup** (recommended)
2. **Fix the Java compilation issues** and use the `ArtistTypesDataInitializer.java`

### **Step 2: Enable the Java Initializer**
If you fix the compilation issues, the `ArtistTypesDataInitializer.java` will automatically run on application startup.

## 📊 **What Gets Added**

### **🎤 Singer (Artist Type ID: 3) - 10 Fields**
- **Required**: Genre, Vocal Range, Languages, Profile Picture
- **Optional**: About, Experience, Achievements, Videos, Audio, Face Verification

### **🎬 Director (Artist Type ID: 4) - 12 Fields**
- **Required**: Genre, Full Name, City, Profile Photo, ID Proof
- **Optional**: Username, Training, Description, Experience, Face Verification, Videos, Drive Link

### **✍️ Writer (Artist Type ID: 5) - 8 Fields**
- **Required**: Genre, Languages
- **Optional**: Education, Demo, Experience, Achievements, Portfolio Link

### **💃 Dancer (Artist Type ID: 2) - 10 Fields**
- **Required**: Dance Style
- **Optional**: Bio, Experience, Training, Achievements, Personal Style, Skills, Videos, Drive Link, Face Verification

## 🧪 **Testing the Setup**

### **1. Check Artist Types**
```bash
curl --location 'http://localhost:8080/api/public/artist-types'
```

### **2. Check Singer Fields**
```bash
curl --location 'http://localhost:8080/api/public/artist-types/3/fields'
```

### **3. Check Director Fields**
```bash
curl --location 'http://localhost:8080/api/public/artist-types/4/fields'
```

### **4. Check Writer Fields**
```bash
curl --location 'http://localhost:8080/api/public/artist-types/5/fields'
```

### **5. Check Dancer Fields**
```bash
curl --location 'http://localhost:8080/api/public/artist-types/2/fields'
```

## 🎯 **Create Test Profiles**

### **Singer Profile**
```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "artistTypeId": 3,
    "firstName": "Priya",
    "lastName": "Singh",
    "stageName": "Priya the Singer",
    "bio": "Professional singer with 8 years of experience...",
    "location": "Mumbai, India",
    "experienceYears": 8,
    "hourlyRate": 4000.00,
    "dynamicFields": [
        {
            "artistTypeFieldId": 1,
            "fieldValue": "Classical,Bollywood,Devotional"
        },
        {
            "artistTypeFieldId": 2,
            "fieldValue": "Soprano"
        },
        {
            "artistTypeFieldId": 3,
            "fieldValue": "Hindi,English,Marathi"
        }
    ]
}'
```

### **Director Profile**
```bash
curl --location 'http://localhost:8080/api/artists/profile' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer YOUR_JWT_TOKEN_HERE' \
--data-raw '{
    "artistTypeId": 4,
    "firstName": "Raj",
    "lastName": "Kumar",
    "stageName": "Raj the Director",
    "bio": "Professional director with 10 years of experience...",
    "location": "Mumbai, India",
    "experienceYears": 10,
    "hourlyRate": 50000.00,
    "dynamicFields": [
        {
            "artistTypeFieldId": 1,
            "fieldValue": "Commercial Directors,Independent filmmaker,Music Video Director"
        },
        {
            "artistTypeFieldId": 2,
            "fieldValue": "Raj Kumar Sharma"
        },
        {
            "artistTypeFieldId": 4,
            "fieldValue": "Mumbai"
        }
    ]
}'
```

## 🔍 **Database Verification**

### **Check All Artist Types**
```sql
SELECT * FROM artist_types ORDER BY name;
```

### **Check Field Counts**
```sql
SELECT 
    at.name as artist_type,
    COUNT(atf.id) as field_count
FROM artist_types at
LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id
GROUP BY at.id, at.name
ORDER BY at.name;
```

### **Check Specific Fields**
```sql
SELECT 
    at.name as artist_type,
    atf.field_name,
    atf.display_name,
    atf.field_type,
    atf.is_required,
    atf.is_searchable
FROM artist_type_fields atf
JOIN artist_types at ON atf.artist_type_id = at.id
WHERE at.name = 'SINGER'
ORDER BY atf.sort_order;
```

## 🎉 **Success Indicators**

1. **Database**: All fields should be inserted successfully
2. **API**: All artist types should be available via API
3. **Fields**: Each artist type should have their specific fields
4. **Profiles**: Artists can create profiles with dynamic fields

## 📝 **Important Notes**

- **Duplicate Prevention**: The SQL uses `INSERT IGNORE` to prevent duplicate inserts
- **Field IDs**: Each artist type has its own field ID sequence starting from 1
- **Required Fields**: Make sure to provide values for all required fields
- **File Uploads**: For FILE fields, provide URLs to uploaded files
- **Multi-select**: Use comma-separated values for MULTI_SELECT fields
- **Boolean**: Use "true" or "false" strings for BOOLEAN fields

## 🚀 **Quick Start**

1. **Run the setup**: `./setup-artist-types-manual.sh`
2. **Start your application**: `mvn spring-boot:run`
3. **Test the API**: Use the curl commands above
4. **Create profiles**: Test with different artist types

All artist types are now fully configured with their dynamic fields! 🎭✨
