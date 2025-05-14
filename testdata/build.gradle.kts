plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.oshai.logging)
    implementation(kotlin("test"))
    implementation(libs.bundles.jvmtest)

    // runTest() for running suspend functions in tests
    implementation(libs.kotlinx.coroutines.test)

    // Fancy property-based testing
    implementation(libs.kotest.property)
}

kotlin {
    jvmToolchain(21)
}