# Innie Movie

A modern movie discovery and social review app built with Jetpack Compose for Android.

## 📱 About

Innie Movie is a Letterboxd-inspired mobile application that allows users to discover movies, write reviews, create watchlists, and engage with a community of film enthusiasts.

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Database**: Room Persistence Library
- **Navigation**: Navigation Compose
- **Image Loading**: Coil
- **Architecture**: MVVM with Repository Pattern

## ✨ Features

### Movies & Discovery
- Browse popular and trending movies
- View detailed movie information (poster, backdrop, synopsis, ratings)
- Add movies to personal watchlist

### Reviews & Ratings
- Write and publish movie reviews with 0.5-5 star ratings
- View community reviews from other users
- Like and comment on reviews

### User Profile
- Personal profile with activity stats (Watched, Likes, Albums, Reviews)
- Watch History tracking
- Favorites/Likes collection
- Custom watchlist categories
- View other users' profiles

### Community
- Community feed with recent reviews
- Highlight your own reviews in the feed
- Navigate to reviewer profiles

### Additional Features
- News articles and updates
- Photo albums/galleries
- Shots (movie stills)

## 🚀 Getting Started

### Requirements
- Android Studio (latest version recommended)
- JDK 17+
- Android SDK 34

### Setup

1. Clone the repository
```bash
git clone <repository-url>
```

2. Open project in **Android Studio**

3. Run **Gradle Sync** (File → Sync Project with Gradle Files)

4. Setup Emulator:
   - Open **Device Manager** (Tools → Device Manager)
   - Create new device: **Pixel 7a** with **API 34**
   - Download system image if needed

5. Run the app on emulator or physical device

## 📂 Project Structure

```
app/src/main/java/com/example/myapplication/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAOs
│   │   ├── db/           # Database & Seeder
│   │   └── entities/     # Data models
│   ├── repository/       # Data repositories
│   └── session/          # User session management
├── ui/
│   ├── navigation/       # Navigation routes
│   ├── screens/          # UI screens
│   │   ├── community/    # Community, Reviews
│   │   ├── home/         # Home, News, Albums
│   │   └── profile/      # Profile, Settings, Watchlist
│   └── theme/            # App theming
└── MainActivity.kt
```

## 📄 License

This project is for educational purposes.