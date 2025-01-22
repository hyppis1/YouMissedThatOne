package com.YouMissedThatOne;

import net.runelite.client.config.*;

@ConfigGroup("YouMissedThatOne")
public interface YouMissedThatOneConfig extends Config
{


	@ConfigItem(
			keyName = "EnableOverlay",
			name = "Show ID overlay",
			description = "Show held weapon name and ID, animation ID and sound effect ID in overlay",
			position = 1
	)
	default boolean EnableOverLay()
	{
		return false;
	}

	@ConfigItem(
			keyName = "EnableSoundSwap",
			name = "Enable sound swap",
			description = "Allows you to swap sound effects, just like sound swapper plugin." +
					"<br/> To swap sound effects, add .wav file to this plugins directory with the correct sound ID." +
					"<br/> For example, if you want to swap sound ID \"1569\" to play something else, name the file \"1569.wav\"" +
					"<br/> Directory location: %USERPROFILE%/.runelite/YouMissedThatOne",
			position = 2
	)
	default boolean EnableSoundSwap()
	{
		return false;
	}

	@ConfigItem(
			keyName = "SoundSwapVolume",
			name = "Sound Swap Volume",
			description = "Set the volume of swapped sounds (0-100)",
			position = 3
	)
	@Range(min = 0, max = 100)
	default int SoundSwapVolume() { return 50; }

	@ConfigItem(
			keyName = "EnableRandomizer",
			name = "Enable sound randomizer",
			description = "Plays random sound effects for the weapons you have selected." +
					"<br/> If sound swap is enabled, those sounds can also play if the ID is selected",
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

	@ConfigSection(
			name = "Mute sounds",
			description = "Settings for muting sounds",
			position = 7
	)
	String MUTE_SOUNDS_SECTION = "MuteSounds";

	@ConfigItem(
			keyName = "SpecialAttackSounds",
			name = "Special attack sound effects",
			description = "Play sound effect when you hit or miss on special attack. 2 tick, or faster weapons can cause problems.",
			position = 1,
			section = SPECIAL_ATTACKS_SECTION
	)
	default boolean SpecialSoundsEnabled() { return false; }


	@ConfigItem(
			keyName = "customSpecialSounds",
			name = "Custom Special Attack Sounds",
			description = "Insert a weapon ID, attack animation ID, sound ID you want to play for hit, and sound ID you want to play for miss." +
						"<br/> Separate ID's with comma. When adding multiple weapons, separate them with semicolon." +
						"<br/> For unarmed attacks, use weapon ID \"-1\". If you only want 1 of the sound effects, give the other sound effect ID \"-1\"" +
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
			description = "Play sound effect when you hit or miss on normal attack. 2 tick, or faster weapons can cause problems.",
			position = 1,
			section = NORMAL_ATTACKS_SECTION
	)
	default boolean NormalSoundsEnabled() { return false; }

	@ConfigItem(
			keyName = "customNormalSounds",
			name = "Custom Normal Attack Sounds",
			description = "Insert a weapon ID, attack animation ID, sound ID you want to play for hit, and sound ID you want to play for miss." +
					"<br/> Separate ID's with comma. When adding multiple weapons, separate them with semicolon." +
					"<br/> For unarmed attacks, use weapon ID \"-1\". If you only want 1 of the sound effects, give the other sound effect ID \"-1\"" +
					"<br/> For example. \"26374, 9168, 1569, 1640;\" Where 26374 is zcb item ID, 9168 is zcb attack animation ID, 1569 is sound ID on hit, and 1640 is sound ID on miss.",
			position = 2,
			section = NORMAL_ATTACKS_SECTION
	)
	default String UserSelectedNormalSounds()
	{
		return "";
	}

	@ConfigItem(
			keyName = "MuteSounds",
			name = "Mute default sound",
			description = "Mute default sounds by sound ID",
			position = 1,
			section = MUTE_SOUNDS_SECTION
	)
	default boolean RemoveSounds()
	{
		return false;
	}

	@ConfigItem(
			keyName = "MutedSounds",
			name = "Muted default Sounds",
			description = "Insert a default sound ID you want to mute" +
					"<br/> Separate ID's with comma. For example. \"1569, 1640\"",
			position = 2,
			section = MUTE_SOUNDS_SECTION
	)
	default String UserSelectedMutedSounds()
	{
		return "";
	}

}
