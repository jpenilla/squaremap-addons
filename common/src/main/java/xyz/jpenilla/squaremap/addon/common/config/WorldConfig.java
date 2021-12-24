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

    protected void set(String path, Object val) {
        this.parent.config.addDefault(wrapDefaultPath(path), val);
        this.parent.config.set(wrapDefaultPath(path), val);
        if (this.parent.config.get(this.wrapPath(path)) != null) {
            this.parent.config.addDefault(this.wrapPath(path), val);
            this.parent.config.set(this.wrapPath(path), val);
        }
    }

    protected boolean getBoolean(String path, boolean def) {
        this.parent.config.addDefault(wrapDefaultPath(path), def);
        return this.parent.config.getBoolean(this.wrapPath(path), this.parent.config.getBoolean(wrapDefaultPath(path)));
    }

    protected int getInt(String path, int def) {
        this.parent.config.addDefault(wrapDefaultPath(path), def);
        return this.parent.config.getInt(this.wrapPath(path), this.parent.config.getInt(wrapDefaultPath(path)));
    }

    protected double getDouble(String path, double def) {
        this.parent.config.addDefault(wrapDefaultPath(path), def);
        return this.parent.config.getDouble(this.wrapPath(path), this.parent.config.getDouble(wrapDefaultPath(path)));
    }

    protected String getString(String path, String def) {
        this.parent.config.addDefault(wrapDefaultPath(path), def);
        return this.parent.config.getString(this.wrapPath(path), this.parent.config.getString(wrapDefaultPath(path)));
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String path, List<T> def) {
        this.parent.config.addDefault(wrapDefaultPath(path), def);
        return (List<T>) this.parent.config.getList(this.wrapPath(path), this.parent.config.getList(wrapDefaultPath(path)));
    }

    private String wrapPath(final String path) {
        return "world-settings." + this.worldIdentifier + "." + path;
    }

    private static String wrapDefaultPath(final String path) {
        return "world-settings.default." + path;
    }
}
