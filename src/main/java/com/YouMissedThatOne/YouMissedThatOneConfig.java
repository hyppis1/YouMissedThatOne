package com.YouMissedThatOne;

import net.runelite.client.config.*;

@ConfigGroup("YouMissedThatOne")
public interface YouMissedThatOneConfig extends Config
{


	@ConfigItem(
			keyName = "EnableOverlay",
			name = "Show ID overlay",
			description = "Shows held weapon item ID and current animation ID",
			position = 1
	)
	default boolean EnableOverLay()
	{
		return false;
	}

	@ConfigItem(
			keyName = "DeBugInfoOverlay",
			name = "Show debug info",
			description = "Additional info for debugging",
			position = 2
	)
	default boolean DeBugInfoOverlay() {return false;}

	@ConfigItem(
			keyName = "EnableSoundSwap",
			name = "Enable sound swap",
			description = "Allows you to replace sound effects, similar to the Sound Swapper plugin." +
					"<br/> To replace a sound effect, add a .wav file to this plugin’s directory with the corresponding sound ID as the filename." +
					"<br/> For example, to replace sound ID \"1569\" with your custom sound, name the file \"1569.wav\"" +
					"<br/> Plugin directory location: %USERPROFILE%/.runelite/YouMissedThatOne",
			position = 3
	)
	default boolean EnableSoundSwap()
	{
		return false;
	}

	@ConfigItem(
			keyName = "SoundSwapVolume",
			name = "Sound swap volume",
			description = "Set the volume of swapped sounds (0-100)",
			position = 4
	)
	@Range(min = 0, max = 100)
	default int SoundSwapVolume() { return 50; }

	@ConfigItem(
			keyName = "SoundSwapOverlap",
			name = "Overlap swapped sounds",
			description = "Enables swapped sound effects to overlap if the audio clips are long enough.",
			position = 5
	)
	default boolean SoundSwapOverlap() { return false; }

	@ConfigSection(
			name = "Sound randomizer",
			description = "Settings for sound randomizing",
			position = 6
	)
	String SOUND_RANDOMIZER_SECTION = "SoundRandomizer";

	@ConfigSection(
			name = "Special attacks",
			description = "Settings for special attacks",
			position = 7
	)
	String SPECIAL_ATTACKS_SECTION = "SpecialAttackSounds";

	@ConfigSection(
			name = "Normal attacks",
			description = "Settings for normal attacks",
			position = 8
	)
	String NORMAL_ATTACKS_SECTION = "NormalAttackSounds";

	@ConfigItem(
			keyName = "RandomizerOnHit",
			name = "Randomize sound on hit",
			description = "Plays random sound effects on hit for the selected weapons." +
					"<br/> If sound swap is enabled, custom sounds can also play when their ID is randomly selected",
			position = 1,
			section = SOUND_RANDOMIZER_SECTION
	)
	default boolean RandomizerOnHit() { return false; }

	@ConfigItem(
			keyName = "RandomizerOnMiss",
			name = "Randomize sound on miss",
			description = "Plays random sound effects on miss for the selected weapons." +
					"<br/> If sound swap is enabled, custom sounds can also play when their ID is randomly selected",
			position = 2,
			section = SOUND_RANDOMIZER_SECTION
	)
	default boolean RandomizerOnMiss() { return false; }

	@ConfigItem(
			keyName = "RandomizerValueRange",
			name = "Randomize ID range",
			description = "Enter a range in format 'min-max' (e.g., 500/850). The values are inclusive"+
					"<br/> At the time of writing, there are approximately 10100 sound IDs available.",
			position = 3,
			section = SOUND_RANDOMIZER_SECTION
	)
	default String valueRange() {return "0/10100";}

	@ConfigItem(
			keyName = "SpecialAttackSounds",
			name = "Special attack sound effects",
			description = "Play sound effect when you hit or miss on special attack." +
					"<br/> Two tick weapons have minor problem with this plugin, they usually play extra sound effect at end of combat.",
			position = 1,
			section = SPECIAL_ATTACKS_SECTION
	)
	default boolean SpecialSoundsEnabled() { return false; }


	@ConfigItem(
			keyName = "customSpecialSounds",
			name = "Custom special attack sounds",
			description = "Enter a weapon item ID, attack animation ID, the sound ID to play on hit, and the sound ID to play on miss." +
						"<br/> Separate IDs with commas. To add multiple weapons, separate each set with a semicolon." +
						"<br/> For unarmed attacks, use the weapon item ID \"-1\". If you only want one of the sound effects, set the other sound ID to \"-1\"" +
						"<br/> For example. \"26374, 9168, 1569, 1640;\" Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss." +
						"<br/> You can add comments by using \"//\". Ensure the comment follows the data and precedes the semicolon. For example: \"26374, 9168, 1569, 1640 //This is zcb;\"",
			position = 2,
			section = SPECIAL_ATTACKS_SECTION
	)
	default String UserSelectedSpecialSounds()
	{
		return "";
	}

	@ConfigItem(
			keyName = "NormalAttackSounds",
			name = "Normal attack sound effects",
			description = "Play sound effect when you hit or miss on normal attack."+
					"<br/> Two tick weapons have minor problem with this plugin, they usually play extra sound effect at end of combat.",
			position = 1,
			section = NORMAL_ATTACKS_SECTION
	)
	default boolean NormalSoundsEnabled() { return false; }

	@ConfigItem(
			keyName = "customNormalSounds",
			name = "Custom normal attack sounds",
			description = "Enter a weapon item ID, attack animation ID, the sound ID to play on hit, and the sound ID to play on miss." +
					"<br/> Separate IDs with commas. To add multiple weapons, separate each set with a semicolon." +
					"<br/> For unarmed attacks, use the weapon item ID \"-1\". If you only want one of the sound effects, set the other sound ID to \"-1\"" +
					"<br/> For example. \"26374, 9168, 1569, 1640;\" Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss." +
					"<br/> You can add comments by using \"//\". Ensure the comment follows the data and precedes the semicolon. For example: \"26374, 9168, 1569, 1640 // This is zcb;\"",
			position = 2,
			section = NORMAL_ATTACKS_SECTION
	)
	default String UserSelectedNormalSounds()
	{
		return "";
	}

}
