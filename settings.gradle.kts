rootProject.name = "Deep"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":core:presentation")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:designsystem")
include(":core:security")
include(":feature:auth:presentation")
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:scan:presentation")
include(":feature:scan:domain")
include(":feature:scan:data")
