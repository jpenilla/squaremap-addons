package xyz.jpenilla.squaremap.addon.mobs.config;

import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;

public final class MobsConfig extends Config<MobsConfig, MobsWorldConfig> {
    public boolean debugMode = false;
    public int updateInterval = 5;

    @SuppressWarnings("unused")
    private void baseSettings() {
        this.debugMode = this.getBoolean("settings.debug-mode", this.debugMode);
        this.updateInterval = this.getInt("update-interval", this.updateInterval);
    }

    public MobsConfig(Plugin plugin) {
        super(MobsConfig.class, MobsWorldConfig.class, plugin);
    }
}
