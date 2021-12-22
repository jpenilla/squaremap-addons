package xyz.jpenilla.squaremap.addon.deathspot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.deathspot.configuration.Config;
import xyz.jpenilla.squaremap.addon.deathspot.hook.SquaremapHook;
import xyz.jpenilla.squaremap.addon.deathspot.listener.PlayerListener;
import xyz.jpenilla.squaremap.api.Pair;

public final class DeathSpots extends JavaPlugin {
    private static DeathSpots instance;
    private final Map<UUID, Pair<String, Location>> deathSpots = new HashMap<>();
    private SquaremapHook squaremapHook;

    public DeathSpots() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();

        if (!new File(this.getDataFolder(), "icon.png").exists()) {
            this.saveResource("icon.png", false);
        }

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        this.squaremapHook = new SquaremapHook(this);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
        this.deathSpots.clear();
    }

    public static DeathSpots getInstance() {
        return instance;
    }

    public Map<UUID, Pair<String, Location>> getDeathSpots() {
        return this.deathSpots;
    }
}
