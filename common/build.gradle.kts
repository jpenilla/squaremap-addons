plugins {
    `base-conventions`
}

description = "Common code shared between all addon plugins in this repository"

dependencies {
    api(platform("org.spongepowered:configurate-bom:4.1.2"))
    api("org.spongepowered:configurate-yaml")

    compileOnlyApi("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT") {
        exclude("org.yaml", "snakeyaml")
    }
    compileOnlyApi("xyz.jpenilla:squaremap-api:1.1.12")
}
