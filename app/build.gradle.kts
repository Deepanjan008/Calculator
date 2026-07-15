plugins {
    alias(libs.plugins.nexura.application)
    alias(libs.plugins.nexura.application.compose)
    alias(libs.plugins.nexura.hilt)
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("nexura_release.jks")
            storePassword = System.getenv("NEO_STORE_PASSWORD")
            keyAlias = System.getenv("NEO_KEY_ALIAS")
            keyPassword = System.getenv("NEO_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))

    implementation(project(":feature:calculator"))
    implementation(project(":feature:converter"))
    implementation(project(":feature:finance"))
    implementation(project(":feature:tools"))
    implementation(project(":feature:history"))
    implementation(project(":feature:settings"))

    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    implementation(libs.findLibrary("androidx-activity-compose").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())

    testImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("androidx-test-ext-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-test-espresso-core").get())
    androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4").get())
}
