description = "Integration with various vanish plugins for squaremap"

dependencies {
    compileOnly("com.github.LeonMangler:SuperVanish:6.2.19") {
        isTransitive = false
    }
    compileOnly("org.sayandev:sayanvanish-api:1.7.3")
    compileOnly("org.sayandev:sayanvanish-bukkit:1.7.3")
}

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.vanish.SquaremapVanish"
    softDepend = listOf("SuperVanish", "PremiumVanish", "SayanVanish")
}
