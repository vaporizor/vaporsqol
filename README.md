# Vapor's Quality of Life Mod
A simple modification for Minecraft using the [Fabric](https://fabricmc.net/) mod loader to add quality of life features to the game.

Requires the [Fabric API](https://modrinth.com/mod/fabric-api).

Written for Minecraft 1.21+ only.

### Features
- Zoom! Default keybinding: `V`
    - Stacks with other FOV modifiers like the Spyglass.
    - Tweak the zoom level by scrolling.
- Full Bright! Default keybinding: `B`
- Idling optimizations! When Minecraft is out of focus:
    - Limit FPS & render distance.
    - Lower or completely mute the game's audio.

### Configuration
All features can be enabled or disabled via the config file at `./config/vapors-qol.json` (relative to your Minecraft directory). Additionally:
- Minimum and maximum zoom levels can be configured by changing `minFOVModifier` and `maxFOVModifier`.
- The speed at which scrolling changes the zoom level can be configured by changing `maxFOVStep`. You can set this property to **0** to disable scrolling to change the zoom level entirely.
- FPS, render distance, and volume limits while the game is out of focus can be configured by changing the `limit` property of `fps`, `renderDistance`, and `audio` respectively.

### Contributions
All contributions are welcome, although reviewing time may be slow depending on my availability and discretion!

### Redistribution
This project is licensed under the GNU GPL v3.0 license which needs to be respected; a tl;dr of which can be found at https://choosealicense.com/licenses/gpl-3.0.

If you wish to distribute a modpack with this mod in it, please include a link to this repository in whatever distribution medium you use.

Finally, in the interest of maintaining a centralized host for ready-to-use builds of this project, I would ask that you - in addition to respecting the license terms - *avoid redistributing unmodified builds outside the official channels*.

Please feel free to contact me through GitHub if you have any questions!