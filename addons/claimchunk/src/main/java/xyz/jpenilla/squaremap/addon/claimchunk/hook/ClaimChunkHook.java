package xyz.jpenilla.squaremap.addon.claimchunk.hook;

import com.cjburkey.claimchunk.ClaimChunk;
import com.cjburkey.claimchunk.chunk.DataChunk;
import com.cjburkey.claimchunk.data.newdata.IClaimChunkDataHandler;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;

public final class ClaimChunkHook {
    public static DataChunk[] getClaims() {
        final ClaimChunk cc =
            (ClaimChunk) Bukkit.getServer().getPluginManager().getPlugin("ClaimChunk");

        IClaimChunkDataHandler dataHandler;
        try {
            Field field = ClaimChunk.class.getDeclaredField("dataHandler");
            field.setAccessible(true);
            dataHandler = (IClaimChunkDataHandler) field.get(cc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return dataHandler.getClaimedChunks();
    }
}
