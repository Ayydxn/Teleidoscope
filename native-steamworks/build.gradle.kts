import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import java.util.Properties

plugins {
    id("java-library")
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

val steamworksSdkLocation: String =
    (localProperties.getProperty("steamworks_sdk_location")
        ?: findProperty("steamworks_sdk_location") as String?
        ?: "").also {
        if (it.isBlank()) {
            logger.warn("\n[Teleidoscope JNI] WARNING: 'steamworks_sdk_location' is not set. Native compilation will be skipped.")
        }
    }

val platform: String = (findProperty("platform") as String?) ?: when {
    DefaultNativePlatform.getCurrentOperatingSystem().isWindows -> "windows-x64"
    DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX -> {
        val arch = System.getProperty("os.arch").lowercase()
        if (arch.contains("aarch64") || arch.contains("arm")) "macos-arm64" else "macos-x64"
    }
    else -> "linux-x64"
}

// Ensure the compiled native binaries in build/ generated resources are included in this subproject's JAR
val generatedNativesDir = layout.buildDirectory.dir("generated/resources")
sourceSets.main.get().resources.srcDir(generatedNativesDir)

val generateJniHeaders by tasks.registering(JavaCompile::class) {
    group = "jni"
    description = "Compiles Java sources and emits JNI headers via javac -h"

    source = sourceSets.main.get().java
    classpath = sourceSets.main.get().compileClasspath
    destinationDirectory.set(layout.buildDirectory.dir("jni-classes"))

    options.compilerArgs.addAll(
        listOf(
            "-h", layout.buildDirectory.dir("jni-headers").get().asFile.absolutePath,
            "-implicit:class"
        )
    )
}

val cmakeBuildDir = layout.buildDirectory.dir("cmake-build").get().asFile

val cmakeConfigure by tasks.registering(Exec::class) {
    group = "jni"
    dependsOn(generateJniHeaders)
    onlyIf { steamworksSdkLocation.isNotBlank() }

    doFirst { cmakeBuildDir.mkdirs() }

    workingDir = cmakeBuildDir
    commandLine(
        "cmake",
        "-S", file("src/main/native").absolutePath,
        "-B", cmakeBuildDir.absolutePath,
        "-DJNI_HEADER_DIR=${layout.buildDirectory.dir("jni-headers").get().asFile}",
        "-DSTEAMWORKS_SDK=${steamworksSdkLocation}",
        "-DPLATFORM=${platform}",
        "-DCMAKE_BUILD_TYPE=Release",
        "-G", if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) "Visual Studio 18 2026" else "Unix Makefiles"
    )
}

val compileNative by tasks.registering(Exec::class) {
    group = "jni"
    dependsOn(cmakeConfigure)
    onlyIf { steamworksSdkLocation.isNotBlank() }

    workingDir = cmakeBuildDir
    commandLine("cmake", "--build", ".", "--config", "Release", "--parallel")

    doLast {
        val libName = when {
            platform.startsWith("windows") -> "TeleidoscopeSteamworks-Win64.dll"
            platform.startsWith("macos")   -> "libTeleidoscopeSteamworks-Mac.dylib"
            else                           -> "libTeleidoscopeSteamworks-Linux.so"
        }

        val produced = file("$cmakeBuildDir/Release/$libName").takeIf { it.exists() }
            ?: file("$cmakeBuildDir/$libName")

        if (produced.exists()) {
            val outputFolder = file("${generatedNativesDir.get().asFile}/natives/$platform")
            copy {
                from(produced)
                into(outputFolder)
            }

            logger.lifecycle("Copied $libName into subproject resources: ${outputFolder.absolutePath}")
        }
    }
}

tasks.named("processResources") {
    dependsOn(compileNative)
}
