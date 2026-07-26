import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("dev.architectury.loom") version "1.11-SNAPSHOT" apply false
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("com.gradleup.shadow") version "8.3.6" apply false
    id("io.github.pacifistmc.forgix") version "2.+"
}

architectury {
    minecraft = project.property("minecraft_version") as String
}

forgix {
    archiveBaseName = "${rootProject.extra["archives_name"]}"
    archiveVersion = "${rootProject.extra["mod_version"]}-mc${rootProject.extra["minecraft_version"]}"
    autoRun = true
}

allprojects {
    apply(plugin = "java")

    group = rootProject.extra["maven_group"] as String
    version = "${rootProject.extra["mod_version"]}-mc${rootProject.extra["minecraft_version"]}"

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release = 17
    }
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
    loom.silentMojangMappingsLicense()

    base {
        // Set up a suffixed format for the mod jar names, e.g. `example-fabric`.
        archivesName.set("${rootProject.extra["archives_name"]}-${project.name}")
    }

    repositories {
        maven("https://maven.terraformersmc.com/") {
            name = "Terraformers"
        }

        maven("https://maven.parchmentmc.org") {
            name = "ParchmentMC"
        }

        maven("https://maven.isxander.dev/releases") {
            name = "Xander Maven"
        }

        maven("https://maven.quiltmc.org/repository/release") {
            name = "Quilt"
        }

        maven("https://oss.sonatype.org/content/repositories/snapshots") {
            name = "Sonatype"
        }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:${rootProject.extra["minecraft_version"]}")

        @Suppress("UnstableApiUsage")
        "mappings"(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${rootProject.property("minecraft_version")}:${rootProject.property("parchment_version")}@zip")
        })
    }

    tasks.processResources {
        val expandProps = mapOf(
            "minecraft_version" to rootProject.extra["minecraft_version"],
            "minecraft_version_range" to rootProject.extra["minecraft_version_range"],
            "mod_id" to rootProject.extra["mod_id"],
            "mod_name" to rootProject.extra["mod_name"],
            "version" to "${rootProject.extra["mod_version"]}-mc${rootProject.extra["minecraft_version"]}",
            "description" to rootProject.extra["mod_description"],
            "sources_url" to rootProject.extra["sources_url"],
            "issue_tracker_url" to rootProject.extra["issue_tracker_url"],
            "license" to rootProject.extra["license"],
            "mod_authors" to rootProject.extra["mod_authors"],
            "icon_path" to rootProject.extra["icon_path"],
            "fabric_api_version" to rootProject.extra["fabric_api_version"],
            "fabric_loader_version" to rootProject.extra["fabric_loader_version"],
            "forge_version" to rootProject.extra["forge_version"],
            "forge_version_range" to rootProject.extra["forge_version_range"],
            "architectury_api_version" to rootProject.extra["architectury_api_version"],
            "architectury_version_range" to rootProject.extra["architectury_version_range"],
            "yacl_version" to rootProject.extra["yacl_version"],
            "yacl_version_range" to rootProject.extra["yacl_version_range"]
        )

        inputs.properties(expandProps)

        filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml")) {
            expand(expandProps)
        }
    }
}
