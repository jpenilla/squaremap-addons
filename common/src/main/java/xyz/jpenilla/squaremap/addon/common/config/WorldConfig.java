package xyz.jpenilla.squaremap.addon.common.config;

import java.util.List;

@SuppressWarnings("unused")
public abstract class WorldConfig {
    private final String worldIdentifier;
    private final Config<?, ?> parent;

    protected WorldConfig(final Config<?, ?> parent, final String world) {
        this.parent = parent;
        this.worldIdentifier = world;
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

    private boolean virtual(String path) {
        return this.parent.config.node((Object[]) Config.splitPath(path)).virtual();
    }

    private String wrapPath(final String path) {
        return "world-settings." + this.worldIdentifier + "." + path;
    }

    private static String wrapDefaultPath(final String path) {
        return "world-settings.default." + path;
    }
}
