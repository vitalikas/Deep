plugins {
    alias(libs.plugins.convention.cmp.feature)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "lt.vitalijus.feature.scan.presentation.generated.resources"
    generateResClass = always
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.cmp.components.resources)

                implementation(projects.core.domain)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)
                implementation(projects.feature.scan.domain)
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
                implementation(libs.maps.compose)
            }
        }

        androidUnitTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
            }
        }

        androidInstrumentedTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.junit)
                implementation(libs.androidx.testExt.junit)
                implementation(libs.androidx.espresso.core)
                implementation(libs.androidx.compose.ui.test)
                implementation(libs.androidx.compose.ui.test.manifest)
            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
