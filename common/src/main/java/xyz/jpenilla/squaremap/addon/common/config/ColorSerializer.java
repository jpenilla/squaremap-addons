package xyz.jpenilla.squaremap.addon.common.config;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

public final class ColorSerializer extends ScalarSerializer<Color> {
    public static final ColorSerializer INSTANCE = new ColorSerializer();

    private ColorSerializer() {
        super(Color.class);
    }

    @Override
    public Color deserialize(Type type, Object obj) throws SerializationException {
        if (obj instanceof String string) {
            try {
                return parseHex(string);
            } catch (final NumberFormatException ex) {
                throw new SerializationException(ex);
            }
        }
        return null;
    }

    @Override
    public Object serialize(Color obj, Predicate<Class<?>> typeSupported) {
        if (obj == null) {
            return null;
        }
        return String.format("#%06x", obj.getRGB() & 0x00FFFFFF);
    }

    private static Color parseHex(final String hex) {
        final int rgb = Integer.parseInt(hex.charAt(0) == '#' ? hex.substring(1) : hex, 16);
        return new Color(rgb);
    }
}
