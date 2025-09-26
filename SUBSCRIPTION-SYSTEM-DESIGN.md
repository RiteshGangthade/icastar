# 💳 iCastar Subscription System - Complete Design

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Plan Structure](#plan-structure)
3. [Artist Plans](#artist-plans)
4. [Recruiter Plans](#recruiter-plans)
5. [Unified Plans](#unified-plans)
6. [Database Schema](#database-schema)
7. [Usage Tracking](#usage-tracking)
8. [API Design](#api-design)
9. [Implementation Guide](#implementation-guide)

---

## 🎯 System Overview

The iCastar Subscription System provides **role-based subscription plans** for both Artists and Recruiters, with different features and limits tailored to each user type.

### 🏗️ Core Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Artist Plans  │    │ Recruiter Plans│    │  Unified Plans  │
│                 │    │                 │    │                 │
│ • Free          │    │ • Free          │    │ • Basic         │
│ • Basic         │    │ • Basic         │    │ • Premium       │
│ • Premium       │    │ • Premium       │    │ • Professional  │
│ • Professional  │    │ • Professional  │    │ • Enterprise    │
│ • Enterprise    │    │ • Enterprise    │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 📊 Plan Structure

### Plan Types
- **FREE** - Basic access with limited features
- **BASIC** - Essential features for growing users
- **PREMIUM** - Advanced features for professionals
- **PROFESSIONAL** - Complete toolkit for established users
- **ENTERPRISE** - Unlimited access for organizations

### User Roles
- **ARTIST** - For performers and creative professionals
- **RECRUITER** - For hiring managers and production houses
- **BOTH** - For users who need both artist and recruiter features

---

## 🎭 Artist Plans

### 1. **Artist Free** - ₹0/month
**Perfect for**: New artists getting started

#### Features:
- ✅ **Basic Profile** - Create and manage basic artist profile
- ✅ **5 Auditions** - Apply to up to 5 auditions per month
- ✅ **10 Applications** - Submit up to 10 job applications
- ✅ **5 Portfolio Items** - Upload up to 5 portfolio items
- ✅ **Basic Search** - Search and filter job opportunities
- ✅ **Email Support** - Basic email support
- ✅ **10 File Uploads** - Upload up to 10 files (10MB each)

### 2. **Artist Basic** - ₹299/month
**Perfect for**: Growing artists ready to invest in their career

#### Features:
- ✅ **Everything in Free**
- ✅ **25 Auditions** - Apply to up to 25 auditions per month
- ✅ **50 Applications** - Submit up to 50 job applications
- ✅ **20 Portfolio Items** - Upload up to 20 portfolio items
- ✅ **Profile Verification** - Get verified profile badge
- ✅ **50 File Uploads** - Upload up to 50 files (25MB each)

### 3. **Artist Premium** - ₹599/month
**Perfect for**: Professional artists serious about their career

#### Features:
- ✅ **Everything in Basic**
- ✅ **100 Auditions** - Apply to up to 100 auditions per month
- ✅ **200 Applications** - Submit up to 200 job applications
- ✅ **100 Portfolio Items** - Upload up to 100 portfolio items
- ✅ **Featured Profile** - Get featured in search results
- ✅ **Advanced Analytics** - Detailed performance analytics
- ✅ **Priority Support** - 24/7 priority customer support
- ✅ **200 File Uploads** - Upload up to 200 files (50MB each)

### 4. **Artist Professional** - ₹999/month
**Perfect for**: Established artists with significant following

#### Features:
- ✅ **Everything in Premium**
- ✅ **500 Auditions** - Apply to up to 500 auditions per month
- ✅ **1000 Applications** - Submit up to 1000 job applications
- ✅ **500 Portfolio Items** - Upload up to 500 portfolio items
- ✅ **Priority Verification** - Fast-track verification process
- ✅ **1000 File Uploads** - Upload up to 1000 files (100MB each)

### 5. **Artist Enterprise** - ₹1999/month
**Perfect for**: Top-tier artists and talent agencies

#### Features:
- ✅ **Everything in Professional**
- ✅ **Unlimited Auditions** - No limit on auditions
- ✅ **Unlimited Applications** - No limit on applications
- ✅ **Unlimited Portfolio** - No limit on portfolio items
- ✅ **Custom Branding** - Custom branding options
- ✅ **API Access** - Full API access for integrations
- ✅ **Unlimited File Uploads** - No limit on file uploads (500MB each)

---

## 🏢 Recruiter Plans

### 1. **Recruiter Free** - ₹0/month
**Perfect for**: New recruiters testing the platform

#### Features:
- ✅ **2 Job Posts** - Post up to 2 jobs per month
- ✅ **10 Messages** - Send up to 10 messages to candidates
- ✅ **20 Candidate Views** - View up to 20 candidate profiles
- ✅ **Basic Search** - Basic candidate search
- ✅ **Email Support** - Basic email support
- ✅ **10 File Uploads** - Upload up to 10 files (10MB each)

### 2. **Recruiter Basic** - ₹499/month
**Perfect for**: Growing recruiters and small production houses

#### Features:
- ✅ **Everything in Free**
- ✅ **10 Job Posts** - Post up to 10 jobs per month
- ✅ **50 Messages** - Send up to 50 messages to candidates
- ✅ **100 Candidate Views** - View up to 100 candidate profiles
- ✅ **2 Job Boosts** - Boost 2 job posts for better visibility
- ✅ **50 File Uploads** - Upload up to 50 files (25MB each)

### 3. **Recruiter Premium** - ₹999/month
**Perfect for**: Professional recruiters and established production houses

#### Features:
- ✅ **Everything in Basic**
- ✅ **50 Job Posts** - Post up to 50 jobs per month
- ✅ **200 Messages** - Send up to 200 messages to candidates
- ✅ **500 Candidate Views** - View up to 500 candidate profiles
- ✅ **5 Job Boosts** - Boost 5 job posts for better visibility
- ✅ **Advanced Search** - Advanced candidate search and filtering
- ✅ **Candidate Verification** - Verify candidate credentials
- ✅ **Priority Support** - 24/7 priority customer support
- ✅ **200 File Uploads** - Upload up to 200 files (50MB each)

### 4. **Recruiter Professional** - ₹1999/month
**Perfect for**: Large production houses and casting agencies

#### Features:
- ✅ **Everything in Premium**
- ✅ **200 Job Posts** - Post up to 200 jobs per month
- ✅ **1000 Messages** - Send up to 1000 messages to candidates
- ✅ **2000 Candidate Views** - View up to 2000 candidate profiles
- ✅ **10 Job Boosts** - Boost 10 job posts for better visibility
- ✅ **1000 File Uploads** - Upload up to 1000 files (100MB each)

### 5. **Recruiter Enterprise** - ₹4999/month
**Perfect for**: Large organizations and major studios

#### Features:
- ✅ **Everything in Professional**
- ✅ **Unlimited Job Posts** - No limit on job posts
- ✅ **Unlimited Messages** - No limit on messages
- ✅ **Unlimited Candidate Views** - No limit on candidate views
- ✅ **25 Job Boosts** - Boost 25 job posts for better visibility
- ✅ **Custom Branding** - Custom branding options
- ✅ **API Access** - Full API access for integrations
- ✅ **White Label** - Complete white-label solution
- ✅ **Dedicated Manager** - Dedicated account manager
- ✅ **Unlimited File Uploads** - No limit on file uploads (500MB each)

---

## 🔄 Unified Plans

### 1. **Unified Basic** - ₹699/month
**Perfect for**: Users who need both artist and recruiter features

#### Features:
- ✅ **15 Auditions** + **5 Job Posts**
- ✅ **30 Applications** + **30 Messages**
- ✅ **50 Candidate Views**
- ✅ **1 Job Boost**
- ✅ **30 File Uploads** (25MB each)

### 2. **Unified Premium** - ₹1299/month
**Perfect for**: Professionals who work in both capacities

#### Features:
- ✅ **50 Auditions** + **25 Job Posts**
- ✅ **100 Applications** + **100 Messages**
- ✅ **250 Candidate Views**
- ✅ **3 Job Boosts**
- ✅ **Advanced Search** + **Profile Verification**
- ✅ **Featured Profile**
- ✅ **Priority Support**
- ✅ **100 File Uploads** (50MB each)

### 3. **Unified Professional** - ₹2499/month
**Perfect for**: Established professionals with dual roles

#### Features:
- ✅ **250 Auditions** + **100 Job Posts**
- ✅ **500 Applications** + **500 Messages**
- ✅ **1000 Candidate Views**
- ✅ **8 Job Boosts**
- ✅ **500 File Uploads** (100MB each)

### 4. **Unified Enterprise** - ₹6999/month
**Perfect for**: Large organizations with diverse needs

#### Features:
- ✅ **Unlimited Auditions** + **Unlimited Job Posts**
- ✅ **Unlimited Applications** + **Unlimited Messages**
- ✅ **Unlimited Candidate Views**
- ✅ **20 Job Boosts**
- ✅ **Custom Branding** + **API Access** + **White Label**
- ✅ **Unlimited File Uploads** (500MB each)

---

## 🗄️ Database Schema

### Core Tables

#### `subscription_plans` - Defines All Plans
```sql
CREATE TABLE subscription_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    plan_type ENUM('FREE', 'BASIC', 'PREMIUM', 'PROFESSIONAL', 'ENTERPRISE') NOT NULL,
    user_role ENUM('ARTIST', 'RECRUITER', 'BOTH') NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    billing_cycle ENUM('MONTHLY', 'YEARLY', 'ONE_TIME') NOT NULL,
    
    -- Artist features
    max_auditions INT,
    unlimited_auditions BOOLEAN DEFAULT FALSE,
    max_applications INT,
    unlimited_applications BOOLEAN DEFAULT FALSE,
    max_portfolio_items INT,
    unlimited_portfolio BOOLEAN DEFAULT FALSE,
    profile_verification BOOLEAN DEFAULT FALSE,
    featured_profile BOOLEAN DEFAULT FALSE,
    advanced_analytics BOOLEAN DEFAULT FALSE,
    
    -- Recruiter features
    max_job_posts INT,
    unlimited_job_posts BOOLEAN DEFAULT FALSE,
    max_messages INT,
    unlimited_messages BOOLEAN DEFAULT FALSE,
    max_candidates_view INT,
    unlimited_candidates BOOLEAN DEFAULT FALSE,
    job_boost_credits INT DEFAULT 0,
    advanced_search BOOLEAN DEFAULT FALSE,
    candidate_verification BOOLEAN DEFAULT FALSE,
    priority_support BOOLEAN DEFAULT FALSE,
    
    -- Common features
    max_file_uploads INT,
    unlimited_uploads BOOLEAN DEFAULT FALSE,
    max_file_size_mb INT DEFAULT 10,
    custom_branding BOOLEAN DEFAULT FALSE,
    api_access BOOLEAN DEFAULT FALSE,
    white_label BOOLEAN DEFAULT FALSE,
    
    is_featured BOOLEAN DEFAULT FALSE,
    is_popular BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### `subscriptions` - User Subscriptions
```sql
CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_plan_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED', 'SUSPENDED', 'TRIAL') NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    auto_renew BOOLEAN DEFAULT FALSE,
    amount_paid DECIMAL(10,2),
    payment_reference VARCHAR(255),
    
    -- Usage tracking
    auditions_used INT DEFAULT 0,
    applications_used INT DEFAULT 0,
    job_posts_used INT DEFAULT 0,
    messages_used INT DEFAULT 0,
    candidates_viewed INT DEFAULT 0,
    job_boosts_used INT DEFAULT 0,
    file_uploads_used INT DEFAULT 0,
    
    is_trial BOOLEAN DEFAULT FALSE,
    trial_end_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plans(id)
);
```

#### `plan_features` - Detailed Feature Management
```sql
CREATE TABLE plan_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_plan_id BIGINT NOT NULL,
    feature_name VARCHAR(100) NOT NULL,
    feature_description TEXT,
    feature_type ENUM('LIMIT', 'BOOLEAN', 'NUMBER', 'TEXT') NOT NULL,
    feature_value VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (subscription_plan_id) REFERENCES subscription_plans(id) ON DELETE CASCADE
);
```

#### `usage_tracking` - Detailed Analytics
```sql
CREATE TABLE usage_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT NOT NULL,
    feature_name VARCHAR(100) NOT NULL,
    usage_count INT DEFAULT 1,
    usage_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    metadata JSON,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE CASCADE
);
```

---

## 📊 Usage Tracking

### Tracked Features
- **Auditions** - Number of auditions applied to
- **Applications** - Number of job applications submitted
- **Job Posts** - Number of jobs posted
- **Messages** - Number of messages sent
- **Candidate Views** - Number of candidate profiles viewed
- **Job Boosts** - Number of job posts boosted
- **File Uploads** - Number of files uploaded
- **Portfolio Items** - Number of portfolio items added

### Analytics Available
- **Daily Usage** - Track usage by day
- **Feature Usage** - Which features are used most
- **Usage Patterns** - Peak usage times and patterns
- **Limit Tracking** - How close users are to their limits
- **Upgrade Suggestions** - Suggest plan upgrades based on usage

---

## 🌐 API Design

### Subscription Management Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/subscriptions/plans` | List all available plans |
| GET | `/api/subscriptions/plans/{role}` | Get plans for specific role |
| GET | `/api/subscriptions/current` | Get current user's subscription |
| POST | `/api/subscriptions/subscribe` | Subscribe to a plan |
| PUT | `/api/subscriptions/upgrade` | Upgrade subscription |
| PUT | `/api/subscriptions/cancel` | Cancel subscription |
| GET | `/api/subscriptions/usage` | Get usage statistics |

### Usage Tracking Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/usage/current` | Get current usage |
| GET | `/api/usage/history` | Get usage history |
| POST | `/api/usage/track` | Track feature usage |
| GET | `/api/usage/limits` | Get usage limits |

---

## 🚀 Implementation Guide

### 1. **Apply Database Schema**
```bash
# Apply subscription system schema
mysql -u root -p icastar_db < subscription-system-design.sql

# Apply subscription plans data
mysql -u root -p icastar_db < subscription-plans-data.sql
```

### 2. **Build and Run**
```bash
# Set Java 17
export JAVA_HOME=/Users/admin/Library/Java/JavaVirtualMachines/azul-17.0.16/Contents/Home

# Build project
mvn clean compile

# Run application
mvn spring-boot:run
```

### 3. **Test Subscription System**
```bash
# Get available plans
curl -X GET "http://localhost:8080/api/subscriptions/plans/ARTIST"

# Subscribe to a plan
curl -X POST "http://localhost:8080/api/subscriptions/subscribe" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "planId": 2,
    "paymentReference": "PAY_123456789"
  }'

# Check current subscription
curl -X GET "http://localhost:8080/api/subscriptions/current" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 💡 Key Features

### ✅ **Role-Based Plans**
- Different plans for Artists, Recruiters, and Unified users
- Tailored features and limits for each role

### ✅ **Flexible Pricing**
- Free plans for getting started
- Affordable basic plans for growing users
- Premium plans for professionals
- Enterprise plans for organizations

### ✅ **Usage Tracking**
- Real-time usage monitoring
- Limit enforcement
- Analytics and insights

### ✅ **Feature Management**
- Granular feature control
- Easy to add new features
- Flexible plan customization

### ✅ **Payment Integration**
- Support for multiple payment methods
- Subscription management
- Auto-renewal options

---

## 📈 Business Benefits

### For Artists:
- **Free Plan** - Get started without investment
- **Premium Features** - Stand out with featured profiles
- **Analytics** - Track performance and growth
- **Unlimited Access** - Scale without limits

### For Recruiters:
- **Cost-Effective** - Start with free plan
- **Advanced Search** - Find the right talent
- **Job Boosts** - Increase visibility
- **Enterprise Features** - Scale for large organizations

### For Platform:
- **Revenue Generation** - Multiple pricing tiers
- **User Retention** - Value-based pricing
- **Scalability** - Handle growth efficiently
- **Analytics** - Data-driven decisions

---

## 🔗 Related Documentation

- [COMPLETE-SYSTEM-GUIDE.md](COMPLETE-SYSTEM-GUIDE.md) - Overall platform guide
- [RECRUITER-SYSTEM-GUIDE.md](RECRUITER-SYSTEM-GUIDE.md) - Recruiter system guide
- [README.md](README.md) - Main project documentation

---

*This subscription system provides a comprehensive, scalable solution for monetizing the iCastar platform while providing value to both artists and recruiters.*
