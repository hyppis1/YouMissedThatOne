package com.YouMissedThatOne;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.RuneLite;
import net.runelite.api.ChatMessageType;

import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "You Missed That One"
)
public class YouMissedThatOnePlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private YouMissedThatOneOverlay youMissedThatOneOverlay;
	@Inject
	private YouMissedThatOneConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ItemManager itemManager;

	File runeliteDir = RuneLite.RUNELITE_DIR;
	File customSoundsDir = new File(runeliteDir, "YouMissedThatOne");

	boolean SpecialUsed = false;
	boolean HpXpDrop = false;
	public int specialPercentage = 0;
	public int HeldWeaponID;
	public int PlayingAnimationID;
	public int PlayingAnimationFrame;
	public int Volume;
	public int CustomSoundID;
	public String UserData;
	public List<UserWeaponData> SpecialWeaponList = new ArrayList<>();
	public List<UserWeaponData> NormalWeaponList = new ArrayList<>();
	Random NextRandom = new Random();
	public int RandomizerMin = 0;
	public int RandomizerMax = 10100;
	private SoundManager soundManager;

	@Override
	protected void startUp() throws Exception
	{
		UserData = config.UserSelectedSpecialSounds();
		if (!UserData.isEmpty())
		{
			SpecialWeaponList.clear();
			SpecialWeaponList = parseWeaponData(UserData);
		}

		UserData = config.UserSelectedNormalSounds();
		if (!UserData.isEmpty())
		{
			NormalWeaponList.clear();
			NormalWeaponList = parseWeaponData(UserData);
		}

		Volume = config.SoundSwapVolume();

		RandomizerValueRange();

		soundManager = new SoundManager(config);

		try
		{
			if (!customSoundsDir.exists())
			{
				customSoundsDir.mkdirs();
			}
		}
		catch (SecurityException securityException)
		{
			log.error("Error creating directory");
		}

		overlayManager.add(youMissedThatOneOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(youMissedThatOneOverlay);
		soundManager.cleanup();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Client client = this.client;

		Actor player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		if (player.getAnimation() == PlayingAnimationID)
		{
			PlayingAnimationFrame = player.getAnimationFrame();
		}

		// if animation frame is greater than 1, animation from previous tick is still playing.
		// only run main code when new animation starts.
		if (PlayingAnimationFrame > 1)
		{
			// make hp xp drop and special attack used false
			HpXpDrop = false;
			SpecialUsed = false;
			return;
		}

		// get currently equipped weaponID
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipment != null)
		{
			int SlotIdx = EquipmentInventorySlot.WEAPON.getSlotIdx();
			Item slotItem = equipment.getItem(SlotIdx);
			if (slotItem != null)
			{
				HeldWeaponID = slotItem.getId();
			}
			else
			{
				HeldWeaponID = -1;
			}
		}

		if (SpecialUsed)
		{
			if (!SpecialWeaponList.isEmpty() && config.SpecialSoundsEnabled())
			{
				for (UserWeaponData weapon : SpecialWeaponList)
				{
					if (weapon.getWeaponID() == HeldWeaponID && weapon.getAnimationID() == PlayingAnimationID)
					{
						if (HpXpDrop)
						{
							if (weapon.getOnHit() != -1)
							{
								if (config.RandomizerOnHit())
								{
									CustomSoundID = RandomizerMin + NextRandom.nextInt((RandomizerMax - RandomizerMin) + 1);
								}
								else
								{
									CustomSoundID = weapon.getOnHit();
								}

								if (config.EnableSoundSwap())
								{
									File soundFile = new File(customSoundsDir, CustomSoundID + ".wav");
									if (soundFile.exists())
									{
										soundManager.playCustomSound(soundFile, Volume);
									}
									else
									{
										client.playSoundEffect(CustomSoundID);
									}
								}
								else
								{
									client.playSoundEffect(CustomSoundID);
								}
							}
						}
						else
						{
							if (weapon.getOnMiss() != -1)
							{
								if (config.RandomizerOnMiss())
								{
									CustomSoundID = RandomizerMin + NextRandom.nextInt((RandomizerMax - RandomizerMin) + 1);
								}
								else
								{
									CustomSoundID = weapon.getOnMiss();
								}

								if (config.EnableSoundSwap())
								{
									File soundFile = new File(customSoundsDir, CustomSoundID + ".wav");
									if (soundFile.exists())
									{
										soundManager.playCustomSound(soundFile, Volume);
									}
									else
									{
										client.playSoundEffect(CustomSoundID);
									}
								}
								else
								{
									client.playSoundEffect(CustomSoundID);
								}
							}
						}
					}
				}
			}
		}
		else
		{
			if (!NormalWeaponList.isEmpty() && config.NormalSoundsEnabled())
			{
				for (UserWeaponData weapon : NormalWeaponList)
				{
					if (weapon.getWeaponID() == HeldWeaponID && weapon.getAnimationID() == PlayingAnimationID)
					{
						if (HpXpDrop)
						{
							if (weapon.getOnHit() != -1)
							{
								if (config.RandomizerOnHit())
								{
									CustomSoundID = RandomizerMin + NextRandom.nextInt((RandomizerMax - RandomizerMin) + 1);
								}
								else
								{
									CustomSoundID = weapon.getOnHit();
								}

								if (config.EnableSoundSwap())
								{
									File soundFile = new File(customSoundsDir, CustomSoundID + ".wav");
									if (soundFile.exists())
									{
										soundManager.playCustomSound(soundFile, Volume);
									}
									else
									{
										client.playSoundEffect(CustomSoundID);
									}
								}
								else
								{
									client.playSoundEffect(CustomSoundID);
								}
							}
						}
						else
						{
							if (weapon.getOnMiss() != -1)
							{
								if (config.RandomizerOnMiss())
								{
									CustomSoundID = RandomizerMin + NextRandom.nextInt((RandomizerMax - RandomizerMin) + 1);
								}
								else
								{
									CustomSoundID = weapon.getOnMiss();
								}

								if (config.EnableSoundSwap())
								{
									File soundFile = new File(customSoundsDir, CustomSoundID + ".wav");
									if (soundFile.exists())
									{
										soundManager.playCustomSound(soundFile, Volume);
									}
									else
									{
										client.playSoundEffect(CustomSoundID);
									}
								}
								else
								{
									client.playSoundEffect(CustomSoundID);
								}
							}
						}
					}
				}
			}
		}

		// make hp xp drop and special attack used false
		HpXpDrop = false;
		SpecialUsed = false;

	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		Actor actor = event.getActor();

		// Check if the animation change is for the local player
		if (actor == client.getLocalPlayer())
		{
			PlayingAnimationID = actor.getAnimation();
			PlayingAnimationFrame = 0;

		}
	}

	@Subscribe
	public void	onConfigChanged(ConfigChanged event)
	{
		switch (event.getKey())
		{
			case "customSpecialSounds":
			{
				UserData = config.UserSelectedSpecialSounds();
				if (!UserData.isEmpty())
				{
					SpecialWeaponList.clear();
					SpecialWeaponList = parseWeaponData(UserData);
				}
				break;
			}
			case "customNormalSounds":
			{
				UserData = config.UserSelectedNormalSounds();
				if (!UserData.isEmpty())
				{
					NormalWeaponList.clear();
					NormalWeaponList = parseWeaponData(UserData);
				}
				break;
			}
			case "SoundSwapVolume":
			{
				Volume = config.SoundSwapVolume();
				break;
			}
			case "RandomizerValueRange":
			{
				RandomizerValueRange();
				break;
			}
		}
	}

	public void RandomizerValueRange()
	{
		String[] parts = config.valueRange().split("/");
		if (parts.length == 2) {
			try {
				int min = Integer.parseInt(parts[0].trim());
				int max = Integer.parseInt(parts[1].trim());

				if (min > max)
				{
					sendGameMessage("Invalid range: Minimum ("+ min +") cannot be greater than Maximum ("+ max +") Please check your input.");
					return;
				}

				RandomizerMin = min;
				RandomizerMax = max;

			} catch (NumberFormatException e) {
				sendGameMessage("Failed to parse randomizer value range \"" + config.valueRange() + "\" Please check your input.");
			}
		}
		else
		{
			sendGameMessage("Failed to parse randomizer value range \"" + config.valueRange() + "\" Please check your input.");
		}
	}

	public void sendGameMessage(String message)
	{
		clientThread.invoke(() ->
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null)
		);
	}

	public static class UserWeaponData
	{
		private final int WeaponID;
		private final int AnimationID;
		private final int OnHit;
		private final int OnMiss;

		public UserWeaponData(int WeaponID, int AnimationID, int OnHit, int OnMiss)
		{
			this.WeaponID = WeaponID;
			this.AnimationID = AnimationID;
			this.OnHit = OnHit;
			this.OnMiss = OnMiss;
		}

		public int getWeaponID()
		{
			return WeaponID;
		}

		public int getAnimationID() {
			return AnimationID;
		}

		public int getOnHit()
		{
			return OnHit;
		}

		public int getOnMiss()
		{
			return OnMiss;
		}

		@Override
		public String toString()
		{
			return "WeaponData{WeaponID='" + WeaponID + "', AnimationID=" + AnimationID + "', OnHit=" + OnHit + ", OnMiss=" + OnMiss + "}";
		}
	}

	private List<UserWeaponData> parseWeaponData(String input)
	{
		List<UserWeaponData> WeaponList = new ArrayList<>();

		// Split the input string into individual weapon entries
		String[] WeaponEntries = input.split(";");
		for (String entry : WeaponEntries)
		{
			// Remove any comment (text after "//")
			int commentIndex = entry.indexOf("//");
			if (commentIndex != -1)
			{
				entry = entry.substring(0, commentIndex); // Remove the comment
			}

			entry = entry.trim();

			if (!entry.isEmpty())
			{
				// Split each entry into its components
				String[] parts = entry.split(",");
				if (parts.length == 4)
				{
					try
					{
						int WeaponID = Integer.parseInt(parts[0].trim()); // WeaponID
						int AnimationID = Integer.parseInt(parts[1].trim()); // AnimationID
						int OnHit = Integer.parseInt(parts[2].trim()); // On hit
						int OnMiss = Integer.parseInt(parts[3].trim()); // On miss

						// Add the parsed data to the list
						WeaponList.add(new UserWeaponData(WeaponID, AnimationID, OnHit, OnMiss));
					}
					catch (NumberFormatException e)
					{
						sendGameMessage("Failed to parse custom sound entry \"" + entry + "\" Please check your input.");
					}
				}
				else
				{
					sendGameMessage("Failed to parse custom sound entry \"" + entry + "\" Please check your input.");
				}
			}
		}
		return WeaponList;
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		// check if skill is hp
		if (event.getSkill() == Skill.HITPOINTS)
		{
			HpXpDrop = true;
		}
	}
	@Subscribe
	public void onFakeXpDrop(FakeXpDrop event)
	{
		// check if skill is hp
		if (event.getSkill() == Skill.HITPOINTS)
		{
			HpXpDrop = true;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (event.getVarpId() != VarPlayer.SPECIAL_ATTACK_PERCENT)
		{
			return;
		}
		// save previous value
		int wasSpecialPercentage = specialPercentage;
		// get new value from event
		specialPercentage = event.getValue();

		// compare if it is less than last time
		if (wasSpecialPercentage > specialPercentage)
		{
			SpecialUsed = true;
		}

	}


	@Provides
	YouMissedThatOneConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(YouMissedThatOneConfig.class);
	}
}
