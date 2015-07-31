package io.github.phantamanta44.pwarfare;

import io.github.phantamanta44.pwarfare.data.GameConfig;
import io.github.phantamanta44.pwarfare.data.GameDB;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;

import java.util.Set;
import java.util.logging.Logger;

import net.dmulloy2.swornguns.SwornGuns;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Sets;

public class PWarfare extends JavaPlugin {

	public static PWarfare INSTANCE;
	public GameDB database;
	public Logger logger;
	public GameConfig config;
	public SwornGuns sguns;
	
	private Set<ITickHandler> tickHandlers = Sets.newHashSet();
	private long currentTick = 0;
	private BukkitRunnable ticker = new BukkitRunnable() {
		@Override
		public void run() {
			for (ITickHandler th : PWarfare.this.tickHandlers)
				th.tick(currentTick);
			currentTick++;
		}
	};
	
	@Override
	public void onEnable() {
		INSTANCE = this;
		logger = Logger.getLogger("PWarfare");
		sguns = (SwornGuns)Bukkit.getServer().getPluginManager().getPlugin("SwornGuns");
		
		loadConfig();
		database = new GameDB();
		
		registerHandlers();
		
		ticker.runTaskTimer(this, 1, 1);
		Game.INSTANCE.startGame();
	}
	
	@Override
	public void onDisable() {
		Game.INSTANCE.stopGame();
		Game.broadcast("Plugin destructed.");
		ticker.cancel();
		database.dbWrite();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pwarfare")) {
			if (args.length == 1 && sender.hasPermission("pwarfare.admin")) {
				if (args[0].equalsIgnoreCase("reload"))
					reloadConfig();
				if (args[0].equalsIgnoreCase("start"))
					Game.INSTANCE.startGame();
				if (args[0].equalsIgnoreCase("stop"))
					Game.INSTANCE.stopGame();
			}
		}
		return true;
	}
	
	private void loadConfig() {
		if (!GameConfig.isValid(getConfig()))
			saveDefaultConfig();
		config = new GameConfig(getConfig());
	}
	
	private void registerHandlers() {
		tickHandlers.add(Game.INSTANCE);
	}
	
	public void registerTickHandler(ITickHandler handler) {
		tickHandlers.add(handler);
	}
	
	public void unreigsterTickHandler(ITickHandler handler) {
		tickHandlers.remove(handler);
	}
	
}
