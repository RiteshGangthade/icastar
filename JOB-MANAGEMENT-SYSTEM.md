# 💼 iCastar Job Management System

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Features](#features)
3. [API Endpoints](#api-endpoints)
4. [Filtering Options](#filtering-options)
5. [Usage Examples](#usage-examples)
6. [Database Schema](#database-schema)
7. [Setup Instructions](#setup-instructions)

---

## 🎯 System Overview

The iCastar Job Management System provides **admin and super admin** with comprehensive tools to view, filter, and manage job posts across the platform.

### 🏗️ Core Features

- ✅ **View All Job Posts** - Complete job listing with pagination
- ✅ **Advanced Filtering** - Filter by recruiter, status, subscription plan
- ✅ **Toggle Visibility** - Show/hide job posts
- ✅ **Bulk Operations** - Manage multiple jobs at once
- ✅ **Statistics Dashboard** - Job analytics and insights
- ✅ **Audit Trail** - Complete logging of all actions

---

## 🚀 Features

### 📊 **Job Viewing & Filtering**

#### **Filter by Recruiter**
- Recruiter ID
- Recruiter Name
- Recruiter Email
- Recruiter Company
- Recruiter Category (Production House, Casting Director, Individual)

#### **Filter by Status**
- Active/Inactive jobs
- Visible/Hidden jobs
- Job status (OPEN, CLOSED, DRAFT, etc.)

#### **Filter by Subscription Plan**
- Subscription Plan ID
- Plan Name
- Plan Type (FREE, BASIC, PREMIUM, etc.)
- Subscription Status

#### **Filter by Job Details**
- Job Type (FULL_TIME, PART_TIME, CONTRACT, etc.)
- Location
- Title/Description search
- Date ranges (created, application deadline)

#### **Filter by Statistics**
- Application count ranges
- View count ranges
- Boost status
- Expired jobs
- Urgent jobs

### 🔧 **Job Management**

#### **Visibility Toggle**
- Show/hide individual jobs
- Bulk visibility toggle
- Reason tracking for visibility changes
- Admin notes for actions

#### **Bulk Operations**
- Select multiple jobs
- Bulk visibility toggle
- Bulk status updates
- Mass actions

#### **Statistics & Analytics**
- Total jobs count
- Active/Inactive jobs
- Jobs by type
- Jobs by recruiter category
- Recent jobs
- Average applications per job

---

## 🌐 API Endpoints

### Job Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/admin/jobs` | Get all jobs with filtering | USER_MANAGEMENT (READ) |
| GET | `/api/admin/jobs/{jobId}` | Get job details | USER_MANAGEMENT (READ) |
| POST | `/api/admin/jobs/{jobId}/toggle-visibility` | Toggle job visibility | USER_MANAGEMENT (WRITE) |
| POST | `/api/admin/jobs/bulk-toggle-visibility` | Bulk toggle visibility | USER_MANAGEMENT (WRITE) |
| GET | `/api/admin/jobs/statistics` | Get job statistics | USER_MANAGEMENT (READ) |
| GET | `/api/admin/jobs/{jobId}/logs` | Get job logs | USER_MANAGEMENT (READ) |
| GET | `/api/admin/jobs/recruiters` | Get recruiters list | USER_MANAGEMENT (READ) |
| GET | `/api/admin/jobs/subscription-plans` | Get subscription plans | USER_MANAGEMENT (READ) |

---

## 🔍 Filtering Options

### Query Parameters

#### **Recruiter Filters**
```bash
?recruiterId=123
?recruiterName=John
?recruiterEmail=john@example.com
?recruiterCompany=ABC Studios
?recruiterCategory=Production House
```

#### **Status Filters**
```bash
?status=OPEN
?isActive=true
?isVisible=true
```

#### **Subscription Filters**
```bash
?subscriptionId=456
?subscriptionPlan=Premium
?subscriptionStatus=ACTIVE
```

#### **Job Detail Filters**
```bash
?jobType=FULL_TIME
?location=Mumbai
?title=Director
```

#### **Date Filters**
```bash
?createdFrom=2024-01-01
?createdTo=2024-01-31
?applicationDeadlineFrom=2024-02-01
?applicationDeadlineTo=2024-02-28
```

#### **Statistics Filters**
```bash
?minApplications=5
?maxApplications=50
?minViews=10
?maxViews=100
?hasBoost=true
?isExpired=false
?isUrgent=true
```

#### **Pagination**
```bash
?page=0&size=20&sort=createdAt,desc
```

---

## 🎬 Usage Examples

### 1. **Get All Jobs with Basic Filtering**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs?page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Film Director Needed",
      "description": "Looking for an experienced film director...",
      "jobType": "FULL_TIME",
      "location": "Mumbai, India",
      "status": "OPEN",
      "isActive": true,
      "isVisible": true,
      "recruiterName": "John Smith",
      "recruiterCompany": "ABC Studios",
      "recruiterCategory": "Production House",
      "subscriptionPlan": "Premium",
      "applicationCount": 15,
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "totalElements": 150,
  "totalPages": 8,
  "currentPage": 0,
  "size": 20
}
```

### 2. **Filter by Recruiter**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs?recruiterId=123&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### 3. **Filter by Status**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs?status=OPEN&isActive=true&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### 4. **Filter by Subscription Plan**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs?subscriptionPlan=Premium&subscriptionStatus=ACTIVE&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### 5. **Search Jobs**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs?title=Director&location=Mumbai&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

### 6. **Toggle Job Visibility**

```bash
curl -X POST "http://localhost:8080/api/admin/jobs/123/toggle-visibility" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "isVisible": false,
    "reason": "Inappropriate content",
    "adminNotes": "Job contains misleading information"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Job visibility updated successfully",
  "data": {
    "id": 123,
    "title": "Film Director Needed",
    "isVisible": false,
    "updatedAt": "2024-01-15T11:00:00"
  }
}
```

### 7. **Bulk Toggle Visibility**

```bash
curl -X POST "http://localhost:8080/api/admin/jobs/bulk-toggle-visibility" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '[
    {
      "jobId": 123,
      "isVisible": false,
      "reason": "Inappropriate content"
    },
    {
      "jobId": 124,
      "isVisible": true,
      "reason": "Content reviewed and approved"
    }
  ]'
```

**Response:**
```json
{
  "success": true,
  "message": "Bulk job visibility updated successfully",
  "data": [
    {
      "id": 123,
      "title": "Film Director Needed",
      "isVisible": false
    },
    {
      "id": 124,
      "title": "Music Composer Wanted",
      "isVisible": true
    }
  ],
  "count": 2
}
```

### 8. **Get Job Statistics**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs/statistics" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalJobs": 150,
    "activeJobs": 120,
    "inactiveJobs": 30,
    "expiredJobs": 25,
    "jobsByType": {
      "FULL_TIME": 80,
      "PART_TIME": 45,
      "CONTRACT": 25
    },
    "jobsByCategory": {
      "Production House": 90,
      "Casting Director": 35,
      "Individual Recruiter": 25
    },
    "recentJobs": 12,
    "avgApplicationsPerJob": 8.5
  }
}
```

### 9. **Get Recruiters List**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs/recruiters" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 123,
      "name": "John Smith",
      "email": "john@abcstudios.com",
      "company": "ABC Studios",
      "category": "Production House",
      "isActive": true
    },
    {
      "id": 124,
      "name": "Sarah Johnson",
      "email": "sarah@casting.com",
      "company": "Casting Agency",
      "category": "Casting Director",
      "isActive": true
    }
  ]
}
```

### 10. **Get Subscription Plans**

```bash
curl -X GET "http://localhost:8080/api/admin/jobs/subscription-plans" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Recruiter Free",
      "planType": "FREE",
      "userRole": "RECRUITER",
      "price": 0.00,
      "isActive": true
    },
    {
      "id": 2,
      "name": "Recruiter Premium",
      "planType": "PREMIUM",
      "userRole": "RECRUITER",
      "price": 999.00,
      "isActive": true
    }
  ]
}
```

---

## 🗄️ Database Schema

### Enhanced Job Management

The system uses existing tables with enhanced queries:

#### **job_posts** - Main job table
```sql
-- Existing fields
id, title, description, requirements, job_type, location, salary_min, salary_max, 
currency, status, is_active, application_deadline, start_date, created_at, updated_at

-- Enhanced with admin management
is_visible BOOLEAN DEFAULT TRUE,
admin_notes TEXT,
last_admin_action DATETIME,
last_admin_action_by BIGINT
```

#### **Enhanced Queries**
```sql
-- Count jobs by status
SELECT status, COUNT(*) FROM job_posts GROUP BY status;

-- Count jobs by recruiter category
SELECT rc.display_name, COUNT(*) 
FROM job_posts jp 
JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id 
JOIN recruiter_categories rc ON rp.recruiter_category_id = rc.id 
GROUP BY rc.display_name;

-- Count jobs by subscription plan
SELECT sp.name, COUNT(*) 
FROM job_posts jp 
JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id 
JOIN users u ON rp.user_id = u.id 
JOIN subscriptions s ON u.id = s.user_id 
JOIN subscription_plans sp ON s.subscription_plan_id = sp.id 
GROUP BY sp.name;
```

---

## 🚀 Setup Instructions

### 1. **Build and Run**

```bash
# Set Java 17
export JAVA_HOME=/Users/admin/Library/Java/JavaVirtualMachines/azul-17.0.16/Contents/Home

# Build project
mvn clean compile

# Run application
mvn spring-boot:run
```

### 2. **Test the System**

```bash
# Login as admin
curl -X POST "http://localhost:8080/api/auth/email/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@icastar.com",
    "password": "admin_password"
  }'

# Get all jobs
curl -X GET "http://localhost:8080/api/admin/jobs?page=0&size=20" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"

# Get job statistics
curl -X GET "http://localhost:8080/api/admin/jobs/statistics" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

---

## 🔧 Key Features

### ✅ **Comprehensive Filtering**
- Filter by recruiter details
- Filter by job status and visibility
- Filter by subscription plan
- Filter by job details and dates
- Filter by statistics and metrics

### ✅ **Bulk Operations**
- Select multiple jobs
- Bulk visibility toggle
- Mass status updates
- Efficient batch processing

### ✅ **Statistics Dashboard**
- Total jobs count
- Active/inactive jobs
- Jobs by type and category
- Recent activity
- Average metrics

### ✅ **Audit Trail**
- Complete logging of all actions
- Admin notes and reasons
- IP address tracking
- User agent logging

### ✅ **Permission System**
- Role-based access control
- Granular permissions
- Secure API endpoints
- Admin-only access

---

## 📊 Job Management Workflow

### Typical Admin Workflow

1. **View Jobs** - Browse all jobs with filtering options
2. **Filter Results** - Apply filters to find specific jobs
3. **Review Details** - Check job information and statistics
4. **Take Action** - Toggle visibility or update status
5. **Monitor Results** - Track changes and statistics

### Filtering Process

```
All Jobs → Apply Filters → Review Results → Take Action → Log Changes → Update Statistics
```

---

## 🔗 Related Documentation

- [ACCOUNT-MANAGEMENT-SYSTEM.md](ACCOUNT-MANAGEMENT-SYSTEM.md) - Account management guide
- [SUBSCRIPTION-SYSTEM-DESIGN.md](SUBSCRIPTION-SYSTEM-DESIGN.md) - Subscription system
- [RECRUITER-SYSTEM-GUIDE.md](RECRUITER-SYSTEM-GUIDE.md) - Recruiter system guide
- [COMPLETE-SYSTEM-GUIDE.md](COMPLETE-SYSTEM-GUIDE.md) - Overall platform guide
- [README.md](README.md) - Main project documentation

---

*This job management system provides comprehensive tools for admins to view, filter, and manage job posts with complete control and audit trails.*
