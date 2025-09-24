#!/bin/bash

# iCastar Platform - Dancer Data Setup Script
# This script sets up the database with Dancer artist type and sample data

echo "🎭 iCastar Platform - Dancer Data Setup"
echo "========================================"

# Check if MySQL is running
if ! pgrep -x "mysqld" > /dev/null; then
    echo "❌ MySQL is not running. Please start MySQL first."
    exit 1
fi

# Get database credentials
read -p "Enter MySQL username (default: root): " DB_USER
DB_USER=${DB_USER:-root}

read -s -p "Enter MySQL password: " DB_PASS
echo ""

# Set database name
DB_NAME="icastar_db"

echo "📊 Setting up database: $DB_NAME"

# Check if database exists
if mysql -u"$DB_USER" -p"$DB_PASS" -e "USE $DB_NAME;" 2>/dev/null; then
    echo "✅ Database $DB_NAME exists"
else
    echo "❌ Database $DB_NAME does not exist. Please run database-schema.sql first."
    exit 1
fi

# Run the dancer data script
echo "🎨 Adding Dancer artist type and sample data..."
if mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" < dancer-default-data.sql; then
    echo "✅ Dancer data added successfully!"
else
    echo "❌ Failed to add dancer data. Please check the SQL script."
    exit 1
fi

# Verify the setup
echo "🔍 Verifying setup..."

# Check artist types
ARTIST_TYPE_COUNT=$(mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -s -N -e "SELECT COUNT(*) FROM artist_types WHERE name='DANCER';")
if [ "$ARTIST_TYPE_COUNT" -eq 1 ]; then
    echo "✅ Dancer artist type created"
else
    echo "❌ Dancer artist type not found"
fi

# Check fields
FIELD_COUNT=$(mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -s -N -e "SELECT COUNT(*) FROM artist_type_fields atf JOIN artist_types at ON atf.artist_type_id = at.id WHERE at.name='DANCER';")
if [ "$FIELD_COUNT" -eq 12 ]; then
    echo "✅ $FIELD_COUNT Dancer fields created"
else
    echo "⚠️  Expected 12 fields, found $FIELD_COUNT"
fi

# Check sample profiles
PROFILE_COUNT=$(mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -s -N -e "SELECT COUNT(*) FROM artist_profiles ap JOIN artist_types at ON ap.artist_type_id = at.id WHERE at.name='DANCER';")
if [ "$PROFILE_COUNT" -eq 2 ]; then
    echo "✅ $PROFILE_COUNT sample dancer profiles created"
else
    echo "⚠️  Expected 2 profiles, found $PROFILE_COUNT"
fi

# Check users
USER_COUNT=$(mysql -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" -s -N -e "SELECT COUNT(*) FROM users WHERE email LIKE '%dancer%';")
if [ "$USER_COUNT" -eq 2 ]; then
    echo "✅ $USER_COUNT test users created"
else
    echo "⚠️  Expected 2 users, found $USER_COUNT"
fi

echo ""
echo "🎉 Setup completed!"
echo ""
echo "📋 Sample Login Credentials:"
echo "   Email: test.dancer@example.com"
echo "   Password: password123"
echo "   Role: ARTIST (Classical Dancer)"
echo ""
echo "   Email: hiphop.dancer@example.com"
echo "   Password: password123"
echo "   Role: ARTIST (Hip-Hop Dancer)"
echo ""
echo "🚀 Next Steps:"
echo "   1. Start the application: mvn spring-boot:run"
echo "   2. Test API endpoints using the provided curl commands"
echo "   3. Check the DATABASE-SETUP-GUIDE.md for detailed instructions"
echo ""
echo "📚 Documentation:"
echo "   - ARTIST-ONBOARDING-GUIDE.md - How to add artists"
echo "   - ADDING-NEW-ARTIST-TYPES-GUIDE.md - How to add new artist types"
echo "   - DATABASE-SETUP-GUIDE.md - Complete database setup guide"
echo ""
echo "Happy Dancing! 💃✨"

