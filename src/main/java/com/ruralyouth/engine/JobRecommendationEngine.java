package com.ruralyouth.engine;

import com.ruralyouth.dsa.*;
import com.ruralyouth.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main job recommendation engine that integrates all data structures and algorithms
 * Implements skill matching, location-based search, and intelligent ranking
 */
public class JobRecommendationEngine {
    private List<Job> jobs;
    private List<User> users;
    private Trie jobTitleTrie;
    private Trie skillTrie;
    private LocationGraph locationGraph;
    private Map<String, Job> jobMap; // HashMap for O(1) job lookup
    private Map<String, User> userMap; // HashMap for O(1) user lookup

    public JobRecommendationEngine() {
        this.jobs = new ArrayList<>();
        this.users = new ArrayList<>();
        this.jobTitleTrie = new Trie();
        this.skillTrie = new Trie();
        this.locationGraph = new LocationGraph();
        this.jobMap = new HashMap<>();
        this.userMap = new HashMap<>();
    }

    /**
     * Add a job to the system and update all data structures
     * Time Complexity: O(m + s) where m is job title length, s is number of skills
     */
    public void addJob(Job job) {
        jobs.add(job);
        jobMap.put(job.getId(), job);
        
        // Add to Trie for search
        jobTitleTrie.insert(job.getTitle());
        
        // Add skills to Trie
        for (String skill : job.getRequiredSkills()) {
            skillTrie.insert(skill);
        }
        
        // Add location to graph if not exists
        if (!locationGraph.hasLocation(job.getLocation())) {
            locationGraph.addLocation(job.getLocation(), job.getLatitude(), job.getLongitude());
        }
    }

    /**
     * Add a user to the system
     */
    public void addUser(User user) {
        users.add(user);
        userMap.put(user.getId(), user);
        
        // Add user location to graph if not exists
        if (!locationGraph.hasLocation(user.getLocation())) {
            // Use default coordinates if not provided
            locationGraph.addLocation(user.getLocation(), 0.0, 0.0);
        }
    }

    /**
     * Get job recommendations for a user
     * Time Complexity: O(n log n) where n is number of jobs
     */
    public List<JobRecommendation> getRecommendations(String userId, int limit) {
        User user = userMap.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        JobPriorityQueue priorityQueue = new JobPriorityQueue();
        
        // Score and add all jobs to priority queue
        for (Job job : jobs) {
            double distance = calculateDistance(user, job);
            double score = JobPriorityQueue.calculateJobScore(job, user, distance);
            priorityQueue.addJob(job, score);
        }

        // Get top recommendations
        List<JobPriorityQueue.JobScore> topJobs = priorityQueue.getTopKJobs(limit);
        
        return topJobs.stream()
                .map(js -> new JobRecommendation(js.job, js.score, calculateDistance(user, js.job)))
                .collect(Collectors.toList());
    }

    /**
     * Search jobs by title using Trie
     * Time Complexity: O(m + k) where m is prefix length, k is number of matching jobs
     */
    public List<Job> searchJobsByTitle(String prefix) {
        List<String> matchingTitles = jobTitleTrie.getWordsWithPrefix(prefix);
        List<Job> matchingJobs = new ArrayList<>();
        
        for (Job job : jobs) {
            if (matchingTitles.contains(job.getTitle())) {
                matchingJobs.add(job);
            }
        }
        
        return matchingJobs;
    }

    /**
     * Search jobs by skill using Trie
     * Time Complexity: O(m + k) where m is skill prefix length, k is number of matching jobs
     */
    public List<Job> searchJobsBySkill(String skillPrefix) {
        List<String> matchingSkills = skillTrie.getWordsWithPrefix(skillPrefix);
        List<Job> matchingJobs = new ArrayList<>();
        
        for (Job job : jobs) {
            for (String skill : matchingSkills) {
                if (job.requiresSkill(skill)) {
                    matchingJobs.add(job);
                    break; // Add job only once even if it matches multiple skills
                }
            }
        }
        
        return matchingJobs;
    }

    /**
     * Find jobs near a specific location using graph algorithms
     * Time Complexity: O(V + E) for BFS
     */
    public List<Job> findJobsNearLocation(String location, double maxDistance) {
        List<String> nearbyLocations = locationGraph.findNearbyLocationsBFS(location, maxDistance);
        List<Job> nearbyJobs = new ArrayList<>();
        
        for (Job job : jobs) {
            if (nearbyLocations.contains(job.getLocation())) {
                nearbyJobs.add(job);
            }
        }
        
        return nearbyJobs;
    }

    /**
     * Get personalized recommendations based on user preferences
     */
    public List<JobRecommendation> getPersonalizedRecommendations(String userId, 
                                                                 double minSalary, 
                                                                 double maxDistance, 
                                                                 List<String> preferredSkills,
                                                                 int limit) {
        User user = userMap.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        // Filter jobs based on criteria
        List<Job> filteredJobs = jobs.stream()
                .filter(job -> job.getSalary() >= minSalary)
                .filter(job -> calculateDistance(user, job) <= maxDistance)
                .collect(Collectors.toList());

        // If preferred skills are specified, prioritize jobs with those skills
        if (!preferredSkills.isEmpty()) {
            filteredJobs.sort((j1, j2) -> {
                int score1 = calculatePreferredSkillScore(j1, preferredSkills);
                int score2 = calculatePreferredSkillScore(j2, preferredSkills);
                return Integer.compare(score2, score1); // Descending order
            });
        }

        // Score and rank filtered jobs
        JobPriorityQueue priorityQueue = new JobPriorityQueue();
        for (Job job : filteredJobs) {
            double distance = calculateDistance(user, job);
            double score = JobPriorityQueue.calculateJobScore(job, user, distance);
            priorityQueue.addJob(job, score);
        }

        List<JobPriorityQueue.JobScore> topJobs = priorityQueue.getTopKJobs(limit);
        
        return topJobs.stream()
                .map(js -> new JobRecommendation(js.job, js.score, calculateDistance(user, js.job)))
                .collect(Collectors.toList());
    }

    /**
     * Find career path suggestions using graph traversal
     * This suggests training paths to reach desired jobs
     */
    public List<CareerPath> suggestCareerPaths(String userId, String targetJobTitle) {
        User user = userMap.get(userId);
        if (user == null) {
            return new ArrayList<>();
        }

        List<CareerPath> paths = new ArrayList<>();
        
        // Find target job
        Job targetJob = jobs.stream()
                .filter(job -> job.getTitle().equalsIgnoreCase(targetJobTitle))
                .findFirst()
                .orElse(null);

        if (targetJob == null) {
            return paths;
        }

        // Analyze skill gaps
        Set<String> userSkills = user.getSkillSet();
        Set<String> requiredSkills = targetJob.getRequiredSkills();
        Set<String> missingSkills = new HashSet<>(requiredSkills);
        missingSkills.removeAll(userSkills);

        if (missingSkills.isEmpty()) {
            // User already has all required skills
            paths.add(new CareerPath(targetJob, 0, "Direct application possible"));
        } else {
            // Suggest training paths
            for (String missingSkill : missingSkills) {
                List<Job> trainingJobs = findTrainingJobs(missingSkill);
                if (!trainingJobs.isEmpty()) {
                    paths.add(new CareerPath(targetJob, trainingJobs.size(), 
                            "Training needed for: " + missingSkill));
                }
            }
        }

        return paths;
    }

    /**
     * Calculate distance between user and job
     */
    private double calculateDistance(User user, Job job) {
        // If we have coordinates, use them
        if (job.getLatitude() != 0.0 && job.getLongitude() != 0.0) {
            return job.calculateDistance(user.getLatitude(), user.getLongitude());
        }
        
        // Otherwise, use graph distance if available
        if (locationGraph.hasLocation(user.getLocation()) && 
            locationGraph.hasLocation(job.getLocation())) {
            Map<String, Double> distances = locationGraph.findShortestDistances(user.getLocation());
            return distances.getOrDefault(job.getLocation(), Double.MAX_VALUE);
        }
        
        // Default: assume same location if locations match
        return user.getLocation().equals(job.getLocation()) ? 0.0 : 50.0;
    }

    /**
     * Calculate score based on preferred skills
     */
    private int calculatePreferredSkillScore(Job job, List<String> preferredSkills) {
        int score = 0;
        for (String skill : preferredSkills) {
            if (job.requiresSkill(skill)) {
                score++;
            }
        }
        return score;
    }

    /**
     * Find training jobs for a specific skill
     */
    private List<Job> findTrainingJobs(String skill) {
        return jobs.stream()
                .filter(job -> job.requiresSkill(skill) && job.getExperienceLevel() <= 2)
                .collect(Collectors.toList());
    }

    /**
     * Get statistics about the system
     */
    public SystemStats getSystemStats() {
        return new SystemStats(
            jobs.size(),
            users.size(),
            jobTitleTrie.size(),
            skillTrie.size(),
            locationGraph.getAllLocations().size()
        );
    }

    /**
     * Clear all data from the system
     */
    public void clearAll() {
        jobs.clear();
        users.clear();
        jobMap.clear();
        userMap.clear();
        jobTitleTrie.clear();
        skillTrie.clear();
        locationGraph = new LocationGraph();
    }

    // Inner classes for return types
    public static class JobRecommendation {
        public final Job job;
        public final double score;
        public final double distance;

        public JobRecommendation(Job job, double score, double distance) {
            this.job = job;
            this.score = score;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return String.format("JobRecommendation{job=%s, score=%.2f, distance=%.2f km}", 
                               job.getTitle(), score, distance);
        }
    }

    public static class CareerPath {
        public final Job targetJob;
        public final int trainingSteps;
        public final String description;

        public CareerPath(Job targetJob, int trainingSteps, String description) {
            this.targetJob = targetJob;
            this.trainingSteps = trainingSteps;
            this.description = description;
        }

        @Override
        public String toString() {
            return String.format("CareerPath{target=%s, steps=%d, description='%s'}", 
                               targetJob.getTitle(), trainingSteps, description);
        }
    }

    public static class SystemStats {
        public final int totalJobs;
        public final int totalUsers;
        public final int uniqueJobTitles;
        public final int uniqueSkills;
        public final int totalLocations;

        public SystemStats(int totalJobs, int totalUsers, int uniqueJobTitles, 
                          int uniqueSkills, int totalLocations) {
            this.totalJobs = totalJobs;
            this.totalUsers = totalUsers;
            this.uniqueJobTitles = uniqueJobTitles;
            this.uniqueSkills = uniqueSkills;
            this.totalLocations = totalLocations;
        }

        @Override
        public String toString() {
            return String.format("SystemStats{jobs=%d, users=%d, titles=%d, skills=%d, locations=%d}", 
                               totalJobs, totalUsers, uniqueJobTitles, uniqueSkills, totalLocations);
        }
    }
} 