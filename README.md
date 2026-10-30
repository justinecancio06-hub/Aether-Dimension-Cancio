# Aether Dimension

> **Warning: Work in Progress**
>
> This mod is under active development. It is **not finished** and still has
> **a lot of bugs to fix** and **many more features to add**. Things may break,
> change, or be removed at any time. Do not use it on a world you care about
> without a backup.

A Fabric mod for Minecraft that adds a simple **Aether** dimension, a custom
portal, and a couple of items.

- **Author:** Justine Cancio
- **Mod ID:** `aether-dimension`
- **Minecraft:** 26.1
- **Loader:** Fabric Loader 0.19.5+
- **Fabric API:** 0.145.1+26.1
- **Java:** 25+
- **License:** CC0-1.0

## Features

### Aether dimension

A custom dimension (`aether-dimension:aether`) generated as a flat grass world
with the End sky effect, fixed daytime, and peaceful-ish settings. It has no
ceiling and uses a 256-block height range.

### Aether portal

- Build a rectangular frame out of **Glowstone**. Minimum interior size is
  **2x3** blocks, maximum is **21x21**.
- The frame can face either the X or Z axis.
- Right-click any Glowstone block of a valid frame while holding the **Spectro**
  (internally `loom`) item to light the portal. The item is consumed in
  survival mode.
- Walking into the portal teleports you between the Overworld and the Aether.
  A small platform is generated on arrival so you don't fall.
- Breaking the portal or its Glowstone frame removes the connected portal
  blocks.

### Items

- **Spectro** (`item.aether-dimension.loom`) — used to ignite Aether portals.
- **Prismarine X Shard** (`item.aether-dimension.prismarine_x_shard`) — a
  crafting/material item.

Both items appear in the **Aether Dimension** creative tab and in the Ingredients
tab.

## Requirements

- Java 25
- Minecraft 26.1
- [Fabric Loader](https://fabricmc.net/use/) 0.19.5 or newer
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Installation

1. Install Fabric Loader for Minecraft 26.1.
2. Download the Fabric API jar and place it in your `mods` folder.
3. Build or download the mod jar and place it in your `mods` folder.
4. Launch the game with the Fabric profile.

## Building from source

Requires JDK 25.

```bash
# Windows
gradlew.bat build

# macOS / Linux
./gradlew build
```

The built jar will be in `build/libs/`. For local development you can also run
the game with:

```bash
# Windows
gradlew.bat runClient
gradlew.bat runServer

# macOS / Linux
./gradlew runClient
./gradlew runServer
```

To regenerate data (models, recipes, etc.):

```bash
./gradlew runDatagen
```

## Getting the items

### Prismarine X Shard

There is **no crafting recipe** for the Prismarine X Shard yet. For now you
obtain it by:

- **Killing an Elder Guardian** — it always drops one shard (added as a custom
  loot pool).
- Grabbing it from the **Aether Dimension** creative tab (creative mode only).

### Spectro

The Spectro is crafted in a crafting table with this shaped recipe
(shapeless-free, diagonal pattern):

```
P . .
. M .
. . G
```

| Slot | Item |
|------|------|
| `P` | Prismarine X Shard (`aether-dimension:prismarine_x_shard`) |
| `M` | Phantom Membrane (`minecraft:phantom_membrane`) |
| `G` | Glow Ink Sac (`minecraft:glow_ink_sac`) |

Result: **1 Spectro**. The recipe unlocks once you have a Prismarine X Shard.

## Usage

1. Gather Phantom Membranes, Glow Ink Sacs, and a Prismarine X Shard (from an
   Elder Guardian), then craft the **Spectro** with the recipe above.
2. Build a hollow Glowstone rectangle (at least 2x3 interior, up to 21x21).
3. Right-click the Glowstone frame with the Spectro to light the portal.
4. Step into the portal to travel to the Aether.
5. Step back into the portal (or build one there) to return to the Overworld.

## Known issues / Roadmap

This is an early prototype. Expect bugs, including (but not limited to):

- Portal linking is basic and always drops you at Y=100 rather than at a
  matching portal.
- Dimension terrain is a simple flat world with no real Aether content yet.
- Bugs in portal detection, teleporting, and platform generation are likely.
- No proper Aether blocks, mobs, biomes, or structures yet.

Planned work includes fixing the above and adding more features, blocks, items,
and world content. Contributions and bug reports are welcome.

## License

This project is available under the CC0-1.0 license. See [LICENSE](LICENSE).
