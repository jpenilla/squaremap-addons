package xyz.jpenilla.squaremap.addon.essentialsx.task;

import com.earth2me.essentials.Warps;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.essentialsx.config.EssXWorldConfig;
import xyz.jpenilla.squaremap.addon.essentialsx.hook.EssentialsHook;
import xyz.jpenilla.squaremap.addon.essentialsx.hook.SquaremapHook;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public final class SquaremapTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private final EssXWorldConfig worldConfig;

    private boolean stop;

    public SquaremapTask(MapWorld world, EssXWorldConfig worldConfig, SimpleLayerProvider provider) {
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

        Warps warps = EssentialsHook.getWarps();

        final World bukkitWorld = BukkitAdapter.bukkitWorld(this.world);

        warps.getList().forEach(warpName -> {
            try {
                Location loc = warps.getWarp(warpName);
                if (loc.getWorld().getUID().equals(bukkitWorld.getUID())) {
                    this.handle(warpName, loc);
                }
            } catch (WarpNotFoundException | InvalidWorldException ignore) {
            }
        });
    }

    private void handle(String warpName, Location loc) {
        Icon icon = Marker.icon(BukkitAdapter.point(loc), SquaremapHook.WARP_ICON_KEY, this.worldConfig.iconSize);

        icon.anchor(Point.of(this.worldConfig.iconAnchorX, this.worldConfig.iconAnchorZ));

        icon.markerOptions(
            MarkerOptions.builder()
                .hoverTooltip(this.worldConfig.warpsTooltip.replace("{warp}", warpName))
        );

        String markerid = "essentials_warp_" + warpName.hashCode();
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
