plugins {
    `base-conventions`
}

description = "Common code shared between all addon plugins in this repository"

dependencies {
    api(platform("org.spongepowered:configurate-bom:4.1.2"))
    api("org.spongepowered:configurate-yaml")
    api(platform("org.incendo:cloud-minecraft-bom:2.0.0-beta.7"))
    api("org.incendo:cloud-paper")

    compileOnlyApi("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT") {
        exclude("org.yaml", "snakeyaml")
    }
    compileOnlyApi("xyz.jpenilla:squaremap-api:1.2.3")
}
