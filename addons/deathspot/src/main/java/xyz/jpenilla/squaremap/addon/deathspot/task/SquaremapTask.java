package xyz.jpenilla.squaremap.addon.deathspot.task;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.deathspot.DeathSpots;
import xyz.jpenilla.squaremap.addon.deathspot.config.DeathSpotWorldConfig;
import xyz.jpenilla.squaremap.addon.deathspot.hook.SquaremapHook;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public final class SquaremapTask extends BukkitRunnable {
    private final DeathSpots plugin;
    private final SimpleLayerProvider provider;
    private final DeathSpotWorldConfig worldConfig;
    private final MapWorld world;

    private boolean stop;

    public SquaremapTask(DeathSpots plugin, MapWorld world, SimpleLayerProvider provider) {
        this.plugin = plugin;
        this.world = world;
        this.provider = provider;
        this.worldConfig = plugin.config().worldConfig(world.identifier());
    }

    @Override
    public void run() {
        if (this.stop) {
            this.cancel();
        }

        this.provider.clearMarkers();

        final World bukkitWorld = BukkitAdapter.bukkitWorld(this.world);

        this.plugin.getDeathSpots().forEach((uuid, pair) -> {
            if (pair.right().getWorld().getUID().equals(bukkitWorld.getUID())) {
                this.handle(uuid, pair.left(), pair.right());
            }
        });
    }

    private void handle(UUID uuid, String name, Location location) {
        Icon icon = Marker.icon(BukkitAdapter.point(location), SquaremapHook.DEATH_SPOT_ICON_KEY, 16);

        icon.markerOptions(
            MarkerOptions.builder()
                .hoverTooltip(worldConfig.tooltip.replace("{name}", name))
        );

        final String markerid = "deathspots_player_" + uuid;
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
