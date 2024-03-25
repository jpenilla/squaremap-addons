import xyz.jpenilla.resourcefactory.bukkit.Permission

description = "Signs addon for squaremap"

bukkitPluginYaml {
    main = "xyz.jpenilla.squaremap.addon.signs.SignsPlugin"
    authors.add("BillyGalbreath")

    permissions {
        register("squaremap.signs.admin") {
            description = "Allow controlling the plugin"
            default = Permission.Default.OP
        }
    }
}
