-- MySQL Migration Script for Job Recommendation System
-- This script creates all necessary tables for the rural youth job recommendation system

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS rural_youth_jobs;
USE rural_youth_jobs;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    age INT,
    location VARCHAR(100),
    education_level ENUM('High School', 'Associate', 'Bachelor', 'Master', 'PhD', 'Other') DEFAULT 'High School',
    experience_years INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_location (location)
);

-- Skills table
CREATE TABLE IF NOT EXISTS skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_category (category)
);

-- User skills (many-to-many relationship)
CREATE TABLE IF NOT EXISTS user_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced', 'Expert') DEFAULT 'Beginner',
    years_experience INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_skill (user_id, skill_id),
    INDEX idx_user_id (user_id),
    INDEX idx_skill_id (skill_id)
);

-- Job categories
CREATE TABLE IF NOT EXISTS job_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- Jobs table
CREATE TABLE IF NOT EXISTS jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    company VARCHAR(150) NOT NULL,
    location VARCHAR(100) NOT NULL,
    category_id INT,
    description TEXT,
    requirements TEXT,
    salary_min DECIMAL(10,2),
    salary_max DECIMAL(10,2),
    salary_currency VARCHAR(3) DEFAULT 'USD',
    job_type ENUM('Full-time', 'Part-time', 'Contract', 'Internship', 'Remote') DEFAULT 'Full-time',
    experience_level ENUM('Entry', 'Junior', 'Mid-level', 'Senior', 'Lead') DEFAULT 'Entry',
    education_required ENUM('High School', 'Associate', 'Bachelor', 'Master', 'PhD', 'Any') DEFAULT 'Any',
    is_remote BOOLEAN DEFAULT FALSE,
    is_rural_friendly BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES job_categories(id) ON DELETE SET NULL,
    INDEX idx_title (title),
    INDEX idx_company (company),
    INDEX idx_location (location),
    INDEX idx_category_id (category_id),
    INDEX idx_job_type (job_type),
    INDEX idx_experience_level (experience_level),
    INDEX idx_is_rural_friendly (is_rural_friendly)
);

-- Job skills (many-to-many relationship)
CREATE TABLE IF NOT EXISTS job_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    skill_id INT NOT NULL,
    is_required BOOLEAN DEFAULT TRUE,
    importance_level ENUM('Low', 'Medium', 'High', 'Critical') DEFAULT 'Medium',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY unique_job_skill (job_id, skill_id),
    INDEX idx_job_id (job_id),
    INDEX idx_skill_id (skill_id),
    INDEX idx_is_required (is_required)
);

-- User job preferences
CREATE TABLE IF NOT EXISTS user_preferences (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    preferred_locations TEXT,
    preferred_job_types TEXT,
    preferred_salary_min DECIMAL(10,2),
    preferred_salary_max DECIMAL(10,2),
    preferred_remote BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_preference (user_id),
    INDEX idx_user_id (user_id)
);

-- Job applications
CREATE TABLE IF NOT EXISTS job_applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    job_id INT NOT NULL,
    status ENUM('Applied', 'Under Review', 'Interview', 'Rejected', 'Accepted') DEFAULT 'Applied',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (user_id, job_id),
    INDEX idx_user_id (user_id),
    INDEX idx_job_id (job_id),
    INDEX idx_status (status)
);

-- Job recommendations (cached recommendations)
CREATE TABLE IF NOT EXISTS job_recommendations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    job_id INT NOT NULL,
    score DECIMAL(5,4) NOT NULL,
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    UNIQUE KEY unique_recommendation (user_id, job_id),
    INDEX idx_user_id (user_id),
    INDEX idx_job_id (job_id),
    INDEX idx_score (score)
);

-- Insert sample data
INSERT IGNORE INTO job_categories (name, description) VALUES
('Technology', 'Software development, IT support, and technology-related roles'),
('Healthcare', 'Medical, nursing, and healthcare support positions'),
('Education', 'Teaching, training, and educational support roles'),
('Agriculture', 'Farming, ranching, and agricultural support positions'),
('Manufacturing', 'Production, assembly, and manufacturing roles'),
('Retail', 'Sales, customer service, and retail management'),
('Construction', 'Building, maintenance, and construction trades'),
('Transportation', 'Driving, logistics, and transportation roles');

-- Insert common skills
INSERT IGNORE INTO skills (name, category, description) VALUES
('Java Programming', 'Technology', 'Object-oriented programming with Java'),
('Python Programming', 'Technology', 'Scripting and data analysis with Python'),
('SQL Database', 'Technology', 'Database querying and management'),
('Customer Service', 'Retail', 'Interacting with customers professionally'),
('Microsoft Office', 'Administrative', 'Word, Excel, PowerPoint proficiency'),
('Driving License', 'Transportation', 'Valid driver license for vehicle operation'),
('First Aid', 'Healthcare', 'Basic medical emergency response'),
('Teaching', 'Education', 'Instruction and educational delivery'),
('Welding', 'Manufacturing', 'Metal joining and fabrication'),
('Carpentry', 'Construction', 'Woodworking and building construction'),
('Animal Care', 'Agriculture', 'Livestock and animal management'),
('Crop Management', 'Agriculture', 'Plant cultivation and farming'),
('Heavy Equipment', 'Construction', 'Operating construction machinery'),
('Inventory Management', 'Retail', 'Stock control and warehouse operations'),
('Communication', 'General', 'Verbal and written communication skills'); 