# YouMissedThatOne

This plugin enables you to play custom sound effects for both hit and miss actions during attacks.
You can configure whether these sound effects should apply to normal attacks, special attacks, or both.
The sound effects are triggered based on the attack animation, and a hit is determined when an HP XP drop occurs.

## How to use

In the plugin settings, you can enable the "Show ID Overlay" option. 
This feature will help you identify your weapon's item ID and attack animation ID.
Once you've obtained the weapon's item ID and corresponding attack animation ID, you can add them to the
"Custom Special Attack Sounds" or "Custom Normal Attack Sounds" sections, depending on whether it's a special or normal attack.
Next, choose the custom sound effects you’d like to play on a hit or miss. ID of "-1" plays no sound.
You can find a list of known sound IDs on the OSRS wiki https://oldschool.runescape.wiki/w/List_of_sound_IDs
Remember to separate all the ID's with a comma.

After completing these steps, you should have a list of IDs similar to this: "26374, 9168, 1569, 1640".
Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss

To add multiple weapons, separate each entry with a semicolon. For example "26374, 9168, 1569, 1640; xxx, xxx, xxx, xxx;" etc.

## Sound swapping

In the plugin settings, you can enable the "Enable Sound Swap" option. 
This feature lets you replace the default hit and miss sound effects with custom sound files of your choice.
The custom sound files must be in .wav format, and their filenames must match the sound ID you want to replace. 
For example, to swap sound ID 1640 with your own sound effect, name the file 1640.wav and place it in the plugin’s directory.
The directory can be found at: "%USERPROFILE%/.runelite/YouMissedThatOne".

Tip: For sound swapping, the sound ID doesn’t need to be a valid in-game ID. For example, you can use "26374, 9168, -2, -3".
As long as you have sound files named "-2.wav" and "-3.wav" in the plugin directory, the sounds will play.
