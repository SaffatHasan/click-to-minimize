package com.click2minimize;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("click2minimize")
public interface Click2MinimizeConfig extends Config
{
	@ConfigItem(
		keyName = "debugMode",
		name = "Enable Debug Mode",
		description = "When enabled, any action you perform will be logged to the game chat in the format 'Action:Target', so you can easily copy it to the Minimize Targets list."
	)
	default boolean debugMode()
	{
		return false;
	}

	@ConfigItem(
		keyName = "minimizeTargets",
		name = "Minimize Targets",
		description = "Comma-separated list of target actions (e.g. 'Chop down:Tree, Lure:Fishing spot')."
	)
	default String minimizeTargets()
	{
		return "";
	}
}
