#!/bin/bash

# iCastar Platform Development Startup Script

echo "🎭 Starting iCastar Platform Development Environment..."

# Check if Java 17+ is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi

echo "✅ Maven version: $(mvn -version | head -n 1)"

# Check if MySQL is running (optional)
if command -v mysql &> /dev/null; then
    if mysqladmin ping -h localhost -u root --silent; then
        echo "✅ MySQL is running"
    else
        echo "⚠️  MySQL is not running. Please start MySQL server."
        echo "   You can also use H2 in-memory database for development by setting:"
        echo "   spring.profiles.active=dev"
    fi
else
    echo "⚠️  MySQL not found. Using H2 in-memory database for development."
fi

# Create logs directory if it doesn't exist
mkdir -p logs

# Set development profile
export SPRING_PROFILES_ACTIVE=dev

echo ""
echo "🚀 Starting iCastar Platform..."
echo "📊 API Documentation: http://localhost:8080/api"
echo "🔧 Actuator Health: http://localhost:8080/actuator/health"
echo "📝 Logs: logs/icastar.log"
echo ""

# Start the application
mvn spring-boot:run
