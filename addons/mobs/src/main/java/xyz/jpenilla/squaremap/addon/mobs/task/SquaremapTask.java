package xyz.jpenilla.squaremap.addon.mobs.task;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.mobs.configuration.WorldConfig;
import xyz.jpenilla.squaremap.addon.mobs.data.Icons;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public final class SquaremapTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private final WorldConfig worldConfig;

    private boolean stop;

    public SquaremapTask(MapWorld world, WorldConfig worldConfig, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
        this.worldConfig = worldConfig;
    }

    @Override
    public void run() {
        if (this.stop) {
            this.cancel();
        }

        this.provider.clearMarkers();

        for (final Mob mob : BukkitAdapter.bukkitWorld(this.world).getEntitiesByClass(Mob.class)) {
            final EntityType type = mob.getType();
            if (!this.worldConfig.ALLOWED_TYPES.contains(type)) {
                continue;
            }
            final Location loc = mob.getLocation();
            if (loc.getY() < this.worldConfig.MINIMUM_Y) {
                continue;
            }
            if (this.worldConfig.SURFACE_ONLY && aboveSurface(loc)) {
                continue;
            }
            this.handleMob(type, mob.getEntityId(), loc);
        }
    }

    private static boolean aboveSurface(final Location loc) {
        return loc.getY() < loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ(), HeightMap.WORLD_SURFACE);
    }

    private void handleMob(EntityType type, int id, Location loc) {
        final Icon icon = Marker.icon(
            BukkitAdapter.point(loc),
            Icons.getIcon(type),
            this.worldConfig.ICON_SIZE
        );

        final String name = PaperComponents.plainTextSerializer().serialize(Component.translatable(type.translationKey()));

        icon.markerOptions(
            MarkerOptions.builder()
                .hoverTooltip(
                    this.worldConfig.ICON_TOOLTIP
                        .replace("{id}", Integer.toString(id))
                        .replace("{key}", type.getKey().asString())
                        .replace("{name}", name)
                )
        );

        final String markerid = "mob_" + type + "_id_" + id;
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
