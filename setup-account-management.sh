#!/bin/bash

# Setup Account Management System for iCastar Platform
# This script applies the account management schema and creates admin permissions

echo "🔐 Setting up iCastar Account Management System..."

# Check if MySQL is running
if ! pgrep -x "mysqld" > /dev/null; then
    echo "❌ MySQL is not running. Please start MySQL first."
    exit 1
fi

# Check if database exists
if ! mysql -u root -p -e "USE icastar_db;" 2>/dev/null; then
    echo "❌ Database 'icastar_db' does not exist. Please create it first."
    echo "Run: mysql -u root -p -e 'CREATE DATABASE icastar_db;'"
    exit 1
fi

echo "📊 Applying account management schema..."
mysql -u root -p icastar_db < account-management-schema.sql

if [ $? -eq 0 ]; then
    echo "✅ Account management schema applied successfully"
else
    echo "❌ Failed to apply account management schema"
    exit 1
fi

echo "🔍 Verifying setup..."

# Check account statuses
echo "📊 User Account Statuses:"
mysql -u root -p icastar_db -e "SELECT account_status, COUNT(*) as count FROM users GROUP BY account_status;"

echo ""
echo "👥 Admin Permissions:"
mysql -u root -p icastar_db -e "SELECT permission_type, permission_level, COUNT(*) as count FROM admin_permissions GROUP BY permission_type, permission_level;"

echo ""
echo "📋 Account Management Logs:"
mysql -u root -p icastar_db -e "SELECT action, COUNT(*) as count FROM account_management_log GROUP BY action;"

echo ""
echo "📈 Account Status History:"
mysql -u root -p icastar_db -e "SELECT status, COUNT(*) as count FROM account_status_history GROUP BY status;"

echo ""
echo "🎉 Account Management System Setup Complete!"
echo ""
echo "📝 Available Account Statuses:"
echo "   • ACTIVE - Normal account status"
echo "   • INACTIVE - Temporarily deactivated"
echo "   • SUSPENDED - Suspended for violations"
echo "   • BANNED - Permanently banned"
echo "   • PENDING_VERIFICATION - Awaiting verification"
echo ""
echo "👤 Admin Permission Levels:"
echo "   • READ - View-only access"
echo "   • WRITE - Basic management (deactivate/reactivate)"
echo "   • ADMIN - Full management (suspend/ban)"
echo "   • SUPER_ADMIN - Complete control (unban)"
echo ""
echo "🔧 Permission Types:"
echo "   • ACCOUNT_MANAGEMENT - Manage user accounts"
echo "   • USER_MANAGEMENT - Manage user profiles"
echo "   • CONTENT_MODERATION - Moderate content"
echo "   • PAYMENT_MANAGEMENT - Handle payments"
echo "   • SYSTEM_ADMIN - System administration"
echo ""
echo "🚀 Next Steps:"
echo "1. Build the project: mvn clean compile"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Create admin users with proper permissions"
echo "4. Test the account management API endpoints"
echo ""
echo "📚 API Endpoints Available:"
echo "   • GET /api/admin/accounts - Get all users"
echo "   • GET /api/admin/accounts/{userId} - Get user details"
echo "   • POST /api/admin/accounts/{userId}/deactivate - Deactivate account"
echo "   • POST /api/admin/accounts/{userId}/reactivate - Reactivate account"
echo "   • POST /api/admin/accounts/{userId}/suspend - Suspend account"
echo "   • POST /api/admin/accounts/{userId}/ban - Ban account"
echo "   • GET /api/admin/accounts/{userId}/logs - Get account logs"
echo "   • GET /api/admin/accounts/statistics - Get statistics"
echo ""
echo "📖 Documentation:"
echo "   • ACCOUNT-MANAGEMENT-SYSTEM.md - Complete system guide"
echo "   • COMPLETE-SYSTEM-GUIDE.md - Overall platform guide"
