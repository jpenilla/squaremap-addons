package xyz.jpenilla.squaremap.addon.worldguard;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.worldguard.configuration.Config;
import xyz.jpenilla.squaremap.addon.worldguard.hook.SquaremapHook;

public final class SquaremapWorldGuard extends JavaPlugin {
    private SquaremapHook squaremapHook;

    @Override
    public void onEnable() {
        Config.reload(this);

        this.squaremapHook = new SquaremapHook(this);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }
}
