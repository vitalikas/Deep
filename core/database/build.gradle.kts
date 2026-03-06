plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.koin.common)
                implementation(libs.kotlinx.serialization.json)

                implementation(projects.core.domain)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}
