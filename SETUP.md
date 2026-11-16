# StyleSync - Detailed Setup Guide

## Table of Contents
1. [System Requirements](#system-requirements)
2. [Installing Java JDK](#installing-java-jdk)
3. [Installing Maven](#installing-maven)
4. [MongoDB Atlas Setup](#mongodb-atlas-setup)
5. [Weather API Setup](#weather-api-setup)
6. [Project Configuration](#project-configuration)
7. [Building the Application](#building-the-application)
8. [Running the Application](#running-the-application)
9. [Troubleshooting](#troubleshooting)

## System Requirements

### Minimum Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux (Ubuntu 18.04+)
- **RAM**: 4 GB
- **Disk Space**: 500 MB free space
- **Display**: 1280x720 minimum resolution
- **Internet**: Required for MongoDB Atlas connection

### Recommended Requirements
- **RAM**: 8 GB or more
- **Disk Space**: 1 GB free space
- **Display**: 1920x1080 or higher

## Installing Java JDK

### Windows

1. **Download JDK**
   - Visit https://www.oracle.com/java/technologies/downloads/
   - Download Java SE 11 or later for Windows

2. **Install JDK**
   - Run the installer
   - Follow installation wizard
   - Note the installation path (e.g., `C:\Program Files\Java\jdk-11`)

3. **Set Environment Variables**
   - Open System Properties → Environment Variables
   - Add new system variable:
     - Variable name: `JAVA_HOME`
     - Variable value: `C:\Program Files\Java\jdk-11`
   - Edit `Path` variable:
     - Add: `%JAVA_HOME%\bin`

4. **Verify Installation**
```cmd
java -version
javac -version
```

### macOS

1. **Using Homebrew**
```bash
brew install openjdk@11
sudo ln -sfn /usr/local/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk
```

2. **Verify Installation**
```bash
java -version
```

### Linux (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install openjdk-11-jdk
java -version
```

## Installing Maven

### Windows

1. **Download Maven**
   - Visit https://maven.apache.org/download.cgi
   - Download Binary zip archive (e.g., apache-maven-3.9.5-bin.zip)

2. **Extract Archive**
   - Extract to `C:\Program Files\Apache\maven`

3. **Set Environment Variables**
   - Add new system variable:
     - Variable name: `MAVEN_HOME`
     - Variable value: `C:\Program Files\Apache\maven`
   - Edit `Path` variable:
     - Add: `%MAVEN_HOME%\bin`

4. **Verify Installation**
```cmd
mvn -version
```

### macOS

```bash
brew install maven
mvn -version
```

### Linux

```bash
sudo apt update
sudo apt install maven
mvn -version
```

## MongoDB Atlas Setup

### Step 1: Create Account

1. Visit https://www.mongodb.com/atlas
2. Click "Try Free"
3. Sign up with email or Google account
4. Verify email address

### Step 2: Create Cluster

1. **Choose Cluster Type**
   - Select "Shared" (Free tier - M0)
   - Choose cloud provider (AWS recommended)
   - Select region closest to you

2. **Cluster Configuration**
   - Cluster Name: `StyleSyncCluster` (or any name)
   - Click "Create Cluster"
   - Wait 3-5 minutes for cluster creation

### Step 3: Create Database User

1. Go to "Database Access" in left sidebar
2. Click "Add New Database User"
3. Choose "Password" authentication
4. Set username and password (save these!)
   - Example: username=`stylesync_user`, password=`StrongPass123`
5. Set "Database User Privileges" to "Read and write to any database"
6. Click "Add User"

### Step 4: Configure Network Access

1. Go to "Network Access" in left sidebar
2. Click "Add IP Address"
3. Choose one option:
   - **For Development**: Click "Allow Access from Anywhere" (0.0.0.0/0)
   - **For Production**: Add your specific IP address
4. Click "Confirm"

### Step 5: Get Connection String

1. Go to "Database" in left sidebar
2. Click "Connect" on your cluster
3. Choose "Connect your application"
4. Select "Java" and version "4.3 or later"
5. Copy connection string:
```
mongodb+srv://stylesync_user:<password>@stylesynccluster.xxxxx.mongodb.net/?retryWrites=true&w=majority
```
6. Replace `<password>` with your actual password

### Step 6: Create Database

1. Click "Browse Collections"
2. Click "Add My Own Data"
3. Database name: `stylesync_db`
4. Collection name: `users`
5. Click "Create"

> **Note**: Other collections (clothing_items, outfits) will be created automatically by the application.

## Weather API Setup (Optional)

1. **Create OpenWeatherMap Account**
   - Visit https://openweathermap.org/api
   - Click "Sign Up"
   - Verify email

2. **Get API Key**
   - Go to https://home.openweathermap.org/api_keys
   - Copy your default API key
   - Or create new key with a name

3. **Test API Key** (Wait 10 minutes after creation)
```
https://api.openweathermap.org/data/2.5/weather?q=London&appid=YOUR_API_KEY
```

> **Note**: If you skip this step, the app will use demo weather data.

## Project Configuration

### Step 1: Clone or Download Project

```bash
git clone https://github.com/yourusername/stylesync.git
cd stylesync
```

Or download ZIP and extract.

### Step 2: Configure Database

Edit `src/main/resources/config.properties`:

```properties
# Replace with your MongoDB Atlas connection string
mongodb.uri=mongodb+srv://stylesync_user:YOUR_PASSWORD@stylesynccluster.xxxxx.mongodb.net/?retryWrites=true&w=majority
mongodb.database=stylesync_db

# Optional: Add your OpenWeatherMap API key
weather.api.key=YOUR_OPENWEATHERMAP_API_KEY
weather.api.url=https://api.openweathermap.org/data/2.5/weather

# Application settings (leave as is)
app.name=StyleSync - Smart Wardrobe Manager
app.version=1.0.0
app.image.directory=wardrobe_images
```

**Important**: 
- Replace `YOUR_PASSWORD` with your MongoDB password
- Replace `YOUR_OPENWEATHERMAP_API_KEY` if using weather API
- Keep the connection string in one line

### Step 3: Create Image Directory

The application will create this automatically, but you can create it manually:

```bash
mkdir wardrobe_images
```

## Building the Application

### Step 1: Navigate to Project Directory

```bash
cd stylesync
```

### Step 2: Build with Maven

```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile source code
- Run tests (if any)
- Package application as JAR

**Expected Output**:
```
[INFO] BUILD SUCCESS
[INFO] Total time: 45 s
[INFO] Finished at: 2024-XX-XX...
```

### Step 3: Verify Build

Check that JAR file was created:
```bash
ls target/stylesync-wardrobe-manager-1.0.0.jar
```

## Running the Application

### Method 1: Using Maven

```bash
mvn exec:java -Dexec.mainClass="com.stylesync.Main"
```

### Method 2: Using JAR File

```bash
java -jar target/stylesync-wardrobe-manager-1.0.0.jar
```

### Method 3: Double-Click JAR (Windows/macOS)

Navigate to `target/` folder and double-click `stylesync-wardrobe-manager-1.0.0.jar`

### First Run

1. **Login Screen Appears**
   - If you see login screen, setup is successful!

2. **Create Account**
   - Click "Sign Up"
   - Enter details
   - Click "Sign Up"

3. **Login**
   - Enter username and password
   - Click "Login"

4. **Start Using Application**
   - Dashboard appears
   - Add your first clothing item!

## Troubleshooting

### Issue 1: MongoDB Connection Failed

**Error**: "Failed to connect to MongoDB"

**Solutions**:
1. Check internet connection
2. Verify MongoDB Atlas credentials
3. Check IP whitelist in Network Access
4. Ensure connection string is correct
5. Test connection string format:
   ```
   mongodb+srv://username:password@cluster.mongodb.net/
   ```

### Issue 2: Maven Build Failed

**Error**: "mvn: command not found"

**Solution**: Maven not installed or not in PATH
- Reinstall Maven
- Add Maven to PATH
- Restart terminal/command prompt

### Issue 3: Java Version Error

**Error**: "UnsupportedClassVersionError"

**Solution**: Java version too old
- Install Java 11 or higher
- Check: `java -version`
- Set JAVA_HOME correctly

### Issue 4: Port Already in Use

**Error**: "Address already in use"

**Solution**: Another application using same port
- Close conflicting application
- Or restart computer

### Issue 5: Image Upload Failed

**Error**: "Failed to save image"

**Solutions**:
1. Check write permissions in project directory
2. Ensure `wardrobe_images` directory exists
3. Check disk space
4. Try smaller image file

### Issue 6: Camera Not Working

**Error**: "No webcam detected"

**Solutions**:
1. Check if camera is connected
2. Grant camera permissions to Java
3. Close other apps using camera
4. Restart application

### Issue 7: Weather API Not Working

**Error**: Weather shows "N/A" or demo data

**Solutions**:
1. Check if API key is correct
2. Wait 10 minutes after creating API key
3. Verify API key is active
4. Check internet connection
5. **Note**: Demo weather data is used if API key is not configured

## Database Collections

The application automatically creates these collections:

1. **users**
   - Stores user accounts
   - Fields: username, email, passwordHash, fullName, createdAt, lastLogin

2. **clothing_items**
   - Stores wardrobe items
   - Fields: userId, name, type, color, seasons, occasions, styles, imagePath, wearCount, etc.

3. **outfits**
   - Stores created outfits
   - Fields: userId, name, clothingItemIds, occasion, season, wearCount, etc.

## Performance Tips

1. **Image Optimization**
   - Use compressed images (< 2MB)
   - App automatically resizes images

2. **Database Optimization**
   - Create indexes in MongoDB (optional):
   ```javascript
   db.clothing_items.createIndex({ userId: 1, addedDate: -1 })
   db.outfits.createIndex({ userId: 1, createdDate: -1 })
   ```

3. **Memory Settings** (for large wardrobes)
   ```bash
   java -Xmx1024m -jar target/stylesync-wardrobe-manager-1.0.0.jar
   ```

## Support

For issues not covered here:
1. Check error logs in console
2. Verify all configuration settings
3. Test MongoDB connection separately
4. Try with fresh database

---

**Ready to start!** If all steps completed successfully, you're ready to manage your wardrobe with StyleSync! 🎉
