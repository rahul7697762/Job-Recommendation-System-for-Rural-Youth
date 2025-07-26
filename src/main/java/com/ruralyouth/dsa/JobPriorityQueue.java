package com.ruralyouth.dsa;

import com.ruralyouth.model.Job;
import com.ruralyouth.model.User;
import java.util.*;

/**
 * Priority Queue implementation for ranking jobs based on multiple criteria
 * Uses a max heap to prioritize jobs with higher scores
 */
public class JobPriorityQueue {
    private List<JobScore> heap;
    private int size;

    public JobPriorityQueue() {
        this.heap = new ArrayList<>();
        this.size = 0;
    }

    /**
     * Add a job with its calculated score to the priority queue
     * Time Complexity: O(log n) where n is the number of jobs
     */
    public void addJob(Job job, double score) {
        JobScore jobScore = new JobScore(job, score);
        heap.add(jobScore);
        size++;
        heapifyUp(size - 1);
    }

    /**
     * Remove and return the job with the highest score
     * Time Complexity: O(log n)
     */
    public Job removeTopJob() {
        if (isEmpty()) {
            return null;
        }

        Job topJob = heap.get(0).job;
        heap.set(0, heap.get(size - 1));
        heap.remove(size - 1);
        size--;

        if (size > 0) {
            heapifyDown(0);
        }

        return topJob;
    }

    /**
     * Peek at the job with the highest score without removing it
     * Time Complexity: O(1)
     */
    public Job peekTopJob() {
        if (isEmpty()) {
            return null;
        }
        return heap.get(0).job;
    }

    /**
     * Get the score of the top job
     */
    public double peekTopScore() {
        if (isEmpty()) {
            return 0.0;
        }
        return heap.get(0).score;
    }

    /**
     * Check if the priority queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get the number of jobs in the queue
     */
    public int size() {
        return size;
    }

    /**
     * Clear all jobs from the queue
     */
    public void clear() {
        heap.clear();
        size = 0;
    }

    /**
     * Get all jobs in the queue (not sorted)
     */
    public List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        for (JobScore jobScore : heap) {
            jobs.add(jobScore.job);
        }
        return jobs;
    }

    /**
     * Get top k jobs with their scores
     */
    public List<JobScore> getTopKJobs(int k) {
        List<JobScore> result = new ArrayList<>();
        JobPriorityQueue tempQueue = new JobPriorityQueue();
        
        // Copy all jobs to temporary queue
        for (JobScore jobScore : heap) {
            tempQueue.addJob(jobScore.job, jobScore.score);
        }
        
        // Extract top k jobs
        int count = Math.min(k, size);
        for (int i = 0; i < count; i++) {
            Job job = tempQueue.removeTopJob();
            if (job != null) {
                double score = tempQueue.peekTopScore();
                result.add(new JobScore(job, score));
            }
        }
        
        return result;
    }

    /**
     * Heapify up operation for maintaining heap property
     */
    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        
        while (index > 0 && heap.get(index).score > heap.get(parent).score) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    /**
     * Heapify down operation for maintaining heap property
     */
    private void heapifyDown(int index) {
        int largest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < size && heap.get(leftChild).score > heap.get(largest).score) {
            largest = leftChild;
        }

        if (rightChild < size && heap.get(rightChild).score > heap.get(largest).score) {
            largest = rightChild;
        }

        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    /**
     * Swap two elements in the heap
     */
    private void swap(int i, int j) {
        JobScore temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Calculate job score based on multiple criteria
     * This is a comprehensive scoring algorithm that considers:
     * - Skill match percentage (40% weight)
     * - Distance factor (30% weight)
     * - Salary factor (20% weight)
     * - Experience level match (10% weight)
     */
    public static double calculateJobScore(Job job, User user, double distance) {
        double skillScore = calculateSkillMatchScore(job, user);
        double distanceScore = calculateDistanceScore(distance, user.getMaxDistance());
        double salaryScore = calculateSalaryScore(job.getSalary());
        double experienceScore = calculateExperienceScore(job.getExperienceLevel(), user.getAge());

        // Weighted combination
        return skillScore * 0.4 + distanceScore * 0.3 + salaryScore * 0.2 + experienceScore * 0.1;
    }

    /**
     * Calculate skill match score (0-100)
     */
    private static double calculateSkillMatchScore(Job job, User user) {
        Set<String> userSkills = user.getSkillSet();
        Set<String> requiredSkills = job.getRequiredSkills();
        
        if (requiredSkills.isEmpty()) {
            return 50.0; // Neutral score for jobs with no specific requirements
        }

        int matchedSkills = 0;
        double totalProficiency = 0.0;
        
        for (String skill : requiredSkills) {
            if (userSkills.contains(skill)) {
                matchedSkills++;
                totalProficiency += user.getSkillProficiency(skill);
            }
        }

        double matchPercentage = (double) matchedSkills / requiredSkills.size();
        double avgProficiency = matchedSkills > 0 ? totalProficiency / matchedSkills : 0;
        
        // Score based on both match percentage and proficiency
        return (matchPercentage * 70) + (avgProficiency * 3); // Max 100
    }

    /**
     * Calculate distance score (0-100)
     */
    private static double calculateDistanceScore(double distance, double maxDistance) {
        if (distance <= 0) {
            return 100.0; // Same location
        }
        
        if (distance > maxDistance) {
            return 0.0; // Beyond user's limit
        }
        
        // Exponential decay: closer jobs get higher scores
        return 100.0 * Math.exp(-distance / (maxDistance / 3));
    }

    /**
     * Calculate salary score (0-100)
     */
    private static double calculateSalaryScore(double salary) {
        // Normalize salary to a 0-100 scale
        // Assuming salary range from 20,000 to 200,000
        double minSalary = 20000;
        double maxSalary = 200000;
        
        if (salary <= minSalary) {
            return 0.0;
        }
        
        if (salary >= maxSalary) {
            return 100.0;
        }
        
        return ((salary - minSalary) / (maxSalary - minSalary)) * 100;
    }

    /**
     * Calculate experience level match score (0-100)
     */
    private static double calculateExperienceScore(int jobExperienceLevel, int userAge) {
        // Simple age-based experience estimation
        int estimatedExperience = Math.max(0, (userAge - 18) / 5); // Rough estimate
        
        int levelDifference = Math.abs(jobExperienceLevel - estimatedExperience);
        
        if (levelDifference == 0) {
            return 100.0; // Perfect match
        } else if (levelDifference == 1) {
            return 80.0; // Good match
        } else if (levelDifference == 2) {
            return 60.0; // Acceptable match
        } else {
            return Math.max(0, 40 - (levelDifference - 2) * 10); // Decreasing score
        }
    }

    /**
     * Inner class to hold job and its score
     */
    public static class JobScore {
        public final Job job;
        public final double score;

        public JobScore(Job job, double score) {
            this.job = job;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format("JobScore{job=%s, score=%.2f}", job.getTitle(), score);
        }
    }
} 