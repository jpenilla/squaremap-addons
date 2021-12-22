package xyz.jpenilla.squaremap.addon.griefprevention.task;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.griefprevention.configuration.Config;
import xyz.jpenilla.squaremap.addon.griefprevention.hook.GPHook;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

public final class SquaremapTask extends BukkitRunnable {
    private final World bukkitWorld;
    private final SimpleLayerProvider provider;

    private boolean stop;

    public SquaremapTask(MapWorld world, SimpleLayerProvider provider) {
        this.bukkitWorld = BukkitAdapter.bukkitWorld(world);
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
        Collection<Claim> topLevelClaims = GPHook.getClaims();
        if (topLevelClaims != null) {
            topLevelClaims.stream()
                .filter(claim -> claim.getGreaterBoundaryCorner().getWorld().getUID().equals(this.bukkitWorld.getUID()))
                .filter(claim -> claim.parent == null)
                .forEach(this::handleClaim);
        }
    }

    private void handleClaim(Claim claim) {
        Location min = claim.getLesserBoundaryCorner();
        Location max = claim.getGreaterBoundaryCorner();
        if (min == null) {
            return;
        }

        Rectangle rect = Marker.rectangle(Point.of(min.getBlockX(), min.getBlockZ()), Point.of(max.getBlockX() + 1, max.getBlockZ() + 1));

        ArrayList<String> builders = new ArrayList<>();
        ArrayList<String> containers = new ArrayList<>();
        ArrayList<String> accessors = new ArrayList<>();
        ArrayList<String> managers = new ArrayList<>();
        claim.getPermissions(builders, containers, accessors, managers);

        String worldName = min.getWorld().getName();

        MarkerOptions.Builder options = MarkerOptions.builder()
            .strokeColor(Config.STROKE_COLOR)
            .strokeWeight(Config.STROKE_WEIGHT)
            .strokeOpacity(Config.STROKE_OPACITY)
            .fillColor(Config.FILL_COLOR)
            .fillOpacity(Config.FILL_OPACITY)
            .clickTooltip(
                (claim.isAdminClaim() ? Config.ADMIN_CLAIM_TOOLTIP : Config.CLAIM_TOOLTIP)
                    .replace("{world}", worldName)
                    .replace("{id}", Long.toString(claim.getID()))
                    .replace("{owner}", claim.getOwnerName())
                    .replace("{managers}", getNames(managers))
                    .replace("{builders}", getNames(builders))
                    .replace("{containers}", getNames(containers))
                    .replace("{accessors}", getNames(accessors))
                    .replace("{area}", Integer.toString(claim.getArea()))
                    .replace("{width}", Integer.toString(claim.getWidth()))
                    .replace("{height}", Integer.toString(claim.getHeight()))
            );

        if (claim.isAdminClaim()) {
            options.strokeColor(Color.BLUE).fillColor(Color.BLUE);
        }

        rect.markerOptions(options);

        String markerid = "griefprevention_region_" + Long.toHexString(claim.getID());
        this.provider.addMarker(Key.of(markerid), rect);
    }

    private static String getNames(List<String> list) {
        List<String> names = new ArrayList<>();
        for (String str : list) {
            try {
                UUID uuid = UUID.fromString(str);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                names.add(offlinePlayer.getName());
            } catch (Exception e) {
                names.add(str);
            }
        }
        return String.join(", ", names);
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}

