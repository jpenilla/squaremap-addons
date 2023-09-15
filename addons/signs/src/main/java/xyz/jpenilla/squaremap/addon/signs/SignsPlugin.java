package xyz.jpenilla.squaremap.addon.signs;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.signs.config.SignsConfig;
import xyz.jpenilla.squaremap.addon.signs.data.CustomIcons;
import xyz.jpenilla.squaremap.addon.signs.data.Icons;
import xyz.jpenilla.squaremap.addon.signs.data.SignManager;
import xyz.jpenilla.squaremap.addon.signs.hook.LayerProviderManager;
import xyz.jpenilla.squaremap.addon.signs.listener.SignListener;
import xyz.jpenilla.squaremap.addon.signs.listener.WorldListener;

public final class SignsPlugin extends JavaPlugin {
    private static SignsPlugin instance;
    private LayerProviderManager layerProviderManager;
    private SignManager signManager;
    private SignsConfig config;
    private CustomIcons customIcons;

    public SignsPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.config = new SignsConfig(this);
        this.config.reload();

        Icons.register(this);
        this.customIcons = CustomIcons.register(this);

        this.layerProviderManager = new LayerProviderManager(this);
        this.signManager = new SignManager(this);
        this.layerProviderManager.load();

        this.getServer().getPluginManager().registerEvents(new SignListener(this), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    @Override
    public void onDisable() {
        if (this.layerProviderManager != null) {
            this.layerProviderManager.disable();
            this.layerProviderManager = null;
        }
        this.signManager = null;

        this.customIcons.unregister();
        Icons.unregister();
    }

    public static SignsPlugin instance() {
        return instance;
    }

    public LayerProviderManager layerProviders() {
        return this.layerProviderManager;
    }

    public SignManager signManager() {
        return this.signManager;
    }

    public SignsConfig config() {
        return this.config;
    }

    public CustomIcons customIcons() {
        return this.customIcons;
    }
}
