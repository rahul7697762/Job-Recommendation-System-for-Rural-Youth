@echo off
echo 🌾 Job Recommendation System for Rural Youth 🌾
echo ================================================

echo.
echo Creating directories...
if not exist "bin" mkdir bin

echo.
echo Compiling Java files...
javac -d bin -cp "src/main/java" ^
  src/main/java/com/ruralyouth/JobRecommendationApp.java ^
  src/main/java/com/ruralyouth/model/User.java ^
  src/main/java/com/ruralyouth/model/Job.java ^
  src/main/java/com/ruralyouth/dsa/Trie.java ^
  src/main/java/com/ruralyouth/dsa/LocationGraph.java ^
  src/main/java/com/ruralyouth/dsa/JobPriorityQueue.java ^
  src/main/java/com/ruralyouth/engine/JobRecommendationEngine.java ^
  src/main/java/com/ruralyouth/data/SampleDataLoader.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed!
    echo.
    echo Please make sure you have Java installed and in your PATH.
    pause
    exit /b 1
)

echo.
echo ✅ Compilation successful!
echo.
echo Running the application...
echo.

java -cp "bin" com.ruralyouth.JobRecommendationApp

echo.
echo Application finished.
pause 