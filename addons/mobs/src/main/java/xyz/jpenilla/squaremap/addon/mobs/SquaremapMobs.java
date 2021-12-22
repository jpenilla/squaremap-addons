package xyz.jpenilla.squaremap.addon.mobs;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.mobs.configuration.Config;
import xyz.jpenilla.squaremap.addon.mobs.data.Icons;
import xyz.jpenilla.squaremap.addon.mobs.hook.SquaremapHook;

public final class SquaremapMobs extends JavaPlugin {
    private static SquaremapMobs instance;
    private SquaremapHook squaremapHook;

    public SquaremapMobs() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();

        @SuppressWarnings("unused") final String staticInit = Icons.class.getTypeName();

        this.squaremapHook = new SquaremapHook(this);
        this.squaremapHook.load();
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
        this.squaremapHook = null;
    }

    public static SquaremapMobs getInstance() {
        return instance;
    }
}
