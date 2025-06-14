plugins {
    alias(libs.plugins.kotlin.multiplatform)
    // id ("java-test-fixtures")
}

group = "com.sunya.netchdf"
version = libs.versions.netchdf.get()

kotlin {
    jvm()

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val arch = System.getProperty("os.arch")
    when {
        hostOs == "Mac OS X" && arch == "aarch64" -> macosArm64("macosArm64")

        hostOs == "Mac OS X"-> macosX64("macosX64")

        hostOs == "Linux" -> linuxX64("linuxX64") {
         /*   binaries {
                // executable()
                sharedLib {
                    baseName = "netchdf"
                }
            } */
        }

        isMingwX64 -> mingwX64("mingwX64")

        else -> throw GradleException("Host OS is not supported.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.oshai.logging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.okio)
                implementation(libs.fleeksoft)
            }
        }
        val jvmMain by
        getting {
                dependencies {
                    implementation(libs.slf4j.jvm)
                }
        }
        val commonTest by getting {
            dependencies {
                // implementation(project(":testFixtures"))
                implementation(kotlin("test"))
                implementation(libs.kotest.property)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

tasks.withType<Wrapper> {
    // gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Jar> {
    archiveFileName.set("netchdf-$version.jar")
}

kotlin {
    jvmToolchain(21)
}

