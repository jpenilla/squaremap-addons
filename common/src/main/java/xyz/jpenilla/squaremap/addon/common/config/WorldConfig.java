package xyz.jpenilla.squaremap.addon.common.config;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("unused")
public abstract class WorldConfig {
    public static final String DOT = "____dot____";

    private final String worldIdentifier;
    private final Config<?, ?> parent;

    protected WorldConfig(final Config<?, ?> parent, final String world) {
        this.parent = parent;
        this.worldIdentifier = world.replace(".", DOT);
    }

    protected final boolean getBoolean(String path, boolean def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getBoolean(wrapDefaultPath(path), def);
        }
        return this.parent.getBoolean(this.wrapPath(path), this.parent.getBoolean(wrapDefaultPath(path), def));
    }

    protected final int getInt(String path, int def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getInt(wrapDefaultPath(path), def);
        }
        return this.parent.getInt(this.wrapPath(path), this.parent.getInt(wrapDefaultPath(path), def));
    }

    protected final double getDouble(String path, double def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getDouble(wrapDefaultPath(path), def);
        }
        return this.parent.getDouble(this.wrapPath(path), this.parent.getDouble(wrapDefaultPath(path), def));
    }

    protected final String getString(String path, String def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getString(wrapDefaultPath(path), def);
        }
        return this.parent.getString(this.wrapPath(path), this.parent.getString(wrapDefaultPath(path), def));
    }

    protected final <T> List<T> getList(Class<T> elementType, String path, List<T> def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getList(elementType, wrapDefaultPath(path), def);
        }
        return this.parent.getList(elementType, this.wrapPath(path), this.parent.getList(elementType, wrapDefaultPath(path), def));
    }

    protected final Color getColor(String path, Color def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getColor(wrapDefaultPath(path), def);
        }
        return this.parent.getColor(this.wrapPath(path), this.parent.getColor(wrapDefaultPath(path), def));
    }

    protected final <E extends Enum<E>> E getEnum(String path, Class<E> enumClass, E def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.getEnum(wrapDefaultPath(path), enumClass, def);
        }
        return this.parent.getEnum(this.wrapPath(path), enumClass, this.parent.getEnum(wrapDefaultPath(path), enumClass, def));
    }

    protected final <T> T get(String path, Class<T> type, T def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.get(wrapDefaultPath(path), type, def);
        }
        return this.parent.get(this.wrapPath(path), type, this.parent.get(wrapDefaultPath(path), type, def));
    }

    protected final <T> T get(String path, Type type, T def) {
        if (this.virtual(this.wrapPath(path))) {
            return this.parent.get(wrapDefaultPath(path), type, def);
        }
        return this.parent.get(this.wrapPath(path), type, this.parent.get(wrapDefaultPath(path), type, def));
    }

    private boolean virtual(String path) {
        return this.parent.config.node(Config.splitPath(path)).virtual();
    }

    private String wrapPath(final String path) {
        return "world-settings." + this.worldIdentifier + "." + path;
    }

    private static String wrapDefaultPath(final String path) {
        return "world-settings.default." + path;
    }
}
