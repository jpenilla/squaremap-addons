import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun BukkitPluginDescription.addAuthor(name: String) {
    authors = (authors as MutableList<String>? ?: arrayListOf())
        .also { it.add(name) }
}

fun BukkitPluginDescription.addDepend(name: String) {
    depend = (depend as MutableList<String>? ?: arrayListOf())
        .also { it.add(name) }
}

fun DependencyHandlerScope.squaremapApi(
    version: String = "1.1.0-SNAPSHOT",
    block: ExternalModuleDependency.() -> Unit = {},
): ExternalModuleDependency =
    (create("xyz.jpenilla:squaremap-api:$version") as ExternalModuleDependency).also(block)

fun DependencyHandlerScope.paperApi(
    version: String = "1.18.1-R0.1-SNAPSHOT",
    block: ExternalModuleDependency.() -> Unit = {},
): ExternalModuleDependency =
    (create("io.papermc.paper:paper-api:$version") as ExternalModuleDependency).also(block)
