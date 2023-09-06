package xyz.jpenilla.squaremap.addon.signs.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.addon.signs.config.SignsWorldConfig;
import xyz.jpenilla.squaremap.addon.signs.data.SignLayerProvider;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

@DefaultQualifier(NonNull.class)
public final class LayerProviderManager {
    private static final Key SIGNS_LAYER_KEY = key("signs");

    private final SignsPlugin plugin;
    private final Map<WorldIdentifier, SignLayerProvider> providers = new HashMap<>();

    public LayerProviderManager(final SignsPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<WorldIdentifier, SignLayerProvider> providers() {
        return this.providers;
    }

    public void load() {
        Bukkit.getWorlds().forEach(this::provider);
    }

    public @Nullable SignLayerProvider provider(final World world) {
        return this.providers.computeIfAbsent(BukkitAdapter.worldIdentifier(world), id -> {
            final @Nullable MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(id).orElse(null);
            if (mapWorld == null) {
                return null;
            }
            final SignsWorldConfig worldConfig = this.plugin.config().worldConfig(id);
            if (!worldConfig.enabled) {
                return null;
            }
            final SignLayerProvider provider = new SignLayerProvider(worldConfig);
            mapWorld.layerRegistry().register(SIGNS_LAYER_KEY, provider);
            return provider;
        });
    }

    public void unloadProvider(final World world) {
        this.unloadProvider(BukkitAdapter.worldIdentifier(world));
    }

    public void unloadProvider(final WorldIdentifier id) {
        SquaremapProvider.get().getWorldIfEnabled(id)
            .ifPresent(mapWorld -> mapWorld.layerRegistry().unregister(SIGNS_LAYER_KEY));
        this.providers.remove(id);
    }

    public void disable() {
        this.providers.forEach((id, provider) -> this.unloadProvider(id));
    }
}
