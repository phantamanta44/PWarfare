package io.github.phantamanta44.pwarfare.data;

import io.github.phantamanta44.pwarfare.Game;
import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GameKit.GameKitSerializable;
import io.github.phantamanta44.pwarfare.data.GameKit.KitClass;
import io.github.phantamanta44.pwarfare.data.GameKit.PlayerGun;
import io.github.phantamanta44.pwarfare.gui.PostMatchGui;
import io.github.phantamanta44.pwarfare.gui.RespawnGui;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;
import io.github.phantamanta44.pwarfare.util.SGUtil;
import io.github.phantamanta44.pwarfare.util.TitleHelper;

import java.util.UUID;

import net.dmulloy2.swornguns.types.Gun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;

public class GamePlayer implements ITickHandler {
	
	private static final String SB_OBJ_NAME = ChatColor.GOLD + "Battlefield";
	private static final String[] rankNames1 = new String[] {
		"Private First Class", "Lance Corporal", "Corporal", "Sergeant"
	};
	private static final String[] rankNames2 = new String[] {
		"Staff Sergeant", "Gunnery Sergeant", "Master Sergeant", "First Sergeant",
		"Master Gunnery Sergeant", "Sergeant Major"
	};
	private static final String[] rankNames3 = new String[] {
		"One", "Two", "Three", "Four", "Five"
	};
	private static final String PROM_TEXT = "[\"\",{\"text\":\"PROMOTION!\",\"color\":\"blue\",\"bold\":\"true\",\"insertion\":\"/title @a subtitle %s\"}]";
	
	private OfflinePlayer player;
	private int kills, deaths, level, xp;
	private int matchKills = 0, matchDeaths = 0;
	private GameKit[] kits;
	private GameKit globalKit;
	private boolean primGlobal = false;
	private int kit = 0;
	private int deadTime = 0;
	private boolean stateChanged = false;
	private PlayerState state = PlayerState.OFFLINE;
	private GameTeam team = GameTeam.NONE;
	private GameSquad currentSquad;

	public GamePlayer(GamePlayerSerializable data) {
		player = Bukkit.getServer().getOfflinePlayer(data.id);
		kills = data.kills;
		deaths = data.deaths;
		level = data.level;
		xp = data.xp;
		kits = new GameKit[data.kits.length];
		for (int i = 0; i < kits.length; i++)
			kits[i] = new GameKit(data.kits[i]);
		globalKit = new GameKit(data.globalKit);
		kit = data.kit;
	}
	
	public GamePlayer(OfflinePlayer pl) {
		player = pl;
		kills = deaths = level = xp = 0;
	}

	public String getName() {
		return player.getName();
	}
	
	public UUID getId() {
		return player.getUniqueId();
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(getId());
	}
	
	public GamePlayerSerializable toSerializable() {
		return new GamePlayerSerializable(this);
	}

	public PlayerState getState() {
		return state;
	}
	
	public void setState(PlayerState newState) {
		state = newState;
		stateChanged = true;
	}

	public static class GamePlayerSerializable extends Serializable {
		
		public final UUID id;
		public final int kills, deaths, level, xp;
		public final GameKitSerializable[] kits;
		public final int kit;
		private final GameKitSerializable globalKit;
		
		public GamePlayerSerializable(GamePlayer pl) {
			parentType = pl.getClass().getName();
			id = pl.getId();
			kills = pl.kills;
			deaths = pl.deaths;
			level = pl.level;
			xp = pl.xp;
			kits = new GameKitSerializable[pl.getKits().length];
			for (int i = 0; i < kits.length; i++)
				kits[i] = pl.getKit(i).serialize();
			kit = pl.kit;
			globalKit = pl.getGlobalKit().serialize();
		}
		
	}
	
	public static enum PlayerState {
		OFFLINE, WAITING, INGAME, DEAD, RESPAWNING, POST;
	}
	
	public static enum GameTeam {
		RED, BLUE, NONE;
	}

	public GameTeam getTeam() {
		return team;
	}

	public void setTeam(GameTeam newTeam) {
		team = newTeam;
	}

	@Override
	public void tick(long tick) {
		if (state == PlayerState.OFFLINE) {
			if (player.isOnline())
				state = PlayerState.WAITING;
			else
				return;
		}
		switch (state) {
		case WAITING:
			matchKills = matchDeaths = 0;
			setTeam(GameTeam.NONE);
			break;
		case INGAME:
			if (player.getPlayer().getGameMode() == GameMode.SPECTATOR || player.getPlayer().getGameMode() == GameMode.SURVIVAL)
				player.getPlayer().setGameMode(GameMode.ADVENTURE);
			break;
		case POST:
			player.getPlayer().teleport(Game.INSTANCE.getCurrentMap().getPostMatchPos().toLocation(player.getPlayer().getWorld()));
			if (stateChanged) {
				if (Game.INSTANCE.getCurrentMap().isTeamWinning(team))
					player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.WITHER_IDLE, 1.0F, 1.0F);
				else
					player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.WITHER_DEATH, 1.0F, 1.0F);
			}
			deadTime = 0;
			if (player.getPlayer().getGameMode() != GameMode.SPECTATOR)
				player.getPlayer().setGameMode(GameMode.SPECTATOR);
			if (player.getPlayer().getOpenInventory() == null) {
				if (stateChanged)
					new PostMatchGui(Game.INSTANCE.getCurrentMap().isTeamWinning(team)).showGui(player.getPlayer());
				else
					new PostMatchGui(this).showGui(player.getPlayer());
			}
			break;
		case DEAD:
			if (stateChanged)
				deadTime = 120;
			player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 128, 3));
			if (player.getPlayer().getGameMode() != GameMode.SPECTATOR)
				player.getPlayer().setGameMode(GameMode.SPECTATOR);
			deadTime--;
			if (deadTime <= 0)
				setState(PlayerState.RESPAWNING);
			break;
		case RESPAWNING:
			player.getPlayer().teleport(Game.INSTANCE.getCurrentMap().getLobbyPoint());
			if (player.getPlayer().getOpenInventory() != null)
				new RespawnGui(this).showGui(player.getPlayer());
			break;
		default:
			break;
		}
		stateChanged = false;
		
		if (xp > getXpNeeded()) {
			level++;
			TitleHelper.sendTitleJson(player.getPlayer(), PROM_TEXT, TitleAction.TITLE);
			TitleHelper.sendTitle(player.getPlayer(), "[" + level + "] " + getRank(), TitleAction.SUBTITLE);
			getGlobalKit().setXp(xp);
		}
		
		Scoreboard sb;
		Objective obj;
		if ((sb = player.getPlayer().getScoreboard()) == null || (obj = sb.getObjective("bfdummyobjective")) == null) {
			sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
			obj = sb.registerNewObjective("bfdummyobjective", "DUMMY");
			obj.setDisplayName(SB_OBJ_NAME);
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			player.getPlayer().setScoreboard(sb);
		}
		obj.getScore("Kills").setScore(matchKills);
		obj.getScore("Deaths").setScore(matchDeaths);
		obj.getScore("Level").setScore(level);
		obj.getScore("XP").setScore((int)((float)xp / (float)getXpNeeded()));
	}
	
	public void sendHudAlert(String alert) {
		TitleHelper.sendTitle(player.getPlayer(), alert, TitleAction.SUBTITLE);
	}
	
	public int getXpNeeded() {
		int xp = 1000;
		if (level >= 1)
			xp += 7000;
		if (level >= 2)
			xp += 10000;
		if (level >= 3)
			xp += 11000;
		if (level >= 4)
			xp += 12000;
		if (level >= 5) {
			for (int i = 5; i <= 6 && i <= level; i++)
				xp += 13000;
		}
		if (level >= 7)
			xp += 14000;
		if (level >= 8)
			xp += 15000;
		if (level >= 9)
			xp += 15000;
		if (level >= 10)
			xp += 19000;
		if (level >= 11) {
			for (int i = 11; i <= 13 && i <= level; i++)
				xp += 20000;
		}
		if (level >= 14) {
			for (int i = 14; i <= 20 && i <= level; i++)
				xp += 30000;
		}
		if (level >= 21) {
			for (int i = 21; i <= 28 && i <= level; i++)
				xp += 40000;
		}
		if (level >= 29) {
			for (int i = 29; i <= 36 && i <= level; i++)
				xp += 50000;
		}
		if (level >= 37) {
			for (int i = 37; i <= 38 && i <= level; i++)
				xp += 55000;
		}
		if (level >= 39) {
			for (int i = 39; i <= 43 && i <= level; i++)
				xp += 60000;
		}
		if (level >= 44)
			xp += 80000;
		if (level >= 45) {
			for (int i = 45; i <= 145 && i <= level; i++)
				xp += 230000;
		}
		return xp;
	}
	
	public String getRank() {
		if (level == 0)
			return "Private";
		if (level <= 16) {
			int stars = (level - 1) % 4;
			return rankNames1[(level + (3 -stars)) / 4] + (stars > 0 ? (stars > 1 ? " " + stars + " Stars" : " 1 Star") : "");
		}
		if (level <= 34) {
			int stars = (level - 2) % 3;	
			return rankNames2[(level + (2 - stars) - 16) / 3] + (stars > 0 ? (stars > 1 ? " 2 Stars" : " 1 Star") : "");
		}
		if (level <= 39) {
			int rank = level - 35;
			return "Chief Warrant Officier " + rankNames3[rank];
		}
		if (level == 40)
			return "Second Lieutenant";
		if (level == 41)
			return "First Lieutenant";
		if (level == 42)
			return "Captain";
		if (level == 43)
			return "Major";
		if (level == 44)
			return "Lieutenant Colonel";
		if (level == 45)
			return "Colonel";
		return "Colonel (" + (level - 45) + " Service Stars)";
	}
	
	public void awardXp(int amt) {
		xp += amt;
		getKit().addXp(amt);
	}
	
	public void awardXp(int amt, String reason) {
		awardXp(amt);
		sendHudAlert(reason + "   " + amt);
	}
	
	public void onKill(GamePlayer victim) {
		matchKills++;
		kills++;
		awardXp(100, "ENEMY DOWN");
		try {
			Gun gun = PWarfare.INSTANCE.sguns.getGunsByItem(getPlayer().getItemInHand()).get(0);
			if (primGlobal)
				getGlobalKit().getGun(gun).onKill();
			else
				getKit().getGun(gun).onKill();
		} catch (IndexOutOfBoundsException ex) { }
		catch (NullPointerException ex) {
			PWarfare.INSTANCE.logger.warning("NPE registering kill on gun!");
			ex.printStackTrace();
		}
	}

	public void onDeath(GamePlayer killer) {
		matchDeaths++;
		deaths++;
		setState(PlayerState.DEAD);
		deadTime = 200;
	}

	public void setSquad(GameSquad squad) {
		if (currentSquad != null)
			currentSquad.leaveSquad(this);
		currentSquad = squad;
		squad.joinSquad(this);
	}
	
	public void leaveSquad() {
		if (currentSquad != null) {
			currentSquad.leaveSquad(this);
			currentSquad = null;
		}
	}
	
	public GameSquad getSquad() {
		return currentSquad;
	}
	
	public void setKit(int newKit) {
		kit = newKit;
	}
	
	public GameKit[] getKits() {
		if (kits == null)
			kits = new GameKit[] {new GameKit(KitClass.ASSAULT, this), new GameKit(KitClass.ENGINEER, this), new GameKit(KitClass.SUPPORT, this), new GameKit(KitClass.RECON, this)};
		return kits;
	}
	
	public GameKit getKit(int index) {
		return getKits()[index];
	}
	
	public GameKit getKit() {
		return getKit(kit);
	}
	
	public GameKit getGlobalKit() {
		if (globalKit == null)
			globalKit = new GameKit(KitClass.GENERIC, this);
		return globalKit;
	}
	
	public int getKitIndex() {
		return kit;
	}

	public int getTimeToRespawn() {
		return (int)Math.ceil((float)deadTime / 20F);
	}

	public void receiveKit() {
		try {
			PlayerInventory inv = player.getPlayer().getInventory();
			GunWrapper[] guns = new GunWrapper[] {primGlobal ? getGlobalKit().getSelectedGun(0).gun : getKit().getSelectedGun(0).gun, getGlobalKit().getSelectedGun(1).gun, getKit().getSelectedGun(2).gun};
			for (int i = 0; i < 3; i++) {
				inv.addItem(guns[i].gun.getMaterial().newItemStack(guns[i].amt));
			}
			PlayerGun primGun = primGlobal ? getGlobalKit().getSelectedGun(0) : getKit().getSelectedGun(0);
			Gun pGun = SGUtil.getPlayerGun(player.getPlayer(), inv.getItem(0));
			for (int i = 0; i < 2; i++) {
				AttachmentWrapper wrapper;
				if ((wrapper = primGun.getEquipped(i)) != null)
					pGun.addAttachment(wrapper.att.getFileName());
			}
			replenishAmmo();
				
		} catch (Throwable th) {
			PWarfare.INSTANCE.logger.warning("Error giving kit " + getKit().getName() + " to " + player.getName() + "!");
			th.printStackTrace();
		}
	}
	
	public void replenishAmmo() {
		PlayerInventory inv = player.getPlayer().getInventory();
		GunWrapper[] guns = new GunWrapper[] {primGlobal ? getGlobalKit().getSelectedGun(0).gun : getKit().getSelectedGun(0).gun, getGlobalKit().getSelectedGun(1).gun, getKit().getSelectedGun(2).gun};
		for (int i = 0; i < 3; i++) {
			if (guns[i].gun.isHasClip()) {
				inv.remove(guns[i].gun.getAmmo().getMaterial());
				int k = 0;
				for (int n = guns[i].gun.getMaxClipSize() * (kit == 2 ? 2 : (kit == 3 ? 9 : 8)); n > 0; n -= Math.min(64, n)) {
					inv.setItem(9 * (i + 1) + k, guns[i].gun.getAmmo().newItemStack(Math.min(64, n)));
					k++;
				}
			}
		}
	}

	public boolean isPrimGlobal() {
		return primGlobal;
	}

	public void setPrimGlobal(boolean b) {
		primGlobal = b;
	}
	
}
