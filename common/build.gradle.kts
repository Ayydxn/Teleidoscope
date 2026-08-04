architectury {
    common(rootProject.extra["enabled_platforms"].toString().split(','))
}

loom {
    accessWidenerPath.set(file("src/main/resources/${project(":common").property("mod_id")}.accesswidener"))
}

dependencies {
    /* -- Native Subproject -- */
    api(project(":native-steamworks"))

    /* -- Core Dependencies -- */
    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
    modImplementation("dev.architectury:architectury:${rootProject.extra["architectury_api_version"]}")

    /* -- Mod Dependencies -- */
    modImplementation("dev.isxander:yet-another-config-lib:${rootProject.extra["yacl_version"]}-fabric")
}
