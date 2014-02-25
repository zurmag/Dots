package com.games.dots.ui.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameStateChange {
	
	public Move move;
	public User currentPlayer;
	public String errorMessage;
	public List<Coordinates[]> newCycles = new ArrayList<>();
	public Set<Coordinates> newDeadDots = new HashSet<>();
	public User newPlayer;
	public User removedPlayer;
	public String newState;
}
