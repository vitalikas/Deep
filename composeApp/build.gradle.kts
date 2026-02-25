plugins {
    alias(libs.plugins.convention.cmp.application)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.cmp.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.designsystem)
            implementation(projects.core.presentation)

            implementation(projects.feature.auth.domain)
            implementation(projects.feature.auth.presentation)

            implementation(projects.feature.scan.domain)
            implementation(projects.feature.scan.data)
            implementation(projects.feature.scan.database)
            implementation(projects.feature.scan.presentation)

            implementation(libs.cmp.runtime)
            implementation(libs.cmp.foundation)
            implementation(libs.cmp.material3)
            implementation(libs.cmp.ui)
            implementation(libs.cmp.components.resources)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
        }
    }
}
