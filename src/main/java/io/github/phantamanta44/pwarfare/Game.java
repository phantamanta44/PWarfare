package io.github.phantamanta44.pwarfare;

import io.github.phantamanta44.pwarfare.data.GameMap;
import io.github.phantamanta44.pwarfare.data.GamePlayer;
import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.data.GamePlayer.PlayerState;
import io.github.phantamanta44.pwarfare.data.GameSquad;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;
import io.github.phantamanta44.pwarfare.util.TitleHelper;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.collect.Sets;

public class Game implements ITickHandler {

	public static final Game INSTANCE = new Game();
	public static final String msgPrefix = String.format("%1$s[%2$sBF%1$s] %3$s", ChatColor.GOLD, ChatColor.DARK_GRAY, ChatColor.GRAY);
	public static final String[] squadName = new String[] {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel"};
	private GameState state = GameState.STOPPED;
	private boolean stateChanged = false;
	private long stateChangeTime = -1;
	private GameMap currentMap;
	private GameSquad[] rSquads = new GameSquad[8];
	private GameSquad[] bSquads = new GameSquad[8];
	
	public void startGame() {
		if (state == GameState.STOPPED)
			updateState(GameState.INIT);
	}
	
	public void stopGame() {
		if (state != GameState.STOPPED)
			updateState(GameState.STOPPED);
	}
	
	@Override
	public void tick(long tick) {
		if (stateChanged) {
			stateChangeTime = tick;
			stateChanged = false;
		}
		switch (state) {
		case STOPPED:
			if (tick % 600 == 0)
				broadcast("The game is currently stopped.");
			break;
		case INIT:
			broadcast("The game is initializing...");
			if (PWarfare.INSTANCE.database.getMaps().isEmpty()) {
				broadcast("No loaded maps! Stopping the game...");
				stopGame();
				return;
			}
			currentMap = PWarfare.INSTANCE.database.getRandomMap(new Random());
			for (int i = 0; i < 8; i++) {
				rSquads[i] = new GameSquad(squadName[i]);
				bSquads[i] = new GameSquad(squadName[i]);
			}
			updateState(GameState.WAITING);
			break;
		case WAITING:
			if (stateChangeTime == tick) {
				for (int i = 0; i < 8; i++) {
					rSquads[i].clearSquad();
					bSquads[i].clearSquad();
				}
			}
			if (tick % 600 == 0)
				broadcast("The game is standing by.");
			for (GamePlayer pl : PWarfare.INSTANCE.database.getPlayers())
				pl.setState(PlayerState.WAITING);
			if (getPlayersInState(PlayerState.WAITING).size() >= PWarfare.INSTANCE.config.minPlayers)
				updateState(GameState.INGAME);
			break;
		case INGAME:
			for (GamePlayer pl : getPlayersInState(PlayerState.WAITING)) {
				pl.setTeam(getWeakerTeam());
				pl.setSquad(nextSquad(pl.getTeam()));
				pl.setState(PlayerState.RESPAWNING);
			}
			if (currentMap.isObjectiveCompleted() || tick - stateChangeTime > PWarfare.INSTANCE.config.maxTime)
				updateState(GameState.POST);
			break;
		case POST:
			if (stateChangeTime == tick) {
				currentMap.reset();
				PWarfare.INSTANCE.database.dbWrite();
			}
			for (GamePlayer pl : PWarfare.INSTANCE.database.getPlayers())
				pl.setState(PlayerState.POST);
			if (tick % 600 == 0)
				broadcast("The game will start soon...");
			if (tick - stateChangeTime > 900) {
				currentMap = PWarfare.INSTANCE.database.getRandomMap(new Random());
				updateState(GameState.WAITING);
			}
			break;
		}
		for (GamePlayer pl : PWarfare.INSTANCE.database.getPlayers())
			pl.tick(tick);
		if (currentMap != null)
			currentMap.tick(tick);
		TitleHelper.tick();
	}

	private GameSquad nextSquad(GameTeam team) {
		if (team == GameTeam.RED) {
			for (GameSquad s : rSquads) {
				if (s.members.size() <= 4)
					return s;
			}
		}
		else {
			for (GameSquad s : bSquads) {
				if (s.members.size() <= 4)
					return s;
			}
		}
		throw new IllegalStateException("No empty squads!");
	}

	private Collection<GamePlayer> getPlayersInState(PlayerState state) {
		Set<GamePlayer> ret = Sets.newHashSet();
		for (GamePlayer pl : PWarfare.INSTANCE.database.getPlayers()) {
			if (pl.getState() == state)
				ret.add(pl);
		}
		return ret;
	}
	
	private Collection<GamePlayer> getPlayersOnTeam(GameTeam team) {
		Set<GamePlayer> ret = Sets.newHashSet();
		for (GamePlayer pl : PWarfare.INSTANCE.database.getPlayers()) {
			if (pl.getTeam() == team)
				ret.add(pl);
		}
		return ret;
	}
	
	private GameTeam getWeakerTeam() {
		int red = getPlayersOnTeam(GameTeam.RED).size();
		int blue = getPlayersOnTeam(GameTeam.BLUE).size();
		if (red >= blue)
			return GameTeam.RED;
		return GameTeam.BLUE;
	}

	public static void broadcast(String msg) {
		Bukkit.getServer().broadcastMessage(msgPrefix + msg);
	}
	
	public void updateState(GameState newState) {
		state = newState;
		stateChanged = true;
	}
	
	public GameState getState() {
		return state;
	}
	
	public static enum GameState {
		STOPPED, INIT, WAITING, INGAME, POST;
	}
	
	public GameMap getCurrentMap() {
		return currentMap;
	}
	
}
