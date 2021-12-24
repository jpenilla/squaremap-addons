package xyz.jpenilla.squaremap.addon.deathspot.config;

import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;

public final class DeathSpotConfig extends Config<DeathSpotConfig, DeathSpotWorldConfig> {
    public DeathSpotConfig(final Plugin plugin) {
        super(DeathSpotConfig.class, DeathSpotWorldConfig.class, plugin);
    }

    public boolean debugMode = false;

    @SuppressWarnings("unused")
    private void baseSettings() {
        this.debugMode = this.getBoolean("settings.debug-mode", this.debugMode);
    }
}
