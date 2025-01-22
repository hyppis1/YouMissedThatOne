package com.YouMissedThatOne;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class YouMissedThatOnePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(YouMissedThatOnePlugin.class);
		RuneLite.main(args);
	}
}