import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("nexura.android.library")

            val extension = extensions.getByType<LibraryExtension>()
            extension.buildFeatures {
                compose = true
            }
            configureComposeDependencies(extension)
        }
    }
}
