package xyz.jpenilla.squaremap.addon.banners.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;

public class WorldListener implements Listener {
    private final SquaremapBanners plugin;

    public WorldListener(SquaremapBanners plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        plugin.squaremapHook().getProvider(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldUnload(WorldUnloadEvent event) {
        plugin.squaremapHook().unloadProvider(event.getWorld());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        if (!event.isNewChunk()) {
            plugin.bannerManager().checkChunk(event.getChunk());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        plugin.bannerManager().checkChunk(event.getChunk());
    }
}
