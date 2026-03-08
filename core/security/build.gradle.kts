plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.touchlab.kermit)
                implementation(libs.koin.core)

                implementation(projects.core.domain)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.datastore.preferences)
                implementation(libs.androidx.security.crypto)
            }
        }

        iosMain {
            dependencies {
                // iOS Keychain via platform APIs
            }
        }
    }
}
