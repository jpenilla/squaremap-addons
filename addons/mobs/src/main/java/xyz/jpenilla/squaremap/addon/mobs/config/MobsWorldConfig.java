package xyz.jpenilla.squaremap.addon.mobs.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.jpenilla.squaremap.addon.common.config.Config;
import xyz.jpenilla.squaremap.addon.common.config.WorldConfig;
import xyz.jpenilla.squaremap.addon.mobs.SquaremapMobs;
import xyz.jpenilla.squaremap.addon.mobs.data.Icons;

@SuppressWarnings("unused")
public final class MobsWorldConfig extends WorldConfig {
    public boolean enabled = true;

    private void worldSettings() {
        this.enabled = this.getBoolean("enabled", this.enabled);
    }

    public String layerLabel = "Mobs";
    public boolean layerShowControls = true;
    public boolean layerControlsHidden = false;
    public int layerPriority = 999;
    public int layerZindex = 999;

    private void layerSettings() {
        this.layerLabel = this.getString("layer.label", this.layerLabel);
        this.layerShowControls = this.getBoolean("layer.controls.enabled", this.layerShowControls);
        this.layerControlsHidden = this.getBoolean("layer.controls.hide-by-default", this.layerControlsHidden);
        this.layerPriority = this.getInt("layer.priority", this.layerPriority);
        this.layerZindex = this.getInt("layer.z-index", this.layerZindex);
    }

    public int minimumY = 64;
    public boolean surfaceOnly = true;

    public int iconSize = 16;
    public String iconTooltip = "{name}";

    private void iconSettings() {
        this.iconSize = getInt("icon.size", this.iconSize);
        this.iconTooltip = getString("icon.tooltip", this.iconTooltip);
    }

    public final Set<EntityType> allowedTypes = new HashSet<>();

    private void allowedTypes() {
        this.allowedTypes.clear();
        this.getList(
            String.class,
            "allowed-mobs",
            List.of(
                "allay",
                "armadillo",
                "camel",
                "cat",
                "chicken",
                "cod",
                "cow",
                "dolphin",
                "fox",
                "frog",
                "goat",
                "happy_ghast",
                "horse",
                "iron_golem",
                "llama",
                "mooshroom",
                "mule",
                "ocelot",
                "panda",
                "parrot",
                "pig",
                "polar_bear",
                "pufferfish",
                "rabbit",
                "salmon",
                "sheep",
                "sniffer",
                "snow_golem",
                "squid",
                "strider",
                "tadpole",
                "trader_llama",
                "tropical_fish",
                "turtle",
                "villager",
                "wandering_trader",
                "wolf"
            )
        ).forEach(key -> {
            if (key.equals("*")) {
                this.allowedTypes.addAll(Icons.BY_TYPE.keySet());
                return;
            }
            EntityType type = parseEntityType(key);
            if (type != null) {
                this.allowedTypes.add(type);
            } else {
                SquaremapMobs.getInstance().getLogger().warning("Unknown entity type: " + key);
            }
        });
    }

    public static @Nullable EntityType parseEntityType(String name) {
        final NamespacedKey entityTypeKey = NamespacedKey.fromString(name);
        if (entityTypeKey == null) {
            return null;
        }
        return Registry.ENTITY_TYPE.get(entityTypeKey);
    }

    private MobsWorldConfig(Config<?, ?> parent, String world) {
        super(parent, world);
    }
}
