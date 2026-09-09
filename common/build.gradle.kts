plugins {
    `base-conventions`
}

description = "Common code shared between all addon plugins in this repository"

dependencies {
    api(platform("org.spongepowered:configurate-bom:4.2.0"))
    api("org.spongepowered:configurate-yaml")

    compileOnlyApi("io.papermc.paper:paper-api:26.2.build.123-stable") {
        exclude("org.yaml", "snakeyaml")
    }
    compileOnlyApi("xyz.jpenilla:squaremap-api:1.3.9")
}
