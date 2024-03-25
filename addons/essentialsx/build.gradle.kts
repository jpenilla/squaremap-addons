description = "EssentialsX addon for squaremap"

dependencies {
    compileOnly("net.essentialsx:EssentialsX:2.20.1") {
        isTransitive = false
    }
}

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials"
    authors.add("BillyGalbreath")
    depend.add("Essentials")
}
