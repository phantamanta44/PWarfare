package io.github.phantamanta44.pwarfare.data;

import java.util.List;

import com.google.common.collect.Lists;

public class GameSquad {

	public final String name;
	public List<GamePlayer> members;
	public GamePlayer leader;
	
	public GameSquad(String squadName) {
		members = Lists.newArrayList();
		name = squadName;
	}
	
	public void joinSquad(GamePlayer member) {
		members.add(member);
		if (leader == null)
			leader = member;
	}
	
	public void leaveSquad(GamePlayer member) {
		members.remove(member);
		if (members.size() > 0) {
			if (leader == member)
				leader = members.get(0);
		}
		else
			leader = null;
	}
	
	public void clearSquad() {
		members.clear();
		leader = null;
	}
	
}
