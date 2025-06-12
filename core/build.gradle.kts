plugins {
    alias(libs.plugins.kotlin.multiplatform)
    // id ("java-test-fixtures")
}

kotlin {
    jvm()
    linuxX64("native") {
        binaries {
            // executable()
            sharedLib {
                baseName = "native"
            }
        }
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
            }
        }
        val commonTest by getting {
            dependencies {
                // implementation(project(":testFixtures"))
                implementation(kotlin("test"))
                // implementation(kotlin("test-junit"))
                // implementation(libs.bundles.jvmtest)
                implementation(libs.kotest.property)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        /*
        val jvmTest by
        getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

         */
    }
}

tasks.withType<Wrapper> {
    // gradleVersion = "8.10"
    distributionType = Wrapper.DistributionType.BIN
}

kotlin {
    jvmToolchain(21)
}

