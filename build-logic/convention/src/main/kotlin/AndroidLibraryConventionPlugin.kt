import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                namespace = "com.nexuralabs.calculator.${target.path.trimStart(':').replace(":", ".").replace("-", "")}"

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                // Lint warnings fail the build here for the same reason compiler
                // warnings do: the gate is only a gate if nothing slips past it.
                lint {
                    warningsAsErrors = true
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
