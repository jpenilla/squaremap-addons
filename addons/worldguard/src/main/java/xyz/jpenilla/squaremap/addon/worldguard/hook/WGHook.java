package xyz.jpenilla.squaremap.addon.worldguard.hook;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.World;
import xyz.jpenilla.squaremap.api.WorldIdentifier;

public final class WGHook {
    public static Map<String, ProtectedRegion> getRegions(WorldIdentifier identifier) {
        World bukkitWorld = Bukkit.getWorld(xyz.jpenilla.squaremap.api.BukkitAdapter.namespacedKey(identifier));
        if (bukkitWorld == null) {
            return null;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(BukkitAdapter.adapt(bukkitWorld));
        if (manager == null) {
            return null;
        }
        return manager.getRegions();
    }
}
