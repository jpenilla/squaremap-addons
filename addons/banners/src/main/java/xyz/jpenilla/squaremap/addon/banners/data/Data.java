package xyz.jpenilla.squaremap.addon.banners.data;

import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.marker.Marker;

public record Data(Marker marker, Key key, String name) {
}
