# Massive Stacks

A Fabric mod that lets items stack far beyond vanilla limits. The maximum stack size is configurable, defaults to 999 and goes up to one billion items per slot.

## Requirements

| Requirement | Version |
| --- | --- |
| Minecraft | 26.3 |
| Fabric Loader | 0.20.1 or newer |
| Fabric API | any version for 26.3 |
| Java | 25 or newer |
| Mod Menu (optional) | 21.0.0 or newer, for the in-game settings screen |

## Installation

Put the mod and Fabric API in the `mods` folder.

For multiplayer, the mod must be installed on both the server and every client. Players who join without it are disconnected with a message explaining that the mod is required.

## Features

- **Configurable stack size:** every stackable item, and unstackable items without durability such as potions, boats, buckets and enchanted books, stack up to the configured limit.
- **Tools stay unstackable:** anything with durability (tools, weapons, armor, elytra, shields, bows, fishing rods and so on) keeps its vanilla limit of 1.
- **Readable counts:** large stacks show short labels like `1.5k`, `12.3m` and `1b`, and the tooltip shows the exact amount.
- **Take an exact amount:** ctrl + right-click a stack in any container with an empty cursor to open a menu where you can type an amount, drag a slider or pick 1, 64, half or all.
- **Batch crafting:** shift-clicking a crafting result crafts everything the grid allows in one go, up to one full stack of the result.
- **Anvils:** stacks of unstackable items such as enchanted books are combined one at a time, and shift-clicking repeats while you can afford the XP cost.
- **Comparators:** a slot reads as full at the item's normal vanilla stack size, so item sorters and storage meters work as in vanilla.
- **Recipe book:** filling the crafting grid from the recipe book works with very large stacks.

## Configuration

Settings are stored in `config/massivestacks.json`, which is created on first launch:

```json
{
  "maxStackSize": 999
}
```

`maxStackSize` accepts values from 1 to 1,000,000,000. With Mod Menu installed, it can also be changed in game from the mod's settings button, and changes apply immediately.

On a multiplayer server, the server's value is used while you are connected, and your own value applies again once you leave.

## Known limitations

- Lowering the limit does not shrink stacks that are already larger. They stay as they are until split.
- The recipe book counts at most one billion of each ingredient, so very large inventories may fill the grid with less than a full stack per slot.
- Shift-clicking results in other menus, such as the stonecutter or loom, takes at most 64 times per click.
- In a singleplayer world opened to LAN, guests keep the old limit until they reconnect if the host changes it mid-game.

## License

CC0-1.0
