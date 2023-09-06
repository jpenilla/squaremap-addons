package xyz.jpenilla.squaremap.addon.signs.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.addon.signs.config.SignsWorldConfig;
import xyz.jpenilla.squaremap.addon.signs.data.SignLayerProvider;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    private static final Key SIGNS_LAYER_KEY = key("signs");

    private final Map<WorldIdentifier, SignLayerProvider> providers = new HashMap<>();

    public Map<WorldIdentifier, SignLayerProvider> getProviders() {
        return this.providers;
    }

    public void load() {
        Bukkit.getWorlds().forEach(this::getProvider);
    }

    public SignLayerProvider getProvider(World world) {
        SignLayerProvider provider = this.providers.get(BukkitAdapter.worldIdentifier(world));
        if (provider != null) {
            return provider;
        }
        MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).orElse(null);
        if (mapWorld == null) {
            return null;
        }
        SignsWorldConfig worldConfig = SignsPlugin.getInstance().config().worldConfig(mapWorld.identifier());
        if (!worldConfig.enabled) {
            return null;
        }
        provider = new SignLayerProvider(worldConfig);
        mapWorld.layerRegistry().register(SIGNS_LAYER_KEY, provider);
        this.providers.put(BukkitAdapter.worldIdentifier(world), provider);
        return provider;
    }

    public void unloadProvider(World world) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world))
            .ifPresent(mapWorld -> mapWorld.layerRegistry().unregister(SIGNS_LAYER_KEY));
    }

    public void unloadProvider(WorldIdentifier id) {
        final World world = Bukkit.getWorld(BukkitAdapter.namespacedKey(id));
        if (world == null) {
            return;
        }
        this.unloadProvider(world);
    }

    public void disable() {
        this.providers.forEach((id, provider) -> unloadProvider(id));
    }
}
