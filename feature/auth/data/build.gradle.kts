plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.room)
    alias(libs.plugins.convention.buildkonfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.koin.common)
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)

                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.core.database)
                implementation(projects.core.security)
                implementation(projects.feature.auth.domain)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
