import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

description = "Signs addon for squaremap"

bukkit {
    main = "xyz.jpenilla.squaremap.addon.signs.SignsPlugin"
    addAuthor("BillyGalbreath")
    permissions {
        register("squaremap.signs.admin") {
            description = "Allow controlling the plugin"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}
