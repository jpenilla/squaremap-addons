package xyz.jpenilla.squaremap.addon.signs.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.addon.signs.config.SignsWorldConfig;
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

    public void clear() {
        this.data.clear();
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

    public void add(Position position, SignType signType, Component[] front, Component[] back) {
        Icon icon = Marker.icon(position.point(), signType.iconKey, worldConfig.iconSize);
        icon.markerOptions(MarkerOptions.builder().hoverTooltip(this.renderHoverTooltip(front, back)));
        this.data.put(position, new Data(icon, signType, front, back));
    }

    private @Nullable String renderHoverTooltip(Component[] front, Component[] back) {
        final boolean frontBlank = isBlank(front);
        final boolean backBlank = isBlank(back);
        if (frontBlank && backBlank) {
            if (this.worldConfig.blankTooltip.isBlank()) {
                return null;
            }
            return this.worldConfig.blankTooltip;
        } else if (backBlank) {
            return this.worldConfig.frontOnlyTooltip
                .replace("{line1}", serialize(front[0]))
                .replace("{line2}", serialize(front[1]))
                .replace("{line3}", serialize(front[2]))
                .replace("{line4}", serialize(front[3]));
        } else if (frontBlank) {
            return this.worldConfig.backOnlyTooltip
                .replace("{line1}", serialize(back[0]))
                .replace("{line2}", serialize(back[1]))
                .replace("{line3}", serialize(back[2]))
                .replace("{line4}", serialize(back[3]));
        }
        return this.worldConfig.frontAndBackTooltip
            .replace("{line1}", serialize(front[0]))
            .replace("{line2}", serialize(front[1]))
            .replace("{line3}", serialize(front[2]))
            .replace("{line4}", serialize(front[3]))
            .replace("{line1b}", serialize(back[0]))
            .replace("{line2b}", serialize(back[1]))
            .replace("{line3b}", serialize(back[2]))
            .replace("{line4b}", serialize(back[3]));
    }

    private static boolean isBlank(final Component[] components) {
        for (final Component component : components) {
            if (!PlainTextComponentSerializer.plainText().serialize(component).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static String serialize(final Component c) {
        return PlainTextComponentSerializer.plainText().serialize(c);
    }

    public void remove(Position position) {
        this.data.remove(position);
    }
}
