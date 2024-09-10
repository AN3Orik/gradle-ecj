pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention")
}

rootProject.name = "gradle-ecj"

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")