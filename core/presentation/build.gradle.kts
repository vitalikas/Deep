plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.cmp.components.resources)
                implementation(libs.material3.adaptive)
                implementation(libs.jetbrains.lifecycle.viewmodel)

                implementation(projects.core.domain)
            }
        }

        androidMain {
            dependencies {

            }
        }

        iosMain {
            dependencies {

            }
        }
    }
}
