package io.github.phantamanta44.pwarfare.handler;

import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.data.GamePlayer.PlayerState;
import net.dmulloy2.swornguns.types.Gun;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setKeepLevel(true);
		event.setKeepInventory(true);
		event.setDroppedExp(0);
		try {
			GamePlayer victimGp = PWarfare.INSTANCE.database.getPlayer(event.getEntity().getUniqueId());
			Player killer = event.getEntity().getKiller();
			GamePlayer killerGp = PWarfare.INSTANCE.database.getPlayer(killer.getUniqueId());
			victimGp.onDeath(killerGp);
			killerGp.onKill(victimGp);
			String killWeapon;
			if (killer.getItemInHand() == null)
				killWeapon = "HANDS";
			else if (killer.getItemInHand().getType() == Material.IRON_SWORD)
				killWeapon = "KNIFE";
			else {
				Gun gun = PWarfare.INSTANCE.sguns.getGunPlayer(killer).getGun(killer.getItemInHand());
				if (gun == null)
					killWeapon = killer.getItemInHand().getType().toString().replaceAll("_", "");
				else
					killWeapon = gun.getGunName();
			}
			event.setDeathMessage(killer.getName() + " [" + killWeapon + "] " + event.getEntity().getName());
		} catch (Throwable th) {
			event.setDeathMessage("");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (PWarfare.INSTANCE.database.getPlayer(event.getPlayer().getUniqueId()) == null)
			PWarfare.INSTANCE.database.registerPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		GamePlayer gp = PWarfare.INSTANCE.database.getPlayer(event.getPlayer().getUniqueId());
		gp.setState(PlayerState.OFFLINE);
		gp.setTeam(GameTeam.NONE);
		gp.leaveSquad();
	}
	
}
