package io.github.phantamanta44.pwarfare.gui;

import io.github.phantamanta44.pwarfare.Game;
import io.github.phantamanta44.pwarfare.data.GameMap;
import io.github.phantamanta44.pwarfare.data.GameMap.GameMode;
import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.data.GamePlayer.PlayerState;
import io.github.phantamanta44.pwarfare.data.GameSquad;
import io.github.phantamanta44.pwarfare.objective.ConqObjective;
import io.github.phantamanta44.pwarfare.util.StackFactory;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RespawnGui extends GameGui {
	
	public static final String[] classNames = new String[] {"ASSAULT", "ENGINEER", "SUPPORT", "RECON"};
	
	private GamePlayer player;
	private boolean canRespawn = false;
	private Object point;

	public RespawnGui(GamePlayer pl) {
		super("DEPLOY", 5);
		player = pl;
		generateLoadoutGui();
		inv.setItem(40, StackFactory.generateStack(Material.BOOK, 1, 0, "Edit Loadout"));
		inv.setItem(43, StackFactory.generateStack(Material.STAINED_GLASS_PANE, 1, 8, ChatColor.AQUA + "DEPLOY"));
		inv.setItem(44, StackFactory.generateStack(Material.STAINED_GLASS_PANE, player.getTimeToRespawn(), 8, ChatColor.AQUA + "DEPLOY"));
		if (Game.INSTANCE.getCurrentMap().getGameMode() == GameMode.CONQ)
			setupConqGui();
	}
	
	private void setupConqGui() {
		ConqObjective obj = (ConqObjective)Game.INSTANCE.getCurrentMap().getObjective();
		ItemStack blueIs = StackFactory.generateStack(Material.STAINED_GLASS_PANE, 1, 11, ChatColor.DARK_GRAY + "Select a spawn point.");
		ItemStack greenIs = StackFactory.generateStack(Material.STAINED_GLASS_PANE, 1, 5, ChatColor.DARK_GRAY + "Select a spawn point.");
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++)
				inv.setItem(y * 9 + x, blueIs);
		}
		for (int i = 0; i < 9; i++)
			inv.setItem(27 + i, greenIs);
		inv.setItem(10, StackFactory.generateStack(Material.STAINED_CLAY, 1, 9, "Deploy on Team Spawn"));
		for (int i = 0; i < 3; i++) {
			GameTeam flagOwner = obj.getFlagStatus(i);
			if (flagOwner == player.getTeam())
				inv.setItem(i * 2 + 12, StackFactory.generateStack(Material.STAINED_CLAY, 1, 14, ChatColor.BLUE + "Deploy on Point " + ConqObjective.flagName[i]));
			else if (flagOwner == GameTeam.NONE)
				inv.setItem(i * 2 + 12, StackFactory.generateStack(Material.WOOL, 1, 0, "Point " + ConqObjective.flagName[i]));
			else
				inv.setItem(i * 2 + 12, StackFactory.generateStack(Material.STAINED_CLAY, 1, 1, "Point " + ConqObjective.flagName[i]));
		}
		GameSquad squad = player.getSquad();
		if (squad != null) {
			for (int i = 0; i < 3; i++) {
				try {
					GamePlayer member = squad.members.get(i);
					if (member.getState() == PlayerState.INGAME)
						inv.setItem(27 + i, StackFactory.generateStack(Material.ARMOR_STAND, 1, 0, ChatColor.BLUE + "Deploy on " + member.getName()));
					else
						inv.setItem(27 + i, StackFactory.generateStack(Material.STEP, 1, 0, member.getPlayer().getName()));
				} catch (Throwable th) { }
			}
		}
	}

	private void generateLoadoutGui() {
		for (int i = 36; i <= 39; i++)
			inv.setItem(i, StackFactory.generateStack(Material.STAINED_GLASS, 1, player.getKitIndex() == (i - 36) ? 9 : 8, classNames[i - 36]));
	}
	
	@Override
	protected void inventoryClicked(InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getCurrentItem() != null) {
			ItemStack stack = event.getCurrentItem();
			if (event.getSlot() >= 36 && event.getSlot() <= 39) {
				player.setKit(event.getSlot() - 36);
				generateLoadoutGui();
			}
			else if (event.getSlot() == 40) {
				killGui();
				new LoadoutGui(player).showGui(player.getPlayer());
			}
			else if (event.getSlot() >= 43) {
				if (canRespawn && point != null) {
					if (point instanceof Location)
						player.getPlayer().teleport((Location)point);
					else if (point instanceof Entity)
						player.getPlayer().teleport((Entity)point);
					player.receiveKit();
					player.setState(PlayerState.INGAME);
				}
			}
			else if (Game.INSTANCE.getCurrentMap().getGameMode() == GameMode.CONQ)
				onConqClick(stack, event);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void onConqClick(ItemStack stack, InventoryClickEvent event) {
		GameMap map = Game.INSTANCE.getCurrentMap();
		if (event.getSlot() == 10) {
			point = map.getSpawnPoint(player.getTeam());
			setupConqGui();
			inv.setItem(10, StackFactory.generateStack(Material.STAINED_GLASS, 1, 5, "Deploy on Team Spawn"));
		}
		else if (event.getSlot() == 12) {
			if (stack.getData().getData() == (byte)14) {
				point = ((ConqObjective)map.getObjective()).getCap(0).toLocation(map.getWorld());
				setupConqGui();
				inv.setItem(12, StackFactory.generateStack(Material.STAINED_GLASS, 1, 5, ChatColor.BLUE + "Deploy on Point A"));
			}
		}
		else if (event.getSlot() == 14) {
			if (stack.getData().getData() == (byte)14) {
				point = ((ConqObjective)map.getObjective()).getCap(1).toLocation(map.getWorld());
				setupConqGui();
				inv.setItem(14, StackFactory.generateStack(Material.STAINED_GLASS, 1, 5, ChatColor.BLUE + "Deploy on Point B"));
			}
		}
		else if (event.getSlot() == 16) {
			if (stack.getData().getData() == (byte)14) {
				point = ((ConqObjective)map.getObjective()).getCap(2).toLocation(map.getWorld());
				setupConqGui();
				inv.setItem(16, StackFactory.generateStack(Material.STAINED_GLASS, 1, 5, ChatColor.BLUE + "Deploy on Point C"));
			}
		}
		else if (event.getSlot() >= 27 && event.getSlot() <= 29) {
			int i = 27 - event.getSlot();
			if (player.getSquad() != null && player.getSquad().members.size() >= i) {
				point = player.getSquad().members.get(i).getPlayer();
				setupConqGui();
				inv.setItem(event.getSlot(), StackFactory.generateStack(Material.STAINED_GLASS, 1, 5, ChatColor.BLUE + "Deploy on " + player.getSquad().members.get(i).getName()));
			}
		}
	}

	@Override
	public void tick(long tick) {
		if (player.getTimeToRespawn() <= 0) {
			if (canRespawn = false) {
				canRespawn = true;
				for (int i = 0; i < 2; i++)
					inv.setItem(43 + i, StackFactory.generateStack(Material.STAINED_GLASS_PANE, 1, 9, ChatColor.AQUA + "DEPLOY"));
			}
		}
		else
			inv.getItem(44).setAmount(player.getTimeToRespawn());
	}

}
