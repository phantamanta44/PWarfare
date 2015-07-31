package io.github.phantamanta44.pwarfare.util;

import io.github.phantamanta44.pwarfare.PWarfare;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.entity.Player;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.EnumWrappers.TitleAction;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

public class TitleHelper {
	
	public static List<Title> titleQueue = Lists.newArrayList();
	public static Map<Player, MutableInt> tickMap = Maps.newHashMap();
	
	public static void tick() {
		Iterator<Title> iter = titleQueue.iterator();
		while (iter.hasNext()) {
			Title t = iter.next();
			if (!tickMap.containsKey(t.player)) {
				sendTitle(t);
				tickMap.put(t.player, new MutableInt(128));
				iter.remove();
			}
		}
		
		Deque<Player> toRemove = Queues.newArrayDeque();
		for (Entry<Player, MutableInt> entry : tickMap.entrySet()) {
			entry.getValue().decrement();
			if (entry.getValue().toInteger() <= 0)
				toRemove.offer(entry.getKey());
		}
		
		Player pl;
		while ((pl = toRemove.poll()) != null)
			tickMap.remove(pl);
	}
	
	public static void sendTitle(Player player, String title, TitleAction titleType) {
		titleQueue.add(new Title(player, WrappedChatComponent.fromText(title), titleType));
	}
	
	public static void sendTitleJson(Player player, String title, TitleAction titleType) {
		titleQueue.add(new Title(player, WrappedChatComponent.fromJson(title), titleType));
	}
	
	private static void sendTitle(Title title) {
		try {
			WrapperPlayServerTitle titlePacket = new WrapperPlayServerTitle();
			titlePacket.setFadeIn(16);
			titlePacket.setFadeOut(8);
			titlePacket.setStay(104);
			titlePacket.setTitle(title.message);
			titlePacket.setAction(title.type);
			ProtocolLibrary.getProtocolManager().sendServerPacket(title.player, titlePacket.getHandle());
		} catch (Throwable th) {
			PWarfare.INSTANCE.logger.warning("Failed to send title packet!");
			th.printStackTrace();
		}
	}
	
	public static class Title {
		
		public final Player player;
		public final WrappedChatComponent message;
		public final TitleAction type;
		
		public Title(Player pl, WrappedChatComponent cc, TitleAction action) {
			player = pl;
			message = cc;
			type = action;
		}
		
	}
	
}
