#!/bin/bash

# Setup Job Management System for iCastar Platform
# This script verifies the job management system setup

echo "💼 Setting up iCastar Job Management System..."

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

echo "🔍 Verifying job management system setup..."

# Check job posts table
echo "📊 Job Posts Table:"
mysql -u root -p icastar_db -e "SELECT COUNT(*) as total_jobs FROM job_posts;"

echo ""
echo "📋 Job Posts by Status:"
mysql -u root -p icastar_db -e "SELECT status, COUNT(*) as count FROM job_posts GROUP BY status;"

echo ""
echo "👥 Job Posts by Recruiter:"
mysql -u root -p icastar_db -e "SELECT rp.company_name, COUNT(*) as job_count FROM job_posts jp JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id GROUP BY rp.company_name ORDER BY job_count DESC LIMIT 10;"

echo ""
echo "📈 Job Posts by Type:"
mysql -u root -p icastar_db -e "SELECT job_type, COUNT(*) as count FROM job_posts GROUP BY job_type;"

echo ""
echo "🏢 Recruiter Categories:"
mysql -u root -p icastar_db -e "SELECT rc.display_name, COUNT(*) as count FROM job_posts jp JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id JOIN recruiter_categories rc ON rp.recruiter_category_id = rc.id GROUP BY rc.display_name;"

echo ""
echo "💳 Subscription Plans:"
mysql -u root -p icastar_db -e "SELECT sp.name, COUNT(*) as count FROM job_posts jp JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id JOIN users u ON rp.user_id = u.id JOIN subscriptions s ON u.id = s.user_id JOIN subscription_plans sp ON s.subscription_plan_id = sp.id GROUP BY sp.name;"

echo ""
echo "📊 Recent Jobs (Last 7 days):"
mysql -u root -p icastar_db -e "SELECT COUNT(*) as recent_jobs FROM job_posts WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY);"

echo ""
echo "🎉 Job Management System Setup Complete!"
echo ""
echo "📝 Available Features:"
echo "   • View all job posts with pagination"
echo "   • Filter by recruiter, status, subscription plan"
echo "   • Toggle job visibility"
echo "   • Bulk operations"
echo "   • Statistics dashboard"
echo "   • Audit trail"
echo ""
echo "🔧 Filtering Options:"
echo "   • Recruiter: ID, name, email, company, category"
echo "   • Status: Active/inactive, visible/hidden"
echo "   • Subscription: Plan, status, type"
echo "   • Job Details: Type, location, title"
echo "   • Dates: Created, application deadline"
echo "   • Statistics: Applications, views, boost"
echo ""
echo "🚀 Next Steps:"
echo "1. Build the project: mvn clean compile"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Test the job management API endpoints"
echo "4. Verify admin permissions"
echo ""
echo "📚 API Endpoints Available:"
echo "   • GET /api/admin/jobs - Get all jobs with filtering"
echo "   • GET /api/admin/jobs/{jobId} - Get job details"
echo "   • POST /api/admin/jobs/{jobId}/toggle-visibility - Toggle visibility"
echo "   • POST /api/admin/jobs/bulk-toggle-visibility - Bulk toggle"
echo "   • GET /api/admin/jobs/statistics - Get statistics"
echo "   • GET /api/admin/jobs/recruiters - Get recruiters list"
echo "   • GET /api/admin/jobs/subscription-plans - Get subscription plans"
echo ""
echo "📖 Documentation:"
echo "   • JOB-MANAGEMENT-SYSTEM.md - Complete system guide"
echo "   • ACCOUNT-MANAGEMENT-SYSTEM.md - Account management guide"
echo "   • COMPLETE-SYSTEM-GUIDE.md - Overall platform guide"
