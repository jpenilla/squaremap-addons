package xyz.jpenilla.squaremap.addon.signs.hook;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.World;
import xyz.jpenilla.squaremap.addon.signs.configuration.WorldConfig;
import xyz.jpenilla.squaremap.addon.signs.data.SignLayerProvider;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapHook {
    private static final Key SIGNS_LAYER_KEY = key("signs");

    private final Map<UUID, SignLayerProvider> providers = new HashMap<>();

    public Map<UUID, SignLayerProvider> getProviders() {
        return this.providers;
    }

    public void load() {
        Bukkit.getWorlds().forEach(this::getProvider);
    }

    public SignLayerProvider getProvider(World world) {
        SignLayerProvider provider = this.providers.get(world.getUID());
        if (provider != null) {
            return provider;
        }
        MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).orElse(null);
        if (mapWorld == null) {
            return null;
        }
        WorldConfig worldConfig = WorldConfig.get(world);
        if (!worldConfig.ENABLED) {
            return null;
        }
        provider = new SignLayerProvider(worldConfig);
        mapWorld.layerRegistry().register(SIGNS_LAYER_KEY, provider);
        this.providers.put(world.getUID(), provider);
        return provider;
    }

    public void unloadProvider(World world) {
        SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world))
            .ifPresent(mapWorld -> mapWorld.layerRegistry().unregister(SIGNS_LAYER_KEY));
    }

    public void unloadProvider(UUID uuid) {
        final World world = Bukkit.getWorld(uuid);
        if (world == null) {
            return;
        }
        this.unloadProvider(world);
    }

    public void disable() {
        this.providers.forEach((uuid, provider) -> unloadProvider(uuid));
    }
}
