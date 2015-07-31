package io.github.phantamanta44.pwarfare.gui;

import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.util.StackFactory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PostMatchGui extends GameGui {
	
	private boolean statsScreen;
	private GamePlayer player;
	
	public PostMatchGui(GamePlayer statSource) {
		super("END OF ROUND", 6);
		statsScreen = true;
		player = statSource;
		//TODO Stats screen
	}

	public PostMatchGui(boolean won) {
		super(won ? "YOUR TEAM WON!" : "YOUR TEAM LOST!", 1);
		ItemStack is = StackFactory.generateStack(Material.STAINED_GLASS_PANE, 1, won ? 5 : 14, ChatColor.GRAY.toString() + ChatColor.BOLD + "»" + ChatColor.BLUE + " Continue");
		for (int i = 0; i < 9; i++)
			inv.setItem(i, is);
	}
	
	@Override
	protected void inventoryClicked(InventoryClickEvent event) {
		event.setCancelled(true);
		if (!statsScreen)
			killGui();
	}
	
}
