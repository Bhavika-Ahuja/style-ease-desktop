# StyleSync - Smart Wardrobe Manager

**Your digital closet, organized and intelligent**

## 🌟 Overview

StyleSync is a comprehensive Java desktop application for managing your wardrobe digitally. Built with Java Swing and MongoDB Atlas, it provides an elegant interface to catalog clothing items, create outfits, get smart recommendations based on weather and occasion, and analyze your wardrobe usage patterns.

## ✨ Features

### Core Features
- ✅ **User Authentication** - Secure registration/login with BCrypt password hashing
- ✅ **Digital Wardrobe** - Manage clothing items with photo uploads (file/camera/URL)
- ✅ **Categorization** - Organize by type, color, season, occasion, and style
- ✅ **Search & Filters** - Advanced filtering and multiple sorting options
- ✅ **Outfit Creator** - Combine items into complete outfits
- ✅ **Smart Recommendations** - Weather and occasion-based outfit suggestions
- ✅ **Wear History** - Track usage patterns and statistics
- ✅ **Analytics Dashboard** - Visual charts and wardrobe insights
- ✅ **Favorites** - Mark favorite items and outfits
- ✅ **Modern UI** - Beautiful purple/pink gradient theme

## 🛠 Technology Stack

- **Java 11+** - Core application
- **Java Swing** - Desktop GUI
- **MongoDB Atlas** - Cloud database
- **Maven** - Build automation
- **BCrypt** - Password security
- **OkHttp** - HTTP client
- **Webcam Capture** - Camera integration
- **Gson** - JSON processing

## 🏗 Architecture

Clean **MVC Architecture** with separate layers:
- **Model** - Domain objects (User, ClothingItem, Outfit, WardrobeStats)
- **View** - Swing UI components and dialogs
- **Controller** - Business logic controllers
- **Service** - Application services
- **DAO** - Data access layer
- **Util** - Helper classes

## 📚 Collections Framework Usage

Meaningful implementation of Java Collections Framework:

- **ArrayList** - Dynamic lists for items and outfits
- **HashMap** - Category distributions and analytics
- **HashSet** - Unique seasons, occasions, styles (no duplicates)
- **TreeSet** - Automatically sorted items by name
- **PriorityQueue** - Most/least worn items ranking
- **Comparator** - Custom sorting (name, date, wear count)

## 🚀 Installation

### Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.6+
- MongoDB Atlas account (free tier)

### Steps

1. **Clone Repository**
```bash
git clone https://github.com/yourusername/stylesync.git
cd stylesync
```

2. **Configure MongoDB**
   - Create MongoDB Atlas account at https://www.mongodb.com/atlas
   - Create a new cluster (free tier)
   - Get connection string
   - Whitelist your IP address

3. **Update Configuration**

Edit `src/main/resources/config.properties`:
```properties
mongodb.uri=mongodb+srv://username:password@cluster.mongodb.net/?retryWrites=true&w=majority
mongodb.database=stylesync_db
weather.api.key=YOUR_API_KEY (optional)
```

4. **Build Project**
```bash
mvn clean install
```

5. **Run Application**
```bash
mvn exec:java -Dexec.mainClass="com.stylesync.Main"
```

Or run JAR:
```bash
java -jar target/stylesync-wardrobe-manager-1.0.0.jar
```

## 📖 Usage Guide

### 1. Register & Login
- Launch application
- Click "Sign Up" to create account
- Fill in details (username, email, password)
- Login with credentials

### 2. Add Clothing Items
- Navigate to "My Wardrobe"
- Click "+ Add Clothing"
- Choose upload method:
  - 📁 From File
  - 📷 From Camera
  - 🔗 From URL
- Fill in details and categories
- Save item

### 3. Create Outfits
- Navigate to "Outfit Planner"
- Click "+ Create Outfit"
- Select items from wardrobe
- Set occasion and season
- Save outfit

### 4. Get Recommendations
- Navigate to "Smart Recommendations"
- Enter city name
- Select occasion
- Click "Get Recommendations"
- Save suggested outfits

### 5. View Analytics
- Navigate to "Wear History"
- See statistics and distributions
- View most/least worn items
- Analyze wardrobe patterns

## 📁 Project Structure

```
stylesync/
├── src/main/java/com/stylesync/
│   ├── Main.java
│   ├── model/
│   │   ├── User.java
│   │   ├── ClothingItem.java
│   │   ├── Outfit.java
│   │   └── WardrobeStats.java
│   ├── view/
│   │   ├── LoginView.java
│   │   ├── RegisterView.java
│   │   ├── DashboardView.java
│   │   ├── WardrobeView.java
│   │   ├── OutfitView.java
│   │   ├── RecommendationView.java
│   │   ├── AnalyticsView.java
│   │   ├── StyleSyncColors.java
│   │   ├── components/
│   │   │   ├── ModernButton.java
│   │   │   ├── ModernTextField.java
│   │   │   ├── ModernPasswordField.java
│   │   │   └── RoundedPanel.java
│   │   └── dialog/
│   │       ├── AddItemDialog.java
│   │       ├── ItemDetailDialog.java
│   │       └── CreateOutfitDialog.java
│   ├── controller/
│   │   ├── LoginController.java
│   │   ├── RegisterController.java
│   │   ├── DashboardController.java
│   │   ├── WardrobeController.java
│   │   └── OutfitController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── WardrobeService.java
│   │   └── OutfitService.java
│   ├── dao/
│   │   ├── UserDAO.java
│   │   ├── ClothingItemDAO.java
│   │   └── OutfitDAO.java
│   └── util/
│       ├── DatabaseConnection.java
│       ├── ConfigUtil.java
│       ├── PasswordUtil.java
│       ├── ImageUtil.java
│       └── WeatherService.java
├── src/main/resources/
│   └── config.properties
├── pom.xml
└── README.md
```

## 🎨 UI Design

Modern purple/pink gradient theme inspired by fashion apps:
- Clean card-based layouts
- Smooth navigation sidebar
- Grid view for items and outfits
- Image previews and thumbnails
- Responsive dialogs
- Beautiful gradient backgrounds

## 🔐 Security Features

- BCrypt password hashing
- Secure session management
- MongoDB connection encryption
- Input validation
- Protected user data

## 📊 Collections Framework Implementation

### ArrayList Usage
```java
// Dynamic lists for wardrobe items
private ArrayList<ClothingItem> displayedItems;
public ArrayList<ClothingItem> getAllItems() {
    return new ArrayList<>(wardrobeService.getAllClothingItems(currentUser));
}
```

### HashMap Usage
```java
// Category distribution analysis
private HashMap<String, Integer> typeDistribution;
for (ClothingItem item : items) {
    typeDistribution.put(item.getType(), 
        typeDistribution.getOrDefault(item.getType(), 0) + 1);
}
```

### HashSet Usage
```java
// Unique categories (no duplicates)
private Set<String> seasons = new HashSet<>();
private Set<String> occasions = new HashSet<>();
private HashSet<ClothingItem> selectedItems = new HashSet<>();
```

### TreeSet Usage
```java
// Automatically sorted items
private TreeSet<ClothingItem> sortedItemsByName = new TreeSet<>(
    Comparator.comparing(ClothingItem::getName));
```

### PriorityQueue Usage
```java
// Most/least worn ranking
private PriorityQueue<ClothingItem> mostWornQueue = new PriorityQueue<>(
    Comparator.comparing(ClothingItem::getWearCount).reversed());
```

### Comparator Usage
```java
// Custom sorting
Comparator<ClothingItem> comparator = Comparator.comparing(ClothingItem::getName);
Collections.sort(sortedList, comparator);
```

## 🐛 Troubleshooting

### MongoDB Connection Issues
- Check internet connection
- Verify MongoDB Atlas credentials
- Whitelist your IP in MongoDB Atlas
- Check connection string format

### Image Upload Issues
- Ensure wardrobe_images directory exists
- Check file permissions
- Verify image format (jpg, png, gif)

### Camera Not Working
- Grant camera permissions
- Check if webcam is connected
- Verify no other app is using camera

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Created with ❤️ for smart wardrobe management

## 🙏 Acknowledgments

- Java Swing for desktop UI framework
- MongoDB Atlas for cloud database
- BCrypt for security
- OpenWeatherMap for weather API
- Webcam Capture library

---

**StyleSync** - Look Good, Feel Great, Live Better! 💜
