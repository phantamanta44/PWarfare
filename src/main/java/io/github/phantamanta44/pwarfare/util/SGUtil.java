package io.github.phantamanta44.pwarfare.util;

import io.github.phantamanta44.pwarfare.PWarfare;
import net.dmulloy2.swornguns.types.Attachment;
import net.dmulloy2.swornguns.types.Gun;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SGUtil {

	public static Gun getGun(String name) {
		return PWarfare.INSTANCE.sguns.getLoadedGuns().get(name);
	}
	
	public static Attachment getAtt(String name) {
		return PWarfare.INSTANCE.sguns.getLoadedAttachments().get(name);
	}
	
	public static Gun getPlayerGun(Player player, ItemStack stack) {
		return PWarfare.INSTANCE.sguns.getGunPlayer(player).getGun(stack);
	}
	
}
