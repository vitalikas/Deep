package lt.vitalijus.deep.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        buildFeatures {
            compose = true
        }

        dependencies {
            "debugImplementation"(libs.findLibrary("cmp-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("cmp-ui-tooling").get())
        }
    }
}
