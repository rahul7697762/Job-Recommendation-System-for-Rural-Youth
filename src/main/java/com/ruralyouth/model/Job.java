package com.ruralyouth.model;

import java.util.*;

/**
 * Job class representing available job opportunities
 * Uses HashSet for required skills for efficient matching
 */
public class Job {
    private String id;
    private String title;
    private String company;
    private String location;
    private double salary;
    private Set<String> requiredSkills; // Using HashSet for O(1) lookup
    private String description;
    private String jobType; // full-time, part-time, contract
    private double latitude;
    private double longitude;
    private int experienceLevel; // 1-5 (entry to senior)
    private List<String> benefits;

    public Job(String id, String title, String company, String location, double salary) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.requiredSkills = new HashSet<>();
        this.benefits = new ArrayList<>();
        this.experienceLevel = 1; // default entry level
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public Set<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(Set<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(int experienceLevel) { this.experienceLevel = experienceLevel; }

    public List<String> getBenefits() { return benefits; }
    public void setBenefits(List<String> benefits) { this.benefits = benefits; }

    // Skill management methods
    public void addRequiredSkill(String skill) {
        requiredSkills.add(skill.toLowerCase());
    }

    public boolean requiresSkill(String skill) {
        return requiredSkills.contains(skill.toLowerCase());
    }

    public void addBenefit(String benefit) {
        if (!benefits.contains(benefit)) {
            benefits.add(benefit);
        }
    }

    // Calculate distance to another location using Haversine formula
    public double calculateDistance(double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - this.latitude);
        double lonDistance = Math.toRadians(lon2 - this.longitude);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    @Override
    public String toString() {
        return String.format("Job{id='%s', title='%s', company='%s', location='%s', salary=%.2f, skills=%s}", 
                           id, title, company, location, salary, requiredSkills);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Job job = (Job) obj;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 