import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

description = "Banners addon for squaremap"

bukkit {
    main = "xyz.jpenilla.squaremap.addon.banners.SquaremapBanners"
    addAuthor("BillyGalbreath")
    addAuthor("granny")

    permissions {
        register("squaremap.banners.admin") {
            description = "Allow controlling the plugin"
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}
