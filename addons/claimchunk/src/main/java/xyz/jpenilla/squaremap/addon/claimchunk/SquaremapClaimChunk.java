package xyz.jpenilla.squaremap.addon.claimchunk;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.addon.claimchunk.config.ClaimChunkConfig;
import xyz.jpenilla.squaremap.addon.claimchunk.hook.SquaremapHook;

public final class SquaremapClaimChunk extends JavaPlugin {
    private SquaremapHook squaremapHook;
    private ClaimChunkConfig config;

    @Override
    public void onEnable() {
        this.config = new ClaimChunkConfig(this);
        this.config.reload();

        final Runnable load = () -> {
            this.squaremapHook = new SquaremapHook(this);
        };

        this.config.registerReloadCommand(() -> {
            this.onDisable();
            load.run();
        });

        load.run();
    }

    @Override
    public void onDisable() {
        if (this.squaremapHook != null) {
            this.squaremapHook.disable();
        }
    }

    public ClaimChunkConfig config() {
        return this.config;
    }
}
