# Vapor's QOL Mod
A simple modification for Minecraft using the [Fabric](https://fabricmc.net/) mod loader to add a myriad of quality of life improvements to the game.

This mod aims to bundle as many of these QOL tweaks as possible for the latest version of the game in a difficult-to-break and easy-to-update fashion, so you can throw it into any of your modded environments and not have to think about it again!

Requires:
- Minecraft 1.21+
- [Fabric API](https://modrinth.com/mod/fabric-api)

### Features
- **Zoom** Default keybinding: `V`
    - Stacks with other FOV modifiers like the Spyglass.
    - Tweak the zoom level by scrolling.
- **Full Bright** Default keybinding: `B`
    - Comes with a neat visual indicator.
    - Compatible with most rendering optimization mods.
- **AFK Improvements** When Minecraft is out of focus:
    - Limit FPS & render distance.
    - Lower or completely mute the game's audio.
- **Borderless "Fullscreen"**
    - Launches the game in windowed mode but removes the top bar and maxes the window size for a fullscreen-like experience, *without* the hassle that comes with the default fullscreen on most operating systems.
    - This is disabled by default and can only be enabled through the `"borderless"` property in the [configuration](#configuration).
    - *Warning: only tested on a few machines. Please create an [Issue](/../../issues) if it does not work for you!*
- Other Tweaks:
    - Play sounds with no volume: Vanilla Minecraft will stop playing a sound if you lower the volume to 0. This mod will disable this behavior by default, so you can continue hearing the sound if you turn your volume up again!

*More to come! Have any ideas? Open an [Issue](../../issues) or submit a [Pull Request](../../pulls)*.

### Configuration
The comment above each property describes what it does, and the value of each property represents the default.
```json5
{
    // Enables / disables the entire mod. Set this to false if you want to disable loading any mixins.
    "enabled": true,
    // Enables / disables borderless "fullscreen". Please see the #features section above for more details.
    "borderless": false,
    /*
     * Enables / disables a fix for Minecraft sounds being stopped if the volume is lowered to 0.
     * See the #features section above for more details.
     */
    "playNoVolumeSounds": true,
    // Grouping for all configurations related to the zoom feature.
    "zoom": {
        /*
         * Enables / disables the entire feature including any related keybindings and
         * loading any related mixins.
         */
        "enabled": true,
        /*
         * The zoom feature currently works by multiplying the player's field of view by a modifier.
         * These values set the minimum and maximum modifiers achievable by scrolling while zooming.
         * The lower your FOV, the more you zoom in, so the `minFOVModifier` actually controls your
         * maximum zoom level! The opposite also applies to `maxFOVModifier`.
         * When you first press your zoom key, the mod will zoom to the minimum amount possible, so
         * change `maxFOVModifier` to control your initial zoom level.
         * Note that values below 0.0001 for `minFOVModifier` and values above 1.5 for `maxFOVModifier`
         * will have no effect.
         */
        "minFOVModifier": 0.01,
        "maxFOVModifier": 0.1,
        /*
         * This controls how much you zoom in per distance scrolled.
         * Higher values let you zoom in faster while lower values let you zoom in slower.
         * Note that this does not affect the speed of the zoom-in animation, which is currently
         * not configurable. This is also separate from your maximum and minimum zoom levels (see above).
         */
        "maxFOVStep": 0.02
    },
    // Grouping for all configurations related to the fullbright feature.
    "fullbright": {
        /*
         * Enables / disables the entire feature including any related keybindings and
         * loading any related mixins.
         */
        "enabled": true,
        // Tells the mod if fullbright is currently on. Generally does not need to be touched by the player.
        "toggled": false,
        // Creates a neat effect icon in the top right corner of your screen to indicate that fullbright is on.
        "visualIndicator": true
    },
    // Grouping for all configurations related to AFK improvements.
    "idle": {
        // Each AFK feature is separated into its own group and can be enabled or disabled individually.
        // Enabling / disabling any of these will also enable / disable loading any mixins related to that specific feature.
        "fps": {
            // Enables / disables lowering FPS when the game is out of focus.
            "enabled": true,
            /*
             * If the above is `true`, this property defines what FPS to lower the game to.
             * It is highly recommended not to set this below 5 FPS as lower values will cause the game to
             * freeze for the first second or so after switching back in.
             * This value must be a whole number and cannot be smaller than 1.
             */
            "limit": 5
        },
        "renderDistance": {
            // Enables / disables lowering render distance when the game is out of focus.
            "enabled": true,
            /*
             * If the above is `true`, this property defines what render distance to lower the game to.
             * This value must be a whole number and cannot be smaller than 1.
             */
            "limit": 5
        },
        "audio": {
            // Enables / disables lowering the game's audio when it is out of focus.
            "enabled": true,
           /*
            * If the above is `true`, this property defines what volume to lower the game to.
            * This value must be a decimal between 0 and 1. If 0, the audio is completely muted
            * and if 1, the audio is at its maximum volume. If this value is above your normal
            * master volume in settings, it will have no effect.
            */
            "limit": 0.0
        }
    }
}
```

### Contributions
All contributions are welcome, although reviewing time may be slow depending on my availability and discretion.

### Usage and Distribution
This project is licensed under the GNU GPL v3.0 license which needs to be respected; a tl;dr of which can be found at https://choosealicense.com/licenses/gpl-3.0.

If you wish to distribute a modpack with this mod in it, please include a link to this repository in whatever distribution medium you use.

Finally, in the interest of maintaining a centralized host for ready-to-use builds of this project, I would ask that you - in addition to respecting the license terms - *avoid redistributing unmodified builds outside the official channels wherever possible*.

Please feel free to contact me through GitHub if you have any questions!