# Vapor's QOL
A [Fabric](https://fabricmc.net/) modification to Minecraft with various of quality of life tweaks to the game!.

This mod aims to bundle as many of these tweaks as possible in a difficult-to-break and easy-to-update fashion.

Available for most versions of Minecraft starting from 1.21.1

*I need to think of a better name for this project soon >_<*

### Features
- **Zoom** Default keybinding: `V`
    - Scroll to zoom further.
    - Stacks with the Spyglass! (and other FOV modifiers).
- **Full Bright** Default keybinding: `B`
    - Comes with a neat effect indicator.
- **AFK Improvements** When Minecraft is out of focus:
    - Limit FPS & render distance.
    - Lower or completely mute the game's audio.
- **Borderless "Fullscreen"**
    - Launches the game in windowed mode but removes the top bar and maxes the window size for a fullscreen-like experience, *without* the hassle that comes with the default fullscreen on most operating systems.
    - **This is disabled by default and can only be enabled through the `"borderless"` property in the [configuration](#configuration).**
    - *Warning: only tested on a few machines. Please create an [Issue](/../../issues) if it does not work for you!*
- **Other Tweaks**:
    - Continue playing sounds when the audio is muted: Vanilla Minecraft will stop playing any ongoing sounds if you lower the game's volume completely. This feature removes this behavior so you can continue hearing the sound if you turn your volume up again!

*More to come! Have any ideas? Open an [Issue](../../issues) or submit a [Pull Request](../../pulls)*.

### Configuration
The configuration file can found at `./config/vaporsqol.json` in your Minecraft directory.

Disabling any of the configurable features below will also prevent the mod from registering related keybindings and mixins. You can use this to debug compatibility issues with other mods!

The comments above each setting describe what it does, and the value shown next to it is the default.
```json5
{
    // Enables/disables the entire mod including keybindings and mixins.
    "enabled": true,
    // See the #features section above for more details.
    "borderless": false,
    // See the #features section above, under the Other Tweaks heading, for more details.
    "play-sounds-with-no-volume": true,
    // All configurations related to the zoom feature.
    "zoom": {
        // Enables/disables the zoom feature including keybindings and mixins.
        "enabled": true,
        /*
         * Sets the minimum and maximum modifiers by which the player's
         * FOV can be multiplied when zooming.
         * Lower values for `min-fov-modifier` will increase the maximum
         * zoom level, and higher values for `max-fov-modifier` will
         * decrease the minimum zoom level.
         *
         * Values below 0.0001 for `minFOVModifier` and values above 1.5
         * for `maxFOVModifier` will have no effect.
         */
        "min-fov-modifier": 0.01,
        "max-fov-modifier": 0.1,
        /*
         * Sets how quickly the player can adjust their zoom level when
         * scrolling with the mouse wheel.
         * Higher values will zoom faster while lower values will zoom
         * slower.
         */
        "fov-step": 0.02
    },
    // All configurations related to the fullbright feature.
    "fullbright": {
        /*
         * Enables/disables the fullbright feature including any related
         * keybindings and mixins.
         */
        "enabled": true,
        /*
         * If true, will additionally display an effect icon to indicate
         * that fullbright is on
         */
        "indicator": true
    },
    // All configurations related to AFK improvements.
    "afk": {
        /* 
         * Limits the FPS the of the game when out of focus. Must be a whole
         * number and cannot be 0. If set to -1, the feature is disabled.
         */
        "fps": 5,
         /*
          * Limits the render distance of the game when out of focus. Must
          * be a whole number and cannot be 0. If set to -1, the feature is
          * disabled.
          */
        "render": 1,
         /*
          * Reduces the volume of the game when out of focus. Must be
          * a number between 0 and 1 where 0 is completely muted and 1 is
          * max volume.
          * If set to -1, the feature is disabled.
          */
        "volume": 0.0
    }
}
```

### Contributions
All contributions are gladly welcome, although reviewing time *may* be slow depending on my availability and discretion.

### Usage and Distribution
This project is licensed under the [GNU GPL v3.0 license](https://license.com) which needs to be respected; a tl;dr of which can be found at https://choosealicense.com/licenses/gpl-3.0.

If you wish to distribute a modpack with this mod in it, please include a link to this repository in whatever distribution medium you use.

Finally, in the interest of maintaining a centralized host for ready-to-use builds of this project, I would ask that you - in addition to respecting the license terms - *avoid redistributing unmodified builds outside the official channels wherever possible*.

Please feel free to contact me through GitHub if you have any questions!
