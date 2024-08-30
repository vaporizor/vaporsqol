# Vapor's Quality of Life Mod
A simple modification for Minecraft using the [Fabric](https://fabricmc.net/) mod loader to add quality of life features to the game.

Requires the [Fabric API](https://modrinth.com/mod/fabric-api).

Written for Minecraft 1.21+ only.

### Features
 - Zoom in! Default keybinding: `V`
   - Stacks with other FOV modifiers like the Spyglass.
   - Tweak the zoom level by scrolling.
 - Full-Bright! Default keybinding: `J`
 - Idling improvements! When Minecraft is out of focus:
   - Reduce FPS & render distance.
   - Mute the game's audio.
 
### Configuration
All features can be enabled or disabled via the config file at `./config/vapors-qol.json` (relative to your Minecraft directory). Additionally:
 - Minimum and maximum zoom level can be configured by changing `minFOVModifier` and `maxFOVModifier`.
 - Scrolling speed to change the zoom level can be configured by changing `maxFOVStep`.
 - FPS and render distance limits while out of focus can be configured by changing the `limit` property of `fps` or `renderDistance`.

### Contributions
All contributions are welcome, although reviewing time may be slow depending on my availability and discretion!

### Usage
The source code can be used in accordance to the project's license. Keep in mind that the mod references Mojang's official mappings whose license needs to be respected as well.
 