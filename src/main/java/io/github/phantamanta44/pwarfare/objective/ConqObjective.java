package io.github.phantamanta44.pwarfare.objective;

import io.github.phantamanta44.pwarfare.Game;
import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.data.Serializable;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ConqObjective implements IGameObjective {

	public static final String[] flagName = new String[] {"A", "B", "C"};
	
	private int red = 750, blue = 750;
	private Vector[] caps;
	private int[] capHp = new int[] {500, 500, 500};
	private GameTeam[] capStatus = new GameTeam[] {GameTeam.NONE, GameTeam.NONE, GameTeam.NONE};
	
	@Override
	public void tick(long tick) {
		if (tick % 32 == 0) {
			int rFlagCnt = 0, bFlagCnt = 0;
			for (GameTeam t : capStatus) {
				if (t == GameTeam.RED)
					rFlagCnt++;
				else
					bFlagCnt++;
			}
			while (rFlagCnt > 0 || bFlagCnt > 0) {
				rFlagCnt--;
				bFlagCnt--;
			}
			red -= bFlagCnt;
			blue -= rFlagCnt;
		}
		for (int i = 0; i < 3; i++) {
			int rp = 0, bp = 0;
			World world = Game.INSTANCE.getCurrentMap().getWorld();
			Location loc = caps[i].toLocation(world);
			Collection<Entity> entities = world.getNearbyEntities(loc, 5, 5, 5);
			Iterator<Entity> iter = entities.iterator();
			while (iter.hasNext()) {
				Entity e = iter.next();
				if (e instanceof Player && ((Player)e).getGameMode() == GameMode.ADVENTURE) {
					GamePlayer gp = PWarfare.INSTANCE.database.getPlayer(e.getUniqueId());
					if (gp.getTeam() == GameTeam.RED)
						rp++;
					else if (gp.getTeam() == GameTeam.BLUE)
						bp++;
					gp.getPlayer().setExp((float)capHp[i] / (capStatus[i] == GameTeam.NONE ? 500F : 1000F));
				}
				else
					iter.remove();
			}
			switch (capStatus[i]) {
			case NONE:
				if (bp == 0)
					capHp[i] -= rp;
				else if (rp == 0)
					capHp[i] -= bp;
				else
					capHp[i] = 500;
				if (capHp[i] <= 0) {
					capStatus[i] = bp > 0 ? GameTeam.BLUE : GameTeam.RED;
					capHp[i] = 1000;
					if (entities.size() > 1) {
						for (Entity e : entities) {
							GamePlayer gp = PWarfare.INSTANCE.database.getPlayer(e.getUniqueId());
							if (gp.getTeam() == (bp > 0 ? GameTeam.BLUE : GameTeam.RED))
							gp.awardXp(50, "FLAG CAPTURE ASSIST");
						}
					}
					else
						PWarfare.INSTANCE.database.getPlayer(entities.toArray(new Entity[0])[0].getUniqueId()).awardXp(250, "FLAG CAPTURED");
				}
				break;
			case BLUE:
				capHp[i] += bp;
				capHp[i] -= rp;
				if (capHp[i] <= 0) {
					capStatus[i] = GameTeam.NONE;
					capHp[i] = 500;
					if (entities.size() > 1) {
						for (Entity e : entities) {
							GamePlayer gp = PWarfare.INSTANCE.database.getPlayer(e.getUniqueId());
							if (gp.getTeam() == GameTeam.RED)
							gp.awardXp(150, "FLAG NEUTRALIZE ASSIST");
						}
					}
					else
						PWarfare.INSTANCE.database.getPlayer(entities.toArray(new Entity[0])[0].getUniqueId()).awardXp(250, "FLAG CAPTURED");
				}
				break;
			case RED:
				capHp[i] += rp;
				capHp[i] -= bp;
				if (capHp[i] <= 0) {
					capStatus[i] = GameTeam.NONE;
					capHp[i] = 500;
					if (entities.size() > 1) {
						for (Entity e : entities) {
							GamePlayer gp = PWarfare.INSTANCE.database.getPlayer(e.getUniqueId());
							if (gp.getTeam() == GameTeam.BLUE)
							gp.awardXp(150, "FLAG NEUTRALIZE ASSIST");
						}
					}
					else
						PWarfare.INSTANCE.database.getPlayer(entities.toArray(new Entity[0])[0].getUniqueId()).awardXp(250, "FLAG CAPTURED");
				}
				break;
			}
		}
	}
	
	@Override
	public void resetObjective() {
		capHp = new int[] {500, 500, 500};
		capStatus = new GameTeam[] {GameTeam.NONE, GameTeam.NONE, GameTeam.NONE};	
	}

	@Override
	public boolean isCompleted(GameTeam t) {
		if (t == GameTeam.RED)
			return blue <= 0;	
		return red <= 0;
	}

	@Override
	public float getPercentage(GameTeam t) {
		if (t == GameTeam.RED)
			return (float)(500 - blue) / 500F;	
		return (float)(500 - red) / 500F;
	}

	@Override
	public int getCompletion(GameTeam t) {
		if (t == GameTeam.RED)
			return 500 - blue;
		return 500 - red;
	}

	@Override
	public int getMaxCompletion() {
		return 500;
	}

	@Override
	public void deserialize(Serializable ser) {
		caps = ((ConqObjectiveSerializable)ser).caps;
	}

	@Override
	public Serializable serialize() {
		return new ConqObjectiveSerializable(this);
	}
	
	public static class ConqObjectiveSerializable extends Serializable {
		
		public final Vector[] caps;
		
		public ConqObjectiveSerializable(ConqObjective obj) {
			caps = obj.caps;
		}
		
	}
	
	public GameTeam getFlagStatus(int flag) {
		return capStatus[flag];
	}
	
	public boolean isFlagOwnedBy(GameTeam team, int flag) {
		return capStatus[flag] == team;
	}
	
	public Vector getCap(int flag) {
		return caps[flag];
	}

}
