plugins {
    alias(libs.plugins.nexura.library.compose)
    alias(libs.plugins.nexura.hilt)
}

dependencies {
    // The screen talks to contracts and use cases. It never names a repository,
    // so :core:data is deliberately absent from this list - if a screen needs
    // something new, it needs a use case, not a dependency.
    implementation(project(":core:domain"))
    implementation(project(":core:presentation"))
    implementation(project(":core:ui"))
    implementation(project(":core:navigation"))

    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("hilt-navigation-compose").get())
    implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.core)
}
