package io.github.phantamanta44.pwarfare.gui;

import io.github.phantamanta44.pwarfare.data.AttachmentWrapper;
import io.github.phantamanta44.pwarfare.data.GameKit;
import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.data.GunWrapper;
import io.github.phantamanta44.pwarfare.util.StackFactory;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LoadoutGui extends GameGui {
	
	private GamePlayer player;
	private boolean editingMain = false;

	public LoadoutGui(GamePlayer pl) {
		super("LOADOUT", 5);
		player = pl;
		constructGui();
	}
	
	public void constructGui() {
		for (int i = 0; i < 4; i++)
			inv.setItem(i, StackFactory.generateStack(Material.STAINED_GLASS, 1, player.getKitIndex() == (i) ? 9 : 8, RespawnGui.classNames[i]));
		
		GunWrapper[] prim = player.getKit().getOwnedGuns(0);
		GunWrapper[] primGlob = player.getGlobalKit().getOwnedGuns(0);
		AttachmentWrapper[] scopes = player.getKit().getSelectedGun(0).getAttachments(0);
		AttachmentWrapper[] att1 = player.getKit().getSelectedGun(0).getAttachments(1);
		AttachmentWrapper[] att2 = player.getKit().getSelectedGun(0).getAttachments(2);
		GunWrapper[] sec = player.getGlobalKit().getOwnedGuns(1);
		GunWrapper[] gadget = player.getKit().getOwnedGuns(2);
		
		if (editingMain) { // TODO Display overflowing guns/attachments somehow
			inv.setItem(8, StackFactory.generateStack(Material.SULPHUR, 1, 0, ChatColor.BOLD + "» " + ChatColor.RESET + "Return"));
			inv.setItem(9, StackFactory.generateStack(getPrimKit().getSelectedGun(0).gun.gun.getMaterial().getMaterial(), 1, 0, getPrimKit().getSelectedGun(0).gun.gun.getName()));
			for (int i = 0; i < scopes.length && i < 9; i++)
				inv.setItem(18 + i, StackFactory.generateStack(Material.STAINED_GLASS, 1, i == getPrimKit().getSelectedGun(0).getEquippedIndex(0) ? 9 : 8, scopes[i].att.getName()));
			for (int i = 0; i < att1.length && i < 9; i++)
				inv.setItem(27 + i, StackFactory.generateStack(Material.STAINED_GLASS, 1, i == getPrimKit().getSelectedGun(0).getEquippedIndex(1) ? 9 : 8, att1[i].att.getName()));
			for (int i = 0; i < att2.length && i < 9; i++)
				inv.setItem(36 + i, StackFactory.generateStack(Material.STAINED_GLASS, 1, i == getPrimKit().getSelectedGun(0).getEquippedIndex(2) ? 9 : 8, att2[i].att.getName()));
		}
		else {
			inv.setItem(8, StackFactory.generateStack(Material.REDSTONE, 1, 0, ChatColor.BOLD + "» " + ChatColor.RESET + "Modify Attachments"));
			for (int i = 0; i < prim.length && i < 9; i++) {
				inv.setItem(9 + i, StackFactory.generateStack(prim[i].gun.getMaterial().getMaterial(), 1, 0, prim[i].gun.getName()));
				if (i == player.getKit().getSelectedIndex(0))
					inv.getItem(9 + i).addEnchantment(Enchantment.LURE, 1);
			}
			for (int i = 0; i < primGlob.length && i < 9; i++) {
				inv.setItem(18 + i, StackFactory.generateStack(primGlob[i].gun.getMaterial().getMaterial(), 1, 0, primGlob[i].gun.getName()));
				if (i == player.getKit().getSelectedIndex(0))
					inv.getItem(18 + i).addEnchantment(Enchantment.LURE, 1);
			}
			for (int i = 0; i < sec.length && i < 9; i++) {
				inv.setItem(27 + i, StackFactory.generateStack(sec[i].gun.getMaterial().getMaterial(), 1, 0, sec[i].gun.getName()));
				if (i == player.getKit().getSelectedIndex(1))
					inv.getItem(27 + i).addEnchantment(Enchantment.LURE, 1);
			}
			for (int i = 0; i < gadget.length && i < 9; i++) {
				inv.setItem(36 + i, StackFactory.generateStack(gadget[i].gun.getMaterial().getMaterial(), 1, 0, gadget[i].gun.getName()));
				if (i == player.getKit().getSelectedIndex(2))
					inv.getItem(36 + i).addEnchantment(Enchantment.LURE, 1);
			}
		}
	}
	
	@Override
	protected void inventoryClicked(InventoryClickEvent event) {
		event.setCancelled(true);
		if (event.getSlot() < 4)
			player.setKit(event.getSlot());
		else if (event.getSlot() == 8)
			editingMain = !editingMain;
		else {
			if (editingMain) {
				if (event.getSlot() >= 18 && event.getSlot() < 27)
					getPrimKit().getSelectedGun(0).selectAttachment(0, event.getSlot() - 18);
				if (event.getSlot() >= 27 && event.getSlot() < 36)
					getPrimKit().getSelectedGun(0).selectAttachment(1, event.getSlot() - 27);
				if (event.getSlot() >= 36)
					getPrimKit().getSelectedGun(0).selectAttachment(2, event.getSlot() - 36);
			}
			else {
				if (event.getSlot() >= 9 && event.getSlot() < 18) {
					player.getKit().selectGun(0, event.getSlot() - 9);
					player.setPrimGlobal(false);
				}
				if (event.getSlot() >= 18 && event.getSlot() < 27) {
					player.getGlobalKit().selectGun(0, event.getSlot() - 18);
					player.setPrimGlobal(true);
				}
				else if (event.getSlot() >= 27 && event.getSlot() < 36)
					player.getGlobalKit().selectGun(1, event.getSlot() - 27);
				else if (event.getSlot() >= 36)
					player.getKit().selectGun(2, event.getSlot() - 36);
			}
		}
		constructGui();
	}
	
	private GameKit getPrimKit() {
		if (player.isPrimGlobal())
			return player.getGlobalKit();
		return player.getKit();
	}

}
