package xyz.jpenilla.squaremap.addon.worldguard;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.worldguard.config.WGConfig;
import xyz.jpenilla.squaremap.addon.worldguard.hook.SquaremapHook;

public final class SquaremapWorldGuard extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private WGConfig config;
    public StateFlag visibleFlag;
    public StringFlag strokeColorFlag;
    public IntegerFlag strokeWeightFlag;
    public DoubleFlag strokeOpacityFlag;
    public StringFlag fillColorFlag;
    public DoubleFlag fillOpacityFlag;
    public StringFlag clickTooltipFlag;

    @Override
    public void onEnable() {
        this.config = new WGConfig(this);
        this.config.reload();

        final Runnable load = () -> {
            this.squaremapHook = new SquaremapHook(this);
        };

        this.config.registerReloadCommand(() -> {
            this.onDisable();
            load.run();
        });

        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onLoad() {
        this.setupFlags();
    }

    private void setupFlags() {
        this.visibleFlag = new StateFlag("squaremap-visible", true);
        this.strokeColorFlag = new StringFlag("squaremap-stroke-color", (String) null);
        this.strokeWeightFlag = new IntegerFlag("squaremap-stroke-weight");
        this.strokeOpacityFlag = new DoubleFlag("squaremap-stroke-opacity");
        this.fillColorFlag = new StringFlag("squaremap-fill-color", (String) null);
        this.fillOpacityFlag = new DoubleFlag("squaremap-fill-opacity");
        this.clickTooltipFlag = new StringFlag("squaremap-click-tooltip", (String) null);

        final FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();
        flagRegistry.registerAll(List.of(
            this.visibleFlag, this.strokeColorFlag, this.strokeWeightFlag, this.strokeOpacityFlag, this.fillColorFlag, this.fillOpacityFlag, this.clickTooltipFlag
        ));
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

    public SquaremapHook squaremapHook() {
        return this.squaremapHook;
    }
}
