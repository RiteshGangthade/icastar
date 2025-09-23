# iCastar Platform API Documentation

## Overview
This document provides comprehensive API documentation for the iCastar Talent Management Platform. The API follows RESTful principles and uses JWT authentication.

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## Common Response Formats

### Success Response
```json
{
  "success": true,
  "data": { ... },
  "message": "Operation successful"
}
```

### Error Response
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Error description",
    "details": { ... }
  }
}
```

### Pagination Response
```json
{
  "success": true,
  "data": {
    "content": [ ... ],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

---

## 1. Authentication & User Management

### 1.1 OTP-based Login
```http
POST /auth/otp/send
Content-Type: application/json

{
  "mobile": "+919876543210"
}
```

```http
POST /auth/otp/verify
Content-Type: application/json

{
  "mobile": "+919876543210",
  "otp": "123456"
}
```

### 1.2 User Registration
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "mobile": "+919876543210",
  "password": "password123",
  "role": "ARTIST",
  "firstName": "John",
  "lastName": "Doe"
}
```

### 1.3 Profile Management
```http
GET /users/profile
Authorization: Bearer <token>

PUT /users/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com"
}
```

---

## 2. Artist Type Management (Dynamic System)

### 2.1 Get All Artist Types
```http
GET /public/artist-types
```

Response:
```json
{
  "success": true,
  "data": [
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
}
```

### 2.2 Get Artist Type by ID
```http
GET /public/artist-types/{id}
```

### 2.3 Get Artist Type Fields
```http
GET /public/artist-types/{id}/fields
GET /public/artist-types/{id}/fields/required
```

### 2.4 Admin - Create Artist Type
```http
POST /admin/artist-types
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "name": "NEW_TYPE",
  "displayName": "New Artist Type",
  "description": "Description of new type",
  "iconUrl": "/icons/new-type.png",
  "sortOrder": 10
}
```

### 2.5 Admin - Create Artist Type Field
```http
POST /admin/artist-types/{id}/fields
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "fieldName": "new_field",
  "displayName": "New Field",
  "fieldType": "SELECT",
  "isRequired": true,
  "isSearchable": true,
  "sortOrder": 1,
  "options": {
    "values": ["Option 1", "Option 2", "Option 3"]
  }
}
```

---

## 3. Artist Profile Management

### 3.1 Create Artist Profile
```http
POST /artists/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "artistTypeId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "stageName": "Johnny",
  "bio": "Professional actor with 5 years experience",
  "dateOfBirth": "1990-01-01",
  "gender": "MALE",
  "location": "Mumbai, India",
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
}
```

### 3.2 Get Artist Profile
```http
GET /artists/profile
Authorization: Bearer <token>

GET /artists/{id}/profile
```

### 3.3 Update Artist Profile
```http
PUT /artists/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "bio": "Updated bio",
  "hourlyRate": 6000.00,
  "dynamicFields": [
    {
      "id": 1,
      "fieldValue": "185"
    }
  ]
}
```

### 3.4 Search Artists
```http
GET /artists/search?artistType=ACTOR&location=Mumbai&minRate=3000&maxRate=8000
```

### 3.5 Get Artist by Type
```http
GET /artists/type/{artistTypeId}
GET /artists/type/name/{artistTypeName}
```

---

## 4. Recruiter Profile Management

### 4.1 Create Recruiter Profile
```http
POST /recruiters/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "companyName": "ABC Productions",
  "contactPersonName": "Jane Smith",
  "designation": "HR Manager",
  "companyDescription": "Leading production house",
  "companyWebsite": "https://abcproductions.com",
  "industry": "Entertainment",
  "companySize": "50-100",
  "location": "Mumbai, India"
}
```

### 4.2 Get Recruiter Profile
```http
GET /recruiters/profile
Authorization: Bearer <token>

GET /recruiters/{id}/profile
```

### 4.3 Update Recruiter Profile
```http
PUT /recruiters/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "companyDescription": "Updated description",
  "companySize": "100-500"
}
```

---

## 5. Job Management

### 5.1 Create Job Post
```http
POST /jobs
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "title": "Lead Actor for Web Series",
  "description": "Looking for a talented actor for lead role",
  "requirements": "5+ years experience, good communication skills",
  "skillsRequired": ["Acting", "Dancing", "Singing"],
  "jobType": "FULL_TIME",
  "experienceLevel": "MID",
  "budgetMin": 50000.00,
  "budgetMax": 100000.00,
  "location": "Mumbai",
  "isRemote": false,
  "applicationDeadline": "2024-02-01T23:59:59"
}
```

### 5.2 Get Job Posts
```http
GET /jobs?page=0&size=20&status=ACTIVE&jobType=FULL_TIME
GET /jobs/{id}
```

### 5.3 Update Job Post
```http
PUT /jobs/{id}
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "title": "Updated Job Title",
  "budgetMax": 120000.00
}
```

### 5.4 Search Jobs
```http
GET /jobs/search?location=Mumbai&minBudget=30000&maxBudget=80000&skills=Acting
```

### 5.5 Boost Job
```http
POST /jobs/{id}/boost
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "duration": 7
}
```

---

## 6. Job Applications

### 6.1 Apply for Job
```http
POST /applications
Authorization: Bearer <artist_token>
Content-Type: application/json

{
  "jobPostId": 1,
  "coverLetter": "I am interested in this role...",
  "portfolioUrl": "https://portfolio.com",
  "resumeUrl": "https://resume.com",
  "proposedRate": 75000.00,
  "availabilityDate": "2024-01-15T00:00:00"
}
```

### 6.2 Get Applications
```http
GET /applications?status=PENDING&page=0&size=20
Authorization: Bearer <token>

GET /applications/{id}
```

### 6.3 Update Application Status
```http
PUT /applications/{id}/status
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "status": "SHORTLISTED",
  "notes": "Great portfolio, shortlisted for audition"
}
```

### 6.4 Get Job Applications (Recruiter)
```http
GET /jobs/{jobId}/applications
Authorization: Bearer <recruiter_token>
```

---

## 7. Auditions

### 7.1 Schedule Audition
```http
POST /auditions
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "jobApplicationId": 1,
  "artistId": 1,
  "auditionType": "LIVE_VIDEO",
  "scheduledAt": "2024-01-20T14:00:00",
  "durationMinutes": 30,
  "meetingLink": "https://meet.google.com/abc-defg-hij",
  "instructions": "Please prepare a monologue"
}
```

### 7.2 Get Auditions
```http
GET /auditions?status=SCHEDULED&page=0&size=20
Authorization: Bearer <token>

GET /auditions/{id}
```

### 7.3 Update Audition
```http
PUT /auditions/{id}
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "status": "COMPLETED",
  "feedback": "Excellent performance",
  "rating": 5
}
```

---

## 8. Messaging

### 8.1 Send Message
```http
POST /messages
Authorization: Bearer <token>
Content-Type: application/json

{
  "recipientId": 2,
  "content": "Hello, I'm interested in your job posting",
  "messageType": "TEXT",
  "isPaidMessage": false
}
```

### 8.2 Get Messages
```http
GET /messages/conversation/{recipientId}?page=0&size=50
Authorization: Bearer <token>
```

### 8.3 Mark Message as Read
```http
PUT /messages/{id}/read
Authorization: Bearer <token>
```

### 8.4 Unlock Chat (Recruiter)
```http
POST /messages/unlock
Authorization: Bearer <recruiter_token>
Content-Type: application/json

{
  "artistId": 1,
  "credits": 5
}
```

---

## 9. Subscription Management

### 9.1 Get Subscription Plans
```http
GET /subscriptions/plans
```

### 9.2 Subscribe to Plan
```http
POST /subscriptions
Authorization: Bearer <token>
Content-Type: application/json

{
  "subscriptionPlanId": 2,
  "billingCycle": "MONTHLY",
  "autoRenew": true
}
```

### 9.3 Get User Subscriptions
```http
GET /subscriptions
Authorization: Bearer <token>
```

### 9.4 Cancel Subscription
```http
PUT /subscriptions/{id}/cancel
Authorization: Bearer <token>
```

---

## 10. Payment Management

### 10.1 Create Payment
```http
POST /payments
Authorization: Bearer <token>
Content-Type: application/json

{
  "paymentType": "SUBSCRIPTION",
  "amount": 999.00,
  "currency": "INR",
  "paymentMethod": "RAZORPAY",
  "subscriptionId": 1
}
```

### 10.2 Get Payments
```http
GET /payments?status=SUCCESS&page=0&size=20
Authorization: Bearer <token>
```

### 10.3 Payment Webhook (Razorpay)
```http
POST /payments/webhook/razorpay
Content-Type: application/json

{
  "event": "payment.captured",
  "payload": { ... }
}
```

---

## 11. Admin Panel APIs

### 11.1 User Management
```http
GET /admin/users?role=ARTIST&status=ACTIVE&page=0&size=20
Authorization: Bearer <admin_token>

PUT /admin/users/{id}/status
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "status": "BANNED",
  "reason": "Violation of terms"
}

PUT /admin/users/{id}/verify
Authorization: Bearer <admin_token>
```

### 11.2 Job Management
```http
GET /admin/jobs?status=ACTIVE&recruiterId=1&page=0&size=20
Authorization: Bearer <admin_token>

PUT /admin/jobs/{id}/visibility
Authorization: Bearer <admin_token>
Content-Type: application/json

{
  "isVisible": false,
  "reason": "Inappropriate content"
}
```

### 11.3 Analytics
```http
GET /admin/analytics/dashboard
Authorization: Bearer <admin_token>

GET /admin/analytics/users?period=30d
Authorization: Bearer <admin_token>

GET /admin/analytics/jobs?period=7d
Authorization: Bearer <admin_token>
```

### 11.4 Audit Logs
```http
GET /admin/audit-logs?userId=1&action=LOGIN&page=0&size=50
Authorization: Bearer <admin_token>
```

---

## 12. Notifications

### 12.1 Get Notifications
```http
GET /notifications?isRead=false&page=0&size=20
Authorization: Bearer <token>
```

### 12.2 Mark Notification as Read
```http
PUT /notifications/{id}/read
Authorization: Bearer <token>
```

### 12.3 Mark All as Read
```http
PUT /notifications/read-all
Authorization: Bearer <token>
```

---

## 13. File Upload

### 13.1 Upload Profile Image
```http
POST /upload/profile-image
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <image_file>
```

### 13.2 Upload Portfolio File
```http
POST /upload/portfolio
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <file>
type: "video" | "audio" | "document"
```

### 13.3 Upload Resume
```http
POST /upload/resume
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <resume_file>
```

---

## 14. Search & Filtering

### 14.1 Advanced Artist Search
```http
GET /search/artists?artistType=ACTOR&location=Mumbai&minExperience=3&maxRate=10000&skills=Acting,Dancing&isVerified=true&page=0&size=20
```

### 14.2 Advanced Job Search
```http
GET /search/jobs?location=Mumbai&jobType=FULL_TIME&minBudget=50000&maxBudget=200000&experienceLevel=MID&isRemote=false&page=0&size=20
```

### 14.3 Global Search
```http
GET /search?q=actor&type=artist,job&page=0&size=20
```

---

## 15. WebSocket Endpoints

### 15.1 Real-time Messaging
```
ws://localhost:8080/ws/messages?token=<jwt_token>
```

### 15.2 Live Notifications
```
ws://localhost:8080/ws/notifications?token=<jwt_token>
```

---

## Error Codes

| Code | Description |
|------|-------------|
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Validation Error |
| 500 | Internal Server Error |

## Rate Limiting
- Public APIs: 100 requests per minute
- Authenticated APIs: 1000 requests per minute
- Admin APIs: 5000 requests per minute

## Third-party Integration Placeholders

### AWS S3 (File Storage)
- Profile images: `s3://icastar-uploads/profile-images/`
- Portfolio files: `s3://icastar-uploads/portfolio/`
- Resumes: `s3://icastar-uploads/resumes/`

### Razorpay (Payments)
- Webhook URL: `/api/payments/webhook/razorpay`
- Payment methods: UPI, Cards, Net Banking, Wallets

### Stripe (International Payments)
- Webhook URL: `/api/payments/webhook/stripe`
- Payment methods: Cards, International transfers

### Firebase (Push Notifications)
- FCM server key configuration
- Topic-based notifications
- Device token management

### SMTP (Email Notifications)
- Email templates for various notifications
- SMTP configuration for transactional emails
- Email verification and password reset

---

## Development Notes

1. **Dynamic Artist Types**: The system supports adding new artist types with custom fields without code changes
2. **File Upload**: All file uploads are handled through AWS S3 with proper validation
3. **Real-time Features**: WebSocket connections for messaging and notifications
4. **Payment Integration**: Support for both Razorpay (India) and Stripe (International)
5. **Admin Panel**: Comprehensive admin APIs for platform management
6. **Audit Logging**: All admin actions are logged for compliance
7. **Search & Filtering**: Advanced search capabilities with multiple filters
8. **Subscription Management**: Flexible subscription plans with usage tracking
