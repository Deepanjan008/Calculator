plugins {
    alias(libs.plugins.nexura.library.compose)
    alias(libs.plugins.nexura.hilt)
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))

    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("hilt-navigation-compose").get())
    implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())
}
