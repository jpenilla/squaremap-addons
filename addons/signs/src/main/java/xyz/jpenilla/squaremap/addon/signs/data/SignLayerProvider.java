package xyz.jpenilla.squaremap.addon.signs.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.jpenilla.squaremap.addon.signs.config.SignsWorldConfig;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.LayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public final class SignLayerProvider implements LayerProvider {
    private final Map<Position, Data> data = new ConcurrentHashMap<>();
    private final SignsWorldConfig worldConfig;

    public SignLayerProvider(SignsWorldConfig worldConfig) {
        this.worldConfig = worldConfig;
    }

    @Override
    public @NonNull String getLabel() {
        return this.worldConfig.layerLabel;
    }

    @Override
    public boolean showControls() {
        return this.worldConfig.layerControls;
    }

    @Override
    public boolean defaultHidden() {
        return this.worldConfig.layerControlsHidden;
    }

    @Override
    public int layerPriority() {
        return this.worldConfig.layerPriority;
    }

    @Override
    public int zIndex() {
        return this.worldConfig.layerZindex;
    }

    @Override
    public @NonNull Collection<Marker> getMarkers() {
        return this.data.values().stream()
                .map(Data::marker)
                .collect(Collectors.toSet());
    }

    public Set<Position> getPositions() {
        return this.data.keySet();
    }

    public Map<Position, Data> getData() {
        return this.data;
    }

    public Data getData(Position position) {
        return this.data.get(position);
    }

    public void add(Position position, Key key, String[] lines) {
        Icon icon = Marker.icon(position.point(), key, worldConfig.iconSize);
        icon.markerOptions(MarkerOptions.builder()
                .hoverTooltip(worldConfig.tooltip
                        .replace("{line1}", lines[0])
                        .replace("{line2}", lines[1])
                        .replace("{line3}", lines[2])
                        .replace("{line4}", lines[3])));
        // System.out.println("2"); // todo ?
        this.data.put(position, new Data(icon, key, lines));
    }

    public void remove(Position position) {
        this.data.remove(position);
    }
}
