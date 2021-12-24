package xyz.jpenilla.squaremap.addon.essentialsx.config;

import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;

public final class EssXConfig extends Config<EssXConfig, EssXWorldConfig> {
    public boolean debugMode = false;
    public int updateInterval = 5;
    public boolean hideVanished = true;

    @SuppressWarnings("unused")
    private void baseSettings() {
        this.debugMode = this.getBoolean("settings.debug-mode", this.debugMode);
        this.updateInterval = this.getInt("settings.update-interval", this.updateInterval);
        this.hideVanished = this.getBoolean("settings.hide-vanished", this.hideVanished);
    }

    public EssXConfig(final Plugin plugin) {
        super(EssXConfig.class, EssXWorldConfig.class, plugin);
    }
}
