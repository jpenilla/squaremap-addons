description = "EssentialsX addon for squaremap"

dependencies {
    compileOnly("net.essentialsx:EssentialsX:2.20.0") {
        isTransitive = false
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials"
    addAuthor("BillyGalbreath")
    addDepend("Essentials")
}
