package xyz.jpenilla.squaremap.addon.banners.hook;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;
import xyz.jpenilla.squaremap.addon.banners.configuration.BannersWorldConfig;
import xyz.jpenilla.squaremap.addon.banners.data.BannerLayerProvider;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SquaremapProvider;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public class SquaremapHook {
    private static final Key BANNERS_LAYER = Key.key("banners");

    private final Map<WorldIdentifier, BannerLayerProvider> providers = new HashMap<>();

    public Map<WorldIdentifier, BannerLayerProvider> getProviders() {
        return this.providers;
    }

    public void load() {
        Bukkit.getWorlds().forEach(this::getProvider);
    }

    public BannerLayerProvider getProvider(World world) {
        BannerLayerProvider provider = this.providers.get(BukkitAdapter.worldIdentifier(world));
        if (provider != null) {
            return provider;
        }
        MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(BukkitAdapter.worldIdentifier(world)).orElse(null);
        if (mapWorld == null) {
            return null;
        }
        BannersWorldConfig worldConfig = SquaremapBanners.instance().config().worldConfig(mapWorld.identifier());
        if (!worldConfig.enabled) {
            return null;
        }
        provider = new BannerLayerProvider(worldConfig);
        Key key = Key.of("banners");
        mapWorld.layerRegistry().register(key, provider);
        this.providers.put(mapWorld.identifier(), provider);
        return provider;
    }

    public void unloadProvider(World world) {
        unloadProvider(BukkitAdapter.worldIdentifier(world));
    }

    public void unloadProvider(WorldIdentifier id) {
        SquaremapProvider.get().getWorldIfEnabled(id).ifPresent(world -> world.layerRegistry().unregister(BANNERS_LAYER));
    }

    public void disable() {
        this.providers.forEach((uuid, provider) -> unloadProvider(uuid));
    }
}
