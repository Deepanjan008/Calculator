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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
