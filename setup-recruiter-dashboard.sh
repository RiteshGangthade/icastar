#!/bin/bash

# Setup Recruiter Dashboard System for iCastar Platform
# This script verifies the recruiter dashboard system setup

echo "🎯 Setting up iCastar Recruiter Dashboard System..."

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

echo "🔍 Verifying recruiter dashboard system setup..."

# Check recruiter profiles
echo "👥 Recruiter Profiles:"
mysql -u root -p icastar_db -e "SELECT COUNT(*) as total_recruiters FROM recruiter_profiles;"

echo ""
echo "📊 Recruiter Categories:"
mysql -u root -p icastar_db -e "SELECT rc.display_name, COUNT(*) as count FROM recruiter_profiles rp JOIN recruiter_categories rc ON rp.recruiter_category_id = rc.id GROUP BY rc.display_name;"

echo ""
echo "💼 Job Posts by Recruiter:"
mysql -u root -p icastar_db -e "SELECT rp.company_name, COUNT(*) as job_count FROM job_posts jp JOIN recruiter_profiles rp ON jp.recruiter_id = rp.id GROUP BY rp.company_name ORDER BY job_count DESC LIMIT 10;"

echo ""
echo "📈 Job Posts by Status:"
mysql -u root -p icastar_db -e "SELECT status, COUNT(*) as count FROM job_posts GROUP BY status;"

echo ""
echo "🎭 Artist Profiles:"
mysql -u root -p icastar_db -e "SELECT COUNT(*) as total_artists FROM artist_profiles;"

echo ""
echo "🎨 Artist Types:"
mysql -u root -p icastar_db -e "SELECT at.display_name, COUNT(*) as count FROM artist_profiles ap JOIN artist_types at ON ap.artist_type_id = at.id GROUP BY at.display_name;"

echo ""
echo "📝 Job Applications:"
mysql -u root -p icastar_db -e "SELECT COUNT(*) as total_applications FROM job_applications;"

echo ""
echo "💳 Subscriptions:"
mysql -u root -p icastar_db -e "SELECT sp.name, COUNT(*) as count FROM subscriptions s JOIN subscription_plans sp ON s.subscription_plan_id = sp.id GROUP BY sp.name;"

echo ""
echo "📊 Usage Tracking:"
mysql -u root -p icastar_db -e "SELECT feature_type, COUNT(*) as count FROM usage_tracking GROUP BY feature_type;"

echo ""
echo "🎉 Recruiter Dashboard System Setup Complete!"
echo ""
echo "📝 Available Features:"
echo "   • Post jobs (requires subscription)"
echo "   • View artists who applied"
echo "   • Browse artist profiles"
echo "   • Get AI-powered suggestions"
echo "   • Track hires and past jobs"
echo "   • Analytics dashboard"
echo ""
echo "🔧 Dashboard Features:"
echo "   • Job posting with subscription validation"
echo "   • Application management"
echo "   • Artist browsing and search"
echo "   • AI-powered artist suggestions"
echo "   • Hiring analytics and tracking"
echo "   • Performance rating and feedback"
echo ""
echo "🚀 Next Steps:"
echo "1. Build the project: mvn clean compile"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Test the recruiter dashboard API endpoints"
echo "4. Verify subscription integration"
echo ""
echo "📚 API Endpoints Available:"
echo "   • GET /api/recruiter/dashboard - Get dashboard overview"
echo "   • POST /api/recruiter/dashboard/jobs - Post new job"
echo "   • GET /api/recruiter/dashboard/jobs - Get my jobs"
echo "   • GET /api/recruiter/dashboard/jobs/{jobId}/applications - Get job applications"
echo "   • GET /api/recruiter/dashboard/artists - Browse artists"
echo "   • GET /api/recruiter/dashboard/suggestions - Get artist suggestions"
echo "   • GET /api/recruiter/dashboard/hires - Get hires and past jobs"
echo "   • GET /api/recruiter/dashboard/statistics - Get recruiter statistics"
echo "   • GET /api/recruiter/dashboard/subscription - Get subscription status"
echo ""
echo "💡 Key Features:"
echo "   • Subscription-based job posting"
echo "   • AI-powered artist suggestions"
echo "   • Advanced artist browsing"
echo "   • Application management"
echo "   • Hiring analytics"
echo "   • Performance tracking"
echo ""
echo "📖 Documentation:"
echo "   • RECRUITER-DASHBOARD-SYSTEM.md - Complete system guide"
echo "   • JOB-MANAGEMENT-SYSTEM.md - Admin job management guide"
echo "   • SUBSCRIPTION-SYSTEM-DESIGN.md - Subscription system guide"
echo "   • COMPLETE-SYSTEM-GUIDE.md - Overall platform guide"
