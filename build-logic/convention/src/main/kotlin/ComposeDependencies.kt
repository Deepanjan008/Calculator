import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Adds the Compose BOM plus the common Compose UI + Material3 libraries that almost every
 * screen-owning module (feature modules, core:ui) needs. Debug-only tooling deps are added too.
 */
internal fun Project.configureComposeDependencies(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        // compileSdk / minSdk already set by configureKotlinAndroid via the base convention plugin
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.findLibrary("androidx-compose-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())

        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
    }
}
