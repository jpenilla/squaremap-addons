package xyz.jpenilla.squaremap.addon.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.worldguard.config.WGConfig;
import xyz.jpenilla.squaremap.addon.worldguard.hook.SquaremapHook;

public final class SquaremapWorldGuard extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private WGConfig config;
    private StateFlag visibleFlag;

    @Override
    public void onEnable() {
        this.config = new WGConfig(this);
        this.config.reload();

        this.squaremapHook = new SquaremapHook(this);
    }

    @Override
    public void onLoad() {
        this.setupFlags();
    }

    private void setupFlags() {
        final FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        this.visibleFlag = new StateFlag("squaremap-visible", true);
        flagRegistry.register(this.visibleFlag);
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }

    public WGConfig config() {
        return this.config;
    }

    public StateFlag visibleFlag() {
        return this.visibleFlag;
    }
}
