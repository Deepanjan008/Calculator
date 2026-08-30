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

    // Per-architecture release APKs instead of one universal fat APK.
    // Set isUniversalApk = true below if you also want a single all-in-one
    // APK generated alongside the per-ABI ones (bigger file, works everywhere).
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = false
        }
    }
}

// Gives each ABI-specific APK a distinct versionCode (Google's recommended
// scheme) so device package managers treat them as proper per-arch builds
// instead of colliding on the same versionCode.
val abiVersionCodes = mapOf(
    "armeabi-v7a" to 1,
    "arm64-v8a" to 2,
    "x86" to 3,
    "x86_64" to 4,
)

androidComponents {
    onVariants(selector().withBuildType("release")) { variant ->
        variant.outputs.forEach { output ->
            val abiFilter = output.filters.find {
                it.filterType == com.android.build.api.variant.FilterConfiguration.FilterType.ABI
            }?.identifier
            val abiOffset = abiVersionCodes[abiFilter] ?: 0
            output.versionCode.set((output.versionCode.orNull ?: 0) * 10 + abiOffset)
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
