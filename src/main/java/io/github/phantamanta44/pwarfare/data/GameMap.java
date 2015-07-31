package io.github.phantamanta44.pwarfare.data;

import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;
import io.github.phantamanta44.pwarfare.objective.IGameObjective;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class GameMap implements ITickHandler {
	
	private World world;
	private GameMode mode;
	private IGameObjective objective;
	private Vector lobbyPoint, rSpawn, bSpawn, postMatchPos;

	public GameMap(GameMapSerializable data) {
		try {
			world = Bukkit.getServer().getWorld(data.id);
			mode = data.mode;
			Class<?> clazz;
			clazz = Class.forName(data.objective.parentType);
			objective = (IGameObjective)clazz.newInstance();
			objective.deserialize(data.objective);
			lobbyPoint = data.lobbyPoint;
			rSpawn = data.rSpawn;
			bSpawn = data.bSpawn;
			postMatchPos = data.postMatchPos;
		} catch (Throwable th) {
			PWarfare.INSTANCE.logger.severe("Errored while deserializing map data!");
			th.printStackTrace();
		}
	}
	
	public GameMap(World w, GameMode m) {
		world = w;
		mode = m;
	}
	
	public String getName() {
		return world.getName();
	}
	
	public UUID getId() {
		return world.getUID();
	}
	
	public boolean isObjectiveCompleted() {
		if (objective.isCompleted(GameTeam.RED) || objective.isCompleted(GameTeam.BLUE))
			return true;
		return false;
	}
	
	public GameMapSerializable toSerializable() {
		return new GameMapSerializable(this);
	}

	public static class GameMapSerializable extends Serializable {
		
		public final UUID id;
		public final GameMode mode;
		public final Serializable objective;
		private Vector lobbyPoint, rSpawn, bSpawn, postMatchPos;
		
		public GameMapSerializable(GameMap map) {
			parentType = map.getClass().getName();
			id = map.getId();
			mode = map.mode;
			objective = map.objective.serialize();
			lobbyPoint = map.lobbyPoint;
			rSpawn = map.rSpawn;
			bSpawn = map.bSpawn;
			postMatchPos = map.postMatchPos;
		}
		
	}
	
	public static enum GameMode {
		TDM, CONQ, RUSH;
	}

	@Override
	public void tick(long tick) {
		objective.tick(tick);
	}
	
	public void reset() {
		objective.resetObjective();
	}

	public boolean isTeamWinning(GameTeam team) {
		int red = objective.getCompletion(GameTeam.RED), blue = objective.getCompletion(GameTeam.BLUE);
		if (team == GameTeam.RED)
			return red >= blue;
		return blue >= red;
	}

	public Location getLobbyPoint() {
		return lobbyPoint.toLocation(world);
	}

	public Object getGameMode() {
		return mode;
	}

	public World getWorld() {
		return world;
	}
	
	public void setObjective(IGameObjective newObj) {
		objective = newObj;
	}
	
	public IGameObjective getObjective() {
		return objective;
	}
	
	public Vector getPostMatchPos() {
		return postMatchPos;
	}

	public Location getSpawnPoint(GameTeam team) {
		if (team == GameTeam.RED)
			return rSpawn.toLocation(world);
		return bSpawn.toLocation(world);
	}
	
}
