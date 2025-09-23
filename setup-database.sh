#!/bin/bash

# iCastar Platform Database Setup Script

echo "🗄️ Setting up iCastar Platform Database..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Database configuration
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-3306}
DB_USERNAME=${DB_USERNAME:-root}
DB_PASSWORD=${DB_PASSWORD:-password}
DEV_DB_NAME="icastar_dev"
PROD_DB_NAME="icastar_db"

echo -e "${BLUE}Database Configuration:${NC}"
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Username: $DB_USERNAME"
echo "Development DB: $DEV_DB_NAME"
echo "Production DB: $PROD_DB_NAME"
echo ""

# Function to check if MySQL is running
check_mysql() {
    echo -e "${YELLOW}Checking MySQL connection...${NC}"
    if mysqladmin ping -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USERNAME" -p"$DB_PASSWORD" --silent; then
        echo -e "${GREEN}✅ MySQL is running and accessible${NC}"
        return 0
    else
        echo -e "${RED}❌ Cannot connect to MySQL${NC}"
        echo "Please ensure MySQL is running and credentials are correct."
        return 1
    fi
}

# Function to create database
create_database() {
    local db_name=$1
    echo -e "${YELLOW}Creating database: $db_name${NC}"
    
    mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USERNAME" -p"$DB_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $db_name CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Database '$db_name' created successfully${NC}"
    else
        echo -e "${RED}❌ Failed to create database '$db_name'${NC}"
        return 1
    fi
}

# Function to run SQL schema
run_schema() {
    local db_name=$1
    echo -e "${YELLOW}Running schema for database: $db_name${NC}"
    
    if [ -f "database-schema.sql" ]; then
        mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USERNAME" -p"$DB_PASSWORD" "$db_name" < database-schema.sql 2>/dev/null
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ Schema applied successfully to '$db_name'${NC}"
        else
            echo -e "${RED}❌ Failed to apply schema to '$db_name'${NC}"
            return 1
        fi
    else
        echo -e "${RED}❌ database-schema.sql file not found${NC}"
        return 1
    fi
}

# Function to verify database setup
verify_database() {
    local db_name=$1
    echo -e "${YELLOW}Verifying database setup: $db_name${NC}"
    
    local table_count=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USERNAME" -p"$DB_PASSWORD" "$db_name" -e "SHOW TABLES;" 2>/dev/null | wc -l)
    
    if [ "$table_count" -gt 1 ]; then
        echo -e "${GREEN}✅ Database '$db_name' has $((table_count-1)) tables${NC}"
        return 0
    else
        echo -e "${RED}❌ Database '$db_name' appears to be empty${NC}"
        return 1
    fi
}

# Main setup process
main() {
    echo -e "${BLUE}Starting database setup...${NC}"
    
    # Check MySQL connection
    if ! check_mysql; then
        exit 1
    fi
    
    # Create development database
    if ! create_database "$DEV_DB_NAME"; then
        exit 1
    fi
    
    # Create production database
    if ! create_database "$PROD_DB_NAME"; then
        exit 1
    fi
    
    # Apply schema to development database
    if ! run_schema "$DEV_DB_NAME"; then
        exit 1
    fi
    
    # Apply schema to production database
    if ! run_schema "$PROD_DB_NAME"; then
        exit 1
    fi
    
    # Verify both databases
    echo ""
    echo -e "${BLUE}Verifying database setup...${NC}"
    verify_database "$DEV_DB_NAME"
    verify_database "$PROD_DB_NAME"
    
    echo ""
    echo -e "${GREEN}🎉 Database setup completed successfully!${NC}"
    echo ""
    echo -e "${BLUE}Next steps:${NC}"
    echo "1. Start the application: ./start-dev.sh"
    echo "2. Access the API: http://localhost:8080/api"
    echo "3. Check health: http://localhost:8080/api/actuator/health"
    echo ""
    echo -e "${BLUE}Database URLs:${NC}"
    echo "Development: jdbc:mysql://$DB_HOST:$DB_PORT/$DEV_DB_NAME"
    echo "Production: jdbc:mysql://$DB_HOST:$DB_PORT/$PROD_DB_NAME"
}

# Check if help is requested
if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "iCastar Platform Database Setup Script"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Environment Variables:"
    echo "  DB_HOST      MySQL host (default: localhost)"
    echo "  DB_PORT      MySQL port (default: 3306)"
    echo "  DB_USERNAME  MySQL username (default: root)"
    echo "  DB_PASSWORD  MySQL password (default: password)"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Use default settings"
    echo "  DB_PASSWORD=mypass $0                # Use custom password"
    echo "  DB_HOST=192.168.1.100 $0             # Use remote MySQL"
    echo ""
    exit 0
fi

# Run main function
main
