package com.click2minimize;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
	name = "Click2Minimize",
	description = "Minimizes the game client when you click on specific skilling spots",
	tags = {"afk", "minimize", "netflix"}
)
public class Click2MinimizePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private Click2MinimizeConfig config;

	@Inject
	private Notifier notifier;

	private Set<String> minimizeTargets = new HashSet<>();
	private Set<String> cancelTargets = new HashSet<>();

	@Override
	protected void startUp() throws Exception
	{
		log.info("Click2Minimize started!");
		parseTargets();
		parseCancelTargets();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Click2Minimize stopped!");
		minimizeTargets.clear();
		cancelTargets.clear();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("click2minimize"))
		{
			if (event.getKey().equals("minimizeTargets"))
			{
				parseTargets();
			}
			else if (event.getKey().equals("cancelChatMessages"))
			{
				parseCancelTargets();
			}
		}
	}

	private void parseTargets()
	{
		String targetsString = config.minimizeTargets();
		if (targetsString == null || targetsString.isEmpty())
		{
			minimizeTargets.clear();
			return;
		}

		minimizeTargets = Arrays.stream(targetsString.split(","))
			.map(String::trim)
			.map(String::toLowerCase)
			.collect(Collectors.toSet());
	}

	private void parseCancelTargets()
	{
		String cancelString = config.cancelChatMessages();
		if (cancelString == null || cancelString.isEmpty())
		{
			cancelTargets.clear();
			return;
		}

		cancelTargets = Arrays.stream(cancelString.split(","))
			.map(String::trim)
			.map(String::toLowerCase)
			.collect(Collectors.toSet());
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		String option = event.getMenuOption();
		String target = Text.removeTags(event.getMenuTarget());
		
		if (option == null || target == null || option.isEmpty())
		{
			return;
		}

		String actionString = option + ":" + target;

		if (config.debugMode())
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Click2Minimize Action: " + actionString, null);
		}

		if (minimizeTargets.contains(actionString.toLowerCase()))
		{
			minimizeWindow();
		}
	}

	@Subscribe
	public void onChatMessage(net.runelite.api.events.ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE && event.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		String message = Text.removeTags(event.getMessage()).toLowerCase();

		if (message.startsWith("click2minimize"))
		{
			return;
		}

		if (!isWindowMinimized())
		{
			return;
		}

		for (String cancelTarget : cancelTargets)
		{
			if (message.contains(cancelTarget))
			{
				if (config.debugMode())
				{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Click2Minimize Cancelled by rule '" + cancelTarget + "' from message: " + message, null);
				}
				notifier.notify("Click2Minimize action cancelled");
				break;
			}
		}
	}

	private boolean isWindowMinimized()
	{
		Component canvas = client.getCanvas();
		if (canvas != null)
		{
			Window window = SwingUtilities.windowForComponent(canvas);
			if (window instanceof Frame)
			{
				return (((Frame) window).getExtendedState() & Frame.ICONIFIED) != 0;
			}
		}
		return false;
	}

	public void minimizeWindow()
	{
		Component canvas = client.getCanvas();
		if (canvas != null)
		{
			Window window = SwingUtilities.windowForComponent(canvas);
			if (window instanceof Frame)
			{
				((Frame) window).setExtendedState(Frame.ICONIFIED);
			}
		}
	}

	@Provides
	Click2MinimizeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(Click2MinimizeConfig.class);
	}
}
