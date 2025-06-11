 plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":core"))

    implementation(libs.bundles.jvmtest)
    implementation(libs.kotest.property)
    implementation(libs.kotlinx.coroutines.core)
    // runTest() for running suspend functions in tests
    implementation(libs.kotlinx.coroutines.test)
}

kotlin {
    jvmToolchain(21)
}
