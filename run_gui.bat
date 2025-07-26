@echo off
echo 🌾 Job Recommendation System GUI 🌾
echo ====================================

echo.
echo Creating directories...
if not exist "bin" mkdir bin

echo.
echo Compiling Java files...
javac -d bin -cp "src/main/java" ^
  src/main/java/com/ruralyouth/JobRecommendationApp.java ^
  src/main/java/com/ruralyouth/gui/JobRecommendationGUI.java ^
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
echo Launching GUI application...
echo.

java -cp "bin" com.ruralyouth.gui.JobRecommendationGUI

echo.
echo GUI application finished.
pause 