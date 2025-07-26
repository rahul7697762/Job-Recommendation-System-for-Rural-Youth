@echo off
echo ========================================
echo   Switching to Supabase Configuration
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
java -cp target/classes com.ruralyouth.database.DatabaseSwitcher supabase

if %errorlevel% equ 0 (
    echo.
    echo ✅ Successfully switched to Supabase configuration!
    echo.
    echo Next steps:
    echo 1. Edit database.properties and update your Supabase credentials
    echo 2. Run setup_supabase.bat for setup instructions
    echo 3. Run run_gui.bat to start the application
) else (
    echo.
    echo ❌ Failed to switch to Supabase configuration!
)

echo.
pause 