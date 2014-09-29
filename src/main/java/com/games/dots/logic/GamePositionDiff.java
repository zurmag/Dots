package com.games.dots.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Move;

public class GamePositionDiff {
	
	public List<Move> Moves = new LinkedList<>();
	public List<Coordinates> RemovedDots = new LinkedList<>();
	public Map<Integer, List<Move>> CapturedDots = new HashMap<>();

	public Map<String, Collection<Coordinates[]>> NewCycles = new HashMap<>();
	public Map<String, Collection<Coordinates[]>> NewEmptyCycles = new HashMap<>();
	public Map<String, Collection<Coordinates[]>> RemovedEmptyCycles = new HashMap<>();
	
	public GamePositionDiff(GamePosition position){
		for (String color : position.Cycles.keySet()){
			addColor(color);
		}
	}
	
	public void addColor(String color){
		NewCycles.put(color, new LinkedList<Coordinates[]>());
		NewEmptyCycles.put(color, new LinkedList<Coordinates[]>());
		RemovedEmptyCycles.put(color, new LinkedList<Coordinates[]>());

	}
	
	public GameMessage toGameMessage(){
		GameMessage message = new GameMessage();
		message.removedDots.addAll(RemovedDots);
		for (int playerId : CapturedDots.keySet()){
			message.capturedDots.addAll(CapturedDots.get(playerId));
			message.scoreChange.put(playerId, CapturedDots.get(playerId).size());
		}
		
		for (Collection<Coordinates[]> cycles : NewCycles.values()){
			message.newCycles.addAll(cycles);
		}
		return message;
	}
}
