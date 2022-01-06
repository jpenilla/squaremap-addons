package xyz.jpenilla.squaremap.addon.vanish;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jpenilla.squaremap.api.Squaremap;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class SquaremapVanish extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        final Squaremap squaremap = SquaremapProvider.get();

        final boolean superVanish = this.getServer().getPluginManager().isPluginEnabled("SuperVanish") ||
            this.getServer().getPluginManager().isPluginEnabled("PremiumVanish");
        if (superVanish) {
            this.getServer().getPluginManager().registerEvents(new SuperVanish(squaremap), this);
        }

        if (!superVanish) {
            this.getLogger().warning("You have installed squaremap-vanish without any supported vanish plugins.");
            this.getLogger().warning("Supported vanish plugins include: SuperVanish, PremiumVanish");
        }
    }
}
