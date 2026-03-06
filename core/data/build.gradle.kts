plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.buildkonfig)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)

                implementation(projects.core.domain)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.koin.core)
            }
        }
    }
}
