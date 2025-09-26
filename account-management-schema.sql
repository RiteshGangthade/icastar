-- Account Management System for iCastar Platform
-- This file adds account deactivation/reactivation functionality

USE icastar_db;

-- Add account status fields to users table if not exists
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION') NOT NULL DEFAULT 'ACTIVE',
ADD COLUMN IF NOT EXISTS deactivated_at DATETIME NULL,
ADD COLUMN IF NOT EXISTS deactivated_by BIGINT NULL,
ADD COLUMN IF NOT EXISTS deactivation_reason TEXT NULL,
ADD COLUMN IF NOT EXISTS reactivated_at DATETIME NULL,
ADD COLUMN IF NOT EXISTS reactivated_by BIGINT NULL,
ADD COLUMN IF NOT EXISTS reactivation_reason TEXT NULL,
ADD COLUMN IF NOT EXISTS last_activity DATETIME NULL,
ADD COLUMN IF NOT EXISTS login_attempts INT DEFAULT 0,
ADD COLUMN IF NOT EXISTS locked_until DATETIME NULL;

-- Add foreign keys for deactivated_by and reactivated_by
ALTER TABLE users 
ADD CONSTRAINT fk_users_deactivated_by FOREIGN KEY (deactivated_by) REFERENCES users(id) ON DELETE SET NULL,
ADD CONSTRAINT fk_users_reactivated_by FOREIGN KEY (reactivated_by) REFERENCES users(id) ON DELETE SET NULL;

-- Account Management Log table for audit trail
CREATE TABLE IF NOT EXISTS account_management_log (
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

-- Account Status History table
CREATE TABLE IF NOT EXISTS account_status_history (
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

-- Admin Permissions table
CREATE TABLE IF NOT EXISTS admin_permissions (
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

-- Create indexes for better performance
CREATE INDEX idx_users_account_status ON users(account_status);
CREATE INDEX idx_users_deactivated_at ON users(deactivated_at);
CREATE INDEX idx_account_management_log_user_id ON account_management_log(user_id);
CREATE INDEX idx_account_management_log_admin_id ON account_management_log(admin_id);
CREATE INDEX idx_account_management_log_action ON account_management_log(action);
CREATE INDEX idx_account_status_history_user_id ON account_status_history(user_id);
CREATE INDEX idx_admin_permissions_user_id ON admin_permissions(user_id);
CREATE INDEX idx_admin_permissions_permission_type ON admin_permissions(permission_type);

-- Insert default admin permissions (assuming admin users exist)
-- Note: You'll need to update these user IDs based on your actual admin users
INSERT INTO admin_permissions (user_id, permission_type, permission_level, granted_by) VALUES
(1, 'ACCOUNT_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'USER_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'CONTENT_MODERATION', 'SUPER_ADMIN', 1),
(1, 'PAYMENT_MANAGEMENT', 'SUPER_ADMIN', 1),
(1, 'SYSTEM_ADMIN', 'SUPER_ADMIN', 1);

-- Update existing users to have proper account status
UPDATE users SET account_status = 'ACTIVE' WHERE account_status IS NULL;
UPDATE users SET last_activity = updated_at WHERE last_activity IS NULL;
