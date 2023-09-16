package xyz.jpenilla.squaremap.addon.signs.data;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import xyz.jpenilla.squaremap.addon.common.Util;
import xyz.jpenilla.squaremap.addon.signs.SignsPlugin;
import xyz.jpenilla.squaremap.addon.signs.config.SignsWorldConfig;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.LayerProvider;
import xyz.jpenilla.squaremap.api.marker.Icon;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

@DefaultQualifier(NonNull.class)
public final class SignLayerProvider implements LayerProvider {
    private final Map<Position, Data> data = new ConcurrentHashMap<>();
    private final SignsWorldConfig worldConfig;
    private final SignsPlugin plugin;
    private final @Nullable Pattern customIconPattern;

    public SignLayerProvider(final SignsWorldConfig worldConfig, final SignsPlugin plugin) {
        this.worldConfig = worldConfig;
        this.plugin = plugin;
        if (this.worldConfig.iconCustom) {
            this.customIconPattern = this.compileCustomIconPattern();
            if (this.plugin.customIcons().isEmpty()) {
                this.plugin.getSLF4JLogger().warn("custom icons were requested but no image files were found in customicons/");
            }
        } else {
            this.customIconPattern = null;
        }
    }

    @Override
    public String getLabel() {
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
    public Collection<Marker> getMarkers() {
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

    public @Nullable Data getData(Position position) {
        return this.data.get(position);
    }

    public void add(
        final Position position,
        final SignType signType,
        @Nullable List<Component> front,
        @Nullable List<Component> back
    ) {
        final Key iconKey = this.tryExtractCustomIcon(front).orElse(signType.iconKey);
        Icon icon = Marker.icon(position.point(), iconKey, worldConfig.iconSize);
        front = front == null || isBlank(front) ? null : front;
        back = back == null || isBlank(back) ? null : back;
        icon.markerOptions(MarkerOptions.builder().hoverTooltip(this.renderHoverTooltip(front, back)));
        this.data.put(position, new Data(icon, signType, front, back));
    }

    private @Nullable String renderHoverTooltip(
        final @Nullable List<Component> front,
        final @Nullable List<Component> back
    ) {
        final boolean frontBlank = front == null;
        final boolean backBlank = back == null;
        if (frontBlank && backBlank) {
            if (this.worldConfig.blankTooltip.isBlank()) {
                return null;
            }
            return this.worldConfig.blankTooltip;
        } else if (backBlank) {
            return this.worldConfig.frontOnlyTooltip
                .replace("{line1}", Util.asHtml(front.get(0)))
                .replace("{line2}", Util.asHtml(front.get(1)))
                .replace("{line3}", Util.asHtml(front.get(2)))
                .replace("{line4}", Util.asHtml(front.get(3)));
        } else if (frontBlank) {
            return this.worldConfig.backOnlyTooltip
                .replace("{line1}", Util.asHtml(back.get(0)))
                .replace("{line2}", Util.asHtml(back.get(1)))
                .replace("{line3}", Util.asHtml(back.get(2)))
                .replace("{line4}", Util.asHtml(back.get(3)));
        }
        return this.worldConfig.frontAndBackTooltip
            .replace("{line1}", Util.asHtml(front.get(0)))
            .replace("{line2}", Util.asHtml(front.get(1)))
            .replace("{line3}", Util.asHtml(front.get(2)))
            .replace("{line4}", Util.asHtml(front.get(3)))
            .replace("{line1b}", Util.asHtml(back.get(0)))
            .replace("{line2b}", Util.asHtml(back.get(1)))
            .replace("{line3b}", Util.asHtml(back.get(2)))
            .replace("{line4b}", Util.asHtml(back.get(3)));
    }

    private static boolean isBlank(final List<Component> components) {
        for (final Component component : components) {
            if (!PlainTextComponentSerializer.plainText().serialize(component).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void remove(Position position) {
        this.data.remove(position);
    }

    private final Optional<Key> tryExtractCustomIcon(final @Nullable List<Component> front) {
        if (this.customIconPattern == null || front == null || front.get(0) == null) {
            return Optional.empty();
        }

        try {
            final String firstLine = PlainTextComponentSerializer.plainText().serialize(front.get(0));
            final Matcher match = this.customIconPattern.matcher(firstLine);
            if (match != null && match.matches()) {
                // get last regexp capture group
                final String matchText = match.group(match.groupCount());
                return this.plugin.customIcons().lookup(matchText);
            }
        } catch (final Exception e) {
        }

        return Optional.empty();
    }

    private final @Nullable Pattern compileCustomIconPattern() {
        try {
            return Pattern.compile(worldConfig.iconCustomRegexp);
        } catch (final Exception e) {
            this.plugin.getSLF4JLogger().warn("invalid regexp in \"icon.custom.regexp\", custom icons disabled: {}",
                    e.getLocalizedMessage());
        }
        return null;
    }
}
