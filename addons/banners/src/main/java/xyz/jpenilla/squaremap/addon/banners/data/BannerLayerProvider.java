package xyz.jpenilla.squaremap.addon.banners.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.jpenilla.squaremap.addon.banners.SquaremapBanners;
import xyz.jpenilla.squaremap.addon.banners.configuration.BannersWorldConfig;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.LayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

public class BannerLayerProvider implements LayerProvider {
    private final Map<Position, Data> data = new ConcurrentHashMap<>();
    private final BannersWorldConfig worldConfig;

    public BannerLayerProvider(BannersWorldConfig worldConfig) {
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

    public void add(Position position, Key key, String name) {
        name = name == null ? "null" : name;
        SquaremapBanners.instance().debug(position + " icon: " + key + " name: " + name);
        Icon icon = Marker.icon(position.point(), key, worldConfig.ICON_SIZE);
        icon.markerOptions(MarkerOptions.builder()
            .hoverTooltip(worldConfig.tooltip
                .replace("{name}", name)));
        this.data.put(position, new Data(icon, key, name));
    }

    public void remove(Position position) {
        SquaremapBanners.instance().debug("Removing banner from " + position);
        this.data.remove(position);
    }
}
