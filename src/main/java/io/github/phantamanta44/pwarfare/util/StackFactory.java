package io.github.phantamanta44.pwarfare.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StackFactory {

	public static ItemStack generateStack(Material type, int count, int data, String name) {
		ItemStack is = new ItemStack(type, count, (short)data);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + name);
		is.setItemMeta(meta);
		return is;
	}
	
}
