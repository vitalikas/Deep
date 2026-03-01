import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import lt.vitalijus.deep.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BuildKonfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = this@with.pathToPackageName()
                defaultConfigs {
                    val localProperties = gradleLocalProperties(
                        rootDir,
                        rootProject.providers
                    )

                    // Backend API key (optional - only set if present)
                    val apiKey = localProperties.getProperty("API_KEY") ?: ""
                    buildConfigField(FieldSpec.Type.STRING, "API_KEY", apiKey)
                }
            }
        }
    }
}
