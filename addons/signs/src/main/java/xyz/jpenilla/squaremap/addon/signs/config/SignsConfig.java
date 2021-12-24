package xyz.jpenilla.squaremap.addon.signs.config;

import org.bukkit.plugin.Plugin;
import xyz.jpenilla.squaremap.addon.common.config.Config;

public final class SignsConfig extends Config<SignsConfig, SignsWorldConfig> {
    public boolean debugMode = false;

    @SuppressWarnings("unused")
    private void baseSettings() {
        this.debugMode = this.getBoolean("settings.debug-mode", this.debugMode);
    }

    public SignsConfig(Plugin plugin) {
        super(SignsConfig.class, SignsWorldConfig.class, plugin);
    }
}
