# 🎯 iCastar Recruiter Dashboard System

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Core Features](#core-features)
3. [API Endpoints](#api-endpoints)
4. [Subscription Integration](#subscription-integration)
5. [Usage Examples](#usage-examples)
6. [Database Schema](#database-schema)
7. [Setup Instructions](#setup-instructions)

---

## 🎯 System Overview

The iCastar **Recruiter Dashboard System** provides recruiters with comprehensive tools to manage their job postings, find the right artists, and track their hiring success.

### 🏗️ Core Features

- ✅ **Post Jobs** - Create job postings (requires subscription)
- ✅ **View Applications** - See artists who applied to jobs
- ✅ **Browse Artists** - Search and filter artist profiles
- ✅ **Get Suggestions** - AI-powered artist recommendations
- ✅ **Track Hires** - Monitor past jobs and hiring success
- ✅ **Analytics Dashboard** - Comprehensive statistics and insights

---

## 🚀 Core Features

### 📝 **Job Posting System**

#### **Post Job (Requires Subscription)**
- Create detailed job postings
- Set job requirements and criteria
- Define salary ranges and benefits
- Set application deadlines
- Add dynamic custom fields
- Boost job visibility (premium feature)

#### **Job Management**
- View all posted jobs
- Filter by status, type, location
- Edit job details
- Close/activate jobs
- Track job performance

### 👥 **Artist Management**

#### **View Applications**
- See all artists who applied
- Filter applications by status
- View artist profiles and portfolios
- Accept/reject applications
- Shortlist candidates
- Send messages to artists

#### **Browse Artist Profiles**
- Search artists by category, skills, location
- Filter by experience level, availability
- View artist portfolios and achievements
- Check verification status
- Contact artists directly

#### **Get Suggestions**
- AI-powered artist recommendations
- Match based on job criteria
- Skills and experience matching
- Location and availability matching
- Performance history analysis

### 📊 **Hiring Analytics**

#### **Track Hires and Past Jobs**
- Monitor hiring success
- Rate artist performance
- Provide feedback and recommendations
- Track project completion
- Analyze hiring patterns

#### **Dashboard Statistics**
- Total jobs posted
- Active vs closed jobs
- Application rates
- Hire success rate
- Popular job categories
- Performance metrics

---

## 🌐 API Endpoints

### Dashboard Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/recruiter/dashboard` | Get dashboard overview | RECRUITER |
| GET | `/api/recruiter/dashboard/statistics` | Get recruiter statistics | RECRUITER |
| GET | `/api/recruiter/dashboard/subscription` | Get subscription status | RECRUITER |

### Job Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| POST | `/api/recruiter/dashboard/jobs` | Post new job | RECRUITER (with subscription) |
| GET | `/api/recruiter/dashboard/jobs` | Get my jobs | RECRUITER |
| GET | `/api/recruiter/dashboard/jobs/{jobId}` | Get job details | RECRUITER |
| PUT | `/api/recruiter/dashboard/jobs/{jobId}/status` | Update job status | RECRUITER |
| POST | `/api/recruiter/dashboard/jobs/{jobId}/boost` | Boost job visibility | RECRUITER (premium) |

### Application Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/recruiter/dashboard/jobs/{jobId}/applications` | Get job applications | RECRUITER |

### Artist Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/recruiter/dashboard/artists` | Browse artists | RECRUITER |
| GET | `/api/recruiter/dashboard/suggestions` | Get artist suggestions | RECRUITER |

### Hiring Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/recruiter/dashboard/hires` | Get hires and past jobs | RECRUITER |
| GET | `/api/recruiter/dashboard/hires/{hireId}` | Get hire details | RECRUITER |
| POST | `/api/recruiter/dashboard/hires/{hireId}/rate` | Rate and provide feedback | RECRUITER |

---

## 💳 Subscription Integration

### **Subscription-Based Features**

#### **Free Plan Features**
- Basic job posting (limited)
- View applications
- Basic artist browsing
- Basic statistics

#### **Premium Plan Features**
- Unlimited job posting
- Advanced artist suggestions
- Job boosting
- Detailed analytics
- Priority support
- Advanced filtering

#### **Subscription Validation**
- Check subscription status before job posting
- Validate remaining job post limits
- Track usage for billing
- Provide upgrade prompts

---

## 🎬 Usage Examples

### 1. **Get Dashboard Overview**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "recruiterId": 123,
    "recruiterName": "John Smith",
    "companyName": "ABC Studios",
    "recruiterCategory": "Production House",
    "email": "john@abcstudios.com",
    "phone": "+1234567890",
    "isActive": true,
    "subscriptionId": 456,
    "subscriptionPlan": "Recruiter Premium",
    "subscriptionStatus": "ACTIVE",
    "subscriptionExpiresAt": "2024-12-31T23:59:59",
    "remainingJobPosts": 25,
    "maxJobPosts": 50,
    "canPostJob": true,
    "totalJobsPosted": 15,
    "activeJobs": 8,
    "closedJobs": 7,
    "totalApplications": 120,
    "totalHires": 5,
    "totalViews": 500,
    "averageApplicationsPerJob": 8.0,
    "hireRate": 0.42,
    "recentJobs": [
      {
        "id": 1,
        "title": "Film Director Needed",
        "description": "Looking for an experienced film director...",
        "jobType": "FULL_TIME",
        "location": "Mumbai, India",
        "status": "OPEN",
        "isActive": true,
        "applicationCount": 15,
        "viewCount": 50,
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "recentApplications": [
      {
        "id": 1,
        "jobId": 1,
        "jobTitle": "Film Director Needed",
        "artistId": 789,
        "artistName": "Sarah Johnson",
        "artistEmail": "sarah@example.com",
        "artistCategory": "Director",
        "applicationStatus": "PENDING",
        "appliedAt": "2024-01-16T09:15:00"
      }
    ],
    "recentHires": [
      {
        "id": 1,
        "jobId": 1,
        "jobTitle": "Film Director Needed",
        "artistId": 789,
        "artistName": "Sarah Johnson",
        "artistCategory": "Director",
        "hireStatus": "COMPLETED",
        "hiredAt": "2024-01-20T14:30:00",
        "performanceRating": "5",
        "isCompleted": true,
        "isRecommended": true
      }
    ],
    "canPostNewJob": true,
    "canViewApplications": true,
    "canBrowseArtists": true,
    "canGetSuggestions": true,
    "canTrackHires": true,
    "availableFeatures": [
      "JOB_POSTING",
      "ARTIST_BROWSING",
      "APPLICATION_MANAGEMENT",
      "BASIC_ANALYTICS"
    ],
    "premiumFeatures": [
      "ADVANCED_SUGGESTIONS",
      "JOB_BOOSTING",
      "DETAILED_ANALYTICS",
      "PRIORITY_SUPPORT"
    ],
    "hasPremiumFeatures": true
  }
}
```

### 2. **Post a New Job**

```bash
curl -X POST "http://localhost:8080/api/recruiter/dashboard/jobs" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN" \
  -d '{
    "title": "Film Director Needed",
    "description": "Looking for an experienced film director for a feature film project",
    "requirements": "5+ years experience, portfolio required",
    "jobType": "FULL_TIME",
    "location": "Mumbai, India",
    "salaryMin": 50000,
    "salaryMax": 80000,
    "currency": "USD",
    "applicationDeadline": "2024-02-15T23:59:59",
    "startDate": "2024-03-01T09:00:00",
    "artistCategories": ["Director", "Filmmaker"],
    "skills": ["Film Direction", "Storytelling", "Team Management"],
    "genres": ["Drama", "Action", "Thriller"],
    "languages": ["English", "Hindi"],
    "workLocation": "Studio",
    "workSchedule": "Full-time",
    "contractType": "FULL_TIME",
    "experienceLevel": "SENIOR",
    "educationLevel": "BACHELORS",
    "projectName": "Feature Film Project",
    "projectType": "Feature Film",
    "projectDuration": "6 months",
    "projectBudget": "500000",
    "projectDescription": "A high-budget feature film project",
    "contactPerson": "John Smith",
    "contactEmail": "john@abcstudios.com",
    "contactPhone": "+1234567890",
    "companyWebsite": "https://abcstudios.com",
    "isVisible": true,
    "isBoosted": false,
    "dynamicFields": [
      {
        "fieldName": "special_requirements",
        "fieldType": "TEXTAREA",
        "fieldValue": "Must have experience with action sequences",
        "displayName": "Special Requirements",
        "isRequired": false,
        "helpText": "Any special requirements for this role"
      }
    ]
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Job posted successfully",
  "data": {
    "jobId": 123,
    "title": "Film Director Needed",
    "status": "OPEN",
    "createdAt": "2024-01-15T10:30:00",
    "remainingJobPosts": 24
  }
}
```

### 3. **Get My Jobs**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/jobs?page=0&size=20" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
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
      "salaryMin": 50000,
      "salaryMax": 80000,
      "currency": "USD",
      "status": "OPEN",
      "isActive": true,
      "applicationDeadline": "2024-02-15T23:59:59",
      "startDate": "2024-03-01T09:00:00",
      "createdAt": "2024-01-15T10:30:00",
      "applicationCount": 15,
      "viewCount": 50,
      "boostCount": 0,
      "canEdit": true,
      "canClose": true,
      "canBoost": true,
      "canViewApplications": true
    }
  ],
  "totalElements": 15,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20
}
```

### 4. **Get Job Applications**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/jobs/123/applications?page=0&size=20" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "jobId": 123,
      "jobTitle": "Film Director Needed",
      "artistId": 789,
      "artistName": "Sarah Johnson",
      "artistEmail": "sarah@example.com",
      "artistPhone": "+1234567890",
      "artistCategory": "Director",
      "applicationStatus": "PENDING",
      "appliedAt": "2024-01-16T09:15:00",
      "lastUpdatedAt": "2024-01-16T09:15:00",
      "artistBio": "Experienced film director with 10+ years in the industry",
      "artistLocation": "Mumbai, India",
      "artistExperience": 10,
      "artistSkills": "Film Direction, Storytelling, Team Management",
      "artistPortfolio": "https://sarahjohnson.com/portfolio",
      "coverLetter": "I am very interested in this position...",
      "expectedSalary": "70000",
      "availability": "Immediately",
      "additionalNotes": "Available for international projects",
      "canViewProfile": true,
      "canAccept": true,
      "canReject": true,
      "canShortlist": true,
      "canMessage": true
    }
  ],
  "totalElements": 15,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20
}
```

### 5. **Browse Artists**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/artists?artistCategory=Director&location=Mumbai&page=0&size=20" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "artistId": 789,
      "artistName": "Sarah Johnson",
      "artistEmail": "sarah@example.com",
      "artistCategory": "Director",
      "artistType": "Film Director",
      "location": "Mumbai, India",
      "bio": "Experienced film director with 10+ years in the industry",
      "profilePhoto": "https://example.com/photo.jpg",
      "matchScore": 0.95,
      "matchReasons": [
        "Skills match: Film Direction, Storytelling",
        "Location match: Mumbai, India",
        "Experience level match: Senior level"
      ],
      "skills": ["Film Direction", "Storytelling", "Team Management"],
      "genres": ["Drama", "Action", "Thriller"],
      "languages": ["English", "Hindi"],
      "experienceYears": 10,
      "experienceLevel": "SENIOR",
      "portfolioItems": ["Film 1", "Film 2", "Film 3"],
      "achievements": ["Award 1", "Award 2"],
      "certifications": ["Certification 1", "Certification 2"],
      "availability": "Immediately",
      "preferredJobType": "FULL_TIME",
      "expectedSalaryMin": 60000,
      "expectedSalaryMax": 90000,
      "currency": "USD",
      "workLocation": "Studio",
      "workSchedule": "Full-time",
      "phone": "+1234567890",
      "website": "https://sarahjohnson.com",
      "socialLinks": ["LinkedIn", "Twitter"],
      "contactPreference": "Email",
      "lastActive": "2024-01-15T10:30:00",
      "totalApplications": 25,
      "totalHires": 8,
      "hireRate": 0.32,
      "verificationStatus": "VERIFIED",
      "isVerified": true,
      "isPremium": true,
      "canViewProfile": true,
      "canContact": true,
      "canShortlist": true,
      "canInvite": true,
      "canMessage": true
    }
  ],
  "totalElements": 50,
  "totalPages": 3,
  "currentPage": 0,
  "size": 20
}
```

### 6. **Get Artist Suggestions**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/suggestions?jobId=123&limit=10" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "artistId": 789,
      "artistName": "Sarah Johnson",
      "artistEmail": "sarah@example.com",
      "artistCategory": "Director",
      "artistType": "Film Director",
      "location": "Mumbai, India",
      "bio": "Experienced film director with 10+ years in the industry",
      "profilePhoto": "https://example.com/photo.jpg",
      "matchScore": 0.95,
      "matchReasons": [
        "Skills match: Film Direction, Storytelling",
        "Location match: Mumbai, India",
        "Experience level match: Senior level"
      ],
      "skills": ["Film Direction", "Storytelling", "Team Management"],
      "genres": ["Drama", "Action", "Thriller"],
      "languages": ["English", "Hindi"],
      "experienceYears": 10,
      "experienceLevel": "SENIOR",
      "portfolioItems": ["Film 1", "Film 2", "Film 3"],
      "achievements": ["Award 1", "Award 2"],
      "certifications": ["Certification 1", "Certification 2"],
      "availability": "Immediately",
      "preferredJobType": "FULL_TIME",
      "expectedSalaryMin": 60000,
      "expectedSalaryMax": 90000,
      "currency": "USD",
      "workLocation": "Studio",
      "workSchedule": "Full-time",
      "phone": "+1234567890",
      "website": "https://sarahjohnson.com",
      "socialLinks": ["LinkedIn", "Twitter"],
      "contactPreference": "Email",
      "lastActive": "2024-01-15T10:30:00",
      "totalApplications": 25,
      "totalHires": 8,
      "hireRate": 0.32,
      "verificationStatus": "VERIFIED",
      "isVerified": true,
      "isPremium": true,
      "canViewProfile": true,
      "canContact": true,
      "canShortlist": true,
      "canInvite": true,
      "canMessage": true
    }
  ],
  "count": 10
}
```

### 7. **Get Hires and Past Jobs**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/hires?page=0&size=20" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "jobId": 123,
      "jobTitle": "Film Director Needed",
      "artistId": 789,
      "artistName": "Sarah Johnson",
      "artistEmail": "sarah@example.com",
      "artistCategory": "Director",
      "hireStatus": "COMPLETED",
      "hiredAt": "2024-01-20T14:30:00",
      "startDate": "2024-03-01T09:00:00",
      "endDate": "2024-08-31T18:00:00",
      "agreedSalary": 75000,
      "currency": "USD",
      "contractType": "FULL_TIME",
      "workLocation": "Studio",
      "workSchedule": "Full-time",
      "performanceRating": "5",
      "feedback": "Excellent work, highly recommended",
      "isCompleted": true,
      "isRecommended": true,
      "canViewProfile": true,
      "canRate": true,
      "canRecommend": true,
      "canRehire": true,
      "canMessage": true
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0,
  "size": 20
}
```

### 8. **Rate a Hire**

```bash
curl -X POST "http://localhost:8080/api/recruiter/dashboard/hires/1/rate?rating=5&feedback=Excellent work" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Rating submitted successfully",
  "data": {
    "hireId": 1,
    "rating": "5",
    "feedback": "Excellent work",
    "message": "Rating submitted successfully"
  }
}
```

### 9. **Get Recruiter Statistics**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/statistics" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalJobsPosted": 15,
    "activeJobs": 8,
    "closedJobs": 7,
    "totalApplications": 120,
    "totalHires": 5,
    "totalViews": 500,
    "averageApplicationsPerJob": 8.0,
    "hireRate": 0.42
  }
}
```

### 10. **Get Subscription Status**

```bash
curl -X GET "http://localhost:8080/api/recruiter/dashboard/subscription" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "hasSubscription": true,
    "subscriptionId": 456,
    "subscriptionPlan": "Recruiter Premium",
    "subscriptionStatus": "ACTIVE",
    "expiresAt": "2024-12-31T23:59:59",
    "remainingJobPosts": 25,
    "maxJobPosts": 50,
    "canPostJob": true,
    "availableFeatures": [
      "JOB_POSTING",
      "ARTIST_BROWSING",
      "APPLICATION_MANAGEMENT",
      "BASIC_ANALYTICS",
      "ADVANCED_SUGGESTIONS",
      "JOB_BOOSTING",
      "DETAILED_ANALYTICS",
      "PRIORITY_SUPPORT"
    ],
    "premiumFeatures": [
      "ADVANCED_SUGGESTIONS",
      "JOB_BOOSTING",
      "DETAILED_ANALYTICS",
      "PRIORITY_SUPPORT"
    ],
    "hasPremiumFeatures": true
  }
}
```

### 11. **Update Job Status**

```bash
curl -X PUT "http://localhost:8080/api/recruiter/dashboard/jobs/123/status?status=CLOSED&reason=Position filled" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Job status updated successfully",
  "data": {
    "jobId": 123,
    "status": "CLOSED",
    "updatedAt": "2024-01-15T11:00:00"
  }
}
```

### 12. **Boost Job Visibility**

```bash
curl -X POST "http://localhost:8080/api/recruiter/dashboard/jobs/123/boost?days=7" \
  -H "Authorization: Bearer RECRUITER_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Job boosted successfully",
  "data": {
    "jobId": 123,
    "boosted": true,
    "boostDays": 7,
    "boostExpiresAt": "2024-01-22T10:30:00"
  }
}
```

---

## 🗄️ Database Schema

### Enhanced Job Management

The system uses existing tables with enhanced functionality:

#### **job_posts** - Main job table
```sql
-- Existing fields
id, title, description, requirements, job_type, location, salary_min, salary_max, 
currency, status, is_active, application_deadline, start_date, created_at, updated_at

-- Enhanced with recruiter dashboard features
recruiter_id BIGINT,
boost_count INT DEFAULT 0,
boost_expires_at DATETIME,
total_views INT DEFAULT 0,
last_boosted_at DATETIME
```

#### **job_applications** - Application tracking
```sql
id, job_post_id, artist_id, status, cover_letter, applied_at, updated_at,
expected_salary, availability, additional_notes
```

#### **usage_tracking** - Subscription usage
```sql
id, user_id, feature_type, feature_value, usage_date, created_at
```

#### **subscriptions** - Subscription management
```sql
id, user_id, subscription_plan_id, status, start_date, expires_at, created_at
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
# Login as recruiter
curl -X POST "http://localhost:8080/api/auth/email/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "recruiter@icastar.com",
    "password": "recruiter_password"
  }'

# Get dashboard
curl -X GET "http://localhost:8080/api/recruiter/dashboard" \
  -H "Authorization: Bearer YOUR_RECRUITER_JWT_TOKEN"

# Post a job
curl -X POST "http://localhost:8080/api/recruiter/dashboard/jobs" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_RECRUITER_JWT_TOKEN" \
  -d '{
    "title": "Film Director Needed",
    "description": "Looking for an experienced film director",
    "jobType": "FULL_TIME",
    "location": "Mumbai, India"
  }'
```

---

## 🔧 Key Features

### ✅ **Job Posting System**
- **Subscription-based posting** - Requires active subscription
- **Dynamic fields** - Custom job fields
- **Job boosting** - Premium visibility feature
- **Status management** - Open, closed, draft states
- **Performance tracking** - Views, applications, hires

### ✅ **Artist Management**
- **Advanced search** - Filter by skills, location, experience
- **AI suggestions** - Smart artist recommendations
- **Portfolio viewing** - Complete artist profiles
- **Direct contact** - Message artists directly
- **Application tracking** - Manage all applications

### ✅ **Hiring Analytics**
- **Success tracking** - Monitor hiring success
- **Performance rating** - Rate hired artists
- **Feedback system** - Provide recommendations
- **Statistics dashboard** - Comprehensive insights
- **Trend analysis** - Hiring patterns and trends

### ✅ **Subscription Integration**
- **Usage tracking** - Monitor subscription limits
- **Feature gating** - Premium features for paid plans
- **Upgrade prompts** - Encourage plan upgrades
- **Billing integration** - Track usage for billing

---

## 📊 Recruiter Dashboard Workflow

### Typical Recruiter Workflow

1. **Login** - Access recruiter dashboard
2. **Post Job** - Create job posting with requirements
3. **View Applications** - See artists who applied
4. **Browse Artists** - Search for additional candidates
5. **Get Suggestions** - AI-powered recommendations
6. **Review Candidates** - Evaluate applications and profiles
7. **Make Hires** - Select and hire artists
8. **Track Performance** - Monitor hiring success
9. **Provide Feedback** - Rate and recommend artists

### Job Posting Process

```
Create Job → Set Requirements → Post Job → Monitor Applications → Review Candidates → Make Hires → Track Performance
```

### Artist Discovery Process

```
Browse Artists → Apply Filters → View Profiles → Get Suggestions → Contact Artists → Evaluate Candidates → Make Decisions
```

---

## 🔗 Related Documentation

- [JOB-MANAGEMENT-SYSTEM.md](JOB-MANAGEMENT-SYSTEM.md) - Admin job management guide
- [SUBSCRIPTION-SYSTEM-DESIGN.md](SUBSCRIPTION-SYSTEM-DESIGN.md) - Subscription system guide
- [RECRUITER-SYSTEM-GUIDE.md](RECRUITER-SYSTEM-GUIDE.md) - Recruiter system guide
- [COMPLETE-SYSTEM-GUIDE.md](COMPLETE-SYSTEM-GUIDE.md) - Overall platform guide
- [README.md](README.md) - Main project documentation

---

*This recruiter dashboard system provides comprehensive tools for recruiters to manage job postings, find the right artists, and track their hiring success with complete subscription integration and analytics.*
