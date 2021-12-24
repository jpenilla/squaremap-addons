package xyz.jpenilla.squaremap.addon.claimchunk.task;

import com.cjburkey.claimchunk.chunk.DataChunk;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.jpenilla.squaremap.addon.claimchunk.SquaremapClaimChunk;
import xyz.jpenilla.squaremap.addon.claimchunk.data.Claim;
import xyz.jpenilla.squaremap.addon.claimchunk.data.Group;
import xyz.jpenilla.squaremap.addon.claimchunk.hook.ClaimChunkHook;
import xyz.jpenilla.squaremap.addon.claimchunk.util.RectangleMerge;
import xyz.jpenilla.squaremap.api.BukkitAdapter;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.MapWorld;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.SimpleLayerProvider;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;
import xyz.jpenilla.squaremap.api.marker.Polygon;
import xyz.jpenilla.squaremap.api.marker.Rectangle;

import static xyz.jpenilla.squaremap.api.Key.key;

public final class SquaremapTask extends BukkitRunnable {
    private final World bukkitWorld;
    private final SimpleLayerProvider provider;
    private final SquaremapClaimChunk plugin;

    private boolean stop;

    public SquaremapTask(
        final SquaremapClaimChunk plugin,
        final MapWorld world,
        final SimpleLayerProvider provider
    ) {
        this.plugin = plugin;
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
        final DataChunk[] dataChunksArr = ClaimChunkHook.getClaims();
        if (dataChunksArr == null) {
            return;
        }
        final List<DataChunk> dataChunks = Arrays.stream(dataChunksArr)
            .filter(claim -> claim.chunk.getWorld().equals(this.bukkitWorld.getName()))
            .toList();

        // show simple markers (marker per chunk)
        if (this.plugin.config().showChunks) {
            dataChunks.forEach(this::drawChunk);
            return;
        }

        // show combined chunks into polygons
        final List<Claim> claims = dataChunks.stream()
            .map(dataChunk -> new Claim(
                dataChunk.chunk.getX(),
                dataChunk.chunk.getZ(),
                dataChunk.player
            ))
            .toList();

        final List<Group> groups = this.groupClaims(claims);
        for (Group group : groups) {
            this.drawGroup(group);
        }
    }

    private List<Group> groupClaims(List<Claim> claims) {
        // break groups down by owner
        Map<UUID, List<Claim>> byOwner = new HashMap<>();
        for (Claim claim : claims) {
            List<Claim> list = byOwner.getOrDefault(claim.owner(), new ArrayList<>());
            list.add(claim);
            byOwner.put(claim.owner(), list);
        }

        // combine touching claims
        Map<UUID, List<Group>> groups = new HashMap<>();
        for (Map.Entry<UUID, List<Claim>> entry : byOwner.entrySet()) {
            UUID owner = entry.getKey();
            List<Claim> list = entry.getValue();
            next1:
            for (Claim claim : list) {
                List<Group> groupList = groups.getOrDefault(owner, new ArrayList<>());
                for (Group group : groupList) {
                    if (group.isTouching(claim)) {
                        group.add(claim);
                        continue next1;
                    }
                }
                groupList.add(new Group(claim, owner));
                groups.put(owner, groupList);
            }
        }

        // combined touching groups
        List<Group> combined = new ArrayList<>();
        for (List<Group> list : groups.values()) {
            next:
            for (Group group : list) {
                for (Group toChk : combined) {
                    if (toChk.isTouching(group)) {
                        toChk.add(group);
                        continue next;
                    }
                }
                combined.add(group);
            }
        }

        return combined;
    }

    private void drawGroup(Group group) {
        final Polygon polygon = RectangleMerge.getPoly(group.claims());
        final MarkerOptions.Builder options = options(group.owner());
        polygon.markerOptions(options);

        final Key markerKey = key("claimchunk_chunk_" + group.id());
        this.provider.addMarker(markerKey, polygon);
    }

    private void drawChunk(DataChunk claim) {
        int minX = claim.chunk.getX() << 4;
        int maxX = (claim.chunk.getX() + 1) << 4;
        int minZ = claim.chunk.getZ() << 4;
        int maxZ = (claim.chunk.getZ() + 1) << 4;

        final Rectangle rect = Marker.rectangle(Point.of(minX, minZ), Point.of(maxX, maxZ));
        final MarkerOptions.Builder options = options(claim.player);
        rect.markerOptions(options);

        final Key markerKey = key("claimchunk_chunk_" + minX + "_" + minZ);
        this.provider.addMarker(markerKey, rect);
    }

    private MarkerOptions.Builder options(UUID owner) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
        final String ownerName = player.getName() == null ? "unknown" : player.getName();
        return MarkerOptions.builder()
            .strokeColor(this.plugin.config().strokeColor)
            .strokeWeight(this.plugin.config().strokeWeight)
            .strokeOpacity(this.plugin.config().strokeOpacity)
            .fillColor(this.plugin.config().fillColor)
            .fillOpacity(this.plugin.config().fillOpacity)
            .clickTooltip(
                this.plugin.config().claimTooltip
                    .replace("{world}", this.bukkitWorld.getName())
                    .replace("{owner}", ownerName)
            );
    }

    public void disable() {
        this.cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
