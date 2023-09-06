package xyz.jpenilla.squaremap.addon.signs.data;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.api.marker.Marker;

public record Data(
    Marker marker,
    SignType type,
    @Nullable List<Component> front,
    @Nullable List<Component> back
) {
}
