package io.github.phantamanta44.pwarfare.gui;

import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Sets;

public class GameGui implements Listener, ITickHandler {
	
	protected Inventory inv;
	protected Collection<HumanEntity> players;

	public GameGui(String name, int rows) {
		inv = Bukkit.getServer().createInventory(null, rows * 9, name);
		players = Sets.newHashSet();
		Bukkit.getServer().getPluginManager().registerEvents(this, PWarfare.INSTANCE);
		PWarfare.INSTANCE.registerTickHandler(this);
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		if (event.getInventory() != inv)
			return;
		players.add(event.getPlayer());
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() != inv)
			return;
		inventoryClicked(event);
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory() != inv)
			return;
		players.remove(event.getPlayer());
		if (players.isEmpty()) {
			HandlerList.unregisterAll(this);
			PWarfare.INSTANCE.unreigsterTickHandler(this);
		}
	}
	
	protected void inventoryClicked(InventoryClickEvent event) {
		// NO-OP
	}
	
	public void showGui(Player pl) {
		pl.openInventory(inv);
	}
	
	public void hideGui(Player pl) {
		if (pl.getOpenInventory() == inv)
			pl.closeInventory();
	}
	
	public void killGui() {
		for (HumanEntity pl : players) {
			if (pl.getOpenInventory() == inv)
				pl.closeInventory();
		}
	}

	@Override
	public void tick(long tick) {
		// NO-OP
	}
	
}
