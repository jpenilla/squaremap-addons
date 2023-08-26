description = "EssentialsX addon for squaremap"

dependencies {
    compileOnly("net.essentialsx:EssentialsX:2.20.1") {
        isTransitive = false
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials"
    addAuthor("BillyGalbreath")
    addDepend("Essentials")
}
