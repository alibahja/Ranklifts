# RankLifts - Strength Ranking System
RankLifts is a comprehensive strength training application that analyzes user performance metrics and generates dynamic rankings based on relative strength, exercise classification, and customizable priority settings.

# Features
Core Functionality
User Authentication - Secure registration and login with JWT token-based authentication

Profile Management - Track bodyweight for relative strength calculations

Exercise Library - Extensive database of compound, calisthenics, and isolation exercises

Performance Tracking - Log sets, reps, and weight for each exercise

Dynamic Rankings - Real-time strength ranking system with multiple tiers

Priority System - Customize exercise category importance for personalized scoring

Ranking System
Individual Exercise Ranks - Bronze I through Elite I based on relative strength

Movement Category Ranks - Separate rankings for Push, Pull, and Legs movements

Overall Strength Rank - Aggregated score combining all movement categories

Automatic Recalculation - Rankings update instantly when data changes

Technical Features
Offline Data Storage - Local caching with SharedPreferences

Batch Updates - Efficient bulk exercise submission

Priority Persistence - User preferences saved for consistent recalculations

Real-time Synchronization - Automatic server sync when online

# Tech Stack
Frontend (Android)
Language: Java

Minimum SDK: API 24 (Android 7.0)

Target SDK: API 36

Architecture: MVVM with LiveData and ViewModel

Networking: Retrofit 2 + OkHttp3

Authentication: JWT (JSON Web Tokens)

Storage: Encrypted SharedPreferences

UI Components: Material Design, RecyclerView, CardView

Backend (PHP)
Framework: Vanilla PHP 8.2+

Server: Apache (XAMPP compatible)

Database: MySQL

Authentication: JWT with HS256

Password Hashing: BCrypt

API Style: RESTful

# Prerequisites
Android Development
Android Studio Hedgehog | 2023.1.1 or later

JDK 11 or later

Android SDK with API 36

Backend Requirements
XAMPP / WAMP / LAMP stack

PHP 8.2 or later

MySQL 5.7 or later

Composer (for JWT library)

# Installation Guide
1. Backend Setup
bash
# Clone or download the backend files to XAMPP htdocs folder
C:\xampp\htdocs\ranklifts-api\

# Install dependencies via Composer
cd C:\xampp\htdocs\ranklifts-api
composer require firebase/php-jwt

# Import database
# Open phpMyAdmin -> Create database 'ranklifts_db' -> Import database.sql
Configure Database Connection:

Update config/database.php with your MySQL credentials

Default: username root, password empty (for XAMPP)

2. Android Setup
bash
# Clone repository
git clone https://github.com/alibahja/ranklifts-android.git

# Open in Android Studio
File -> Open -> Select project folder

# Sync Gradle dependencies
File -> Sync Project with Gradle Files

# Build and run
Run -> Run 'app'
Update API Base URL (if needed):

In api/ApiClient.java, update BASE_URL constant

Default emulator: http://10.0.2.2/ranklifts-api/

Physical device: http://[your-ip-address]/ranklifts-api/

# Usage Guide
First Time Setup
Register Account - Create new user account

Set Bodyweight - Enter your current bodyweight for accurate calculations

Add Exercises - Select exercises for Push, Pull, and Legs modules

Set Priorities - Configure exercise category importance (weights: 45%, 25%, 15%, 10%)

Review Summary - Confirm all entries before final submission

Start Training - Begin logging workouts and watch your rank grow!

Daily Use
Log Exercises - Navigate to Manage Exercises to add/edit performance

View Rankings - Home screen displays overall and category ranks

Track Progress - Rankings update automatically after each submission

Edit Profile - Update bodyweight to maintain accurate relative strength

Refresh Data - Pull down or use refresh button to sync latest rankings

# Ranking Tiers
```
| Tier | Compound | Pure Isolation | Primary Isolation | Overall |
|------|----------|----------------|-------------------|---------|
| Bronze I | < 0.6 | < 0.8 | < 0.9 | < 0.7 |
| Silver III | 0.6-0.7 | 0.8-0.95 | 0.9-1.0 | 0.7-0.85 |
| Silver II | 0.7-0.8 | 0.95-1.1 | 1.0-1.1 | 0.85-1.0 |
| Silver I | 0.8-0.9 | 1.1-1.25 | 1.1-1.2 | 1.0-1.15 |
| Gold III | 0.9-1.0 | 1.25-1.4 | 1.2-1.35 | 1.15-1.3 |
| Gold II | 1.0-1.1 | 1.4-1.55 | 1.35-1.5 | 1.3-1.45 |
| Gold I | 1.1-1.2 | 1.55-1.7 | 1.5-1.65 | 1.45-1.6 |
| Platinum III | 1.2-1.3 | 1.7-1.9 | 1.65-1.8 | 1.6-1.75 |
| Platinum II | 1.3-1.4 | 1.9-2.1 | 1.8-2.0 | 1.75-1.9 |
| Platinum I | 1.4-1.5 | 2.1-2.3 | 2.0-2.2 | 1.9-2.05 |
| Diamond III | 1.5-1.6 | 2.3-2.5 | 2.2-2.35 | 2.05-2.25 |
| Diamond II | 1.6-1.7 | 2.5-2.7 | 2.35-2.5 | 2.25-2.45 |
| Diamond I | 1.7-1.8 | 2.7-2.9 | 2.5-2.6 | 2.45-2.7 |
| Elite III | 1.8-1.9 | 2.9-3.1 | 2.6-2.75 | 2.7-3.0 |
| Elite II | 1.9-2.0 | 3.1-3.3 | 2.75-3.0 | 3.0-3.4 |
| Elite I | 2.0+ | 3.3+ | 3.0+ | 3.4+ |
```
### Project Structure
```
Android
text
app/src/main/java/com/example/ranklifts_java/
├── activities/          # All UI activities
├── adapters/            # RecyclerView adapters
├── api/                 # Retrofit API client and interfaces
├── models/              # Data models
├── repository/          # Token and exercise repositories
├── viewmodel/           # MVVM ViewModels
└── utils/               # Utility classes (optional)
Backend
text
ranklifts-api/
├── config/              # Database and JWT configuration
├── controllers/         # API endpoint handlers
├── models/              # Database model classes
├── repositories/        # Database operations
├── services/            # Business logic
├── middleware/          # Authentication middleware
├── index.php            # Router entry point
└── .htaccess            # Apache rewrite rules
```
# Security Features
Password Encryption - BCrypt hashing for all user passwords

JWT Authentication - Secure token-based API authentication (7-day expiry)

Encrypted Storage - SharedPreferences encrypted with Android Keystore

SQL Injection Protection - Prepared statements in all PHP database queries

CORS Configuration - Proper cross-origin resource sharing headers

# Known Issues & Limitations
Emulator Network - Use 10.0.2.2 for localhost; physical devices require actual IP address

First-time Load - Initial sync may take 2-3 seconds depending on network speed

Offline Mode - Currently requires internet for initial submission; offline caching planned for future

# Roadmap
Workout history calendar view

Social leaderboards

Training program recommendations

Export data to CSV/PDF

Wear OS companion app

Graph visualizations for progress tracking

# Contributing
Fork the repository

Create feature branch (git checkout -b feature/AmazingFeature)

Commit changes (git commit -m 'Add AmazingFeature')

Push to branch (git push origin feature/AmazingFeature)

Open Pull Request

