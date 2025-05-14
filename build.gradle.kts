// copied from kobweb
plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    `kotlin-dsl` apply false
    alias(libs.plugins.kotlinx.serialization) apply false
}

group = "com.sunya"
version = libs.versions.netchdf.get()

subprojects {
    repositories {
        mavenCentral()
        google()
    }
}

// To upgrade gradle, bump the version below and run the wrapper task:
//     ./gradlew wrapper
//
// Note: you should run the task twice when upgrading - once to update
//       the wrapper files, and the second time to actually download
//       and use the new version.
tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.14"
}