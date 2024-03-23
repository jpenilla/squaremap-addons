description = "Integration with various vanish plugins for squaremap"

dependencies {
    compileOnly("com.github.LeonMangler:SuperVanish:6.2.18-3") {
        isTransitive = false
    }
}

bukkitPluginYml {
    main = "xyz.jpenilla.squaremap.addon.vanish.SquaremapVanish"
    softDepend = listOf("SuperVanish", "PremiumVanish")
}
