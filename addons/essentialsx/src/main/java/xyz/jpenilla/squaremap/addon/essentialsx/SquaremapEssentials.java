package xyz.jpenilla.squaremap.addon.essentialsx;

import java.io.File;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.essentialsx.config.EssXConfig;
import xyz.jpenilla.squaremap.addon.essentialsx.hook.SquaremapHook;
import xyz.jpenilla.squaremap.addon.essentialsx.listener.EssentialsListener;

public final class SquaremapEssentials extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private EssXConfig config;

    public EssXConfig config() {
        return this.config;
    }

    @Override
    public void onEnable() {
        this.config = new EssXConfig(this);
        this.config.reload();

        if (!new File(this.getDataFolder(), "warp.png").exists()) {
            this.saveResource("warp.png", false);
        }

        this.squaremapHook = new SquaremapHook(this);
        this.squaremapHook.load();

        this.getServer().getPluginManager().registerEvents(new EssentialsListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
        this.squaremapHook = null;
    }
}
