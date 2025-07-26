@echo off
echo ========================================
echo   Switching to MySQL Configuration
echo ========================================
echo.

echo Compiling and running database switcher...
echo.

REM Compile the project
call mvn compile -q

if %errorlevel% neq 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

REM Run the database switcher
java -cp target/classes com.ruralyouth.database.DatabaseSwitcher mysql

if %errorlevel% equ 0 (
    echo.
    echo ✅ Successfully switched to MySQL configuration!
    echo.
    echo Next steps:
    echo 1. Edit database.properties and update your MySQL credentials
    echo 2. Run setup_mysql.bat to create the database and tables
    echo 3. Run run_gui.bat to start the application
) else (
    echo.
    echo ❌ Failed to switch to MySQL configuration!
)

echo.
pause 