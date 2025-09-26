# 🔐 iCastar Account Management System

## 📋 Table of Contents
1. [System Overview](#system-overview)
2. [Account Statuses](#account-statuses)
3. [Admin Permissions](#admin-permissions)
4. [API Endpoints](#api-endpoints)
5. [Database Schema](#database-schema)
6. [Usage Examples](#usage-examples)
7. [Setup Instructions](#setup-instructions)

---

## 🎯 System Overview

The iCastar Account Management System provides **admin and super admin** with comprehensive tools to manage user accounts, including deactivation, reactivation, suspension, and banning functionality.

### 🏗️ Core Features

- ✅ **Account Deactivation/Reactivation** - Temporarily disable user accounts
- ✅ **Account Suspension** - Suspend accounts for policy violations
- ✅ **Account Banning** - Permanently ban problematic users
- ✅ **Audit Trail** - Complete logging of all account actions
- ✅ **Permission System** - Role-based access control for admins
- ✅ **Statistics Dashboard** - Account management analytics

---

## 📊 Account Statuses

### Account Status Types

| Status | Description | User Access | Admin Action Required |
|--------|-------------|-------------|----------------------|
| **ACTIVE** | Normal account status | ✅ Full access | None |
| **INACTIVE** | Temporarily deactivated | ❌ No access | Admin reactivation |
| **SUSPENDED** | Suspended for violations | ❌ No access | Admin unsuspension |
| **BANNED** | Permanently banned | ❌ No access | Super admin unban |
| **PENDING_VERIFICATION** | Awaiting verification | ⚠️ Limited access | Auto or manual verification |

### Status Transitions

```
ACTIVE ←→ INACTIVE (Admin can deactivate/reactivate)
ACTIVE → SUSPENDED (Admin can suspend)
SUSPENDED → ACTIVE (Admin can unsuspend)
ACTIVE → BANNED (Admin can ban)
BANNED → ACTIVE (Super admin can unban)
PENDING_VERIFICATION → ACTIVE (Auto or manual verification)
```

---

## 👥 Admin Permissions

### Permission Levels

| Level | Description | Capabilities |
|-------|-------------|--------------|
| **READ** | View-only access | View account information |
| **WRITE** | Basic management | Deactivate/reactivate accounts |
| **ADMIN** | Full management | All account actions except unban |
| **SUPER_ADMIN** | Complete control | All actions including unban |

### Permission Types

| Type | Description | Actions |
|------|-------------|---------|
| **ACCOUNT_MANAGEMENT** | Manage user accounts | Deactivate, reactivate, suspend, ban |
| **USER_MANAGEMENT** | Manage user profiles | View, edit user information |
| **CONTENT_MODERATION** | Moderate content | Review and moderate user content |
| **PAYMENT_MANAGEMENT** | Manage payments | Handle payment-related issues |
| **SYSTEM_ADMIN** | System administration | Full system access |

---

## 🌐 API Endpoints

### Account Management Endpoints

| Method | Endpoint | Description | Permission Required |
|--------|----------|-------------|-------------------|
| GET | `/api/admin/accounts` | Get all users with filtering | ACCOUNT_MANAGEMENT (READ) |
| GET | `/api/admin/accounts/{userId}` | Get user details | ACCOUNT_MANAGEMENT (READ) |
| POST | `/api/admin/accounts/{userId}/deactivate` | Deactivate account | ACCOUNT_MANAGEMENT (WRITE) |
| POST | `/api/admin/accounts/{userId}/reactivate` | Reactivate account | ACCOUNT_MANAGEMENT (WRITE) |
| POST | `/api/admin/accounts/{userId}/suspend` | Suspend account | ACCOUNT_MANAGEMENT (ADMIN) |
| POST | `/api/admin/accounts/{userId}/ban` | Ban account | ACCOUNT_MANAGEMENT (ADMIN) |
| GET | `/api/admin/accounts/{userId}/logs` | Get account logs | ACCOUNT_MANAGEMENT (READ) |
| GET | `/api/admin/accounts/statistics` | Get account statistics | ACCOUNT_MANAGEMENT (READ) |

---

## 🗄️ Database Schema

### Core Tables

#### `users` - Enhanced User Table
```sql
-- Account Management Fields
ALTER TABLE users ADD COLUMN account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION') NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE users ADD COLUMN deactivated_at DATETIME NULL;
ALTER TABLE users ADD COLUMN deactivated_by BIGINT NULL;
ALTER TABLE users ADD COLUMN deactivation_reason TEXT NULL;
ALTER TABLE users ADD COLUMN reactivated_at DATETIME NULL;
ALTER TABLE users ADD COLUMN reactivated_by BIGINT NULL;
ALTER TABLE users ADD COLUMN reactivation_reason TEXT NULL;
ALTER TABLE users ADD COLUMN last_activity DATETIME NULL;
ALTER TABLE users ADD COLUMN login_attempts INT DEFAULT 0;
ALTER TABLE users ADD COLUMN locked_until DATETIME NULL;
```

#### `account_management_log` - Audit Trail
```sql
CREATE TABLE account_management_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    admin_id BIGINT NOT NULL,
    action ENUM('ACTIVATE', 'DEACTIVATE', 'SUSPEND', 'UNSUSPEND', 'BAN', 'UNBAN', 'VERIFY', 'UNVERIFY') NOT NULL,
    previous_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION') NOT NULL,
    new_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION') NOT NULL,
    reason TEXT,
    admin_notes TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `account_status_history` - Status History
```sql
CREATE TABLE account_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION') NOT NULL,
    changed_by BIGINT NOT NULL,
    reason TEXT,
    notes TEXT,
    effective_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by) REFERENCES users(id) ON DELETE CASCADE
);
```

#### `admin_permissions` - Permission Management
```sql
CREATE TABLE admin_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    permission_type ENUM('ACCOUNT_MANAGEMENT', 'USER_MANAGEMENT', 'CONTENT_MODERATION', 'PAYMENT_MANAGEMENT', 'SYSTEM_ADMIN') NOT NULL,
    permission_level ENUM('READ', 'WRITE', 'ADMIN', 'SUPER_ADMIN') NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    granted_by BIGINT NOT NULL,
    granted_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (granted_by) REFERENCES users(id) ON DELETE CASCADE
);
```

---

## 🎬 Usage Examples

### 1. **Deactivate User Account**

```bash
curl -X POST "http://localhost:8080/api/admin/accounts/123/deactivate" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "reason": "User requested account deactivation",
    "adminNotes": "Temporary deactivation for personal reasons"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Account deactivated successfully",
  "data": {
    "userId": 123,
    "email": "user@example.com",
    "currentStatus": "INACTIVE",
    "deactivatedAt": "2024-01-15T10:30:00",
    "isActive": false
  }
}
```

### 2. **Reactivate User Account**

```bash
curl -X POST "http://localhost:8080/api/admin/accounts/123/reactivate" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "reason": "User requested account reactivation",
    "adminNotes": "Account reactivated after verification"
  }'
```

### 3. **Suspend User Account**

```bash
curl -X POST "http://localhost:8080/api/admin/accounts/123/suspend" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "reason": "Violation of community guidelines",
    "adminNotes": "Suspended for 30 days due to inappropriate content"
  }'
```

### 4. **Ban User Account**

```bash
curl -X POST "http://localhost:8080/api/admin/accounts/123/ban" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN" \
  -d '{
    "reason": "Repeated violations and harassment",
    "adminNotes": "Permanent ban due to severe policy violations"
  }'
```

### 5. **Get Account Logs**

```bash
curl -X GET "http://localhost:8080/api/admin/accounts/123/logs?page=0&size=10" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "action": "DEACTIVATE",
      "previousStatus": "ACTIVE",
      "newStatus": "INACTIVE",
      "reason": "User requested account deactivation",
      "adminNotes": "Temporary deactivation",
      "adminName": "John Admin",
      "createdAt": "2024-01-15T10:30:00",
      "ipAddress": "192.168.1.100"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "size": 10
}
```

### 6. **Get Account Statistics**

```bash
curl -X GET "http://localhost:8080/api/admin/accounts/statistics" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "activeUsers": 1250,
    "inactiveUsers": 45,
    "suspendedUsers": 12,
    "bannedUsers": 8,
    "pendingUsers": 23,
    "totalUsers": 1338,
    "recentChanges": 15
  }
}
```

### 7. **Get All Users with Filtering**

```bash
# Get all users
curl -X GET "http://localhost:8080/api/admin/accounts?page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"

# Filter by status
curl -X GET "http://localhost:8080/api/admin/accounts?status=ACTIVE&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"

# Filter by role
curl -X GET "http://localhost:8080/api/admin/accounts?role=ARTIST&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"

# Search users
curl -X GET "http://localhost:8080/api/admin/accounts?search=john&page=0&size=20" \
  -H "Authorization: Bearer ADMIN_JWT_TOKEN"
```

---

## 🚀 Setup Instructions

### 1. **Apply Database Schema**

```bash
# Apply account management schema
mysql -u root -p icastar_db < account-management-schema.sql
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

### 3. **Create Admin Users**

```sql
-- Create admin user
INSERT INTO users (email, mobile, password, role, account_status, is_verified) 
VALUES ('admin@icastar.com', '+1234567890', '$2a$10$encrypted_password', 'ADMIN', 'ACTIVE', TRUE);

-- Grant admin permissions
INSERT INTO admin_permissions (user_id, permission_type, permission_level, granted_by) VALUES
(1, 'ACCOUNT_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'USER_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'CONTENT_MODERATION', 'SUPER_ADMIN', 1),
(1, 'PAYMENT_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'SYSTEM_ADMIN', 'SUPER_ADMIN', 1);
```

### 4. **Test the System**

```bash
# Login as admin
curl -X POST "http://localhost:8080/api/auth/email/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@icastar.com",
    "password": "admin_password"
  }'

# Use the JWT token in subsequent requests
curl -X GET "http://localhost:8080/api/admin/accounts/statistics" \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

---

## 🔧 Key Features

### ✅ **Role-Based Access Control**
- Only admins and super admins can access account management
- Granular permissions for different admin levels
- Secure API endpoints with proper authentication

### ✅ **Complete Audit Trail**
- Every account action is logged
- IP address and user agent tracking
- Admin notes and reasons for actions
- Complete history of status changes

### ✅ **Flexible Account Management**
- Deactivate/reactivate for temporary actions
- Suspend for policy violations
- Ban for severe violations
- Easy status transitions

### ✅ **Analytics and Reporting**
- Account statistics dashboard
- Recent activity tracking
- User status distribution
- Admin action analytics

### ✅ **Security Features**
- Permission-based access control
- IP address logging
- User agent tracking
- Secure API endpoints

---

## 📊 Account Management Workflow

### Typical Admin Workflow

1. **View Users** - Browse all users with filtering options
2. **Select User** - Click on specific user to view details
3. **Review History** - Check user's account history and logs
4. **Take Action** - Deactivate, suspend, or ban as needed
5. **Log Action** - Provide reason and admin notes
6. **Monitor Results** - Track account status changes

### Status Change Process

```
User Account → Admin Review → Action Decision → Status Change → Audit Log → Notification
```

---

## 🔗 Related Documentation

- [COMPLETE-SYSTEM-GUIDE.md](COMPLETE-SYSTEM-GUIDE.md) - Overall platform guide
- [SUBSCRIPTION-SYSTEM-DESIGN.md](SUBSCRIPTION-SYSTEM-DESIGN.md) - Subscription system
- [RECRUITER-SYSTEM-GUIDE.md](RECRUITER-SYSTEM-GUIDE.md) - Recruiter system guide
- [README.md](README.md) - Main project documentation

---

*This account management system provides comprehensive tools for admins to manage user accounts with full audit trails and role-based access control.*
