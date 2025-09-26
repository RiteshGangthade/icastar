#!/bin/bash

# Setup Recruiter System for iCastar Platform
# This script applies the recruiter categories and fields to the database

echo "🎯 Setting up iCastar Recruiter System..."

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

echo "📊 Applying recruiter categories schema..."
mysql -u root -p icastar_db < recruiter-categories-schema.sql

if [ $? -eq 0 ]; then
    echo "✅ Recruiter categories schema applied successfully"
else
    echo "❌ Failed to apply recruiter categories schema"
    exit 1
fi

echo "📋 Applying recruiter fields data..."
mysql -u root -p icastar_db < recruiter-fields-data.sql

if [ $? -eq 0 ]; then
    echo "✅ Recruiter fields data applied successfully"
else
    echo "❌ Failed to apply recruiter fields data"
    exit 1
fi

echo "🔍 Verifying setup..."

# Check recruiter categories
echo "📊 Recruiter Categories:"
mysql -u root -p icastar_db -e "SELECT id, name, display_name, description FROM recruiter_categories ORDER BY sort_order;"

echo ""
echo "📋 Fields per Category:"
mysql -u root -p icastar_db -e "SELECT rc.name as category, COUNT(rcf.id) as field_count FROM recruiter_categories rc LEFT JOIN recruiter_category_fields rcf ON rc.id = rcf.recruiter_category_id GROUP BY rc.id, rc.name ORDER BY rc.sort_order;"

echo ""
echo "🎯 Production House Fields:"
mysql -u root -p icastar_db -e "SELECT field_name, display_name, field_type, is_required FROM recruiter_category_fields WHERE recruiter_category_id = 1 ORDER BY sort_order;"

echo ""
echo "🎭 Casting Director Fields:"
mysql -u root -p icastar_db -e "SELECT field_name, display_name, field_type, is_required FROM recruiter_category_fields WHERE recruiter_category_id = 2 ORDER BY sort_order;"

echo ""
echo "👤 Individual Recruiter Fields:"
mysql -u root -p icastar_db -e "SELECT field_name, display_name, field_type, is_required FROM recruiter_category_fields WHERE recruiter_category_id = 3 ORDER BY sort_order;"

echo ""
echo "🎉 Recruiter System Setup Complete!"
echo ""
echo "📝 Next Steps:"
echo "1. Build the project: mvn clean compile"
echo "2. Run the application: mvn spring-boot:run"
echo "3. Test the API endpoints with the examples in RECRUITER-SYSTEM-GUIDE.md"
echo ""
echo "🔗 Available Categories:"
echo "   • Production House (ID: 1) - Film/TV production companies"
echo "   • Casting Director (ID: 2) - Professional casting directors"
echo "   • Individual Recruiter (ID: 3) - Entrepreneurs, event managers, ad agencies"
