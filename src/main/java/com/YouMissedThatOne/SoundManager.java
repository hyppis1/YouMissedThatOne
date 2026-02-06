package com.YouMissedThatOne;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.util.*;

import net.runelite.client.audio.AudioPlayer;



@Slf4j
public class SoundManager
{
    private final YouMissedThatOneConfig config;
    private final AudioPlayer audioPlayer;

    private final Map<File, byte[]> soundCache = new HashMap<>();

    public SoundManager(YouMissedThatOneConfig config, AudioPlayer audioPlayer)
    {
        this.config = config;
        this.audioPlayer = audioPlayer;
    }

    private byte[] loadSoundData(File file) throws IOException
    {
        return soundCache.computeIfAbsent(file, f ->
        {
            try (var is = new java.io.FileInputStream(f);
                 var buffer = new java.io.ByteArrayOutputStream())
            {
                byte[] data = new byte[1024];
                int nRead;
                while ((nRead = is.read(data)) != -1)
                {
                    buffer.write(data, 0, nRead);
                }
                return buffer.toByteArray();
            }
            catch (IOException e)
            {
                log.error("Failed to load sound {}", f, e);
                return null;
            }
        });
    }

    public void playCustomSound(File soundFile)
    {
        try
        {
            // Get the volume from config (0–100)
            int volumePercent = config.SoundSwapVolume();

            // Convert 0–100 volume to decibels for AudioPlayer
            float gainDb = (volumePercent > 0)
                    ? 20f * (float) Math.log10(volumePercent / 100.0f)
                    : -80f; // silent if volume = 0

            audioPlayer.play(soundFile, gainDb);
        }
        catch (IOException | javax.sound.sampled.UnsupportedAudioFileException | javax.sound.sampled.LineUnavailableException e)
        {
            log.error("Error playing custom sound: {}", e.getMessage());
        }
    }

    public void cleanup()
    {
        soundCache.clear();
    }
}
