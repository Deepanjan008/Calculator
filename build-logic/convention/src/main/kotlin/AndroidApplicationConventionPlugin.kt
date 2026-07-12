import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                namespace = "com.nexuralabs.calculator"

                defaultConfig {
                    applicationId = "com.nexuralabs.calculator"
                    targetSdk = 35
                    versionCode = 4
                    versionName = "2.0.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }

                // Keep dependency metadata out of release artifacts (F-Droid / IzzyOnDroid friendly)
                dependenciesInfo {
                    includeInApk = false
                    includeInBundle = false
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
