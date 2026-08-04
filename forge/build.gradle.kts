plugins {
    id("com.gradleup.shadow")
}

loom {
    forge {
        mixinConfig("teleidoscope.mixins.json")
    }
}

architectury {
    platformSetupLoomIde()
    forge()
}

val shadowJar = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar")

configurations {
    val common by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    configurations["compileClasspath"].extendsFrom(common)
    configurations["runtimeClasspath"].extendsFrom(common)
    configurations["developmentForge"].extendsFrom(common)

    val shadowBundle by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

dependencies {
    /* -- Core Dependencies -- */
    "forge"("net.minecraftforge:forge:${rootProject.extra["forge_version"]}")
    modImplementation("dev.architectury:architectury-forge:${rootProject.extra["architectury_api_version"]}")

    compileOnly("io.github.llamalad7:mixinextras-common:${rootProject.extra["mixinextras_version"]}")
    annotationProcessor("io.github.llamalad7:mixinextras-common:${rootProject.extra["mixinextras_version"]}")

    implementation("io.github.llamalad7:mixinextras-forge:${rootProject.extra["mixinextras_version"]}")
    include("io.github.llamalad7:mixinextras-forge:${rootProject.extra["mixinextras_version"]}")

    /* -- Mod Dependencies -- */
    modImplementation("dev.isxander:yet-another-config-lib:${rootProject.extra["yacl_version"]}-forge") {
        isTransitive = false;
    }

    /* -- Dependencies -- */
    forgeRuntimeLibrary("org.quiltmc.parsers:gson:${rootProject.extra["qulitmc_parsers_version"]}") // Needed by YACL. Forge will crash at launch without this.

    add("common", project(mapOf("path" to ":common", "configuration" to "namedElements")))
    add("shadowBundle", project(mapOf("path" to ":common", "configuration" to "transformProductionForge")))
    add("shadowBundle", project(":native-steamworks"))
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    configurations = listOf(project.configurations["shadowBundle"])
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    dependsOn("shadowJar")

    inputFile.set(shadowJar.flatMap { it.archiveFile })
}
