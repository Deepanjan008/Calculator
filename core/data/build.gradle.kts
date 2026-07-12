plugins {
    alias(libs.plugins.nexura.library)
    alias(libs.plugins.nexura.hilt)
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)
}
