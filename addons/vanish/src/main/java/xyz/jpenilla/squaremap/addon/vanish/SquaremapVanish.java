package xyz.jpenilla.squaremap.addon.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class SquaremapVanish extends JavaPlugin implements Listener {
    private Squaremap squaremap;

    private VanishAdapter vanishAdapter = null;

    @Override
    public void onEnable() {
        this.squaremap = SquaremapProvider.get();

        final boolean superVanish = this.getServer().getPluginManager().isPluginEnabled("SuperVanish") ||
            this.getServer().getPluginManager().isPluginEnabled("PremiumVanish");
        if (superVanish) {
            this.vanishAdapter = new SuperVanish(this.squaremap);
        }

        if (this.vanishAdapter == null) {
            this.getLogger().info("You have installed squaremap-vanish without any explicitly supported vanish plugins (SuperVanish, PremiumVanish). Trying to get vanish status from 'vanished' player metadata value used by some vanish plugins.");
            this.vanishAdapter = new VanishFallback(this, squaremap);
        }

        this.getServer().getPluginManager().registerEvents(vanishAdapter, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void join(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (this.vanishAdapter.isVanished(player)) {
            this.squaremap.playerManager().hide(player.getUniqueId());
        }
    }
}
