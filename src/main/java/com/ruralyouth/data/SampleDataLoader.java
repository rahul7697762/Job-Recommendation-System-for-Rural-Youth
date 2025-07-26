package com.ruralyouth.data;

import com.ruralyouth.engine.JobRecommendationEngine;
import com.ruralyouth.model.Job;
import com.ruralyouth.model.User;
import com.ruralyouth.dsa.LocationGraph;

import java.util.Arrays;
import java.util.List;

/**
 * Sample data loader for testing and demonstration
 * Creates realistic job and user data for rural areas
 */
public class SampleDataLoader {
    
    public static void loadSampleData(JobRecommendationEngine engine) {
        loadSampleUsers(engine);
        loadSampleJobs(engine);
        loadSampleLocations(engine);
    }

    private static void loadSampleUsers(JobRecommendationEngine engine) {
        // Create sample users with different skill sets and locations
        User user1 = new User("U001", "Rahul Kumar", 22, "High School", "Village A");
        user1.setLatitude(28.6139);
        user1.setLongitude(77.2090);
        user1.addSkill("farming", 8);
        user1.addSkill("driving", 7);
        user1.addSkill("basic computer", 5);
        user1.addPreference("agriculture");
        user1.setMaxDistance(30.0);
        engine.addUser(user1);

        User user2 = new User("U002", "Priya Singh", 25, "Diploma", "Town B");
        user2.setLatitude(28.7041);
        user2.setLongitude(77.1025);
        user2.addSkill("sewing", 9);
        user2.addSkill("cooking", 8);
        user2.addSkill("english", 6);
        user2.addSkill("basic computer", 7);
        user2.addPreference("textile");
        user2.addPreference("food");
        user2.setMaxDistance(25.0);
        engine.addUser(user2);

        User user3 = new User("U003", "Amit Patel", 28, "Graduate", "City C");
        user3.setLatitude(28.4595);
        user3.setLongitude(77.0266);
        user3.addSkill("java", 8);
        user3.addSkill("python", 7);
        user3.addSkill("database", 6);
        user3.addSkill("web development", 7);
        user3.addSkill("english", 8);
        user3.addPreference("technology");
        user3.setMaxDistance(50.0);
        engine.addUser(user3);

        User user4 = new User("U004", "Sunita Devi", 20, "High School", "Village D");
        user4.setLatitude(28.5355);
        user4.setLongitude(77.3910);
        user4.addSkill("farming", 6);
        user4.addSkill("cooking", 8);
        user4.addSkill("cleaning", 9);
        user4.addPreference("agriculture");
        user4.addPreference("domestic");
        user4.setMaxDistance(20.0);
        engine.addUser(user4);

        User user5 = new User("U005", "Rajesh Verma", 30, "ITI", "Town E");
        user5.setLatitude(28.6562);
        user5.setLongitude(77.2410);
        user5.addSkill("welding", 9);
        user5.addSkill("electrical", 8);
        user5.addSkill("plumbing", 7);
        user5.addSkill("driving", 8);
        user5.addPreference("manufacturing");
        user5.addPreference("construction");
        user5.setMaxDistance(40.0);
        engine.addUser(user5);
    }

    private static void loadSampleJobs(JobRecommendationEngine engine) {
        // Agriculture and Farming Jobs
        Job farmWorker = new Job("J001", "Farm Worker", "Green Farms Ltd", "Village A", 18000);
        farmWorker.setLatitude(28.6139);
        farmWorker.setLongitude(77.2090);
        farmWorker.addRequiredSkill("farming");
        farmWorker.setDescription("Work in agricultural fields, crop management");
        farmWorker.setJobType("full-time");
        farmWorker.setExperienceLevel(1);
        farmWorker.addBenefit("Free accommodation");
        engine.addJob(farmWorker);

        Job tractorDriver = new Job("J002", "Tractor Driver", "Modern Agriculture", "Town B", 22000);
        tractorDriver.setLatitude(28.7041);
        tractorDriver.setLongitude(77.1025);
        tractorDriver.addRequiredSkill("driving");
        tractorDriver.addRequiredSkill("farming");
        tractorDriver.setDescription("Operate tractors and farm machinery");
        tractorDriver.setJobType("full-time");
        tractorDriver.setExperienceLevel(2);
        engine.addJob(tractorDriver);

        // Textile and Garment Jobs
        Job tailor = new Job("J003", "Tailor", "Fashion Stitch", "Town B", 20000);
        tailor.setLatitude(28.7041);
        tailor.setLongitude(77.1025);
        tailor.addRequiredSkill("sewing");
        tailor.setDescription("Stitch and alter garments");
        tailor.setJobType("full-time");
        tailor.setExperienceLevel(2);
        engine.addJob(tailor);

        Job garmentWorker = new Job("J004", "Garment Worker", "Textile Factory", "City C", 25000);
        garmentWorker.setLatitude(28.4595);
        garmentWorker.setLongitude(77.0266);
        garmentWorker.addRequiredSkill("sewing");
        garmentWorker.addRequiredSkill("basic computer");
        garmentWorker.setDescription("Work in garment manufacturing unit");
        garmentWorker.setJobType("full-time");
        garmentWorker.setExperienceLevel(1);
        garmentWorker.addBenefit("Health insurance");
        engine.addJob(garmentWorker);

        // Technology Jobs
        Job javaDeveloper = new Job("J005", "Java Developer", "Tech Solutions", "City C", 45000);
        javaDeveloper.setLatitude(28.4595);
        javaDeveloper.setLongitude(77.0266);
        javaDeveloper.addRequiredSkill("java");
        javaDeveloper.addRequiredSkill("database");
        javaDeveloper.addRequiredSkill("english");
        javaDeveloper.setDescription("Develop Java applications");
        javaDeveloper.setJobType("full-time");
        javaDeveloper.setExperienceLevel(3);
        javaDeveloper.addBenefit("Health insurance");
        javaDeveloper.addBenefit("Work from home");
        engine.addJob(javaDeveloper);

        Job pythonDeveloper = new Job("J006", "Python Developer", "Data Analytics Corp", "City C", 50000);
        pythonDeveloper.setLatitude(28.4595);
        pythonDeveloper.setLongitude(77.0266);
        pythonDeveloper.addRequiredSkill("python");
        pythonDeveloper.addRequiredSkill("database");
        pythonDeveloper.addRequiredSkill("english");
        pythonDeveloper.setDescription("Develop Python applications and data analysis");
        pythonDeveloper.setJobType("full-time");
        pythonDeveloper.setExperienceLevel(3);
        pythonDeveloper.addBenefit("Health insurance");
        pythonDeveloper.addBenefit("Flexible hours");
        engine.addJob(pythonDeveloper);

        Job webDeveloper = new Job("J007", "Web Developer", "Digital Creations", "City C", 40000);
        webDeveloper.setLatitude(28.4595);
        webDeveloper.setLongitude(77.0266);
        webDeveloper.addRequiredSkill("web development");
        webDeveloper.addRequiredSkill("basic computer");
        webDeveloper.addRequiredSkill("english");
        webDeveloper.setDescription("Develop websites and web applications");
        webDeveloper.setJobType("full-time");
        webDeveloper.setExperienceLevel(2);
        engine.addJob(webDeveloper);

        // Food and Hospitality Jobs
        Job cook = new Job("J008", "Cook", "Village Restaurant", "Village D", 18000);
        cook.setLatitude(28.5355);
        cook.setLongitude(77.3910);
        cook.addRequiredSkill("cooking");
        cook.setDescription("Prepare food in restaurant kitchen");
        cook.setJobType("full-time");
        cook.setExperienceLevel(2);
        engine.addJob(cook);

        Job kitchenHelper = new Job("J009", "Kitchen Helper", "Food Court", "Town E", 15000);
        kitchenHelper.setLatitude(28.6562);
        kitchenHelper.setLongitude(77.2410);
        kitchenHelper.addRequiredSkill("cooking");
        kitchenHelper.addRequiredSkill("cleaning");
        kitchenHelper.setDescription("Assist in kitchen operations");
        kitchenHelper.setJobType("part-time");
        kitchenHelper.setExperienceLevel(1);
        engine.addJob(kitchenHelper);

        // Domestic and Service Jobs
        Job housekeeper = new Job("J010", "Housekeeper", "Home Services", "Village D", 16000);
        housekeeper.setLatitude(28.5355);
        housekeeper.setLongitude(77.3910);
        housekeeper.addRequiredSkill("cleaning");
        housekeeper.addRequiredSkill("cooking");
        housekeeper.setDescription("Domestic cleaning and cooking services");
        housekeeper.setJobType("full-time");
        housekeeper.setExperienceLevel(1);
        engine.addJob(housekeeper);

        // Manufacturing and Technical Jobs
        Job welder = new Job("J011", "Welder", "Metal Works Ltd", "Town E", 28000);
        welder.setLatitude(28.6562);
        welder.setLongitude(77.2410);
        welder.addRequiredSkill("welding");
        welder.setDescription("Metal welding and fabrication work");
        welder.setJobType("full-time");
        welder.setExperienceLevel(3);
        welder.addBenefit("Safety equipment provided");
        engine.addJob(welder);

        Job electrician = new Job("J012", "Electrician", "Power Solutions", "Town E", 32000);
        electrician.setLatitude(28.6562);
        electrician.setLongitude(77.2410);
        electrician.addRequiredSkill("electrical");
        electrician.addRequiredSkill("basic computer");
        electrician.setDescription("Electrical installation and maintenance");
        electrician.setJobType("full-time");
        electrician.setExperienceLevel(3);
        electrician.addBenefit("Health insurance");
        engine.addJob(electrician);

        Job plumber = new Job("J013", "Plumber", "Water Works", "Town E", 25000);
        plumber.setLatitude(28.6562);
        plumber.setLongitude(77.2410);
        plumber.addRequiredSkill("plumbing");
        plumber.setDescription("Plumbing installation and repair");
        plumber.setJobType("full-time");
        plumber.setExperienceLevel(2);
        engine.addJob(plumber);

        // Transportation Jobs
        Job truckDriver = new Job("J014", "Truck Driver", "Logistics Corp", "Town B", 30000);
        truckDriver.setLatitude(28.7041);
        truckDriver.setLongitude(77.1025);
        truckDriver.addRequiredSkill("driving");
        truckDriver.addRequiredSkill("english");
        truckDriver.setDescription("Drive trucks for goods transportation");
        truckDriver.setJobType("full-time");
        truckDriver.setExperienceLevel(2);
        truckDriver.addBenefit("Travel allowance");
        engine.addJob(truckDriver);

        // Entry Level Jobs (for career path suggestions)
        Job dataEntry = new Job("J015", "Data Entry Operator", "Office Solutions", "City C", 20000);
        dataEntry.setLatitude(28.4595);
        dataEntry.setLongitude(77.0266);
        dataEntry.addRequiredSkill("basic computer");
        dataEntry.addRequiredSkill("english");
        dataEntry.setDescription("Enter data into computer systems");
        dataEntry.setJobType("full-time");
        dataEntry.setExperienceLevel(1);
        engine.addJob(dataEntry);

        Job computerOperator = new Job("J016", "Computer Operator", "Tech Support", "City C", 22000);
        computerOperator.setLatitude(28.4595);
        computerOperator.setLongitude(77.0266);
        computerOperator.addRequiredSkill("basic computer");
        computerOperator.addRequiredSkill("english");
        computerOperator.setDescription("Basic computer operations and support");
        computerOperator.setJobType("full-time");
        computerOperator.setExperienceLevel(1);
        engine.addJob(computerOperator);
    }

    private static void loadSampleLocations(JobRecommendationEngine engine) {
        // This would typically load location data and road connections
        // For now, we'll create some sample road connections between locations
        
        // Get the location graph from the engine (this would need to be exposed)
        // For demonstration, we'll assume the locations are already added via jobs and users
        
        // Sample road connections (these would be loaded from a file in real implementation)
        // Village A <-> Town B: 15 km
        // Town B <-> City C: 25 km
        // Village D <-> Town E: 12 km
        // Town E <-> City C: 20 km
        // Village A <-> Village D: 18 km
    }

    /**
     * Get a list of sample user IDs for testing
     */
    public static List<String> getSampleUserIds() {
        return Arrays.asList("U001", "U002", "U003", "U004", "U005");
    }

    /**
     * Get a list of sample job titles for testing
     */
    public static List<String> getSampleJobTitles() {
        return Arrays.asList(
            "Farm Worker", "Tractor Driver", "Tailor", "Garment Worker",
            "Java Developer", "Python Developer", "Web Developer",
            "Cook", "Kitchen Helper", "Housekeeper",
            "Welder", "Electrician", "Plumber", "Truck Driver",
            "Data Entry Operator", "Computer Operator"
        );
    }

    /**
     * Get a list of sample skills for testing
     */
    public static List<String> getSampleSkills() {
        return Arrays.asList(
            "farming", "driving", "basic computer", "sewing", "cooking",
            "english", "java", "python", "database", "web development",
            "cleaning", "welding", "electrical", "plumbing"
        );
    }
} 