@echo off
echo ========================================
echo   MySQL Database Setup for Rural Youth
echo ========================================
echo.

echo Setting up MySQL database...
echo.

REM Check if MySQL is installed
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL is not installed or not in PATH
    echo Please install MySQL and add it to your system PATH
    echo Download from: https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
)

echo MySQL found. Creating database and tables...
echo.

REM Create database and run migration
mysql -u root -p < src/main/resources/db/migration/V1__Create_Tables_MySQL.sql

if %errorlevel% equ 0 (
    echo.
    echo ✅ MySQL database setup completed successfully!
    echo.
    echo Next steps:
    echo 1. Copy database_mysql.properties to database.properties
    echo 2. Update the password in database.properties
    echo 3. Run the application with: .\run_gui.bat
) else (
    echo.
    echo ❌ MySQL database setup failed!
    echo Please check your MySQL installation and credentials.
)

echo.
pause 