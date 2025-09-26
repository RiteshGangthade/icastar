#!/bin/bash

# iCastar Platform - Manual Artist Types Data Setup
# This script adds default fields for all artist types: Singer, Director, Writer, Dancer

echo "🎭 Setting up all artist types data for iCastar Platform..."

# Check if MySQL is running
if ! pgrep -x "mysqld" > /dev/null; then
    echo "❌ MySQL is not running. Please start MySQL first."
    exit 1
fi

# Check if database exists
echo "📊 Checking database connection..."
mysql -u root -p -e "USE icastar_db;" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "❌ Database 'icastar_db' does not exist. Please create it first."
    echo "Run: mysql -u root -p -e 'CREATE DATABASE icastar_db;'"
    exit 1
fi

echo "✅ Database connection successful!"

# Execute the SQL script
echo "🚀 Adding all artist types data..."
mysql -u root -p icastar_db < artist-types-default-data.sql

if [ $? -eq 0 ]; then
    echo "✅ All artist types data added successfully!"
    echo ""
    echo "📊 Summary of added fields:"
    echo "🎤 Singer: 10 fields (Genre, Vocal Range, Languages, etc.)"
    echo "🎬 Director: 12 fields (Genre, Full Name, Training, etc.)"
    echo "✍️ Writer: 8 fields (Genre, Education, Demo, etc.)"
    echo "💃 Dancer: 10 fields (Bio, Dance Style, Training, etc.)"
    echo ""
    echo "🎉 Total: 40+ dynamic fields added across all artist types!"
    echo ""
    echo "🔍 You can verify the data by running:"
    echo "mysql -u root -p -e \"USE icastar_db; SELECT at.name, COUNT(atf.id) as field_count FROM artist_types at LEFT JOIN artist_type_fields atf ON at.id = atf.artist_type_id GROUP BY at.id, at.name;\""
else
    echo "❌ Error adding artist types data. Please check the SQL file and try again."
    exit 1
fi
