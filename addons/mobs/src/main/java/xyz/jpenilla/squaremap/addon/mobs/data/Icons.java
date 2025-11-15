package xyz.jpenilla.squaremap.addon.mobs.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.bukkit.entity.EntityType;
import xyz.jpenilla.squaremap.addon.mobs.SquaremapMobs;
import xyz.jpenilla.squaremap.addon.mobs.config.MobsWorldConfig;
import xyz.jpenilla.squaremap.api.Key;
import xyz.jpenilla.squaremap.api.SquaremapProvider;

public final class Icons {
    public static final Map<EntityType, Key> BY_TYPE = new HashMap<>();

    public static final Key ALLAY = register("allay");
    public static final Key ARMADILLO = register("armadillo");
    public static final Key BAT = register("bat");
    public static final Key BEE = register("bee");
    public static final Key BLAZE = register("blaze");
    public static final Key BREEZE = register("breeze");
    public static final Key CAMEL = register("camel");
    public static final Key CAT = register("cat");
    public static final Key CAVE_SPIDER = register("cave_spider");
    public static final Key CHICKEN = register("chicken");
    public static final Key COD = register("cod");
    public static final Key COW = register("cow");
    public static final Key CREAKING = register("creaking");
    public static final Key CREEPER = register("creeper");
    public static final Key DOLPHIN = register("dolphin");
    public static final Key DONKEY = register("donkey");
    public static final Key DROWNED = register("drowned");
    public static final Key ELDER_GUARDIAN = register("elder_guardian");
    public static final Key ENDER_DRAGON = register("ender_dragon");
    public static final Key ENDERMAN = register("enderman");
    public static final Key ENDERMITE = register("endermite");
    public static final Key EVOKER = register("evoker");
    public static final Key FOX = register("fox");
    public static final Key FROG = register("frog");
    public static final Key GHAST = register("ghast");
    public static final Key GIANT = register("giant");
    public static final Key GOAT = register("goat");
    public static final Key GUARDIAN = register("guardian");
    public static final Key HAPPY_GHAST = register("happy_ghast");
    public static final Key HOGLIN = register("hoglin");
    public static final Key HORSE = register("horse");
    public static final Key HUSK = register("husk");
    public static final Key ILLUSIONER = register("illusioner");
    public static final Key IRON_GOLEM = register("iron_golem");
    // public static final Key KILLER_BUNNY = register("killer_bunny");
    public static final Key LLAMA = register("llama");
    public static final Key MAGMA_CUBE = register("magma_cube");
    public static final Key MOOSHROOM = register("mooshroom");
    public static final Key MULE = register("mule");
    public static final Key OCELOT = register("ocelot");
    public static final Key PANDA = register("panda");
    public static final Key PARROT = register("parrot");
    public static final Key PHANTOM = register("phantom");
    public static final Key PIG = register("pig");
    public static final Key PIGLIN = register("piglin");
    public static final Key PIGLIN_BRUTE = register("piglin_brute");
    public static final Key PILLAGER = register("pillager");
    public static final Key POLAR_BEAR = register("polar_bear");
    public static final Key PUFFERFISH = register("pufferfish");
    public static final Key RABBIT = register("rabbit");
    public static final Key RAVAGER = register("ravager");
    public static final Key SALMON = register("salmon");
    public static final Key SHEEP = register("sheep");
    public static final Key SHULKER = register("shulker");
    public static final Key SILVERFISH = register("silverfish");
    public static final Key SKELETON = register("skeleton");
    public static final Key SKELETON_HORSE = register("skeleton_horse");
    public static final Key SLIME = register("slime");
    public static final Key SNIFFER = register("sniffer");
    public static final Key SNOW_GOLEM = register("snow_golem");
    public static final Key SPIDER = register("spider");
    public static final Key SQUID = register("squid");
    public static final Key STRAY = register("stray");
    public static final Key STRIDER = register("strider");
    public static final Key TADPOLE = register("tadpole");
    public static final Key TRADER_LLAMA = register("trader_llama");
    public static final Key TROPICAL_FISH = register("tropical_fish");
    public static final Key TURTLE = register("turtle");
    public static final Key VEX = register("vex");
    public static final Key VILLAGER = register("villager");
    public static final Key VINDICATOR = register("vindicator");
    public static final Key WANDERING_TRADER = register("wandering_trader");
    public static final Key WARDEN = register("warden");
    public static final Key WITCH = register("witch");
    public static final Key WITHER = register("wither");
    public static final Key WITHER_SKELETON = register("wither_skeleton");
    public static final Key WOLF = register("wolf");
    public static final Key ZOGLIN = register("zoglin");
    public static final Key ZOMBIE = register("zombie");
    public static final Key ZOMBIE_HORSE = register("zombie_horse");
    public static final Key ZOMBIE_VILLAGER = register("zombie_villager");
    public static final Key ZOMBIFIED_PIGLIN = register("zombified_piglin");

    private static Key register(String name) {
        SquaremapMobs plugin = SquaremapMobs.getInstance();
        String filename = "icons" + File.separator + name + ".png";
        File file = new File(plugin.getDataFolder(), filename);
        if (!file.exists()) {
            plugin.saveResource(filename, false);
        }

        final Key key = Key.of(name);
        try {
            final BufferedImage image = ImageIO.read(file);
            SquaremapProvider.get().iconRegistry().register(key, image);

            final EntityType type = MobsWorldConfig.parseEntityType(name);
            if (type == null) {
                throw new RuntimeException("unknown entity type: " + name);
            }

            BY_TYPE.put(type, key);
        } catch (final Exception e) {
            SquaremapMobs.getInstance().getLogger().log(Level.WARNING, "Failed to register signs icon", e);
        }
        return key;
    }

    public static Key getIcon(EntityType type) {
        return BY_TYPE.get(type);
    }
}
