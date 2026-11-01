plugins {
    alias(libs.plugins.nexura.library)
    alias(libs.plugins.nexura.hilt)
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))

    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-datastore-preferences").get())
}
