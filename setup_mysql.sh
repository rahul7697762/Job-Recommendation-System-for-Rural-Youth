#!/bin/bash

echo "========================================"
echo "   MySQL Database Setup for Rural Youth"
echo "========================================"
echo

echo "Setting up MySQL database..."
echo

# Check if MySQL is installed
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL is not installed or not in PATH"
    echo "Please install MySQL and add it to your system PATH"
    echo "Download from: https://dev.mysql.com/downloads/mysql/"
    echo "Or install via package manager:"
    echo "  Ubuntu/Debian: sudo apt-get install mysql-server"
    echo "  CentOS/RHEL: sudo yum install mysql-server"
    echo "  macOS: brew install mysql"
    exit 1
fi

echo "MySQL found. Creating database and tables..."
echo

# Create database and run migration
mysql -u root -p < src/main/resources/db/migration/V1__Create_Tables_MySQL.sql

if [ $? -eq 0 ]; then
    echo
    echo "✅ MySQL database setup completed successfully!"
    echo
    echo "Next steps:"
    echo "1. Copy database_mysql.properties to database.properties"
    echo "2. Update the password in database.properties"
    echo "3. Run the application with: ./run_gui.sh"
else
    echo
    echo "❌ MySQL database setup failed!"
    echo "Please check your MySQL installation and credentials."
fi

echo
read -p "Press Enter to continue..." 