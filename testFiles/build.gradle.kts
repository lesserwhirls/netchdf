plugins {
    alias(libs.plugins.kotlin.multiplatform)
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
            binaries {
                // executable()
                sharedLib {
                    baseName = "netchdfTestFiles"
                }
            }
        }

        isMingwX64 -> mingwX64("mingwX64")

        else -> throw GradleException("Host OS is not supported.")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.okio)
            }
        }
    }

    compilerOptions {
        optIn.add("kotlin.RequiresOptIn")
    }
}

tasks.withType<Wrapper> {
    distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Jar> {
    archiveFileName.set("netchdf-testdata-$version.jar")
}

kotlin {
    jvmToolchain(21)
}

//       2. Declare an explicit dependency on ':core:jvmJar' from ':testFiles:transformCommonMainDependenciesMetadata' using Task#dependsOn.
//
//transformCommonMainDependenciesMetadata {
//    project.tasks["compileJava"].dependsOn(":core:allMetadataJar")
//}

