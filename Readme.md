# 🌾 Job Recommendation System for Rural Youth

A comprehensive data structure and algorithm-based job recommendation system designed specifically for rural youth. This system uses efficient data structures and algorithms to provide personalized job recommendations based on skills, location, and preferences.

## 🎯 Project Overview

This system addresses the challenge of connecting rural youth with suitable job opportunities by implementing:

- **Skill Matching Engine**: Uses HashMaps and Sets for efficient skill matching
- **Location-Based Search**: Graph algorithms (BFS/Dijkstra) for finding nearby jobs
- **Intelligent Ranking**: Priority Queue (Heap) for scoring and ranking jobs
- **Fast Search**: Trie data structure for job title and skill autocomplete
- **Career Path Planning**: Graph traversal for suggesting training paths
- **Database Integration**: Support for both MySQL and Supabase (PostgreSQL)

## 🗄️ Database Support

This system supports two database options:

### Option 1: MySQL
- **Local Development**: Perfect for development and testing
- **Easy Setup**: Simple installation and configuration
- **Performance**: Excellent for small to medium datasets

### Option 2: Supabase (PostgreSQL)
- **Cloud Hosted**: Managed PostgreSQL database in the cloud
- **Real-time Features**: Built-in real-time subscriptions
- **Scalability**: Handles large datasets and concurrent users
- **Free Tier**: Generous free tier for development

### Database Setup

#### Quick Database Switching

**Switch to MySQL:**
```bash
# Windows
.\switch_to_mysql.bat

# Linux/Mac
./switch_to_mysql.sh
```

**Switch to Supabase:**
```bash
# Windows
.\switch_to_supabase.bat

# Linux/Mac
./switch_to_supabase.sh
```

#### MySQL Setup

1. **Install MySQL** (if not already installed)
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Add MySQL to your system PATH

2. **Configure Database**
   ```bash
   # Switch to MySQL configuration
   .\switch_to_mysql.bat
   
   # Edit database.properties with your MySQL credentials
   # Update: database.username, database.password
   ```

3. **Create Database and Tables**
   ```bash
   .\setup_mysql.bat
   ```

#### Supabase Setup

1. **Create Supabase Project**
   - Go to https://supabase.com
   - Create a new project
   - Get your database connection details

2. **Configure Database**
   ```bash
   # Switch to Supabase configuration
   .\switch_to_supabase.bat
   
   # Edit database.properties with your Supabase credentials
   # Update: database.host, database.password
   ```

3. **Run Migration Script**
   - Open your Supabase project dashboard
   - Go to SQL Editor
   - Copy and paste: `src/main/resources/db/migration/V1__Create_Tables.sql`
   - Execute the script

#### Database Configuration

The system automatically detects your database type and configures the appropriate connection. Configuration is stored in `database.properties`:

```properties
# Database type (MYSQL, SUPABASE, POSTGRESQL)
database.type=MYSQL

# Connection settings
database.host=localhost
database.port=3306
database.name=rural_youth_jobs
database.username=root
database.password=your_password

# Connection pool settings
database.pool.size=10
database.timeout=30000
```

#### Environment Variables

You can also configure the database using environment variables:

```bash
# MySQL
export DB_TYPE=MYSQL
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=rural_youth_jobs
export DB_USER=root
export DB_PASSWORD=your_password

# Supabase
export DB_TYPE=SUPABASE
export DB_HOST=db.your-project-ref.supabase.co
export DB_PORT=5432
export DB_NAME=postgres
export DB_USER=postgres
export DB_PASSWORD=your_supabase_password
```

#### Database Utilities

**Check Current Configuration:**
```bash
java -cp target/classes com.ruralyouth.database.DatabaseSwitcher check
```

**Test Database Connection:**
```bash
java -cp target/classes com.ruralyouth.database.DatabaseSwitcher test
```

## 🏗️ System Architecture

### Data Structures Used

| Feature | Data Structure | Purpose | Time Complexity |
|---------|----------------|---------|-----------------|
| Skill Matching | HashMap/Set | Fast skill lookup and matching | O(1) |
| Location Search | Graph + BFS/Dijkstra | Nearest job search | O(V+E) / O((V+E)logV) |
| Job Search | Trie | Autocomplete and prefix search | O(m+k) |
| Job Ranking | Priority Queue (Heap) | Score-based sorting | O(log n) |
| User/Job Storage | HashMap | O(1) lookup by ID | O(1) |

### Core Components

1. **Models** (`src/main/java/com/ruralyouth/model/`)
   - `User.java`: User profile with skills and preferences
   - `Job.java`: Job details with requirements and location

2. **Data Structures** (`src/main/java/com/ruralyouth/dsa/`)
   - `Trie.java`: Efficient string search and autocomplete
   - `LocationGraph.java`: Graph-based location search
   - `JobPriorityQueue.java`: Heap-based job ranking

3. **Engine** (`src/main/java/com/ruralyouth/engine/`)
   - `JobRecommendationEngine.java`: Main recommendation logic

4. **Data** (`src/main/java/com/ruralyouth/data/`)
   - `SampleDataLoader.java`: Sample data for testing

## 🚀 How to Run

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd job-recommendation-system
   ```

2. **Compile the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   # Command-line interface
   mvn exec:java -Dexec.mainClass="com.ruralyouth.JobRecommendationApp"
   
   # GUI interface
   mvn exec:java -Dexec.mainClass="com.ruralyouth.gui.JobRecommendationGUI"
   ```

4. **Run tests**
   ```bash
   mvn test
   ```

### Alternative: Direct Java Execution

```bash
# Compile
javac -cp "target/classes:target/dependency/*" src/main/java/com/ruralyouth/*.java

# Run Command-line Interface
java -cp "target/classes:target/dependency/*" com.ruralyouth.JobRecommendationApp

# Run GUI Interface
java -cp "target/classes:target/dependency/*" com.ruralyouth.gui.JobRecommendationGUI
```

### Quick Start Scripts

**Windows:**
```bash
# Command-line interface
.\compile_and_run.bat

# GUI interface
.\run_gui.bat
```

**Linux/Mac:**
```bash
# Command-line interface
chmod +x compile_and_run.sh
./compile_and_run.sh

# GUI interface
chmod +x run_gui.sh
./run_gui.sh
```

## 📋 Features

### 1. Job Recommendations
- **Smart Matching**: Combines skill match, distance, salary, and experience
- **Personalized Scoring**: 40% skills, 30% distance, 20% salary, 10% experience
- **Real-time Ranking**: Priority queue ensures best matches first

### 2. Search Functionality
- **Job Title Search**: Trie-based autocomplete and prefix search
- **Skill-based Search**: Find jobs requiring specific skills
- **Location-based Search**: Graph algorithms for nearby job discovery

### 3. Career Path Planning
- **Skill Gap Analysis**: Identifies missing skills for target jobs
- **Training Recommendations**: Suggests entry-level jobs for skill development
- **Path Visualization**: Shows steps needed to reach career goals

### 4. Advanced Features
- **Personalized Filters**: Salary, distance, and skill preferences
- **System Statistics**: Real-time metrics on jobs, users, and skills
- **Efficient Algorithms**: Optimized for large datasets

### 5. User Interface Options
- **Command-Line Interface**: Interactive menu system for terminal users
- **Graphical User Interface**: Modern Swing-based GUI with tabs and tables
  - **Recommendations Tab**: Get and display job recommendations in a table
  - **Search Tab**: Search jobs by title, skill, or location
  - **Career Path Tab**: Plan career progression paths
  - **Statistics Tab**: View system statistics and sample data

## 🧪 Sample Data

The system comes with realistic sample data including:

### Users (5 profiles)
- **Rahul Kumar**: Farming and driving skills (Village A)
- **Priya Singh**: Sewing and cooking skills (Town B)
- **Amit Patel**: Technology skills (City C)
- **Sunita Devi**: Domestic skills (Village D)
- **Rajesh Verma**: Technical skills (Town E)

### Jobs (16 positions)
- **Agriculture**: Farm Worker, Tractor Driver
- **Textile**: Tailor, Garment Worker
- **Technology**: Java Developer, Python Developer, Web Developer
- **Food Service**: Cook, Kitchen Helper
- **Domestic**: Housekeeper
- **Technical**: Welder, Electrician, Plumber
- **Transportation**: Truck Driver
- **Entry Level**: Data Entry, Computer Operator

### Skills (14 categories)
- Technical: java, python, database, web development
- Practical: farming, driving, sewing, cooking, cleaning
- Trade: welding, electrical, plumbing
- General: basic computer, english

## 📊 Algorithm Details

### Job Scoring Algorithm

```java
Score = (SkillMatch * 0.4) + (DistanceScore * 0.3) + (SalaryScore * 0.2) + (ExperienceScore * 0.1)
```

**Skill Match Score (0-100)**:
- Percentage of required skills matched
- Average proficiency level of matched skills
- Formula: `(matchPercentage * 70) + (avgProficiency * 3)`

**Distance Score (0-100)**:
- Exponential decay based on distance
- Formula: `100 * exp(-distance / (maxDistance / 3))`

**Salary Score (0-100)**:
- Normalized salary range (₹20,000 - ₹200,000)
- Linear scaling within range

**Experience Score (0-100)**:
- Age-based experience estimation
- Level difference penalty

### Location Search

**BFS Algorithm**:
- Finds all locations within maximum distance
- Time Complexity: O(V + E)
- Used for nearby job discovery

**Dijkstra's Algorithm**:
- Finds shortest path between locations
- Time Complexity: O((V + E) log V)
- Used for precise distance calculation

### Trie Search

**Autocomplete**:
- Prefix-based job title search
- Time Complexity: O(m + k) where m = prefix length, k = matching words
- Case-insensitive matching

## 🧪 Testing

Run comprehensive tests to verify functionality:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=JobRecommendationEngineTest

# Run with detailed output
mvn test -Dtest=JobRecommendationEngineTest#testJobRecommendations
```

### Test Coverage

- ✅ System initialization and data loading
- ✅ Job recommendation algorithms
- ✅ Search functionality (title, skill, location)
- ✅ Personalized recommendations
- ✅ Career path suggestions
- ✅ Data structure efficiency
- ✅ Edge cases and error handling

## 📈 Performance Analysis

### Time Complexity

| Operation | Complexity | Description |
|-----------|------------|-------------|
| Add Job | O(m + s) | m = title length, s = skills count |
| Get Recommendations | O(n log n) | n = number of jobs |
| Search by Title | O(m + k) | m = prefix length, k = matches |
| Search by Skill | O(m + k) | m = skill prefix, k = matches |
| Location Search | O(V + E) | V = vertices, E = edges |

### Space Complexity

| Component | Space | Description |
|-----------|-------|-------------|
| Trie | O(ALPHABET_SIZE * N * L) | N = words, L = avg length |
| Graph | O(V + E) | V = vertices, E = edges |
| Priority Queue | O(n) | n = number of jobs |
| HashMaps | O(n) | n = number of users/jobs |

## 🔧 Customization

### Adding New Skills

```java
// In SampleDataLoader.java
user.addSkill("newSkill", proficiencyLevel);
job.addRequiredSkill("newSkill");
```

### Modifying Scoring Weights

```java
// In JobPriorityQueue.java
return skillScore * 0.4 + distanceScore * 0.3 + salaryScore * 0.2 + experienceScore * 0.1;
```

### Adding New Locations

```java
// In SampleDataLoader.java
locationGraph.addLocation("New Location", latitude, longitude);
locationGraph.addRoad("Location A", "New Location", distance);
```

## 📚 DSA Concepts Demonstrated

### Data Structures
- **Arrays/Lists**: Job and user collections
- **HashMaps**: O(1) lookup for users and jobs
- **Sets**: Skill matching and deduplication
- **Trees**: Trie for string search
- **Graphs**: Location connectivity and pathfinding
- **Heaps**: Priority queue for job ranking

### Algorithms
- **Search**: Trie-based prefix search
- **Sort**: Priority queue sorting
- **Graph Traversal**: BFS and Dijkstra's algorithm
- **Dynamic Programming**: Career path optimization
- **Greedy**: Job scoring and ranking

### Core CS Concepts
- **Time/Space Complexity**: Documented for all operations
- **Modularity**: Clean separation of concerns
- **Code Efficiency**: Optimized algorithms and data structures
- **Scalability**: Designed for large datasets

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Implement your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Data Structures and Algorithms concepts from standard CS curriculum
- Sample data inspired by real rural employment scenarios
- Graph algorithms implementation based on standard textbooks

---

**Built with ❤️ for rural youth empowerment through technology**