@echo off
echo 🌾 Job Recommendation System for Rural Youth 🌾
echo ================================================

echo.
echo Compiling the project...
mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo.
echo ✅ Compilation successful!
echo.
echo Running the application...
echo.

mvn exec:java -Dexec.mainClass="com.ruralyouth.JobRecommendationApp"

echo.
echo Application finished.
pause 