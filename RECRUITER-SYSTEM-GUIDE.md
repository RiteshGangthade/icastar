# 🎯 iCastar Recruiter System - Complete Guide

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Recruiter Categories](#recruiter-categories)
3. [Database Schema](#database-schema)
4. [API Endpoints](#api-endpoints)
5. [Setup Instructions](#setup-instructions)
6. [Usage Examples](#usage-examples)

---

## 🎯 System Overview

The iCastar Recruiter System is a **dynamic recruiter management system** that supports three distinct recruiter categories, each with their own specific fields and requirements.

### 🏗️ Core Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Recruiter       │    │ Dynamic Fields  │    │ Profile Data    │
│ Categories      │    │ System          │    │ Storage         │
│                 │    │                 │    │                 │
│ • Production    │    │ • Field Types   │    │ • Field Values  │
│   House         │    │ • Validation    │    │ • File Uploads   │
│ • Casting       │    │ • Options       │    │ • Verification   │
│   Director      │    │ • Requirements  │    │ • Search Data    │
│ • Individual    │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🏢 Recruiter Categories

### 1. **Production House** 🎬
**Target**: Film and TV production companies, studios, and production houses

#### Basic Information
- ✅ **Name of Production House** (Required)
- ✅ **Name of Recruiter** (Required)
- ✅ **Location** (Required)
- ✅ **How long have you been running this production house?** (Required)

#### Advanced Information
- ✅ **Mobile Number Verification** (Required)
- ✅ **Email ID** (Required)
- ✅ **ID Proof** (Required - File Upload)
- ✅ **Registration Certificate of Production House** (Required - File Upload)

#### Additional Fields
- 🌐 **Company Website** (Optional)
- 📝 **Company Description** (Optional)
- 🎯 **Specialization** (Optional - Multi-select)
- 👥 **Team Size** (Optional)

### 2. **Casting Director** 🎭
**Target**: Professional casting directors working for production houses

#### Basic Information
- ✅ **Name of Recruiter** (Required)
- ✅ **Location** (Required)
- ✅ **Name of the Production House** (Required)

#### Advanced Information
- ✅ **Mobile Number Verification** (Required)
- ✅ **Casting Director Card/ID Proof** (Required - File Upload)
- ✅ **Face Verification** (Required)

#### Additional Fields
- 📅 **Years of Experience** (Optional)
- 🎯 **Specialization** (Optional - Multi-select)
- 🔗 **Portfolio Link** (Optional)

### 3. **Individual Recruiter** 👤
**Target**: Entrepreneurs, event managers, ad agencies, and individual recruiters

#### Basic Information
- ✅ **Name** (Required)
- ✅ **Location** (Required)
- ✅ **Email ID** (Required)
- ✅ **Contact Details** (Required)

#### Advanced Information
- ✅ **ID Proof** (Required - File Upload)
- ✅ **Face Verification** (Required)

#### Additional Fields
- 🏢 **Type of Business** (Optional)
- 📅 **Years of Experience** (Optional)
- 📝 **Services Offered** (Optional)

---

## 🗄️ Database Schema

### Core Tables

#### `recruiter_categories` - Defines Recruiter Types
```sql
CREATE TABLE recruiter_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,           -- 'PRODUCTION_HOUSE', 'CASTING_DIRECTOR', 'INDIVIDUAL'
    display_name VARCHAR(100) NOT NULL,         -- 'Production House', 'Casting Director', 'Individual'
    description TEXT,                            -- Description of the recruiter type
    icon_url VARCHAR(500),                      -- Icon for UI
    is_active BOOLEAN DEFAULT TRUE,              -- Enable/disable
    sort_order INT DEFAULT 0,                   -- Display order
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `recruiter_category_fields` - Defines Custom Fields
```sql
CREATE TABLE recruiter_category_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_category_id BIGINT NOT NULL,      -- Links to recruiter_categories
    field_name VARCHAR(100) NOT NULL,          -- 'production_house_name', 'mobile_verification'
    display_name VARCHAR(100) NOT NULL,         -- 'Name of Production House', 'Mobile Verification'
    field_type ENUM('TEXT', 'TEXTAREA', 'NUMBER', 'EMAIL', 'PHONE', 'URL', 'DATE', 'BOOLEAN', 'SELECT', 'MULTI_SELECT', 'FILE') NOT NULL,
    is_required BOOLEAN DEFAULT FALSE,          -- Required field?
    is_searchable BOOLEAN DEFAULT TRUE,         -- Can be searched?
    sort_order INT DEFAULT 0,                  -- Display order
    validation_rules JSON,                      -- Custom validation rules
    options JSON,                               -- Options for SELECT/MULTI_SELECT
    placeholder VARCHAR(255),                  -- Placeholder text
    help_text VARCHAR(500),                    -- Help text for users
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_category_id) REFERENCES recruiter_categories(id)
);
```

#### `recruiter_profile_fields` - Stores Field Values
```sql
CREATE TABLE recruiter_profile_fields (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recruiter_profile_id BIGINT NOT NULL,       -- Links to recruiter_profiles
    recruiter_category_field_id BIGINT NOT NULL, -- Links to recruiter_category_fields
    field_value TEXT,                           -- The actual value
    file_url VARCHAR(500),                      -- For FILE fields
    file_name VARCHAR(255),                     -- Original filename
    file_size BIGINT,                           -- File size in bytes
    mime_type VARCHAR(100),                     -- MIME type
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_profile_id) REFERENCES recruiter_profiles(id),
    FOREIGN KEY (recruiter_category_field_id) REFERENCES recruiter_category_fields(id)
);
```

---

## 🌐 API Endpoints

### Recruiter Profile Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/recruiters/profile` | Create recruiter profile |
| GET | `/api/recruiters/profile/me` | Get current user's profile |
| GET | `/api/recruiters/profile/{id}` | Get specific recruiter profile |
| PUT | `/api/recruiters/profile/{id}` | Update recruiter profile |
| DELETE | `/api/recruiters/profile/{id}` | Delete recruiter profile |

### Recruiter Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/recruiters/categories` | List all recruiter categories |
| GET | `/api/recruiters/categories/{id}` | Get specific category |
| GET | `/api/recruiters/categories/{id}/fields` | Get fields for category |

---

## 🚀 Setup Instructions

### 1. **Apply Database Schema**
```bash
# Apply the recruiter categories schema
mysql -u root -p icastar_db < recruiter-categories-schema.sql

# Apply the recruiter fields data
mysql -u root -p icastar_db < recruiter-fields-data.sql
```

### 2. **Verify Setup**
```bash
# Check categories
mysql -u root -p icastar_db -e "SELECT * FROM recruiter_categories;"

# Check fields for each category
mysql -u root -p icastar_db -e "SELECT rc.name, COUNT(rcf.id) as field_count FROM recruiter_categories rc LEFT JOIN recruiter_category_fields rcf ON rc.id = rcf.recruiter_category_id GROUP BY rc.id, rc.name;"
```

---

## 🎬 Usage Examples

### Example 1: Production House Registration

#### Step 1: Register User
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "production@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Smith",
    "phoneNumber": "+1234567890"
  }'
```

#### Step 2: Create Production House Profile
```bash
curl -X POST "http://localhost:8080/api/recruiters/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "recruiterCategoryId": 1,
    "companyName": "DreamWorks Studios",
    "contactPersonName": "John Smith",
    "designation": "HR Manager",
    "companyDescription": "Leading film production company",
    "companyWebsite": "https://dreamworks.com",
    "location": "Los Angeles, CA",
    "dynamicFields": [
      {
        "recruiterCategoryFieldId": 1,
        "fieldValue": "DreamWorks Studios"
      },
      {
        "recruiterCategoryFieldId": 2,
        "fieldValue": "John Smith"
      },
      {
        "recruiterCategoryFieldId": 3,
        "fieldValue": "Los Angeles, CA"
      },
      {
        "recruiterCategoryFieldId": 4,
        "fieldValue": "15"
      },
      {
        "recruiterCategoryFieldId": 5,
        "fieldValue": "true"
      },
      {
        "recruiterCategoryFieldId": 6,
        "fieldValue": "john.smith@dreamworks.com"
      }
    ]
  }'
```

### Example 2: Casting Director Registration

```bash
curl -X POST "http://localhost:8080/api/recruiters/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "recruiterCategoryId": 2,
    "companyName": "Independent Casting",
    "contactPersonName": "Sarah Johnson",
    "designation": "Casting Director",
    "location": "New York, NY",
    "dynamicFields": [
      {
        "recruiterCategoryFieldId": 9,
        "fieldValue": "Sarah Johnson"
      },
      {
        "recruiterCategoryFieldId": 10,
        "fieldValue": "New York, NY"
      },
      {
        "recruiterCategoryFieldId": 11,
        "fieldValue": "Warner Bros Studios"
      },
      {
        "recruiterCategoryFieldId": 12,
        "fieldValue": "true"
      },
      {
        "recruiterCategoryFieldId": 13,
        "fieldValue": "true"
      }
    ]
  }'
```

### Example 3: Individual Recruiter Registration

```bash
curl -X POST "http://localhost:8080/api/recruiters/profile" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "recruiterCategoryId": 3,
    "companyName": "Event Management Co",
    "contactPersonName": "Mike Wilson",
    "designation": "Event Manager",
    "location": "Chicago, IL",
    "dynamicFields": [
      {
        "recruiterCategoryFieldId": 15,
        "fieldValue": "Mike Wilson"
      },
      {
        "recruiterCategoryFieldId": 16,
        "fieldValue": "Chicago, IL"
      },
      {
        "recruiterCategoryFieldId": 17,
        "fieldValue": "mike@eventco.com"
      },
      {
        "recruiterCategoryFieldId": 18,
        "fieldValue": "Phone: +1-555-0123, Office: 123 Main St"
      },
      {
        "recruiterCategoryFieldId": 19,
        "fieldValue": "true"
      },
      {
        "recruiterCategoryFieldId": 20,
        "fieldValue": "true"
      }
    ]
  }'
```

---

## 🔧 Field Types and Options

### Production House Fields
| Field Name | Type | Required | Options |
|------------|------|----------|---------|
| production_house_name | TEXT | ✅ | - |
| recruiter_name | TEXT | ✅ | - |
| location | TEXT | ✅ | - |
| years_running | NUMBER | ✅ | - |
| mobile_verification | BOOLEAN | ✅ | - |
| email | EMAIL | ✅ | - |
| id_proof | FILE | ✅ | - |
| registration_certificate | FILE | ✅ | - |
| specialization | MULTI_SELECT | ❌ | Film, TV, Web Series, Commercial, etc. |
| team_size | SELECT | ❌ | 1-5, 6-10, 11-20, 21-50, 51-100, 100+ |

### Casting Director Fields
| Field Name | Type | Required | Options |
|------------|------|----------|---------|
| recruiter_name | TEXT | ✅ | - |
| location | TEXT | ✅ | - |
| production_house_name | TEXT | ✅ | - |
| mobile_verification | BOOLEAN | ✅ | - |
| id_proof | FILE | ✅ | - |
| face_verification | BOOLEAN | ✅ | - |
| experience_years | NUMBER | ❌ | - |
| specialization | MULTI_SELECT | ❌ | Film, TV, Web Series, Commercial, etc. |
| portfolio_link | URL | ❌ | - |

### Individual Recruiter Fields
| Field Name | Type | Required | Options |
|------------|------|----------|---------|
| name | TEXT | ✅ | - |
| location | TEXT | ✅ | - |
| email | EMAIL | ✅ | - |
| contact_details | TEXTAREA | ✅ | - |
| id_proof | FILE | ✅ | - |
| face_verification | BOOLEAN | ✅ | - |
| business_type | SELECT | ❌ | Entrepreneur, Event Manager, Ad Agency, etc. |
| experience_years | NUMBER | ❌ | - |
| services_offered | TEXTAREA | ❌ | - |

---

## 📝 Summary

The iCastar Recruiter System provides:

1. **Three Distinct Categories**: Production House, Casting Director, Individual
2. **Dynamic Fields**: Each category has specific required and optional fields
3. **File Upload Support**: For ID proofs, certificates, and documents
4. **Verification System**: Mobile verification and face verification
5. **Flexible Architecture**: Easy to add new categories or fields

### Key Benefits:
- ✅ **Category-Specific Requirements**: Each recruiter type has tailored fields
- ✅ **Verification System**: Built-in mobile and face verification
- ✅ **File Upload Support**: For documents and certificates
- ✅ **Searchable Fields**: Recruiters can be found by their specializations
- ✅ **Extensible**: Easy to add new categories or modify existing ones

---

## 🔗 Related Files

- `recruiter-categories-schema.sql` - Database schema for recruiter categories
- `recruiter-fields-data.sql` - Sample data for all recruiter fields
- `RecruiterCategory.java` - Entity for recruiter categories
- `RecruiterCategoryField.java` - Entity for category fields
- `RecruiterProfileField.java` - Entity for profile field values

---

*This system provides a complete solution for managing different types of recruiters with their specific requirements and verification needs.*
