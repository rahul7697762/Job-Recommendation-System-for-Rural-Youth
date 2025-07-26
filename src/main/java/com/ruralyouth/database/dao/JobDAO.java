package com.ruralyouth.database.dao;

import com.ruralyouth.database.DatabaseManager;
import com.ruralyouth.model.Job;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for Job operations
 * Handles database persistence for job data
 */
public class JobDAO {
    
    /**
     * Save a job to the database
     */
    public static void saveJob(Job job) throws SQLException {
        String sql = "INSERT INTO jobs (id, title, company, location, salary, description, job_type, latitude, longitude, experience_level) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET " +
                    "title = EXCLUDED.title, company = EXCLUDED.company, location = EXCLUDED.location, " +
                    "salary = EXCLUDED.salary, description = EXCLUDED.description, job_type = EXCLUDED.job_type, " +
                    "latitude = EXCLUDED.latitude, longitude = EXCLUDED.longitude, experience_level = EXCLUDED.experience_level";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, job.getId());
            pstmt.setString(2, job.getTitle());
            pstmt.setString(3, job.getCompany());
            pstmt.setString(4, job.getLocation());
            pstmt.setDouble(5, job.getSalary());
            pstmt.setString(6, job.getDescription());
            pstmt.setString(7, job.getJobType());
            pstmt.setDouble(8, job.getLatitude());
            pstmt.setDouble(9, job.getLongitude());
            pstmt.setInt(10, job.getExperienceLevel());
            
            pstmt.executeUpdate();
            
            // Save job skills
            saveJobSkills(job);
            
            // Save job benefits
            saveJobBenefits(job);
        }
    }
    
    /**
     * Load a job from the database by ID
     */
    public static Job loadJob(String jobId) throws SQLException {
        String sql = "SELECT * FROM jobs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, jobId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Job job = new Job(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("location"),
                    rs.getDouble("salary")
                );
                
                job.setDescription(rs.getString("description"));
                job.setJobType(rs.getString("job_type"));
                job.setLatitude(rs.getDouble("latitude"));
                job.setLongitude(rs.getDouble("longitude"));
                job.setExperienceLevel(rs.getInt("experience_level"));
                
                // Load skills and benefits
                loadJobSkills(job);
                loadJobBenefits(job);
                
                return job;
            }
        }
        
        return null;
    }
    
    /**
     * Load all jobs from the database
     */
    public static List<Job> loadAllJobs() throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY title";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Job job = new Job(
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("location"),
                    rs.getDouble("salary")
                );
                
                job.setDescription(rs.getString("description"));
                job.setJobType(rs.getString("job_type"));
                job.setLatitude(rs.getDouble("latitude"));
                job.setLongitude(rs.getDouble("longitude"));
                job.setExperienceLevel(rs.getInt("experience_level"));
                
                // Load skills and benefits
                loadJobSkills(job);
                loadJobBenefits(job);
                
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Delete a job from the database
     */
    public static void deleteJob(String jobId) throws SQLException {
        String sql = "DELETE FROM jobs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, jobId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Search jobs by title using LIKE
     */
    public static List<Job> searchJobsByTitle(String titlePrefix) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE title ILIKE ? ORDER BY title";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, titlePrefix + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Job job = createJobFromResultSet(rs);
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Search jobs by skill
     */
    public static List<Job> searchJobsBySkill(String skillName) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT DISTINCT j.* FROM jobs j " +
                    "JOIN job_skills js ON j.id = js.job_id " +
                    "JOIN skills s ON js.skill_id = s.id " +
                    "WHERE s.name ILIKE ? ORDER BY j.title";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, skillName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Job job = createJobFromResultSet(rs);
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Search jobs by location
     */
    public static List<Job> searchJobsByLocation(String location) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE location ILIKE ? ORDER BY title";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + location + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Job job = createJobFromResultSet(rs);
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Search jobs by salary range
     */
    public static List<Job> searchJobsBySalaryRange(double minSalary, double maxSalary) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE salary BETWEEN ? AND ? ORDER BY salary DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, minSalary);
            pstmt.setDouble(2, maxSalary);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Job job = createJobFromResultSet(rs);
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Get job count from database
     */
    public static int getJobCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM jobs";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Get jobs with specific skills (for career path planning)
     */
    public static List<Job> getJobsWithSkills(Set<String> requiredSkills) throws SQLException {
        if (requiredSkills.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Job> jobs = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT j.*, COUNT(js.skill_id) as skill_match_count FROM jobs j ");
        sql.append("JOIN job_skills js ON j.id = js.job_id ");
        sql.append("JOIN skills s ON js.skill_id = s.id ");
        sql.append("WHERE s.name IN (");
        
        // Add placeholders for skills
        for (int i = 0; i < requiredSkills.size(); i++) {
            if (i > 0) sql.append(", ");
            sql.append("?");
        }
        sql.append(") ");
        sql.append("GROUP BY j.id ");
        sql.append("HAVING COUNT(js.skill_id) = ? ");
        sql.append("ORDER BY j.title");
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            for (String skill : requiredSkills) {
                pstmt.setString(paramIndex++, skill.toLowerCase());
            }
            pstmt.setInt(paramIndex, requiredSkills.size());
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Job job = createJobFromResultSet(rs);
                jobs.add(job);
            }
        }
        
        return jobs;
    }
    
    /**
     * Save job skills to the database
     */
    private static void saveJobSkills(Job job) throws SQLException {
        // First, ensure all skills exist in the skills table
        for (String skill : job.getRequiredSkills()) {
            ensureSkillExists(skill);
        }
        
        // Delete existing job skills
        String deleteSql = "DELETE FROM job_skills WHERE job_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, job.getId());
            pstmt.executeUpdate();
        }
        
        // Insert new job skills
        String insertSql = "INSERT INTO job_skills (job_id, skill_id) " +
                          "SELECT ?, s.id FROM skills s WHERE s.name = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (String skill : job.getRequiredSkills()) {
                pstmt.setString(1, job.getId());
                pstmt.setString(2, skill.toLowerCase());
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Load job skills from the database
     */
    private static void loadJobSkills(Job job) throws SQLException {
        String sql = "SELECT s.name FROM job_skills js " +
                    "JOIN skills s ON js.skill_id = s.id " +
                    "WHERE js.job_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, job.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String skillName = rs.getString("name");
                job.addRequiredSkill(skillName);
            }
        }
    }
    
    /**
     * Save job benefits to the database
     */
    private static void saveJobBenefits(Job job) throws SQLException {
        // Delete existing benefits
        String deleteSql = "DELETE FROM job_benefits WHERE job_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, job.getId());
            pstmt.executeUpdate();
        }
        
        // Insert new benefits
        String insertSql = "INSERT INTO job_benefits (job_id, benefit) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (String benefit : job.getBenefits()) {
                pstmt.setString(1, job.getId());
                pstmt.setString(2, benefit);
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Load job benefits from the database
     */
    private static void loadJobBenefits(Job job) throws SQLException {
        String sql = "SELECT benefit FROM job_benefits WHERE job_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, job.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String benefit = rs.getString("benefit");
                job.addBenefit(benefit);
            }
        }
    }
    
    /**
     * Ensure a skill exists in the skills table
     */
    private static void ensureSkillExists(String skillName) throws SQLException {
        String sql = "INSERT INTO skills (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, skillName.toLowerCase());
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Create a Job object from ResultSet
     */
    private static Job createJobFromResultSet(ResultSet rs) throws SQLException {
        Job job = new Job(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("company"),
            rs.getString("location"),
            rs.getDouble("salary")
        );
        
        job.setDescription(rs.getString("description"));
        job.setJobType(rs.getString("job_type"));
        job.setLatitude(rs.getDouble("latitude"));
        job.setLongitude(rs.getDouble("longitude"));
        job.setExperienceLevel(rs.getInt("experience_level"));
        
        // Load skills and benefits
        loadJobSkills(job);
        loadJobBenefits(job);
        
        return job;
    }
} 