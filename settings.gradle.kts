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

// Pure Kotlin. No Android, no Compose, no framework - only the rules and the
// contracts the rest of the app is written against.
include(":core:domain")

// Pure Kotlin. The three markers a screen agrees with its state holder on, and
// the base class that owns them.
include(":core:presentation")

// Android. Encryption, the database and the repositories that implement the
// :core:domain contracts.
include(":core:data")
include(":core:database")

// Android. The design system and the route table.
include(":core:ui")
include(":core:navigation")

include(":feature:calculator")
include(":feature:converter")
include(":feature:finance")
include(":feature:tools")
include(":feature:history")
include(":feature:settings")
