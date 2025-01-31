package com.YouMissedThatOne;

import lombok.extern.slf4j.Slf4j;
import javax.sound.sampled.*;
import java.io.*;
import java.util.*;


@Slf4j

public class SoundManager {

    private final YouMissedThatOneConfig config;

    public SoundManager(YouMissedThatOneConfig config) {
        this.config = config;
    }

    private final List<Clip> clipPool = new ArrayList<>();
    private final Map<File, byte[]> soundCache = new HashMap<>();
    private final Map<File, Clip> clipMap = new HashMap<>();

    private byte[] loadSoundData(File soundFile) throws IOException {
        if (!soundCache.containsKey(soundFile)) {
            try (InputStream is = new FileInputStream(soundFile);
                 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                byte[] data = new byte[1024];
                int nRead;
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                soundCache.put(soundFile, buffer.toByteArray());
            }
        }
        return soundCache.get(soundFile);
    }

    public void playCustomSound(File soundFile, int volume) {
        try {
            if (!config.SoundSwapOverlap())
            {
                if (clipMap.containsKey(soundFile))
                {
                    Clip existingClip = clipMap.get(soundFile);
                    if (existingClip.isOpen())
                    {
                        existingClip.stop();
                        existingClip.setFramePosition(0);
                        adjustVolume(existingClip, volume);
                        existingClip.start();
                        return;
                    }
                }
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new ByteArrayInputStream(loadSoundData(soundFile))
            );

            Clip clip;

            // Overlapping sounds need to create a new clip every time. Clips are closed later on
            if (config.SoundSwapOverlap())
            {
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            }
            // If not overlapping, reuse existing clip and map the clips
            else
            {
                if (clipMap.containsKey(soundFile))
                {
                    clip = clipMap.get(soundFile);
                    if (!clip.isOpen())
                    {
                        clip.open(audioInputStream);
                    }
                }
                else
                {
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clipMap.put(soundFile, clip);
                }
            }

            adjustVolume(clip, volume);

            // on overlapping sounds, add listener and close clips once clips have stopped
            if (config.SoundSwapOverlap()) {
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }

            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.error("Error playing custom sound: {}", e.getMessage());
        }
    }

    private void adjustVolume(Clip clip, int volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volumeInDecibels = (volume > 0) ? 20f * (float) Math.log10(volume / 100.0) : -80f;
            volumeControl.setValue(volumeInDecibels);
        }
    }

}