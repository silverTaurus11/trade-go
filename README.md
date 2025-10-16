# 📊 Trader Go — Crypto Asset Tracker

**Trader Go** is a modern Android app for tracking real-time cryptocurrency prices and performance.
Built with **Jetpack Compose**, **Hilt**, **Room**, and **Paging 3**, it offers a dynamic watchlist, historical charts, and smooth offline caching powered by the **CoinCap API**.

---

## ✨ Features

✅ **Real-time Crypto Prices** – Stream live asset prices from WebSocket.
✅ **Watchlist Management** – Add or remove favorite assets with instant updates.
✅ **Paging 3 + Room** – Efficient local caching and infinite scrolling.
✅ **MVVM + Clean Architecture** – Well-structured, testable codebase.
✅ **Jetpack Compose UI** – 100% declarative Material 3 design.
✅ **Hilt DI** – Scalable dependency injection.
✅ **Chucker** – Inspect and debug API requests easily.
✅ **MPAndroidChart** – Beautiful interactive charts.

---

## ✨ Preview
<img width="379" height="783" alt="image" src="https://github.com/user-attachments/assets/f581807c-e175-45ed-a58a-13611c2b0dfa" />
<img width="369" height="814" alt="Screenshot 2025-10-16 at 11 57 49" src="https://github.com/user-attachments/assets/cc2f3837-6386-4ff6-b5e1-d8ea733fa77d" />
<img width="369" height="814" alt="Screenshot 2025-10-16 at 11 57 57" src="https://github.com/user-attachments/assets/67a49629-ee00-444f-8be5-e740a96c99a3" />
<img width="366" height="810" alt="Screenshot 2025-10-16 at 11 58 03" src="https://github.com/user-attachments/assets/ff1da9d6-6ebf-4f59-b2d3-c7a966d0ae11" />
<img width="367" height="812" alt="Screenshot 2025-10-16 at 11 58 36" src="https://github.com/user-attachments/assets/c382662c-dfa1-498d-b316-531a252c9970" />
---

## 🧱 Tech Stack

| Layer         | Libraries / Tools                               |
| ------------- | ----------------------------------------------- |
| **UI**        | Jetpack Compose, Material 3, Navigation Compose |
| **DI**        | Hilt                                            |
| **Data**      | Room, Paging 3, Retrofit, OkHttp, Gson          |
| **Debugging** | Chucker                                         |
| **Testing**   | JUnit, MockK, Turbine, Coroutines Test          |
| **Charting**  | MPAndroidChart                                  |

---

## 🛠️ Project Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/silverTaurus11/trade-go.git
cd trader-go
```

### 2️⃣ Add Your API Key

Create a file named `local.properties` in the project root (next to `build.gradle.kts`):

```properties
COINCAP_API_KEY=your_api_key_here
```

> If this file doesn’t exist, the app will still build but won’t display live data from CoinCap API.

---

## ⚙️ Build Configuration

The API key is automatically read from `local.properties` and injected via `buildConfigField`:

```kotlin
val apiKey = localProps.getProperty("COINCAP_API_KEY", "")
buildConfigField("String", "COINCAP_API_KEY", "\"$apiKey\"")
```

You can access it anywhere in the code using:

```kotlin
BuildConfig.COINCAP_API_KEY
```

---

## 📦 Dependencies Overview

### ✳️ Core

* Jetpack Compose
* Material 3
* Navigation Compose
* Lifecycle Runtime

### 🔩 Data Layer

* **Retrofit** + **Gson** for API communication
* **Room** for local persistence
* **Paging 3** for efficient data loading
* **Coroutines** for concurrency
* **Hilt** for dependency injection

### 🧰 Debugging Tools

* **Chucker** (`debugImplementation` and `no-op` for release)
* **MPAndroidChart** for displaying price charts

### 🧪 Testing

* JUnit 4
* MockK
* Turbine (Flow testing)
* kotlinx-coroutines-test

---

## 🚀 How to Run

### 🧩 Via Android Studio

1. Open the project in Android Studio Ladybug or later.
2. Ensure `local.properties` contains your API key.
3. Run on an emulator or physical device (API 24+).

### 💻 Via Command Line

```bash
./gradlew assembleDebug
```

---

## 🧪 Running Unit Tests

Run all unit tests with:

```bash
./gradlew testDebugUnitTest
```

Example test:

```
data/repositoryimpl/AssetRepositoryImplTest.kt
```

---

## 🧩 Example Code

### 🛡️ Request Notification Permission (Android 13+)

Compose utility for notification permissions:

```kotlin
NotificationPermissionRequester(
    onGranted = { /* Notifications enabled */ },
    onDenied = { /* Show explanation */ }
)
```

### ⭐ Toggle Watchlist

```kotlin
fun toggleWatch(assetId: String) = viewModelScope.launch {
    toggleWatchlistUseCase(assetId)
    _watchlistIds.update { ids ->
        if (assetId in ids) ids - assetId else ids + assetId
    }
}
```

---

## 🧰 Development Tools

| Tool                 | Version                                |
| -------------------- | -------------------------------------- |
| **Android Studio**   | Ladybug / Koala                        |
| **Gradle**           | 8.x                                    |
| **Kotlin**           | 2.2.20                                 |
| **Compose Compiler** | `enableStrongSkippingMode` (optimized) |
| **Room**             | 2.8.1                                  |
| **Hilt**             | 2.57+                                  |

---

## 🧠 Notes

* `enableStrongSkippingMode` is deprecated in Kotlin 2.2+; future versions will use `featureFlags`.
* `COINCAP_API_KEY` is loaded locally and **excluded from Git commits**.
* Chucker is automatically disabled in release builds.
* Tested with Android API 24+.

---

## 📜 License

```
Copyright (c) 2025 Gayuh Nurul Huda

Licensed under the MIT License.
See the LICENSE file for more details.
```
