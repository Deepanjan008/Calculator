plugins {
    alias(libs.plugins.nexura.library.compose)
}

dependencies {
    implementation(project(":core:navigation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)
}
