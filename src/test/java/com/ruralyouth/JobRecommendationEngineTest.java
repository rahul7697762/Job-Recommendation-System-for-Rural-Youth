package com.ruralyouth;

import com.ruralyouth.engine.JobRecommendationEngine;
import com.ruralyouth.model.Job;
import com.ruralyouth.model.User;
import com.ruralyouth.data.SampleDataLoader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Unit tests for the Job Recommendation Engine
 * Tests all major functionality and data structures
 */
public class JobRecommendationEngineTest {
    
    private JobRecommendationEngine engine;
    
    @Before
    public void setUp() {
        engine = new JobRecommendationEngine();
        SampleDataLoader.loadSampleData(engine);
    }
    
    @Test
    public void testSystemInitialization() {
        JobRecommendationEngine.SystemStats stats = engine.getSystemStats();
        
        assertNotNull("System stats should not be null", stats);
        assertTrue("Should have jobs loaded", stats.totalJobs > 0);
        assertTrue("Should have users loaded", stats.totalUsers > 0);
        assertTrue("Should have unique job titles", stats.uniqueJobTitles > 0);
        assertTrue("Should have unique skills", stats.uniqueSkills > 0);
        assertTrue("Should have locations", stats.totalLocations > 0);
        
        System.out.println("System Stats: " + stats);
    }
    
    @Test
    public void testJobRecommendations() {
        String userId = "U001"; // Rahul Kumar - farming skills
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations(userId, 5);
        
        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should have recommendations", recommendations.size() > 0);
        assertTrue("Should not exceed requested limit", recommendations.size() <= 5);
        
        // Check that recommendations are sorted by score (descending)
        for (int i = 1; i < recommendations.size(); i++) {
            assertTrue("Recommendations should be sorted by score",
                recommendations.get(i-1).score >= recommendations.get(i).score);
        }
        
        System.out.println("Recommendations for " + userId + ":");
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            System.out.printf("  %s (Score: %.2f, Distance: %.1f km)\n", 
                rec.job.getTitle(), rec.score, rec.distance);
        }
    }
    
    @Test
    public void testSearchJobsByTitle() {
        List<Job> jobs = engine.searchJobsByTitle("Java");
        
        assertNotNull("Search results should not be null", jobs);
        assertTrue("Should find Java Developer job", jobs.size() > 0);
        
        boolean foundJavaDeveloper = false;
        for (Job job : jobs) {
            if (job.getTitle().contains("Java")) {
                foundJavaDeveloper = true;
                break;
            }
        }
        assertTrue("Should find Java Developer job", foundJavaDeveloper);
        
        System.out.println("Jobs with 'Java' in title:");
        for (Job job : jobs) {
            System.out.println("  " + job.getTitle() + " - " + job.getCompany());
        }
    }
    
    @Test
    public void testSearchJobsBySkill() {
        List<Job> jobs = engine.searchJobsBySkill("farming");
        
        assertNotNull("Search results should not be null", jobs);
        assertTrue("Should find farming jobs", jobs.size() > 0);
        
        for (Job job : jobs) {
            assertTrue("Job should require farming skill", 
                job.getRequiredSkills().contains("farming"));
        }
        
        System.out.println("Jobs requiring 'farming' skill:");
        for (Job job : jobs) {
            System.out.println("  " + job.getTitle() + " - " + job.getCompany());
        }
    }
    
    @Test
    public void testPersonalizedRecommendations() {
        String userId = "U003"; // Amit Patel - tech skills
        double minSalary = 40000;
        double maxDistance = 50.0;
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getPersonalizedRecommendations(userId, minSalary, maxDistance, null, 3);
        
        assertNotNull("Recommendations should not be null", recommendations);
        
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            assertTrue("Salary should meet minimum requirement", 
                rec.job.getSalary() >= minSalary);
            assertTrue("Distance should be within limit", 
                rec.distance <= maxDistance);
        }
        
        System.out.println("Personalized recommendations for " + userId + ":");
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            System.out.printf("  %s (Salary: ₹%.0f, Distance: %.1f km, Score: %.2f)\n", 
                rec.job.getTitle(), rec.job.getSalary(), rec.distance, rec.score);
        }
    }
    
    @Test
    public void testCareerPathSuggestions() {
        String userId = "U001"; // Rahul Kumar - farming skills
        String targetJob = "Java Developer";
        
        List<JobRecommendationEngine.CareerPath> paths = 
            engine.suggestCareerPaths(userId, targetJob);
        
        assertNotNull("Career paths should not be null", paths);
        assertTrue("Should suggest career paths", paths.size() > 0);
        
        System.out.println("Career paths to '" + targetJob + "' for " + userId + ":");
        for (JobRecommendationEngine.CareerPath path : paths) {
            System.out.println("  " + path.description);
            System.out.println("  Training steps: " + path.trainingSteps);
        }
    }
    
    @Test
    public void testSkillMatching() {
        // Test user with tech skills
        User techUser = new User("TEST001", "Tech User", 25, "Graduate", "City C");
        techUser.addSkill("java", 8);
        techUser.addSkill("python", 7);
        techUser.addSkill("database", 6);
        engine.addUser(techUser);
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations("TEST001", 3);
        
        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should have recommendations", recommendations.size() > 0);
        
        // Check that tech jobs are prioritized
        boolean foundTechJob = false;
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            if (rec.job.getTitle().contains("Developer")) {
                foundTechJob = true;
                break;
            }
        }
        assertTrue("Should recommend tech jobs for tech user", foundTechJob);
        
        System.out.println("Tech user recommendations:");
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            System.out.printf("  %s (Score: %.2f)\n", rec.job.getTitle(), rec.score);
        }
    }
    
    @Test
    public void testLocationBasedSearch() {
        List<Job> nearbyJobs = engine.findJobsNearLocation("City C", 10.0);
        
        assertNotNull("Nearby jobs should not be null", nearbyJobs);
        
        for (Job job : nearbyJobs) {
            assertEquals("Job should be in City C", "City C", job.getLocation());
        }
        
        System.out.println("Jobs near City C:");
        for (Job job : nearbyJobs) {
            System.out.println("  " + job.getTitle() + " - " + job.getCompany());
        }
    }
    
    @Test
    public void testDataStructureEfficiency() {
        // Test Trie search efficiency
        long startTime = System.currentTimeMillis();
        List<Job> jobs = engine.searchJobsByTitle("Java");
        long endTime = System.currentTimeMillis();
        
        long searchTime = endTime - startTime;
        assertTrue("Trie search should be fast (< 100ms)", searchTime < 100);
        
        System.out.println("Trie search time: " + searchTime + "ms");
        System.out.println("Found " + jobs.size() + " jobs");
    }
    
    @Test
    public void testPriorityQueueRanking() {
        String userId = "U002"; // Priya Singh - sewing skills
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations(userId, 10);
        
        assertNotNull("Recommendations should not be null", recommendations);
        assertTrue("Should have recommendations", recommendations.size() > 0);
        
        // Verify that jobs with sewing skills are ranked higher
        boolean foundSewingJob = false;
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            if (rec.job.getRequiredSkills().contains("sewing")) {
                foundSewingJob = true;
                assertTrue("Sewing jobs should have good scores", rec.score > 50.0);
                break;
            }
        }
        assertTrue("Should find sewing jobs", foundSewingJob);
        
        System.out.println("Recommendations for sewing user:");
        for (JobRecommendationEngine.JobRecommendation rec : recommendations) {
            System.out.printf("  %s (Score: %.2f, Skills: %s)\n", 
                rec.job.getTitle(), rec.score, 
                String.join(", ", rec.job.getRequiredSkills()));
        }
    }
    
    @Test
    public void testEdgeCases() {
        // Test with non-existent user
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations("NONEXISTENT", 5);
        
        assertNotNull("Should return empty list, not null", recommendations);
        assertTrue("Should be empty for non-existent user", recommendations.isEmpty());
        
        // Test with empty search
        List<Job> jobs = engine.searchJobsByTitle("");
        assertNotNull("Empty search should not return null", jobs);
        
        // Test with non-existent skill
        List<Job> skillJobs = engine.searchJobsBySkill("nonexistentskill");
        assertNotNull("Non-existent skill search should not return null", skillJobs);
        assertTrue("Should be empty for non-existent skill", skillJobs.isEmpty());
        
        System.out.println("Edge case tests passed");
    }
} 