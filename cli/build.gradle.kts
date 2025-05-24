plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":core"))

    implementation(libs.kotlinx.cli)
    implementation(libs.oshai.logging)
    implementation(libs.logback.classic)
}

kotlin {
    jvmToolchain(21)
}

tasks.register<Jar>("uberJar") {
    archiveClassifier = "uber"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("netchdf")

    manifest {
        attributes("Main-Class" to "com.sunya.netchdf.cli.ncdump")
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
