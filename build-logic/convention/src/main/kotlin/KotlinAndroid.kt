import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures common Android + Kotlin options shared by both `com.android.application`
 * and `com.android.library` modules (compileSdk, JVM target, Kotlin compiler options).
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = 37

        defaultConfig.apply {
            minSdk = 26
        }

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")

            // A warning is a build failure. Everything else in CONTRIBUTING.md
            // assumes this line: a rule enforced only by review rots, and a
            // deprecated call nobody acts on becomes a removal in the next
            // release, discovered at the worst possible moment.
            allWarningsAsErrors.set(true)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Werror")
    }
}
