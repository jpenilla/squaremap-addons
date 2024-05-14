package xyz.jpenilla.squaremap.addon.banners;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.banners.configuration.BannersConfig;
import xyz.jpenilla.squaremap.addon.banners.data.BannersManager;
import xyz.jpenilla.squaremap.addon.banners.data.Icons;
import xyz.jpenilla.squaremap.addon.banners.hook.SquaremapHook;
import xyz.jpenilla.squaremap.addon.banners.listener.BannersListener;
import xyz.jpenilla.squaremap.addon.banners.listener.WorldListener;
import xyz.jpenilla.squaremap.api.Key;

public final class SquaremapBanners extends JavaPlugin {
    private static SquaremapBanners instance;
    private SquaremapHook squaremapHook;
    private BannersManager bannerManager;
    private BannersConfig config;

    public SquaremapBanners() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.config = new BannersConfig(this);
        this.config.reload();

        final Runnable load = () -> {
            this.squaremapHook = new SquaremapHook();
            this.squaremapHook.load();

            this.bannerManager = new BannersManager(this);
            this.bannerManager.load();
        };

        this.config.registerReloadCommand(() -> {
            this.onDisable();
            load.run();
        });

        //noinspection unused
        Key loadme = Icons.WHITE;

        load.run();

        getServer().getPluginManager().registerEvents(new BannersListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldListener(this), this);
    }

    @Override
    public void onDisable() {
        if (bannerManager != null) {
            bannerManager.save();
            bannerManager = null;
        }

        if (squaremapHook != null) {
            squaremapHook.disable();
            bannerManager = null;
        }
    }

    public BannersConfig config() {
        return this.config;
    }

    public static SquaremapBanners instance() {
        return instance;
    }

    public SquaremapHook squaremapHook() {
        return squaremapHook;
    }

    public BannersManager bannerManager() {
        return bannerManager;
    }

    public void debug(String msg) {
        if (!this.config().debugMode) {
            return;
        }
        for (String part : msg.split("\n")) {
            this.getLogger().info("[debug] " + part);
        }
    }
}
