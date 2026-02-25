import lt.vitalijus.deep.convention.configureAndroidTarget
import lt.vitalijus.deep.convention.configureIosTargetsApp
import lt.vitalijus.deep.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("lt.vitalijus.convention.android.application.compose")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidTarget()
            configureIosTargetsApp()

            dependencies {
                "debugImplementation"(libs.findLibrary("cmp-ui-tooling").get())
            }
        }
    }
}
