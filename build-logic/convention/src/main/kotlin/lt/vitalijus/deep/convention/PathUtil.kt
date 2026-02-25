package lt.vitalijus.deep.convention

import org.gradle.api.Project
import java.util.Locale

// :core:data -> lt.vitalijus.core.data
fun Project.pathToPackageName(): String {
    val relativePackageName = path
        .replace(':', '.')
        .lowercase()

    return "lt.vitalijus$relativePackageName"
}

// :core:data -> core_data_my_resource
fun Project.pathToResourcePrefix(): String {
    return path
        .replace(':', '_')
        .lowercase()
        .drop(1) + "_"
}

// :core:data -> CoreData
fun Project.pathToFrameworkName(): String {
    // :core:data -> ["core", "data"]
    val parts = path.split(":", "-", "_", " ")
    // ["core", "data"] -> ["Core", "Data"] -> "CoreData"
    return parts.joinToString(separator = "") { part ->
        part.replaceFirstChar { char ->
            char.titlecase(Locale.ROOT)
        }
    }
}
