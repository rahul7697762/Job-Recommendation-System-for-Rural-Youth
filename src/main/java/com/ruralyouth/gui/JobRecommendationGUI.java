package com.ruralyouth.gui;

import com.ruralyouth.engine.JobRecommendationEngine;
import com.ruralyouth.data.SampleDataLoader;
import com.ruralyouth.model.Job;
import com.ruralyouth.model.User;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

/**
 * GUI application for the Job Recommendation System
 * Provides a modern, user-friendly interface with all functionality
 */
public class JobRecommendationGUI extends JFrame {
    
    private JobRecommendationEngine engine;
    private JTabbedPane tabbedPane;
    private JTextArea outputArea;
    private JTable recommendationsTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> userComboBox;
    private JComboBox<String> skillComboBox;
    private JComboBox<String> locationComboBox;
    private JSpinner limitSpinner;
    private JSpinner minSalarySpinner;
    private JSpinner maxDistanceSpinner;
    
    public JobRecommendationGUI() {
        // Initialize the engine
        engine = new JobRecommendationEngine();
        SampleDataLoader.loadSampleData(engine);
        
        // Setup the frame
        setupFrame();
        setupComponents();
        setupLayout();
        setupEventHandlers();
        
        // Display initial stats
        displaySystemStats();
    }
    
    private void setupFrame() {
        setTitle("🌾 Job Recommendation System for Rural Youth");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Use default look and feel - removed for compatibility
    }
    
    private void setupComponents() {
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create components
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Setup table
        setupTable();
        
        // Setup combo boxes
        setupComboBoxes();
        
        // Setup spinners
        setupSpinners();
    }
    
    private void setupTable() {
        String[] columnNames = {"Rank", "Job Title", "Company", "Location", "Salary", "Score", "Distance", "Skills"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recommendationsTable = new JTable(tableModel);
        recommendationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recommendationsTable.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        recommendationsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        recommendationsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        recommendationsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        recommendationsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        recommendationsTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        recommendationsTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        recommendationsTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        recommendationsTable.getColumnModel().getColumn(7).setPreferredWidth(200);
    }
    
    private void setupComboBoxes() {
        // User combo box
        userComboBox = new JComboBox<>();
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (String userId : userIds) {
            userComboBox.addItem(userId);
        }
        
        // Skill combo box
        skillComboBox = new JComboBox<>();
        List<String> skills = SampleDataLoader.getSampleSkills();
        for (String skill : skills) {
            skillComboBox.addItem(skill);
        }
        
        // Location combo box
        locationComboBox = new JComboBox<>();
        String[] locations = {"Village A", "Town B", "City C", "Village D", "Town E"};
        for (String location : locations) {
            locationComboBox.addItem(location);
        }
    }
    
    private void setupSpinners() {
        // Limit spinner
        SpinnerNumberModel limitModel = new SpinnerNumberModel(5, 1, 20, 1);
        limitSpinner = new JSpinner(limitModel);
        
        // Min salary spinner
        SpinnerNumberModel salaryModel = new SpinnerNumberModel(20000, 0, 200000, 5000);
        minSalarySpinner = new JSpinner(salaryModel);
        
        // Max distance spinner
        SpinnerNumberModel distanceModel = new SpinnerNumberModel(50.0, 1.0, 200.0, 5.0);
        maxDistanceSpinner = new JSpinner(distanceModel);
    }
    
    private void setupLayout() {
        // Create tabs
        createRecommendationsTab();
        createSearchTab();
        createCareerPathTab();
        createStatsTab();
        
        // Add tabs to pane
        tabbedPane.addTab("🎯 Recommendations", createRecommendationsTab());
        tabbedPane.addTab("🔍 Search", createSearchTab());
        tabbedPane.addTab("🛤️ Career Paths", createCareerPathTab());
        tabbedPane.addTab("📊 Statistics", createStatsTab());
        
        // Add to frame
        add(tabbedPane);
    }
    

    
    private JPanel createRecommendationsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Control panel
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Recommendation Settings"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // User selection
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        controlPanel.add(userComboBox, gbc);
        
        // Limit selection
        gbc.gridx = 0; gbc.gridy = 1;
        controlPanel.add(new JLabel("Number of Recommendations:"), gbc);
        gbc.gridx = 1;
        controlPanel.add(limitSpinner, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton getRecommendationsBtn = new JButton("Get Recommendations");
        JButton personalizedBtn = new JButton("Personalized Recommendations");
        JButton clearBtn = new JButton("Clear Results");
        
        buttonPanel.add(getRecommendationsBtn);
        buttonPanel.add(personalizedBtn);
        buttonPanel.add(clearBtn);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        controlPanel.add(buttonPanel, gbc);
        
        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Recommendation Results"));
        
        JScrollPane scrollPane = new JScrollPane(recommendationsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add to main panel
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(resultsPanel, BorderLayout.CENTER);
        
        // Add event handlers
        getRecommendationsBtn.addActionListener(e -> getRecommendations());
        personalizedBtn.addActionListener(e -> getPersonalizedRecommendations());
        clearBtn.addActionListener(e -> clearTable());
        
        return panel;
    }
    
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search options panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Options"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title search
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Search by Title:"), gbc);
        gbc.gridx = 1;
        JTextField titleField = new JTextField(20);
        searchPanel.add(titleField, gbc);
        
        // Skill search
        gbc.gridx = 0; gbc.gridy = 1;
        searchPanel.add(new JLabel("Search by Skill:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(skillComboBox, gbc);
        
        // Location search
        gbc.gridx = 0; gbc.gridy = 2;
        searchPanel.add(new JLabel("Search by Location:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(locationComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton searchTitleBtn = new JButton("Search by Title");
        JButton searchSkillBtn = new JButton("Search by Skill");
        JButton searchLocationBtn = new JButton("Search by Location");
        
        buttonPanel.add(searchTitleBtn);
        buttonPanel.add(searchSkillBtn);
        buttonPanel.add(searchLocationBtn);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        searchPanel.add(buttonPanel, gbc);
        
        // Results area
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add to main panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultsPanel, BorderLayout.CENTER);
        
        // Add event handlers
        searchTitleBtn.addActionListener(e -> searchByTitle(titleField.getText()));
        searchSkillBtn.addActionListener(e -> searchBySkill());
        searchLocationBtn.addActionListener(e -> searchByLocation());
        
        return panel;
    }
    
    private JPanel createCareerPathTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Career path panel
        JPanel pathPanel = new JPanel(new GridBagLayout());
        pathPanel.setBorder(BorderFactory.createTitledBorder("Career Path Planning"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // User selection
        gbc.gridx = 0; gbc.gridy = 0;
        pathPanel.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> pathUserCombo = new JComboBox<>();
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (String userId : userIds) {
            pathUserCombo.addItem(userId);
        }
        pathPanel.add(pathUserCombo, gbc);
        
        // Target job selection
        gbc.gridx = 0; gbc.gridy = 1;
        pathPanel.add(new JLabel("Target Job:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> targetJobCombo = new JComboBox<>();
        List<String> jobTitles = SampleDataLoader.getSampleJobTitles();
        for (String jobTitle : jobTitles) {
            targetJobCombo.addItem(jobTitle);
        }
        pathPanel.add(targetJobCombo, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton suggestPathBtn = new JButton("Suggest Career Path");
        JButton clearPathBtn = new JButton("Clear Results");
        
        buttonPanel.add(suggestPathBtn);
        buttonPanel.add(clearPathBtn);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        pathPanel.add(buttonPanel, gbc);
        
        // Results area
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Career Path Results"));
        
        JTextArea pathOutputArea = new JTextArea();
        pathOutputArea.setEditable(false);
        pathOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(pathOutputArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add to main panel
        panel.add(pathPanel, BorderLayout.NORTH);
        panel.add(resultsPanel, BorderLayout.CENTER);
        
        // Add event handlers
        suggestPathBtn.addActionListener(e -> suggestCareerPath(pathUserCombo.getSelectedItem().toString(), 
                                                               targetJobCombo.getSelectedItem().toString(), 
                                                               pathOutputArea));
        clearPathBtn.addActionListener(e -> pathOutputArea.setText(""));
        
        return panel;
    }
    
    private JPanel createStatsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("System Statistics"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Stats labels
        JLabel totalJobsLabel = new JLabel("Total Jobs: " + engine.getSystemStats().totalJobs);
        JLabel totalUsersLabel = new JLabel("Total Users: " + engine.getSystemStats().totalUsers);
        JLabel uniqueTitlesLabel = new JLabel("Unique Job Titles: " + engine.getSystemStats().uniqueJobTitles);
        JLabel uniqueSkillsLabel = new JLabel("Unique Skills: " + engine.getSystemStats().uniqueSkills);
        JLabel totalLocationsLabel = new JLabel("Total Locations: " + engine.getSystemStats().totalLocations);
        
        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(totalJobsLabel, gbc);
        gbc.gridx = 1;
        statsPanel.add(totalUsersLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        statsPanel.add(uniqueTitlesLabel, gbc);
        gbc.gridx = 1;
        statsPanel.add(uniqueSkillsLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        statsPanel.add(totalLocationsLabel, gbc);
        
        // Refresh button
        JButton refreshBtn = new JButton("Refresh Statistics");
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        statsPanel.add(refreshBtn, gbc);
        
        // Sample data panel
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBorder(BorderFactory.createTitledBorder("Sample Data Overview"));
        
        JTextArea dataArea = new JTextArea();
        dataArea.setEditable(false);
        dataArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        displaySampleData(dataArea);
        
        JScrollPane scrollPane = new JScrollPane(dataArea);
        dataPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add to main panel
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(dataPanel, BorderLayout.CENTER);
        
        // Add event handler
        refreshBtn.addActionListener(e -> {
            totalJobsLabel.setText("Total Jobs: " + engine.getSystemStats().totalJobs);
            totalUsersLabel.setText("Total Users: " + engine.getSystemStats().totalUsers);
            uniqueTitlesLabel.setText("Unique Job Titles: " + engine.getSystemStats().uniqueJobTitles);
            uniqueSkillsLabel.setText("Unique Skills: " + engine.getSystemStats().uniqueSkills);
            totalLocationsLabel.setText("Total Locations: " + engine.getSystemStats().totalLocations);
        });
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Add window listener for cleanup
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    // Event handler methods
    private void getRecommendations() {
        String userId = userComboBox.getSelectedItem().toString();
        int limit = (Integer) limitSpinner.getValue();
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getRecommendations(userId, limit);
        
        displayRecommendations(recommendations);
    }
    
    private void getPersonalizedRecommendations() {
        String userId = userComboBox.getSelectedItem().toString();
        double minSalary = ((Number) minSalarySpinner.getValue()).doubleValue();
        double maxDistance = ((Number) maxDistanceSpinner.getValue()).doubleValue();
        int limit = (Integer) limitSpinner.getValue();
        
        List<JobRecommendationEngine.JobRecommendation> recommendations = 
            engine.getPersonalizedRecommendations(userId, minSalary, maxDistance, null, limit);
        
        displayRecommendations(recommendations);
    }
    
    private void displayRecommendations(List<JobRecommendationEngine.JobRecommendation> recommendations) {
        clearTable();
        
        for (int i = 0; i < recommendations.size(); i++) {
            JobRecommendationEngine.JobRecommendation rec = recommendations.get(i);
            Job job = rec.job;
            
            Vector<Object> row = new Vector<>();
            row.add(i + 1);
            row.add(job.getTitle());
            row.add(job.getCompany());
            row.add(job.getLocation());
            row.add("₹" + String.format("%.0f", job.getSalary()));
            row.add(String.format("%.1f", rec.score));
            row.add(String.format("%.1f km", rec.distance));
            row.add(String.join(", ", job.getRequiredSkills()));
            
            tableModel.addRow(row);
        }
    }
    
    private void clearTable() {
        tableModel.setRowCount(0);
    }
    
    private void searchByTitle(String prefix) {
        if (prefix.trim().isEmpty()) {
            outputArea.setText("Please enter a job title prefix to search.");
            return;
        }
        
        List<Job> jobs = engine.searchJobsByTitle(prefix);
        
        StringBuilder result = new StringBuilder();
        result.append("🔍 SEARCH RESULTS FOR '").append(prefix).append("':\n");
        result.append("=".repeat(60)).append("\n\n");
        
        if (jobs.isEmpty()) {
            result.append("No jobs found matching '").append(prefix).append("'\n");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                result.append(String.format("%d. %s - %s (%s)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getLocation()));
            }
        }
        
        outputArea.setText(result.toString());
    }
    
    private void searchBySkill() {
        String skill = skillComboBox.getSelectedItem().toString();
        List<Job> jobs = engine.searchJobsBySkill(skill);
        
        StringBuilder result = new StringBuilder();
        result.append("🔍 JOBS REQUIRING '").append(skill.toUpperCase()).append("':\n");
        result.append("=".repeat(60)).append("\n\n");
        
        if (jobs.isEmpty()) {
            result.append("No jobs found requiring '").append(skill).append("'\n");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                result.append(String.format("%d. %s - %s (₹%.0f)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getSalary()));
            }
        }
        
        outputArea.setText(result.toString());
    }
    
    private void searchByLocation() {
        String location = locationComboBox.getSelectedItem().toString();
        double maxDistance = ((Number) maxDistanceSpinner.getValue()).doubleValue();
        
        List<Job> jobs = engine.findJobsNearLocation(location, maxDistance);
        
        StringBuilder result = new StringBuilder();
        result.append("📍 JOBS NEAR '").append(location).append("' (within ").append(maxDistance).append(" km):\n");
        result.append("=".repeat(70)).append("\n\n");
        
        if (jobs.isEmpty()) {
            result.append("No jobs found near '").append(location).append("'\n");
        } else {
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                result.append(String.format("%d. %s - %s (₹%.0f)\n", 
                    i + 1, job.getTitle(), job.getCompany(), job.getSalary()));
            }
        }
        
        outputArea.setText(result.toString());
    }
    
    private void suggestCareerPath(String userId, String targetJob, JTextArea outputArea) {
        List<JobRecommendationEngine.CareerPath> paths = 
            engine.suggestCareerPaths(userId, targetJob);
        
        StringBuilder result = new StringBuilder();
        result.append("🛤️ CAREER PATH TO '").append(targetJob).append("' for ").append(userId).append(":\n");
        result.append("=".repeat(60)).append("\n\n");
        
        if (paths.isEmpty()) {
            result.append("No career path found for '").append(targetJob).append("'\n");
        } else {
            for (int i = 0; i < paths.size(); i++) {
                JobRecommendationEngine.CareerPath path = paths.get(i);
                result.append(String.format("%d. %s\n", i + 1, path.description));
                result.append("   Training steps: ").append(path.trainingSteps).append("\n\n");
            }
        }
        
        outputArea.setText(result.toString());
    }
    
    private void displaySystemStats() {
        JobRecommendationEngine.SystemStats stats = engine.getSystemStats();
        System.out.println("System Stats: " + stats);
    }
    
    private void displaySampleData(JTextArea area) {
        StringBuilder data = new StringBuilder();
        data.append("👥 USERS:\n");
        List<String> userIds = SampleDataLoader.getSampleUserIds();
        for (String userId : userIds) {
            data.append("  - ").append(userId).append("\n");
        }
        
        data.append("\n💼 JOBS:\n");
        List<String> jobTitles = SampleDataLoader.getSampleJobTitles();
        for (String jobTitle : jobTitles) {
            data.append("  - ").append(jobTitle).append("\n");
        }
        
        data.append("\n🔧 SKILLS:\n");
        List<String> skills = SampleDataLoader.getSampleSkills();
        for (String skill : skills) {
            data.append("  - ").append(skill).append("\n");
        }
        
        data.append("\n📍 LOCATIONS:\n");
        data.append("  - Village A\n");
        data.append("  - Town B\n");
        data.append("  - City C\n");
        data.append("  - Village D\n");
        data.append("  - Town E\n");
        
        area.setText(data.toString());
    }
    
    public static void main(String[] args) {
        // Run on EDT
        SwingUtilities.invokeLater(() -> {
            JobRecommendationGUI gui = new JobRecommendationGUI();
            gui.setVisible(true);
        });
    }
} 