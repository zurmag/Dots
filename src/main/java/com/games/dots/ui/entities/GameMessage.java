package com.games.dots.ui.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class GameMessage {
	
	public GamesStateChange newState = new GamesStateChange();
	public List<ScoreChange> scoreChange = new ArrayList<>();
	public Move move;
	public String errorMessage;
	public List<Coordinates[]> newCycles = new ArrayList<>();
	public Set<Coordinates> newDeadDots = new HashSet<>();	
}
