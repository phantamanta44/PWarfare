package io.github.phantamanta44.pwarfare.data;

import org.bukkit.configuration.ConfigurationSection;

public class GameConfig {
	
	public int minPlayers;
	public int maxTime;

	public GameConfig(ConfigurationSection sec) {
		minPlayers = sec.getInt("Config.MinimumPlayers");
		maxTime = sec.getInt("Config.MaximumTime") * 20;
	}
	
	public static boolean isValid(ConfigurationSection sec) {
		return sec.contains("Config");
	}
	
}
