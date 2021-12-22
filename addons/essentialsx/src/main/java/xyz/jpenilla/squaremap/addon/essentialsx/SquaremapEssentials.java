package xyz.jpenilla.squaremap.addon.essentialsx;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.essentialsx.configuration.Config;
import xyz.jpenilla.squaremap.addon.essentialsx.hook.SquaremapHook;
import xyz.jpenilla.squaremap.addon.essentialsx.listener.EssentialsListener;

public final class SquaremapEssentials extends JavaPlugin {
    private SquaremapHook squaremapHook;

    @Override
    public void onEnable() {
        Config.reload(this);

        if (!new File(this.getDataFolder(), "warp.png").exists()) {
            this.saveResource("warp.png", false);
        }

        this.squaremapHook = new SquaremapHook(this);
        this.squaremapHook.load();

        this.getServer().getPluginManager().registerEvents(new EssentialsListener(), this);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
        this.squaremapHook = null;
    }
}
