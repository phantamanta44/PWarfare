package io.github.phantamanta44.pwarfare.data;

import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GameKit.PlayerGun.PlayerGunSerializable;
import io.github.phantamanta44.pwarfare.util.SGUtil;
import io.github.phantamanta44.pwarfare.util.TitleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.dmulloy2.swornguns.types.Attachment;
import net.dmulloy2.swornguns.types.Gun;

import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class GameKit {
	
	public static final Map<String, GunWrapper> gunMap = Maps.newHashMap();
	public static final Map<String, AttachmentWrapper> attMap = Maps.newHashMap();

	private static final Object[] AS_US_ATT = new Object[] {"ACOG", 10, 0, "HeavyBarrel", 20, 2, "Foregrip", 30, 1, "TacticalLight", 40, 1, "Reflex", 50, 0, "Bipod", 60, 1, "Suppressor", 70, 2, "Holo", 80, 0, "LaserSight", 90, 1, "IRNV", 100, 0, "RifleScope", 125, 0, "M145", 150, 0, "FlashSuppressor", 175, 2, "PSO-1", 200, 0, "Kobra", 235, 0, "PKA-S", 270, 0, "PKS-07", 300, 0, "PK-A", 350, 0};
	private static final Object[] AS_RU_ATT = new Object[] {"PSO-1", 10, 0, "HeavyBarrel", 20, 2, "Foregrip", 30, 1, "TacticalLight", 40, 1, "Kobra", 50, 0, "Bipod", 60, 1, "Suppressor", 70, 2, "PKA-S", 80, 0, "LaserSight", 90, 1, "IRNV", 100, 0, "PKS-07", 125, 0, "PK-A", 0, 150, "FlashSuppressor", 175, 2, "ACOG", 200, 0, "Reflex", 235, 0, "Holo", 270, 0, "RifleScope", 300, 0, "M145", 350, 0};
	
	private static final Object[] EN_US_ATT = new Object[] {"Reflex", 10, 0, "LaserSight", 20, 1, "Foregrip", 30, 1, "TacticalLight", 40, 1, "Holo", 50, 0, "Suppressor", 60, 2, "ACOG", 70, 0, "HeavyBarrel", 80, 2, "Bipod", 90, 1, "IRNV", 100, 0, "RifleScope", 125, 0, "M145", 150, 0, "FlashSuppressor", 175, 2, "Kobra", 200, 0, "PKA-S", 235, 0, "PSO-1", 270, 0, "PKS-07", 300, 0, "PK-A", 350, 0};
	private static final Object[] EN_RU_ATT = new Object[] {"Kobra", 10, 0, "LaserSight", 20, 1, "Foregrip", 30, 1, "PK-A", 40, 0, "TacticalLight", 50, 1, "PKA-S", 60, 0, "Suppressor", 70, 2, "PSO-1", 80, 0, "HeavyBarrel", 90, 2, "PKS-07", 100, 0, "IRNV", 125, 0, "RifleScope", 150, 0, "M145", 175, 0, "FlashSupprssor", 200, 0, "Reflex", 235, 0, "Holo", 270, 0, "ACOG", 300, 0};
	
	private static final Object[] SU_US_ATT = new Object[] {"Bipod", 0, 1, "M145", 10, 0, "FlashSuppressor", 20, 2, "ExtendedMag", 30, 2, "TacticalLight", 40, 1, "Holo", 50, 0, "Foregrip", 60, 1, "LaserSight", 70, 1, "Reflex", 80, 0, "Suppressor", 90, 2, "IRNV", 100, 0, "ACOG", 125, 0, "PK-A", 150, 0, "PKA-S", 175, 0, "Kobra", 200, 0, "PSO-1", 235, 0};
	private static final Object[] SU_RU_ATT = new Object[] {"Bipod", 0, 1, "PK-A", 10, 0, "FlashSuppressor", 20, 2, "ExtendedMag", 30, 2, "TacticalLight", 40, 1, "PKA-S", 50, 0, "Foregrip", 60, 1, "LaserSight", 70, 1, "Kobra", 80, 0, "Suppressor", 90, 2, "IRNV", 100, 0, "PSO-1", 125, 0, "M145", 150, 0, "Holo", 175, 0, "Reflex", 200, 0, "ACOG", 235, 0};
	
	private static final Object[] RE_US_SA_ATT = new Object[] {"RifleScope", 0, 0, "ACOG", 10, 0, "LaserSight", 20, 1, "Foregrip", 30, 1, "TacticalLight", 40, 1, "Holo", 50, 0, "Bipod", 60, 1, "Suppressor", 70, 2, "Ballistic", 80, 0, "Reflex", 90, 0, "IRNV", 100, 0, "PKS-07", 125, 0, "M145", 150, 0, "PSO-1", 175, 0, "PKA-S", 200, 0, "Kobra", 235, 0, "PK-A", 270, 0};
	private static final Object[] RE_US_BA_ATT = new Object[] {"RifleScope", 0, 0, "Ballistic", 10, 0, "LaserSight", 20, 1, "Bipod", 30, 1, "TacticalLight", 40, 1, "ACOG", 50, 0, "SPBolt", 60, 2, "Suppressor", 70, 2, "Holo", 80, 0, "Reflex", 90, 0, "IRNV", 100, 0, "PKS-07", 125, 0, "M145", 150, 0, "PSO-1", 175, 0, "PKA-S", 200, 0, "Kobra", 235, 0, "PK-A", 270, 0};
	private static final Object[] RE_RU_SA_ATT = new Object[] {"PKS-07", 0, 0, "PSO-1", 10, 0, "LaserSight", 20, 1, "Foregrip", 30, 1, "TacticalLight", 40, 1, "PKA-S", 50, 0, "Bipod", 60, 1, "Suppressor", 70, 2, "Ballistic", 80, 0, "Kobra", 90, 0, "IRNV", 100, 0, "RifleScope", 125, 0, "PK-A", 150, 0, "ACOG", 175, 0, "Holo", 200, 0, "Reflex", 235, 0, "M145", 270, 0};
	private static final Object[] RE_RU_BA_ATT = new Object[] {"PKS-07", 0, 0, "Ballistic", 10, 0, "LaserSight", 20, 1, "Bipod", 30, 1, "TacticalLight", 40, 1, "PSO-1", 50, 0, "SPBolt", 60, 2, "Suppressor", 70, 2, "PKA-S", 80, 0, "Kobra", 90, 0, "IRNV", 100, 0, "RifleScope", 125, 0, "PK-A", 150, 0, "ACOG", 175, 0, "Holo", 200, 0, "Reflex", 235, 0, "M145", 270, 0};
	
	private int xp;
	private KitClass kit;
	private List<List<PlayerGun>> ownedGuns;
	private int[] selected;
	private UUID playerId;
	private GamePlayer player;
	
	public GameKit(KitClass clazz, GamePlayer pl) {
		xp = 0;
		kit = clazz;
		ownedGuns = Lists.newArrayList();
		for (int i = 0; i < 3; i++)
			ownedGuns.add(new ArrayList<PlayerGun>());
		selected = new int[] {0, 0, 0};
		playerId = pl.getId();
		player = pl;
		checkUnlockables();
	}

	public void addXp(int amt) {
		xp += amt;
		checkUnlockables();
	}
	
	public void setXp(int amt) {
		xp = amt;
		checkUnlockables();
	}
	
	public void checkUnlockables() {
		xpCheck:
		for (GunWrapper g : kit.guns) {
			if (xp >= g.xpNeeded) {
				for (PlayerGun pg : ownedGuns.get(g.slot)) {
					if (pg.gun == g)
						continue xpCheck;
				}
				ownedGuns.get(g.slot).add(new PlayerGun(getPlayer(), g));
				TitleHelper.sendTitle(getPlayer().getPlayer(), "UNLOCKED", TitleAction.TITLE);
				TitleHelper.sendTitle(getPlayer().getPlayer(), g.gun.getName(), TitleAction.SUBTITLE);
			}
		}
	}
	
	public String getName() {
		return kit.toString(); 
	}
	
	public void selectGun(int slot, int select) {
		selected[slot] = Math.max(0, Math.min(select, ownedGuns.size()));
	}
	
	public void selectGun(GunWrapper gun) {
		for (PlayerGun g : ownedGuns.get(gun.slot)) {
			if (g.gun.gun == gun.gun) {
				selected[gun.slot] = ownedGuns.get(gun.slot).indexOf(g);
				return;
			}
		}
	}
	
	public void selectGun(Gun gun) {
		for (List<PlayerGun> list : ownedGuns) {
			for (PlayerGun g : list) {
				if (g.gun.gun == gun) {
					selected[ownedGuns.indexOf(list)] = list.indexOf(g);
					return;
				}
			}
		}
	}
	
	public PlayerGun getSelectedGun(int slot) {
		return ownedGuns.get(slot).get(selected[slot]);
	}
	
	public int getSelectedIndex(int slot) {
		return selected[slot];
	}
	
	public PlayerGun getGun(Gun gun) {
		for (List<PlayerGun> list : ownedGuns) {
			for (PlayerGun g : list) {
				if (g.gun.gun == gun)
					return g;
			}
		}
		return null;
	}
	
	public static class PlayerGun {
		
		public final GunWrapper gun;
		private int kills;
		private List<List<AttachmentWrapper>> att;
		int[] selected;
		private UUID playerId;
		private GamePlayer player;
		
		public PlayerGun(GamePlayer pl, GunWrapper g) {
			player = pl;
			playerId = pl.getId();
			gun = g;
			kills = 0;
			att = Lists.newArrayList();
			selected = new int[] {-1, -1, -1};
			for (int i = 0; i < 3; i++)
				att.add(new ArrayList<AttachmentWrapper>());
			checkUnlockables();
		}
		
		public void onKill() {
			kills++;
			checkUnlockables();
		}
		
		public void checkUnlockables() {
			for (AttachmentWrapper attach : gun.attachments) {
				if (kills > attach.killsNeeded) {
					att.get(attach.slot).add(attach);
					TitleHelper.sendTitle(getPlayer().getPlayer(), "UNLOCKED", TitleAction.TITLE);
					TitleHelper.sendTitle(getPlayer().getPlayer(), gun.gun.getName() + ": " + attach.att.getName(), TitleAction.SUBTITLE);
				}
			}
		}
		
		public void selectAttachment(int slot, int select) {
			selected[slot] = select;
		}
		
		public void selectAttachment(AttachmentWrapper a) {
			if (att.get(a.slot).contains(a))
				selected[a.slot] = att.get(a.slot).indexOf(a);
		}
		
		public AttachmentWrapper getEquipped(int slot) {
			try {
				return att.get(slot).get(selected[slot]);
			} catch (IndexOutOfBoundsException ex) {
				return null;
			}
		}

		public AttachmentWrapper[] getAttachments(int slot) {
			return att.get(slot).toArray(new AttachmentWrapper[0]);
		}

		public int getEquippedIndex(int slot) {
			return selected[slot];
		}
		
		public GamePlayer getPlayer() {
			if (player == null)
				player = PWarfare.INSTANCE.database.getPlayer(playerId);
			return player;
		}
		
		public PlayerGunSerializable serialize() {
			String[][] attList = new String[3][];
			for (int i = 0; i < 3; i++) {
				attList[i] = new String[att.get(i).size()];
				for (int j = 0; j < attList[i].length; j++)
					attList[i][j] = att.get(i).get(j).att.getFileName();
			}
			return new PlayerGunSerializable(kills, selected, gun.gun.getFileName(), attList);
		}
		
		public PlayerGun(PlayerGunSerializable data, UUID pl) {
			kills = data.kills;
			selected = data.selected;
			gun = gunMap.get(data.gun);
			att = Lists.newArrayList();
			for (int i = 0; i < 3; i++) {
				att.add(new ArrayList<AttachmentWrapper>());
				for (int j = 0; j < data.att[i].length; j++)
					att.get(i).add(attMap.get(data.att[i][j]));
			}
			playerId = pl;
		}
		
		public static class PlayerGunSerializable extends Serializable {
			
			public final int kills;
			public final int[] selected;
			public final String gun;
			public final String[][] att;
			
			public PlayerGunSerializable(int kills, int[] selected, String gun, String[][] att) {
				this.kills = kills;
				this.selected = selected;
				this.gun = gun;
				this.att = att;
			}
			
		}
		
	}
	
	public static enum KitClass {
		
		ASSAULT(wrap(
			"M16A3", 0, 0, 1, AS_US_ATT,
			"MedicKit", 2, 0, 1, new Object[0],
			"M320", 2, 11000, 1, new Object[0],
			"M416", 0, 22000, 1, AS_US_ATT,
			"M26", 2, 38000, 1, new Object[0],
			"AEK-971", 0, 60000, 1, AS_RU_ATT,
			"M16A4", 0, 89000, 1, AS_US_ATT,
			"F2000", 0, 124000, 1, AS_US_ATT,
			"AN-94", 0, 166000, 1, AS_RU_ATT,
			"AK-74M", 0, 220000, 1, AS_RU_ATT
		)),
		ENGINEER(wrap(
			"M4A1", 0, 0, 1, EN_US_ATT,
			"SMAW", 2, 0, 1, new Object[0],
			"FIM-92", 2, 3000, 1, new Object[0],
			"M15", 2, 7000, 3, new Object[0],
			"SCAR-H", 0, 14000, 1, new Object[0],
			"M4", 0, 40000, 1, EN_US_ATT,
			"A-91", 0, 58000, 1, EN_RU_ATT,
			"FGM-148", 2, 82000, 1, new Object[0],
			"G36C", 0, 110000, 1, EN_US_ATT,
			"AKS-74u", 145000, 1, EN_RU_ATT
		)),
		SUPPORT(wrap(
			"M27", 0, 0, 1, SU_RU_ATT,
			"SupplyCrate", 2, 0, 1, new Object[0],
			"C4", 4000, 2, 3, new Object[0],
			"M249", 11000, 0, 1, SU_US_ATT,
			"M18", 23000, 2, 1, new Object[0],
			"PKP", 60000, 0, 1, SU_RU_ATT,
			"M240B", 90000, 0, 1, SU_US_ATT,
			"M60E4", 130000, 0, 1, SU_US_ATT,
			"RPK-74M", 170000, 0, 1, SU_RU_ATT
		)),
		RECON(wrap(
			"SVD", 0, 0, 1, RE_RU_SA_ATT,
			"SpawnBeacon", 2, 0, 1, new Object[0],
			"T-UGS", 5000, 0, 3, new Object[0],
			"SV-98", 13000, 0, 1, RE_RU_BA_ATT,
			"SKS", 	71000, 0, 1, RE_RU_SA_ATT,
			"M40A5", 104000, 0, 1, RE_US_BA_ATT,
			"M98B", 146000, 0, 1, RE_US_BA_ATT,
			"Mk11", 195000, 0, 1, RE_US_SA_ATT
		)),
		GENERIC(wrap(
			"870MCS", 1000, 0, 1, new Object[0],
			"MP412", 8000, 1, 1, new Object[0],
			//"", 180000, 0, 1, new Object[0],
			"G17C", 29000, 1, 1, new Object[0],
			//"", 41000, 0, 1, new Object[0],
			//"", 54000, 0, 1, new Object[0],
			"PP-2000", 67000, 0, 1, new Object[0],
			//"", 81000, 0, 1, new Object[0],
			//"", 96000, 0, 1, new Object[0],
			"M9", 111000, 1, 1, new Object[0],
			"MP7", 130000, 0, 1, new Object[0],
			//"", 150000, 0, 1, new Object[0],
			"MP443", 170000, 1, 1, new Object[0],
			//"", 190000, 0, 1, new Object[0],
			//"", 220000, 0, 1, new Object[0],
			"UMP-45", 250000, 0, 1, new Object[0],
			//"", 280000, 0, 1, new Object[0],
			//"", 310000, 0, 1, new Object[0],
			"G17Cs", 340000, 1, 1, new Object[0],
			"93R", 370000, 1, 1, new Object[0],
			//"", 400000, 0, 1, new Object[0],
			"M1014", 430000, 0, 1, new Object[0],
			//"", 470000, 0, 1, new Object[0],
			//"", 510000, 0, 1, new Object[0],
			"M9s", 550000, 1, 1, new Object[0],
			//"", 590000, 0, 1, new Object[0],
			//"", 630000, 0, 1, new Object[0],
			"MP443s", 670000, 1, 1, new Object[0],
			//"", 710000, 0, 1, new Object[0],
			"G18", 760000, 1, 1, new Object[0],
			//"", 810000, 0, 1, new Object[0],
			"PDW-R", 860000, 0, 1, new Object[0],
			//"", 910000, 0, 1, new Object[0],
			"SAIGA", 960000, 0, 1, new Object[0],
			//"", 1010000, 0, 1, new Object[0],
			"44Magnum", 1060000, 1, 1, new Object[0],
			//"", 1110000, 0, 1, new Object[0],
			"DAO-12", 1165000, 0, 1, new Object[0],
			//"", 1220000, 0, 1, new Object[0],
			"P90", 1280000, 0, 1, new Object[0],
			"G18s", 1340000, 1, 1, new Object[0],
			//"", 1400000, 0, 1, new Object[0],
			"USAS-12", 1460000, 0, 1, new Object[0],
			"44Scoped", 1520000, 1, 1, new Object[0],
			"ASVAL", 1600000, 0, 1, new Object[0]
		));
		
		public final List<GunWrapper> guns;
		
		private KitClass(GunWrapper... g) {
			guns = Lists.newArrayList(Arrays.asList(g));
			for (GunWrapper wrapper : g)
				gunMap.put(wrapper.gun.getFileName(), wrapper);
		}
		
		private static GunWrapper[] wrap(Object... obj) {
			GunWrapper[] wrappers = new GunWrapper[obj.length / 5];
			for (int i = 0; i < obj.length; i += 5) {
				Gun gun = SGUtil.getGun((String)obj[i]);
				int amt = (int)obj[i + 3];
				int xp = (int)obj[i + 2];
				int slot = (int)obj[i + 1];
				Object[] attObj = (Object[])obj[i + 4];
				AttachmentWrapper[] attachments = new AttachmentWrapper[attObj.length / 3];
				for (int j = 0; j < attObj.length; j += 3) {
					Attachment att = SGUtil.getAtt((String)attObj[j]);
					if ((attachments[i] = attMap.get(att.getFileName())) == null) {
						int kills = (int)attObj[j + 1];
						int slt = (int)attObj[j + 2];
						attachments[i] = new AttachmentWrapper(att, kills, slt);
					}
				}
				wrappers[i] = new GunWrapper(gun, amt, xp, slot, attachments);
			}
			return wrappers;
		}
		
	}

	public GunWrapper[] getOwnedGuns(int slot) {
		List<GunWrapper> ret = Lists.newArrayList();
		for (PlayerGun gun : ownedGuns.get(slot))
			ret.add(gun.gun);
		return ret.toArray(new GunWrapper[0]);
	}

	public GameKitSerializable serialize() {
		PlayerGunSerializable[][] gunSer = new PlayerGunSerializable[3][];
		for (int i = 0; i < 3; i++) {
			gunSer[i] = new PlayerGunSerializable[ownedGuns.get(i).size()];
			for (int j = 0; j < gunSer[i].length; j++)
				gunSer[i][j] = ownedGuns.get(i).get(j).serialize();
		}
		return new GameKitSerializable(xp, kit, gunSer, selected, player.getId());
	}
	
	private GamePlayer getPlayer() {
		if (player == null)
			player = PWarfare.INSTANCE.database.getPlayer(playerId);
		return player;
	}
	
	public GameKit(GameKitSerializable data) {
		xp = data.xp;
		kit = data.kit;
		selected = data.selected;
		playerId = data.player;
		ownedGuns = Lists.newArrayList();
		for (int i = 0; i < 3; i++) {
			ownedGuns.add(new ArrayList<PlayerGun>());
			for (int j = 0; j < data.ownedGuns.length; j++)
				ownedGuns.get(i).add(new PlayerGun(data.ownedGuns[i][j], playerId));
		}
	}
	
	public static class GameKitSerializable extends Serializable {
		
		public final int xp;
		public final KitClass kit;
		public final PlayerGunSerializable[][] ownedGuns;
		public final int[] selected;
		public final UUID player;
		
		public GameKitSerializable(int xp, KitClass kit, PlayerGunSerializable[][] ownedGuns, int[] selected, UUID player) {
			this.xp = xp;
			this.kit = kit;
			this.ownedGuns = ownedGuns;
			this.selected = selected;
			this.player = player;
		}
		
	}
	
}
