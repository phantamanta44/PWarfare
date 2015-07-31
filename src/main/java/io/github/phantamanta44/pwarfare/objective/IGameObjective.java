package io.github.phantamanta44.pwarfare.objective;

import io.github.phantamanta44.pwarfare.data.GamePlayer.GameTeam;
import io.github.phantamanta44.pwarfare.data.Serializable;
import io.github.phantamanta44.pwarfare.handler.ITickHandler;

public interface IGameObjective extends ITickHandler {

	public void resetObjective();
	
	public boolean isCompleted(GameTeam t);
	
	public float getPercentage(GameTeam t);
	
	public int getCompletion(GameTeam t);
	
	public int getMaxCompletion();
	
	public void deserialize(Serializable ser);
	
	public Serializable serialize();
	
}
