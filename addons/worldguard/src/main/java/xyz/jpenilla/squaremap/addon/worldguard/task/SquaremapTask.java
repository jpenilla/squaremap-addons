package xyz.jpenilla.squaremap.addon.worldguard.task;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import com.sk89q.worldguard.util.profile.cache.ProfileCache;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.worldguard.SquaremapWorldGuard;
import xyz.jpenilla.squaremap.addon.worldguard.hook.WGHook;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public final class SquaremapTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private final SquaremapWorldGuard plugin;

    private boolean stop;

    public SquaremapTask(SquaremapWorldGuard plugin, MapWorld world, SimpleLayerProvider provider) {
        this.plugin = plugin;
        this.world = world;
        this.provider = provider;
    }

    @Override
    public void run() {
        if (this.stop) {
            this.cancel();
        }
        this.updateClaims();
    }

    void updateClaims() {
        this.provider.clearMarkers(); // TODO track markers instead of clearing them
        Map<String, ProtectedRegion> regions = WGHook.getRegions(this.world.identifier());
        if (regions == null) {
            return;
        }
        regions.forEach((id, region) -> handleClaim(region));
    }

    private void handleClaim(ProtectedRegion region) {
        final StateFlag.State state = region.getFlag(this.plugin.visibleFlag());
        if (state == StateFlag.State.DENY) {
            return;
        }

        Marker marker;

        if (region.getType() == RegionType.CUBOID) {
            BlockVector3 min = region.getMinimumPoint();
            BlockVector3 max = region.getMaximumPoint();
            marker = Marker.rectangle(
                Point.of(min.getX(), min.getZ()),
                Point.of(max.getX() + 1, max.getZ() + 1)
            );
        } else if (region.getType() == RegionType.POLYGON) {
            List<Point> points = region.getPoints().stream()
                .map(point -> Point.of(point.getX(), point.getZ()))
                .collect(Collectors.toList());
            marker = Marker.polygon(points);
        } else {
            // do not draw global region
            return;
        }

        ProfileCache pc = WorldGuard.getInstance().getProfileCache();
        Map<Flag<?>, Object> flags = region.getFlags();

        MarkerOptions.Builder options = MarkerOptions.builder()
            .strokeColor(this.plugin.config().strokeColor)
            .strokeWeight(this.plugin.config().strokeWeight)
            .strokeOpacity(this.plugin.config().strokeOpacity)
            .fillColor(this.plugin.config().fillColor)
            .fillOpacity(this.plugin.config().fillOpacity)
            .clickTooltip(
                this.plugin.config().claimTooltip
                    .replace("{world}", BukkitAdapter.bukkitWorld(this.world).getName()) // use names for now
                    .replace("{id}", region.getId())
                    .replace("{owner}", region.getOwners().toPlayersString())
                    .replace("{regionname}", region.getId())
                    .replace("{playerowners}", region.getOwners().toPlayersString(pc))
                    .replace("{groupowners}", region.getOwners().toGroupsString())
                    .replace("{playermembers}", region.getMembers().toPlayersString(pc))
                    .replace("{groupmembers}", region.getMembers().toGroupsString())
                    .replace("{parent}", region.getParent() == null ? "" : region.getParent().getId())
                    .replace("{priority}", String.valueOf(region.getPriority()))
                    .replace(
                        "{flags}",
                        flags.keySet().stream()
                            .map(flag -> flag.getName() + ": " + flags.get(flag) + "<br/>")
                            .collect(Collectors.joining())
                    )
            );


        marker.markerOptions(options);

        String markerid = "worldguard_region_" + region.getId().hashCode();
        this.provider.addMarker(Key.of(markerid), marker);
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}

