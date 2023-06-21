// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        maven { url = uri("https://plugins.gradle.org/m2") }
        maven { url = uri("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.BUILD_GRADLE}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.ANDROID_NAVIGATION}")
        classpath("com.google.gms:google-services:${Versions.GMS_SERVICES}")
        classpath("com.google.firebase:firebase-appdistribution-gradle:${Versions.FIREBASE_APP_DISTRIBUTION}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS_GRADLE}")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version Versions.DETEKT
    id("com.github.ben-manes.versions") version Versions.DEPS_UPDATES
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        config = files("$rootDir/default-detekt-config.yml")
        source = objects.fileCollection().from(
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_JAVA,
            "src/test/java",
            io.gitlab.arturbosch.detekt.extensions.DetektExtension.DEFAULT_SRC_DIR_KOTLIN,
            "src/test/kotlin"
        )
        buildUponDefaultConfig = false
    }
    apply(from = "$rootDir/ktlint.gradle")

    repositories {
        google()
        maven { url = uri("https://plugins.gradle.org/m2") }
        maven { url = uri("https://jitpack.io") }
    }
}

tasks.create<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}

tasks.create<GradleBuild>("x") {
    tasks = listOf(":app:ktlintFormat", ":app:ktlint", ":app:detekt", "lintDevelopRelease")
}
