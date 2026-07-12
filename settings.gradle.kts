pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Calculator"

include(":app")

include(":core:data")
include(":core:ui")
include(":core:navigation")

include(":feature:calculator")
include(":feature:converter")
include(":feature:finance")
include(":feature:tools")
include(":feature:history")
include(":feature:settings")
