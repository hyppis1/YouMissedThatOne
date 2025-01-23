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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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
	boolean TwoTickFix = false;
	private int TwoTickCooldown = 0;
	public int specialPercentage = 0;
	public int HeldWeaponID;
	public int WasHeldWeaponID = -1;
	public int PlayingAnimationID;
	public int WasAnimationID = -1;
	public int Volume;
	public int CustomSoundID;
	public String UserData;
	List<Integer> TwoTickWeaponList = new ArrayList<Integer>();
	public List<UserWeaponData> SpecialWeaponList = new ArrayList<>();
	public List<UserWeaponData> NormalWeaponList = new ArrayList<>();
	Random NextRandom = new Random();

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

		TwoTickWeaponListMaker();

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
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Client client = this.client;

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

			// check if the weapon was same as last tick, less time used to check if its 2 tick weapon or not
			if (HeldWeaponID != WasHeldWeaponID)
			{
				WasHeldWeaponID = HeldWeaponID;

				if (TwoTickWeaponList.contains(HeldWeaponID))
				{
					TwoTickFix = true;
				}
				else
				{
					TwoTickFix = false;
				}
			}
		}

		// If using 2 tick weapon, use this scuffed "fix" to bypass animation length problem
		if (TwoTickFix)
		{
			if (TwoTickCooldown > 0)
			{
				TwoTickCooldown--;
				return;
			}
			TwoTickCooldown = 1;
		}
		// If using slower weapon, just check if current animation ID isn't same as last time
		else if (PlayingAnimationID == WasAnimationID)
		{
			return;
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
									CustomSoundID = NextRandom.nextInt(10000);
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
										playCustomSound(soundFile, Volume);
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
									CustomSoundID = NextRandom.nextInt(10000);
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
										playCustomSound(soundFile, Volume);
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
									CustomSoundID = NextRandom.nextInt(10000);
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
										playCustomSound(soundFile, Volume);
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
									CustomSoundID = NextRandom.nextInt(10000);
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
										playCustomSound(soundFile, Volume);
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
		// keep track what the animation was
		WasAnimationID = PlayingAnimationID;

	}

	@Subscribe
	public void onAnimationChanged(AnimationChanged event)
	{
		Actor actor = event.getActor();

		// Check if the animation change is for the local player
		if (actor == client.getLocalPlayer())
		{
			PlayingAnimationID = actor.getAnimation();
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
		}
	}

	public void TwoTickWeaponListMaker()
	{
		// this is just easy way to make list of all 2 tick weapons which can cause problems on the plugin

		// Knives
		TwoTickWeaponList.add(ItemID.BRONZE_KNIFE);
		TwoTickWeaponList.add(ItemID.BRONZE_KNIFEP);
		TwoTickWeaponList.add(ItemID.BRONZE_KNIFEP_5654);
		TwoTickWeaponList.add(ItemID.BRONZE_KNIFEP_5661);
		TwoTickWeaponList.add(ItemID.IRON_KNIFE);
		TwoTickWeaponList.add(ItemID.IRON_KNIFEP);
		TwoTickWeaponList.add(ItemID.IRON_KNIFEP_5655);
		TwoTickWeaponList.add(ItemID.IRON_KNIFEP_5662);
		TwoTickWeaponList.add(ItemID.STEEL_KNIFE);
		TwoTickWeaponList.add(ItemID.STEEL_KNIFEP);
		TwoTickWeaponList.add(ItemID.STEEL_KNIFEP_5656);
		TwoTickWeaponList.add(ItemID.STEEL_KNIFEP_5663);
		TwoTickWeaponList.add(ItemID.BLACK_KNIFE);
		TwoTickWeaponList.add(ItemID.BLACK_KNIFEP);
		TwoTickWeaponList.add(ItemID.BLACK_KNIFEP_5658);
		TwoTickWeaponList.add(ItemID.BLACK_KNIFEP_5665);
		TwoTickWeaponList.add(ItemID.MITHRIL_KNIFE);
		TwoTickWeaponList.add(ItemID.MITHRIL_KNIFEP);
		TwoTickWeaponList.add(ItemID.MITHRIL_KNIFEP_5657);
		TwoTickWeaponList.add(ItemID.MITHRIL_KNIFEP_5664);
		TwoTickWeaponList.add(ItemID.ADAMANT_KNIFE);
		TwoTickWeaponList.add(ItemID.ADAMANT_KNIFEP);
		TwoTickWeaponList.add(ItemID.ADAMANT_KNIFEP_5659);
		TwoTickWeaponList.add(ItemID.ADAMANT_KNIFEP_5666);
		TwoTickWeaponList.add(ItemID.RUNE_KNIFE);
		TwoTickWeaponList.add(ItemID.RUNE_KNIFEP);
		TwoTickWeaponList.add(ItemID.RUNE_KNIFEP_5660);
		TwoTickWeaponList.add(ItemID.RUNE_KNIFEP_5667);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFE);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFEP);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFE_22812);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFE_22814);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFE_27157);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFEP_22808);
		TwoTickWeaponList.add(ItemID.DRAGON_KNIFEP_22810);

		// Darts
		TwoTickWeaponList.add(ItemID.BRONZE_DART);
		TwoTickWeaponList.add(ItemID.BRONZE_DARTP);
		TwoTickWeaponList.add(ItemID.BRONZE_DARTP_5628);
		TwoTickWeaponList.add(ItemID.BRONZE_DARTP_5635);
		TwoTickWeaponList.add(ItemID.IRON_DART);
		TwoTickWeaponList.add(ItemID.IRON_DARTP);
		TwoTickWeaponList.add(ItemID.IRON_DART_P);
		TwoTickWeaponList.add(ItemID.IRON_DARTP_5636);
		TwoTickWeaponList.add(ItemID.STEEL_DART);
		TwoTickWeaponList.add(ItemID.STEEL_DARTP);
		TwoTickWeaponList.add(ItemID.STEEL_DARTP_5630);
		TwoTickWeaponList.add(ItemID.STEEL_DARTP_5637);
		TwoTickWeaponList.add(ItemID.BLACK_DART);
		TwoTickWeaponList.add(ItemID.BLACK_DARTP);
		TwoTickWeaponList.add(ItemID.BLACK_DARTP_5631);
		TwoTickWeaponList.add(ItemID.BLACK_DARTP_5638);
		TwoTickWeaponList.add(ItemID.MITHRIL_DART);
		TwoTickWeaponList.add(ItemID.MITHRIL_DARTP);
		TwoTickWeaponList.add(ItemID.MITHRIL_DARTP_5632);
		TwoTickWeaponList.add(ItemID.MITHRIL_DARTP_5639);
		TwoTickWeaponList.add(ItemID.ADAMANT_DART);
		TwoTickWeaponList.add(ItemID.ADAMANT_DARTP);
		TwoTickWeaponList.add(ItemID.ADAMANT_DARTP_5633);
		TwoTickWeaponList.add(ItemID.ADAMANT_DARTP_5640);
		TwoTickWeaponList.add(ItemID.RUNE_DART);
		TwoTickWeaponList.add(ItemID.RUNE_DARTP);
		TwoTickWeaponList.add(ItemID.RUNE_DARTP_5634);
		TwoTickWeaponList.add(ItemID.RUNE_DARTP_5641);
		TwoTickWeaponList.add(ItemID.AMETHYST_DART);
		TwoTickWeaponList.add(ItemID.AMETHYST_DARTP);
		TwoTickWeaponList.add(ItemID.AMETHYST_DARTP_25855);
		TwoTickWeaponList.add(ItemID.AMETHYST_DARTP_25857);
		TwoTickWeaponList.add(ItemID.DRAGON_DART);
		TwoTickWeaponList.add(ItemID.DRAGON_DARTP);
		TwoTickWeaponList.add(ItemID.DRAGON_DARTP_11233);
		TwoTickWeaponList.add(ItemID.DRAGON_DARTP_11234);

		// Blowpipe
		TwoTickWeaponList.add(ItemID.TOXIC_BLOWPIPE);
		TwoTickWeaponList.add(ItemID.BLAZING_BLOWPIPE);
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
			// Split each entry into its components: name, number1, number2
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

		return WeaponList;
	}

	public void playCustomSound(File soundFile, int volume)
	{
		try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile))
		{
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);

			// Adjust the volume
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
			{
				FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

				// Convert volume from 0-100 to decibels
				float volumeInDecibels = 20f * (float) Math.log10(volume / 100.0);
				volumeControl.setValue(volumeInDecibels);
			}

			clip.start();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
		{
			log.error("Error playing custom sound: {}", e.getMessage());
		}
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
