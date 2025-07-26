#!/bin/bash

echo "========================================"
echo "   Switching to Supabase Configuration"
echo "========================================"
echo

echo "Compiling and running database switcher..."
echo

# Compile the project
mvn compile -q

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

# Run the database switcher
java -cp target/classes com.ruralyouth.database.DatabaseSwitcher supabase

if [ $? -eq 0 ]; then
    echo
    echo "✅ Successfully switched to Supabase configuration!"
    echo
    echo "Next steps:"
    echo "1. Edit database.properties and update your Supabase credentials"
    echo "2. Run setup_supabase.sh for setup instructions"
    echo "3. Run run_gui.sh to start the application"
else
    echo
    echo "❌ Failed to switch to Supabase configuration!"
fi

echo
read -p "Press Enter to continue..." 