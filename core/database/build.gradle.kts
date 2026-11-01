plugins {
    alias(libs.plugins.nexura.library)
    alias(libs.plugins.nexura.hilt)
}

dependencies {
    implementation(libs.findLibrary("androidx-core-ktx").get())

    implementation(libs.findLibrary("androidx-room-runtime").get())
    implementation(libs.findLibrary("androidx-room-ktx").get())
    ksp(libs.findLibrary("androidx-room-compiler").get())
}
