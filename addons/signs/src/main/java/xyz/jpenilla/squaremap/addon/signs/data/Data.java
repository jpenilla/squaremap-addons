package xyz.jpenilla.squaremap.addon.signs.data;

import net.kyori.adventure.text.Component;
import xyz.jpenilla.squaremap.api.marker.Marker;

public record Data(
    Marker marker,
    SignType type,
    Component[] front,
    Component[] back
) {
}
