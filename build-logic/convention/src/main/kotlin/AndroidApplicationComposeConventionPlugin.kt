import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("nexura.android.application")

            extensions.configure<ApplicationExtension> {
                buildFeatures {
                    compose = true
                }
            }
            configureComposeDependencies(this)
        }
    }
}
