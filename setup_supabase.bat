@echo off
echo ========================================
echo   Supabase Database Setup for Rural Youth
echo ========================================
echo.

echo Setting up Supabase database...
echo.

echo Please follow these steps to setup Supabase:
echo.
echo 1. Go to https://supabase.com and create a new project
echo 2. Get your database connection details from the project settings
echo 3. Copy database_supabase.properties to database.properties
echo 4. Update the connection details in database.properties:
echo    - database.host (your project's database host)
echo    - database.password (your database password)
echo.
echo 5. Run the PostgreSQL migration script in your Supabase SQL editor:
echo    - Open your Supabase project dashboard
echo    - Go to SQL Editor
echo    - Copy and paste the contents of: src/main/resources/db/migration/V1__Create_Tables.sql
echo    - Execute the script
echo.
echo 6. Test the connection by running: .\run_gui.bat
echo.

echo Supabase setup instructions completed!
echo Please follow the steps above to configure your database.
echo.
pause 