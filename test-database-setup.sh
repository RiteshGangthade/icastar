#!/bin/bash

# Test Database Setup for iCastar Platform

echo "🧪 Testing Database Setup..."

# Test 1: Check if database exists
echo "📊 Checking database connection..."
mysql -u root -p -e "USE icastar_db;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Database connection successful!"
else
    echo "❌ Database connection failed!"
    exit 1
fi

# Test 2: Check if tables exist
echo "📋 Checking tables..."
mysql -u root -p icastar_db -e "SHOW TABLES;" 2>/dev/null | grep -q "artist_type_fields"
if [ $? -eq 0 ]; then
    echo "✅ Required tables exist!"
else
    echo "❌ Required tables missing!"
    exit 1
fi

# Test 3: Check artist types
echo "🎭 Checking artist types..."
mysql -u root -p icastar_db -e "SELECT COUNT(*) as artist_type_count FROM artist_types;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Artist types table accessible!"
else
    echo "❌ Artist types table not accessible!"
    exit 1
fi

# Test 4: Check artist type fields
echo "🔧 Checking artist type fields..."
mysql -u root -p icastar_db -e "SELECT COUNT(*) as field_count FROM artist_type_fields;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Artist type fields table accessible!"
else
    echo "❌ Artist type fields table not accessible!"
    exit 1
fi

# Test 5: Show summary
echo "📊 Database Summary:"
mysql -u root -p icastar_db -e "
SELECT 
    at.name as artist_type,
    COUNT(atf.id) as field_count
FROM artist_types at
LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id
GROUP BY at.id, at.name
ORDER BY at.name;
" 2>/dev/null

echo "🎉 Database setup test completed successfully!"
echo ""
echo "📝 Next steps:"
echo "1. Fix Java compilation issues to run the application"
echo "2. Test the API endpoints once the application is running"
echo "3. Create test artist profiles with dynamic fields"
