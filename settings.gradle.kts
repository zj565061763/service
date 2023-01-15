enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    val gradlePluginVersion: String by settings
    val kotlinVersion: String by settings
    val kspVersion: String by settings

    plugins {
        id("com.android.application") version gradlePluginVersion apply false
        id("com.android.library") version gradlePluginVersion apply false
        id("org.jetbrains.kotlin.android") version kotlinVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        id("com.google.devtools.ksp") version kspVersion apply false
    }

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "service"

include(":app")
include(":lib")
include(":app_module_a")
include(":app_module_b")
include(":app_module_common")
