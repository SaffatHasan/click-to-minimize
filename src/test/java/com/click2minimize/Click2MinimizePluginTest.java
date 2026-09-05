package com.click2minimize;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Click2MinimizePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Click2MinimizePlugin.class);
		RuneLite.main(args);
	}
}
