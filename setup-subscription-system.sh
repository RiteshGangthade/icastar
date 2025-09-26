#!/bin/bash

# Setup Subscription System for iCastar Platform
# This script applies the subscription system schema and data

echo "💳 Setting up iCastar Subscription System..."

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

echo "📊 Applying subscription system schema..."
mysql -u root -p icastar_db < subscription-system-design.sql

if [ $? -eq 0 ]; then
    echo "✅ Subscription system schema applied successfully"
else
    echo "❌ Failed to apply subscription system schema"
    exit 1
fi

echo "📋 Applying subscription plans data..."
mysql -u root -p icastar_db < subscription-plans-data.sql

if [ $? -eq 0 ]; then
    echo "✅ Subscription plans data applied successfully"
else
    echo "❌ Failed to apply subscription plans data"
    exit 1
fi

echo "🔍 Verifying setup..."

# Check subscription plans
echo "📊 Subscription Plans:"
mysql -u root -p icastar_db -e "SELECT id, name, plan_type, user_role, price, billing_cycle FROM subscription_plans ORDER BY sort_order;"

echo ""
echo "🎭 Artist Plans:"
mysql -u root -p icastar_db -e "SELECT name, price, max_auditions, max_applications, featured_profile, advanced_analytics FROM subscription_plans WHERE user_role = 'ARTIST' ORDER BY sort_order;"

echo ""
echo "🏢 Recruiter Plans:"
mysql -u root -p icastar_db -e "SELECT name, price, max_job_posts, max_messages, job_boost_credits, advanced_search FROM subscription_plans WHERE user_role = 'RECRUITER' ORDER BY sort_order;"

echo ""
echo "🔄 Unified Plans:"
mysql -u root -p icastar_db -e "SELECT name, price, max_auditions, max_job_posts, max_messages FROM subscription_plans WHERE user_role = 'BOTH' ORDER BY sort_order;"

echo ""
echo "📋 Plan Features:"
mysql -u root -p icastar_db -e "SELECT sp.name, pf.feature_name, pf.feature_type, pf.feature_value FROM subscription_plans sp JOIN plan_features pf ON sp.id = pf.subscription_plan_id ORDER BY sp.sort_order, pf.id;"

echo ""
echo "🎉 Subscription System Setup Complete!"
echo ""
echo "📝 Available Plans:"
echo ""
echo "🎭 ARTIST PLANS:"
echo "   • Artist Free (₹0/month) - 5 auditions, 10 applications"
echo "   • Artist Basic (₹299/month) - 25 auditions, 50 applications, verification"
echo "   • Artist Premium (₹599/month) - 100 auditions, 200 applications, featured profile"
echo "   • Artist Professional (₹999/month) - 500 auditions, 1000 applications"
echo "   • Artist Enterprise (₹1999/month) - Unlimited access"
echo ""
echo "🏢 RECRUITER PLANS:"
echo "   • Recruiter Free (₹0/month) - 2 job posts, 10 messages"
echo "   • Recruiter Basic (₹499/month) - 10 job posts, 50 messages, 2 boosts"
echo "   • Recruiter Premium (₹999/month) - 50 job posts, 200 messages, 5 boosts"
echo "   • Recruiter Professional (₹1999/month) - 200 job posts, 1000 messages, 10 boosts"
echo "   • Recruiter Enterprise (₹4999/month) - Unlimited access"
echo ""
echo "🔄 UNIFIED PLANS:"
echo "   • Unified Basic (₹699/month) - 15 auditions + 5 job posts"
echo "   • Unified Premium (₹1299/month) - 50 auditions + 25 job posts"
echo "   • Unified Professional (₹2499/month) - 250 auditions + 100 job posts"
echo "   • Unified Enterprise (₹6999/month) - Unlimited access"
echo ""
echo "🚀 Next Steps:"
echo "1. Build the project: mvn clean compile"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Test the subscription API endpoints"
echo "4. Implement payment integration"
echo ""
echo "📚 Documentation:"
echo "   • SUBSCRIPTION-SYSTEM-DESIGN.md - Complete system guide"
echo "   • COMPLETE-SYSTEM-GUIDE.md - Overall platform guide"
