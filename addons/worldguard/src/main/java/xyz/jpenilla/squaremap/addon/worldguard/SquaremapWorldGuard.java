package xyz.jpenilla.squaremap.addon.worldguard;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.worldguard.config.WGConfig;
import xyz.jpenilla.squaremap.addon.worldguard.hook.SquaremapHook;

public final class SquaremapWorldGuard extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private WGConfig config;

    @Override
    public void onEnable() {
        this.config = new WGConfig(this);
        this.config.reload();

        this.squaremapHook = new SquaremapHook(this);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }

    public WGConfig config() {
        return this.config;
    }
}
