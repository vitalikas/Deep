package lt.vitalijus.deep.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKotlinMultiplatform() {
    extensions.configure<LibraryExtension> {
        namespace = this@configureKotlinMultiplatform.pathToPackageName()

        resourcePrefix = this@configureKotlinMultiplatform.pathToResourcePrefix()

        // Required to make debug build of app run in iOS simulator
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"

        configureKotlinAndroid(this)
    }

    configureAndroidTarget()

    configureIosTargetsLibrary()
}
