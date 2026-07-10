<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" width="40" align="center"> N Calculator

<p align="left">
<img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License">
  <img src="https://img.shields.io/badge/Version-1.0.3-orange.svg" alt="Version">
  <img src="https://img.shields.io/badge/UI-Glassmorphism-pink.svg" alt="Design">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF.svg" alt="Language">
</p>

Neo Calculator is more than just a calculator — it’s a complete offline utility toolkit built with a strong focus on privacy, performance, and modern design.

No internet. No ads. No tracking.
Just fast, reliable tools that work entirely on your device.

---

## 📱 App Gallery

<div align="center">
  <table>
    <tr>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" width="160"></td>
    </tr>
  </table>
</div>

---

## ✨ Features

### 🧮 Advanced Calculator
- High precision arithmetic engine
- Massive factorial support (up to 10,000!)
- Scientific notation & large number handling
- Smart history tracking

### 🛠️ Utility Tools

**💰 Finance**
- Discount & Tax Calculator
- Investment & SIP Calculator
- EMI Calculator
- Fuel Cost Calculator
- Unit Price Comparison

**🌍 Converters**
- Unit Converter (Length, Weight, Temp, Speed, Time, Data, etc.)
- Land Converter (Bigha, Acre, Sq.ft, Hectare)
- Currency Converter (offline with saved rates)

**❤️ Personal Tools**
- BMI Calculator
- Age Calculator
- GPA Calculator

---

## 🎨 Design
- Modern Glassmorphism UI
- Material Design 3 + Adaptive Layouts
- Smooth animations & transitions
- Full Dark Mode support

---

## 🔒 Privacy First
- 100% Open Source (MIT)
- No Internet Permission
- No Ads
- No Data Collection
- Fully Offline

---

## 🛠️ Tech Stack
- **Language:** Kotlin (2.3.10+)
- **UI:** Jetpack Compose (1.10.4+) + Material 3 Adaptive Layouts
- **Architecture:** Modular + Clean Architecture (:app, :core, :features)
- **Build System:** Gradle (9.3.1+) & AGP (9.0.1+)

---

## 📥 Installation & Build Guide

You can build this project on a PC or directly on your Android device.

### 🖥️ 1. Build on PC (Windows / macOS / Linux)

| OS | Requirements | Steps to Build |
|---|---|---|
| **Windows** | Android Studio, Git, JDK 17+ | `git clone https://github.com/Deepanjan008/Calculator.git` <br> `cd Calculator` <br> Open in Android Studio > Sync Gradle > Press Shift+F10 to Run. |
| **macOS** | Android Studio, Git, JDK 17+ | `git clone https://github.com/Deepanjan008/Calculator.git` <br> `cd Calculator` <br> Open in Android Studio > Sync Gradle > Press Control+R to Run. |
| **Linux** | Android Studio, Git, JDK 17+ | `git clone https://github.com/Deepanjan008/Calculator.git` <br> `cd Calculator` <br> Open in Android Studio > Sync Gradle > Press Shift+F10 to Run. |

### 📱 2. Build on Android (via Termux)

You can compile the app entirely on your phone without needing Android Studio.

**Step 1: Install Dependencies**
```bash
pkg update && pkg upgrade
pkg install git openjdk-17

```
**Step 2: Clone the Repository & Enter Folder**
```bash
cd Calculator
git clone https://github.com/Deepanjan008/Calculator.git
```
**Step 3: Setup Android SDK**
*(Ensure Android command-line tools are downloaded and $ANDROID_HOME is properly configured in your Termux environment).*
**Step 4: Build the APK**
```bash
chmod +x gradlew
./gradlew assembleDebug

```
*Once the build is successful, find the generated APK inside app/build/outputs/apk/debug/ and install it.*
## ⚠️ Important Note for Release Build
If you attempt to generate a release build using ./gradlew assembleRelease, the build will **fail**.
This project uses secure environment variables for signing the release APK. The Keystore file (nexura_release.jks) and the following variables are strictly excluded from this repository for security reasons:
 * NEO_STORE_PASSWORD
 * NEO_KEY_ALIAS
 * NEO_KEY_PASSWORD
### 🔑 How to Generate Your Own Keystore for Release
If you want to build a release APK, you must generate your own Keystore file. Run the following command in your terminal (inside the app directory):
```bash
keytool -genkey -v -keystore your_own_release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias your_alias_name

```
During generation, you will be prompted to enter the following details:
 * **Enter keystore password:** Type a strong password (e.g., MySecurePass123). *Note: It won't show on the screen as you type.*
 * **Re-enter new password:** Type the same password again.
 * **What is your first and last name?** Enter your name or developer name.
 * **What is the name of your organizational unit?** E.g., Development.
 * **What is the name of your organization?** E.g., Nexura Labs.
 * **What is the name of your City or Locality?** E.g., Kolkata.
 * **What is the name of your State or Province?** E.g., West Bengal.
 * **What is the two-letter country code for this unit?** E.g., IN (for India).
 * **Is CN=..., OU=..., O=..., L=..., ST=..., C=IN correct?** Type yes and press Enter.
**Update app/build.gradle.kts:**
After generating your_own_release.jks, replace the signingConfigs in app/build.gradle.kts with your new keystore details, or set the environment variables locally on your machine.
## 🤝 Contributing
Contributions are welcome!
Please open an issue first to discuss changes before submitting a PR.
## 📄 License
Licensed under the MIT License
<p align="center">
<strong>Built with ❤️ for privacy-conscious users</strong>
</p>
```

```
