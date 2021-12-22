package xyz.jpenilla.squaremap.addon.griefprevention.hook;

import java.util.Collection;
import java.util.UUID;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class GPHook {
    public static boolean isWorldEnabled(UUID uuid) {
        World world = Bukkit.getWorld(uuid);
        return GriefPrevention.instance.claimsEnabledForWorld(world);
    }

    public static Collection<Claim> getClaims() {
        return GriefPrevention.instance.dataStore.getClaims();
    }
}
