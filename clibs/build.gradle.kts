plugins {
    alias(libs.plugins.kotlin.jvm)
    // id ("java-test-fixtures")
}

dependencies {
    api(project(":core"))
    implementation(libs.fleeksoft)
    implementation(libs.oshai.logging)
    implementation(libs.kotlinx.coroutines.core)

    implementation(project(":testFixtures"))
    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.jvmtest)
    testImplementation(libs.kotest.property)
}

kotlin {
    jvmToolchain(21)
}

tasks {
    val ENABLE_PREVIEW = "--enable-preview"
    withType<JavaCompile>() {
        options.compilerArgs.add(ENABLE_PREVIEW)
        // Optionally we can show which preview feature we use.
        options.compilerArgs.add("-Xlint:-preview")
        // Explicitly setting compiler option --release
        // is needed when we wouldn't set the
        // sourceCompatiblity and targetCompatibility
        // properties of the Java plugin extension.
        options.release.set(21)
    }
    withType<Test>().all {
        useJUnitPlatform()
        jvmArgs("--enable-preview")
        minHeapSize = "512m"
        maxHeapSize = "4g"
        // https://www.jvt.me/posts/2021/03/11/gradle-speed-parallel/
        // Configuration parameters to execute top-level classes in parallel but methods in same thread
        // https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution
        systemProperties["junit.jupiter.execution.parallel.enabled"] = "false"
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"

        // https://kantis.github.io/posts/Faster-Kotest-startup/
        systemProperty("kotest.framework.discovery.jar.scan.disable", "true")
        systemProperty("kotest.framework.classpath.scanning.config.disable", "true")
        systemProperty("kotest.framework.classpath.scanning.autoscan.disable", "true")
    }
    withType<JavaExec>().all {
        jvmArgs("--enable-preview")
    }
}