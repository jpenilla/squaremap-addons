package xyz.jpenilla.squaremap.addon.claimchunk;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.claimchunk.configuration.Config;
import xyz.jpenilla.squaremap.addon.claimchunk.hook.SquaremapHook;

public final class SquaremapClaimChunk extends JavaPlugin {
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
