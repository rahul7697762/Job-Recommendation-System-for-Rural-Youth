package com.ruralyouth;

import com.ruralyouth.engine.JobRecommendationEngine;
import com.ruralyouth.data.SampleDataLoader;
import com.ruralyouth.model.Job;
import com.ruralyouth.model.User;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class for the Job Recommendation System
 * Provides a command-line interface for testing all features
 */
public class JobRecommendationApp {
    private static JobRecommendationEngine engine;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("🌾 Job Recommendation System for Rural Youth 🌾");
        System.out.println("================================================");
        
        // Initialize the system
        engine = new JobRecommendationEngine();
        scanner = new Scanner(System.in);
        
        // Load sample data
        System.out.println("Loading sample data...");
        SampleDataLoader.loadSampleData(engine);
        
        // Display system stats
        displaySystemStats();
        
        // Start the main menu
        showMainMenu();
        
        scanner.close();
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n📋 MAIN MENU");
            System.out.println("1. Get Job Recommendations");
            System.out.println("2. Search Jobs by Title");
            System.out.println("3. Search Jobs by Skill");
            System.out.println("4. Find Jobs Near Location");
            System.out.println("5. Get Personalized Recommendations");
            System.out.println("6. Suggest Career Paths");
            System.out.println("7. View Sample Data");
            System.out.println("8. System Statistics");
            System.out.println("9. Exit");
            System.out.print("Enter your choice (1-9): ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    getJobRecommendations();
                    break;
                case 2:
                    searchJobsByTitle();
                    break;
                case 3:
                    searchJobsBySkill();
                    break;
                case 4:
                    findJobsNearLocation();
                    break;
                case 5:
                    getPersonalizedRecommendations();
                    break;
                case 6:
                    suggestCareerPaths();
                    break;
                case 7:
                    viewSampleData();
                    break;
                case 8:
                    displaySystemStats();
                    break;
                case 9:
                    System.out.println("Thank you for using the Job Recommendation System!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void getJobRecommendations() {
        System.out.println("\n🎯 GET JOB RECOMMENDATIONS");
        System.out.println("Available users:");
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (int i = 0; i < userIds.size(); i++) {
            System.out.println((i + 1) + ". " + userIds.get(i));
        }
        
        System.out.print("Select user (1-" + userIds.size() + "): ");
        int userChoice = getIntInput();
        
        if (userChoice < 1 || userChoice > userIds.size()) {
            System.out.println("Invalid user selection.");
            return;
        }
        
        String userId = userIds.get(userChoice - 1);
        System.out.print("Number of recommendations (1-10): ");
        int limit = getIntInput();
        limit = Math.min(10, Math.max(1, limit));
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations(userId, limit);
        
        System.out.println("\n📊 RECOMMENDATIONS for User " + userId + ":");
        System.out.println("=".repeat(80));
        
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found.");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                JobRecommendationEngine.JobRecommendation rec = recommendations.get(i);
                System.out.printf("%d. %s\n", i + 1, rec.job.getTitle());
                System.out.printf("   Company: %s\n", rec.job.getCompany());
                System.out.printf("   Location: %s\n", rec.job.getLocation());
                System.out.printf("   Salary: ₹%.0f\n", rec.job.getSalary());
                System.out.printf("   Score: %.2f/100\n", rec.score);
                System.out.printf("   Distance: %.1f km\n", rec.distance);
                System.out.printf("   Skills: %s\n", String.join(", ", rec.job.getRequiredSkills()));
                System.out.println();
            }
        }
    }

    private static void searchJobsByTitle() {
        System.out.println("\n🔍 SEARCH JOBS BY TITLE");
        System.out.print("Enter job title prefix: ");
        String prefix = scanner.nextLine().trim();
        
        List<Job> jobs = engine.searchJobsByTitle(prefix);
        
        System.out.println("\n📋 SEARCH RESULTS:");
        System.out.println("=".repeat(60));
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs found matching '" + prefix + "'");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                System.out.printf("%d. %s - %s (%s)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getLocation());
            }
        }
    }

    private static void searchJobsBySkill() {
        System.out.println("\n🔍 SEARCH JOBS BY SKILL");
        System.out.println("Available skills:");
        List<String> skills = SampleDataLoader.getSampleSkills();
        for (int i = 0; i < skills.size(); i++) {
            System.out.println((i + 1) + ". " + skills.get(i));
        }
        
        System.out.print("Select skill (1-" + skills.size() + "): ");
        int skillChoice = getIntInput();
        
        if (skillChoice < 1 || skillChoice > skills.size()) {
            System.out.println("Invalid skill selection.");
            return;
        }
        
        String skill = skills.get(skillChoice - 1);
        List<Job> jobs = engine.searchJobsBySkill(skill);
        
        System.out.println("\n📋 JOBS REQUIRING '" + skill.toUpperCase() + "':");
        System.out.println("=".repeat(60));
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs found requiring '" + skill + "'");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                System.out.printf("%d. %s - %s (₹%.0f)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getSalary());
            }
        }
    }

    private static void findJobsNearLocation() {
        System.out.println("\n📍 FIND JOBS NEAR LOCATION");
        System.out.println("Available locations:");
        System.out.println("1. Village A");
        System.out.println("2. Town B");
        System.out.println("3. City C");
        System.out.println("4. Village D");
        System.out.println("5. Town E");
        
        System.out.print("Select location (1-5): ");
        int locationChoice = getIntInput();
        
        String[] locations = {"Village A", "Town B", "City C", "Village D", "Town E"};
        if (locationChoice < 1 || locationChoice > 5) {
            System.out.println("Invalid location selection.");
            return;
        }
        
        String location = locations[locationChoice - 1];
        System.out.print("Maximum distance (km): ");
        double maxDistance = getDoubleInput();
        
        List<Job> jobs = engine.findJobsNearLocation(location, maxDistance);
        
        System.out.println("\n📋 JOBS NEAR '" + location + "' (within " + maxDistance + " km):");
        System.out.println("=".repeat(70));
        
        if (jobs.isEmpty()) {
            System.out.println("No jobs found near '" + location + "'");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                System.out.printf("%d. %s - %s (₹%.0f)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getSalary());
            }
        }
    }

    private static void getPersonalizedRecommendations() {
        System.out.println("\n🎯 PERSONALIZED RECOMMENDATIONS");
        System.out.println("Available users:");
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (int i = 0; i < userIds.size(); i++) {
            System.out.println((i + 1) + ". " + userIds.get(i));
        }
        
        System.out.print("Select user (1-" + userIds.size() + "): ");
        int userChoice = getIntInput();
        
        if (userChoice < 1 || userChoice > userIds.size()) {
            System.out.println("Invalid user selection.");
            return;
        }
        
        String userId = userIds.get(userChoice - 1);
        
        System.out.print("Minimum salary (₹): ");
        double minSalary = getDoubleInput();
        
        System.out.print("Maximum distance (km): ");
        double maxDistance = getDoubleInput();
        
        System.out.print("Number of recommendations (1-10): ");
        int limit = getIntInput();
        limit = Math.min(10, Math.max(1, limit));
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getPersonalizedRecommendations(userId, minSalary, maxDistance, null, limit);
        
        System.out.println("\n📊 PERSONALIZED RECOMMENDATIONS:");
        System.out.println("=".repeat(80));
        
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found with the given criteria.");
        } else {
            for (int i = 0; i < recommendations.size(); i++) {
                JobRecommendationEngine.JobRecommendation rec = recommendations.get(i);
                System.out.printf("%d. %s (Score: %.2f)\n", i + 1, rec.job.getTitle(), rec.score);
                System.out.printf("   Company: %s, Salary: ₹%.0f, Distance: %.1f km\n", 
                    rec.job.getCompany(), rec.job.getSalary(), rec.distance);
                System.out.println();
            }
        }
    }

    private static void suggestCareerPaths() {
        System.out.println("\n🛤️ CAREER PATH SUGGESTIONS");
        System.out.println("Available users:");
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (int i = 0; i < userIds.size(); i++) {
            System.out.println((i + 1) + ". " + userIds.get(i));
        }
        
        System.out.print("Select user (1-" + userIds.size() + "): ");
        int userChoice = getIntInput();
        
        if (userChoice < 1 || userChoice > userIds.size()) {
            System.out.println("Invalid user selection.");
            return;
        }
        
        String userId = userIds.get(userChoice - 1);
        
        System.out.println("Available target jobs:");
        List<String> jobTitles = SampleDataLoader.getSampleJobTitles();
        for (int i = 0; i < jobTitles.size(); i++) {
            System.out.println((i + 1) + ". " + jobTitles.get(i));
        }
        
        System.out.print("Select target job (1-" + jobTitles.size() + "): ");
        int jobChoice = getIntInput();
        
        if (jobChoice < 1 || jobChoice > jobTitles.size()) {
            System.out.println("Invalid job selection.");
            return;
        }
        
        String targetJob = jobTitles.get(jobChoice - 1);
        
        List<JobRecommendationEngine.CareerPath> paths = 
            engine.suggestCareerPaths(userId, targetJob);
        
        System.out.println("\n🛤️ CAREER PATH TO '" + targetJob + "':");
        System.out.println("=".repeat(60));
        
        if (paths.isEmpty()) {
            System.out.println("No career path found for '" + targetJob + "'");
        } else {
            for (int i = 0; i < paths.size(); i++) {
                JobRecommendationEngine.CareerPath path = paths.get(i);
                System.out.printf("%d. %s\n", i + 1, path.description);
                System.out.printf("   Training steps: %d\n", path.trainingSteps);
                System.out.println();
            }
        }
    }

    private static void viewSampleData() {
        System.out.println("\n📊 SAMPLE DATA OVERVIEW");
        System.out.println("=".repeat(50));
        
        System.out.println("👥 USERS:");
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (String userId : userIds) {
            System.out.println("  - " + userId);
        }
        
        System.out.println("\n💼 JOBS:");
        List<String> jobTitles = SampleDataLoader.getSampleJobTitles();
        for (String jobTitle : jobTitles) {
            System.out.println("  - " + jobTitle);
        }
        
        System.out.println("\n🔧 SKILLS:");
        List<String> skills = SampleDataLoader.getSampleSkills();
        for (String skill : skills) {
            System.out.println("  - " + skill);
        }
        
        System.out.println("\n📍 LOCATIONS:");
        System.out.println("  - Village A");
        System.out.println("  - Town B");
        System.out.println("  - City C");
        System.out.println("  - Village D");
        System.out.println("  - Town E");
    }

    private static void displaySystemStats() {
        JobRecommendationEngine.SystemStats stats = engine.getSystemStats();
        System.out.println("\n📈 SYSTEM STATISTICS");
        System.out.println("=".repeat(40));
        System.out.println("Total Jobs: " + stats.totalJobs);
        System.out.println("Total Users: " + stats.totalUsers);
        System.out.println("Unique Job Titles: " + stats.uniqueJobTitles);
        System.out.println("Unique Skills: " + stats.uniqueSkills);
        System.out.println("Total Locations: " + stats.totalLocations);
    }

    // Utility methods for input handling
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
} 