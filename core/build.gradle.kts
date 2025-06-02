plugins {
    alias(libs.plugins.kotlin.jvm)
    id ("java-test-fixtures")
}

dependencies {
    implementation(libs.oshai.logging)
    implementation(libs.kotlinx.coroutines.core)

    testFixturesImplementation(libs.bundles.jvmtest)
    testFixturesImplementation(libs.kotest.property)
    testFixturesImplementation(libs.kotlinx.coroutines.core)
    // runTest() for running suspend functions in tests
    testFixturesImplementation(libs.kotlinx.coroutines.test)

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.jvmtest)
    testImplementation(libs.kotest.property)
}

tasks.test {
    useJUnitPlatform()
    minHeapSize = "512m"
    maxHeapSize = "8g"
    jvmArgs = listOf("-Xss128m")

    // Make tests run in parallel
    // More info: https://www.jvt.me/posts/2021/03/11/gradle-speed-parallel/
    systemProperties["junit.jupiter.execution.parallel.enabled"] = "true"
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"
}

kotlin {
    jvmToolchain(21)
}

