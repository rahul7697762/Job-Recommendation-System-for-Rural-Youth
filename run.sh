#!/bin/bash

echo "🌾 Job Recommendation System for Rural Youth 🌾"
echo "================================================"

echo ""
echo "Compiling the project..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo ""
echo "✅ Compilation successful!"
echo ""
echo "Running the application..."
echo ""

mvn exec:java -Dexec.mainClass="com.ruralyouth.JobRecommendationApp"

echo ""
echo "Application finished." 