<div align="center">

# 🏎️ F1 Hub

**A Formula 1 companion app for Android: race calendar, live standings, driver profiles and your favourite teams, all in one place.**

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Java-E10600?logo=openjdk&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-24-0A0E1A)
![Target SDK](https://img.shields.io/badge/targetSdk-35-0A0E1A)
![Firebase](https://img.shields.io/badge/backend-Firebase-FFCA28?logo=firebase&logoColor=black)

*University coursework for CMP6213 Mobile and Wearable Application Development (2025–26).*

</div>

---

## ✨ Features

| | |
|---|---|
| 🏠 **Dashboard** | Countdown to the next Grand Prix, plus the current championship leader and top constructor. |
| 📅 **Race calendar** | The full season schedule. Tap a race to see its location, date and finishing order. |
| 🏆 **Standings** | Driver and constructor championship tables, with a detail view for each team. |
| 👤 **Drivers** | The current grid with official headshots, driver number, nationality and date of birth. |
| ⭐ **Favourites** | Bookmark drivers and teams. They're stored per user in Cloud Firestore, so they sync across devices. |
| 🔔 **Race reminders** | A notification the day before each race weekend so you don't miss lights out. |
| 🔐 **Accounts** | Email and password sign-up and login, password reset, sign out and account deletion (with re-authentication). |

## 🧱 Tech stack

- **Language:** Java 11
- **UI:** Android Views and XML layouts with [Material Components](https://github.com/material-components/material-components-android), in a custom dark F1 theme
- **Architecture:** MVVM using `ViewModel`, `LiveData` and repository classes
- **Navigation:** single `MainActivity` with a `BottomNavigationView` and fragments
- **Networking:** Retrofit, Gson and OkHttp
- **Images:** Glide
- **Backend:** Firebase Authentication and Cloud Firestore
- **Notifications:** `AlarmManager`, a `BroadcastReceiver` and notification channels

## 🌐 Data sources

| API | Used for |
|---|---|
| [Jolpica F1 API](https://github.com/jolpica/jolpica-f1) (Ergast-compatible) | Schedule, results, driver and constructor standings |
| [OpenF1](https://openf1.org/) | Driver headshots and team details |

Neither API needs a key.

## 📂 Project structure

```
app/src/main/java/com/example/f1hub/
├── SplashActivity.java        # Landing screen; skips to the app if already signed in
├── AuthActivity.java          # Login / register / forgot password
├── MainActivity.java          # Bottom-nav shell hosting the fragments
├── NotificationHelper.java    # Channel setup and race-reminder scheduling
├── RaceAlarmReceiver.java     # Fires the reminder notification
├── data/
│   ├── api/                   # Retrofit interfaces and client
│   ├── model/                 # Jolpica and OpenF1 response models
│   ├── db/                    # Favourite entity
│   └── repository/            # F1Repository (network) and FavouritesRepository (Firestore)
└── ui/
    ├── home/                  # Dashboard
    ├── races/                 # Calendar and race detail
    ├── standings/             # Championship tables and constructor detail
    ├── drivers/               # Driver grid and driver detail
    ├── favourites/            # Saved drivers and teams
    └── settings/              # Account management
```

## 🚀 Getting started

### Prerequisites

- Android Studio (recent stable release)
- JDK 11 or newer (the JBR bundled with Android Studio works)
- An Android device or emulator running Android 7.0 (API 24) or newer

### Build and run

```bash
git clone https://github.com/mpettitt72/f1hub.git
cd f1hub
./gradlew assembleDebug
```

Or open the project in Android Studio and press **Run ▶**.

### Using your own Firebase project

The repo includes a `google-services.json` for the original Firebase project. To point the app at your own:

1. Create a project in the [Firebase console](https://console.firebase.google.com/) and add an Android app with the package name `com.example.f1hub`.
2. Enable **Email/Password** sign-in under *Authentication*.
3. Create a **Cloud Firestore** database.
4. Download your `google-services.json` and replace `app/google-services.json`.

Favourites are stored at `users/{uid}/favourites/{id}`. Recommended security rules:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## 📝 Disclaimer

This is an unofficial student project made for educational purposes. It is not associated with Formula 1 companies. F1, FORMULA ONE, FORMULA 1 and related marks are trademarks of Formula One Licensing B.V. Race data comes from the community-run Jolpica and OpenF1 APIs.
