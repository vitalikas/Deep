import io.kotzilla.gradle.ext.KotzillaExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KotzillaConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.kotzilla.kotzilla-plugin")
            }

            extensions.configure<KotzillaExtension> {
                versionName.set("1.0.0")
            }
        }
    }
}
