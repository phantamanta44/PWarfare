package io.github.phantamanta44.pwarfare.data;

import io.github.phantamanta44.pwarfare.PWarfare;
import io.github.phantamanta44.pwarfare.data.GameMap.GameMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class GameDB {

	private Map<UUID, GamePlayer> loadedPlayers;
	private Map<String, GameMap> loadedMaps;
	private File dbFile;
	
	public GameDB() {
		loadedPlayers = Maps.newHashMap();
		loadedMaps = Maps.newHashMap();
		dbFile = new File(PWarfare.INSTANCE.getDataFolder(), "database.json");
		dbRead();
	}
	
	public void dbRead() {
		Gson gson = new Gson();
		loadedPlayers.clear();
		loadedMaps.clear();
		try {
			BufferedReader streamIn = new BufferedReader(new FileReader(dbFile));
			String json;
			while ((json = streamIn.readLine()) != null) {
				Serializable typeCheck = gson.fromJson(json, Serializable.class);
				Class<?> T = Class.forName(typeCheck.type);
				Class<?> P = Class.forName(typeCheck.parentType);
				Object obj = gson.fromJson(json, T);
				Object par = P.getConstructors()[0].newInstance(obj);
				if (par instanceof GamePlayer)
					loadedPlayers.put(((GamePlayer)par).getId(), (GamePlayer)par);
				else if (par instanceof GameMap)
					loadedMaps.put(((GameMap)par).getName(), (GameMap)par);
			}
			streamIn.close();
		} catch (Throwable th) {
			PWarfare.INSTANCE.logger.severe("Failed to read database!");
			th.printStackTrace();
		}
	}
	
	public void dbWrite() {
		Gson gson = new Gson();
		try {
			BufferedWriter streamOut = new BufferedWriter(new FileWriter(dbFile));
			for (GamePlayer gp : loadedPlayers.values()) {
				String ser = gson.toJson(gp.toSerializable());
				streamOut.write(ser);
			}
			for (GameMap gm : loadedMaps.values()) {
				String ser = gson.toJson(gm.toSerializable());
				streamOut.write(ser);
			}
			streamOut.close();
		} catch (Throwable th) {
			PWarfare.INSTANCE.logger.severe("Failed to write database!");
			th.printStackTrace();
		}
	}
	
	public Collection<GamePlayer> getPlayers() {
		return loadedPlayers.values();
	}
	
	public GamePlayer getPlayer(UUID id) {
		return loadedPlayers.get(id);
	}
	
	public Collection<GameMap> getMaps() {
		return loadedMaps.values();
	}
	
	public GameMap getMap(String name) {
		return loadedMaps.get(name);
	}
	
	public GameMap getRandomMap(Random rand) {
		GameMap[] maps = loadedMaps.values().toArray(new GameMap[0]);
		return maps[rand.nextInt(maps.length)];
	}
	
	public GamePlayer registerPlayer(OfflinePlayer pl) {
		GamePlayer p = new GamePlayer(pl);
		loadedPlayers.put(p.getId(), p);
		return p;
	}
	
	public GameMap registerMap(World w, GameMode m) {
		GameMap map = new GameMap(w, m);
		loadedMaps.put(map.getName(), map);
		return map;
	}
	
}
