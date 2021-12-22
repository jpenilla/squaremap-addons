package xyz.jpenilla.squaremap.addon.griefprevention;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.griefprevention.configuration.Config;
import xyz.jpenilla.squaremap.addon.griefprevention.hook.SquaremapHook;

public final class SquaremapGriefPrevention extends JavaPlugin {
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
