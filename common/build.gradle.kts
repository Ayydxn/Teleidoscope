architectury {
    common(rootProject.extra["enabled_platforms"].toString().split(','))
}

dependencies {
    /* -- Core Dependencies -- */
    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")
    modImplementation("dev.architectury:architectury:${rootProject.extra["architectury_api_version"]}")

    /* -- Mod Dependencies -- */
    modImplementation("dev.isxander:yet-another-config-lib:${rootProject.extra["yacl_version"]}-fabric")
}
