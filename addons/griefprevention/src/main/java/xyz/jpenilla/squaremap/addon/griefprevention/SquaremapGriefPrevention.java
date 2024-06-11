package xyz.jpenilla.squaremap.addon.griefprevention;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.griefprevention.config.GPConfig;
import xyz.jpenilla.squaremap.addon.griefprevention.hook.SquaremapHook;

public final class SquaremapGriefPrevention extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private GPConfig config;

    public GPConfig config() {
        return this.config;
    }

    @Override
    public void onEnable() {
        this.config = new GPConfig(this);
        this.config.reload();

        final Runnable load = () -> {
            this.squaremapHook = new SquaremapHook(this);
        };

        this.config.registerReloadCommand(() -> {
            this.onDisable();
            load.run();
        });

        load.run();
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }
}
