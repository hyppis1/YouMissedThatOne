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
			keyName = "EnableSoundSwap",
			name = "Enable sound swap",
			description = "Allows you to replace sound effects, similar to the Sound Swapper plugin." +
					"<br/> To replace a sound effect, add a .wav file to this plugin’s directory with the corresponding sound ID as the filename." +
					"<br/> For example, to replace sound ID \"1569\" with your custom sound, name the file \"1569.wav\"" +
					"<br/> Plugin directory location: %USERPROFILE%/.runelite/YouMissedThatOne",
			position = 2
	)
	default boolean EnableSoundSwap()
	{
		return false;
	}

	@ConfigItem(
			keyName = "SoundSwapVolume",
			name = "Sound swap volume",
			description = "Set the volume of swapped sounds (0-100)",
			position = 3
	)
	@Range(min = 0, max = 100)
	default int SoundSwapVolume() { return 50; }

	@ConfigItem(
			keyName = "EnableRandomizer",
			name = "Enable sound randomizer",
			description = "Plays random sound effects for the weapons you have selected." +
					"<br/> If sound swap is enabled, those sounds can also play if the ID is randomly selected",
			position = 4
	)
	default boolean EnableRandomizer() { return false; }

	@ConfigSection(
			name = "Special attacks",
			description = "Settings for special attacks",
			position = 5
	)
	String SPECIAL_ATTACKS_SECTION = "SpecialAttackSounds";

	@ConfigSection(
			name = "Normal attacks",
			description = "Settings for normal attacks",
			position = 6
	)
	String NORMAL_ATTACKS_SECTION = "NormalAttackSounds";

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
						"<br/> For example. \"26374, 9168, 1569, 1640;\" Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss.",
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
					"<br/> For example. \"26374, 9168, 1569, 1640;\" Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss.",
			position = 2,
			section = NORMAL_ATTACKS_SECTION
	)
	default String UserSelectedNormalSounds()
	{
		return "";
	}

}
