# Mobs

Display live mobs on your map.

## Configuration

### Global options

* `update-interval` - How often (in seconds) to update mobs on the map (default: `5`)

### Per-world options

* `enabled` - Enable or disable mobs for this world (default: `true`)
* `layer.label` - Name of the layer shown in the UI (default: `"Mobs"`)
* `layer.controls.enabled` - Show layer toggle controls (default: `true`)
* `layer.controls.hide-by-default` - Hide the layer by default (default: `false`)
* `layer.priority` - Layer stacking order (default: `999`)
* `layer.z-index` - CSS z-index for the layer (default: `999`)
* `icon.size` - Size in pixels of mob icons (default: `16`)
* `icon.tooltip` - Tooltip text when hovering over a mob. Use `{name}` for the mob name (default: `'{name}'`)
* `allowed-mobs` - List of mob types to display. Use Minecraft entity type names (lowercase with underscores). Use `'*'` to show all supported mobs. By default, only passive and neutral mobs are displayed.

### Allowed-mobs examples

Show all mobs:

```yaml
worlds:
  world:
    allowed-mobs:
      - '*'
```

Show specific mobs (mix of passive and hostile):

```yaml
worlds:
  world:
    allowed-mobs:
      - cow
      - pig
      - sheep
      - blaze
      - creeper
      - skeleton
      - zombie
```

## Supported mobs

### Passive/neutral

allay, armadillo, axolotl, bat, bee, camel, camel_husk, cat, chicken, cod, cow, dolphin, donkey, fox, frog, glow_squid, goat, happy_ghast, horse, iron_golem, llama, mooshroom, mule, nautilus, ocelot, panda, parrot, pig, polar_bear, pufferfish, rabbit, salmon, sheep, sniffer, snow_golem, squid, strider, tadpole, trader_llama, tropical_fish, turtle, villager, wandering_trader, wolf

#### Exceptions

zombie_horse & zombie_nautilus are not added to the default passive/neutral mobs list, since they become passive/neutral once their hostile mob rider gets separated from them.

---

### Hostile

blaze, breeze, cave_spider, creaking, creeper, drowned, elder_guardian, ender_dragon, enderman, endermite, evoker, ghast, giant, guardian, hoglin, husk, illusioner, magma_cube, parched, phantom, piglin, piglin_brute, pillager, ravager, shulker, silverfish, skeleton, skeleton_horse, slime, spider, stray, vex, vindicator, warden, witch, wither, wither_skeleton, zoglin, zombie, zombie_horse, zombie_nautilus, zombie_villager, zombified_piglin
