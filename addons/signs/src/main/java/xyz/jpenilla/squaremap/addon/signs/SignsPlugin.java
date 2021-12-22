package xyz.jpenilla.squaremap.addon.signs;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.signs.configuration.Config;
import xyz.jpenilla.squaremap.addon.signs.data.Icons;
import xyz.jpenilla.squaremap.addon.signs.data.SignManager;
import xyz.jpenilla.squaremap.addon.signs.hook.SquaremapHook;
import xyz.jpenilla.squaremap.addon.signs.listener.SignListener;
import xyz.jpenilla.squaremap.addon.signs.listener.WorldListener;
import xyz.jpenilla.squaremap.api.Key;

public final class SignsPlugin extends JavaPlugin {
    private static SignsPlugin instance;
    private SquaremapHook squaremapHook;
    private SignManager signManager;

    public SignsPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();

        @SuppressWarnings("unused")
        Key staticInit = Icons.OAK;

        this.squaremapHook = new SquaremapHook();
        this.squaremapHook.load();

        this.signManager = new SignManager(this);
        this.signManager.load();

        this.getServer().getPluginManager().registerEvents(new SignListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.signManager != null) {
            this.signManager.save();
            this.signManager = null;
        }

        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
            this.signManager = null;
        }
    }

    public static SignsPlugin getInstance() {
        return instance;
    }

    public SquaremapHook squaremapHook() {
        return this.squaremapHook;
    }

    public SignManager getSignManager() {
        return this.signManager;
    }
}
