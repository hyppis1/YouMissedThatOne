package com.YouMissedThatOne;

import net.runelite.api.Client;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;

public class YouMissedThatOneOverlay extends OverlayPanel
{
    private final YouMissedThatOneConfig config;
    private final YouMissedThatOnePlugin plugin;
    private final Client client;

    @Inject
    YouMissedThatOneOverlay(Client client, YouMissedThatOnePlugin plugin, YouMissedThatOneConfig config)
    {
        super(plugin);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.TOP_LEFT);


    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.EnableOverLay())
        {
            return null;
        }

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Weapon ID: ")
                .right(String.valueOf(plugin.HeldWeaponID))
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Animation ID: ")
                .right(String.valueOf(plugin.PlayingAnimationID))
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Sound ID: ")
                .right(String.valueOf(plugin.DefaultSoundID))
                .build());
        return super.render(graphics);
    }
}
