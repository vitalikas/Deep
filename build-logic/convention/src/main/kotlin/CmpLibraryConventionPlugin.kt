import lt.vitalijus.deep.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("lt.vitalijus.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("cmp-foundation").get())
                "commonMainImplementation"(libs.findLibrary("cmp-material3").get())
                "commonMainImplementation"(libs.findLibrary("cmp-ui").get())
                "commonMainImplementation"(libs.findLibrary("cmp-material-icons-core").get())
                "commonMainImplementation"(libs.findLibrary("cmp-material-icons-extended").get())

                "debugImplementation"(libs.findLibrary("cmp-ui-tooling").get())
            }
        }
    }
}
