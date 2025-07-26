package com.ruralyouth.model;

import java.util.*;

/**
 * User class representing a rural youth seeking job opportunities
 * Uses HashMap for skills storage and efficient lookup
 */
public class User {
    private String id;
    private String name;
    private int age;
    private String education;
    private String location;
    private double latitude;
    private double longitude;
    private Map<String, Integer> skills; // skill -> proficiency level (1-10)
    private List<String> preferences;
    private double maxDistance; // maximum distance willing to travel

    public User(String id, String name, int age, String education, String location) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.education = education;
        this.location = location;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.skills = new HashMap<>();
        this.preferences = new ArrayList<>();
        this.maxDistance = 50.0; // default 50 km
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Map<String, Integer> getSkills() { return skills; }
    public void setSkills(Map<String, Integer> skills) { this.skills = skills; }

    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }

    public double getMaxDistance() { return maxDistance; }
    public void setMaxDistance(double maxDistance) { this.maxDistance = maxDistance; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // Skill management methods
    public void addSkill(String skill, int proficiency) {
        skills.put(skill.toLowerCase(), Math.min(10, Math.max(1, proficiency)));
    }

    public int getSkillProficiency(String skill) {
        return skills.getOrDefault(skill.toLowerCase(), 0);
    }

    public Set<String> getSkillSet() {
        return new HashSet<>(skills.keySet());
    }

    public void addPreference(String preference) {
        if (!preferences.contains(preference)) {
            preferences.add(preference);
        }
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', age=%d, location='%s', skills=%s}", 
                           id, name, age, location, skills);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 