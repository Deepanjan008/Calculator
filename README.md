<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.png" width="40" align="center"> N Calculator

<p align="left">
  <img src="https://img.shields.io/badge/License-GPL--3.0-blue.svg" alt="License">
  <img src="https://img.shields.io/badge/Version-1.0.0-orange.svg" alt="Version">
  <img src="https://img.shields.io/badge/UI-Glassmorphism-pink.svg" alt="Design">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF.svg" alt="Language">
</p>Neo Calculator is more than just a calculator — it’s a complete offline utility toolkit built with a strong focus on privacy, performance, and modern design.

No internet. No ads. No tracking.
Just fast, reliable tools that work entirely on your device.

---

📱 App Gallery

<div align="center">
  <table>
    <tr>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/2.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/3.png" width="160"></td>
      <td><img src="fastlane/metadata/android/en-US/images/phoneScreenshots/4.png" width="160"></td>
    </tr>
  </table>
</div>---

✨ Features

🧮 Advanced Calculator

- High precision arithmetic engine
- Massive factorial support (up to 10,000!)
- Scientific notation & large number handling
- Smart history tracking

🛠️ Utility Tools

💰 Finance

- Discount & Tax Calculator
- Investment & SIP Calculator
- EMI Calculator
- Fuel Cost Calculator
- Unit Price Comparison

🌍 Converters

- Unit Converter (Length, Weight, Temp, Speed, Time, Data, etc.)
- Land Converter (Bigha, Acre, Sq.ft, Hectare)
- Currency Converter (offline with saved rates)

❤️ Personal Tools

- BMI Calculator
- Age Calculator
- GPA Calculator

---

🎨 Design

- Modern Glassmorphism UI
- Material Design 3
- Smooth animations & transitions
- Full Dark Mode support

---

🔒 Privacy First

- 100% Open Source (MIT)
- No Internet Permission
- No Ads
- No Data Collection
- Fully Offline

---

🛠️ Tech Stack

- Language: Kotlin
- UI: Jetpack Compose + Material 3
- Architecture: MVVM

---

📥 Installation

git clone https://github.com/Deepanjan008/Calculator.git
cd Calculator

Open in Android Studio, sync Gradle, and run on your device or emulator.

---

🤝 Contributing

Contributions are welcome!
Please open an issue first to discuss changes before submitting a PR.

---

📄 License

Licensed under the MIT License

---

<p align="center">
  <strong>Built with ❤️ for privacy-conscious users</strong>
</p>

---

🏗️ Architecture (v2.0.0)

Restructured into multi-module Clean Architecture, following the same `build-logic` convention-plugin
pattern as AlphaVisionPro:

```
Calculator/
├── build-logic/convention/     # Precompiled convention plugins (application, library, compose, hilt)
├── app/                        # Thin shell: App, MainActivity, NavGraph aggregator
├── core/
│   ├── data/                   # Room DB, DataStore, HistoryRepository, PreferencesRepository, Hilt DI
│   ├── ui/                     # Shared AppTheme + Typography
│   └── navigation/             # Shared route constants (NexuraRoutes) + cross-feature result passing
└── feature/
    ├── calculator/
    ├── converter/               # Unit / Land / Currency / Unit-price converters
    ├── finance/                 # EMI, Investment, Discount/Tax, Fuel cost
    ├── tools/                   # BMI, Age, GPA, Factorial, Equation solver
    ├── history/
    └── settings/
```

Feature modules never depend on each other directly - only on `core:navigation`'s route
constants - so `:app` is the only module that knows every feature exists.
