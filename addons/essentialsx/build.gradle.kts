description = "EssentialsX addon for squaremap"

dependencies {
    compileOnly("net.essentialsx:EssentialsX:2.19.7") {
        isTransitive = false
    }
}

bukkit {
    main = "xyz.jpenilla.squaremap.addon.essentialsx.SquaremapEssentials"
    addAuthor("BillyGalbreath")
    addDepend("Essentials")
}
