import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

fun BukkitPluginDescription.addAuthor(name: String) {
    authors = (authors as MutableList<String>? ?: arrayListOf())
        .also { it.add(name) }
}

fun BukkitPluginDescription.addDepend(name: String) {
    depend = (depend as MutableList<String>? ?: arrayListOf())
        .also { it.add(name) }
}
