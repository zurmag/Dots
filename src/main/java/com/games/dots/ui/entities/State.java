package com.games.dots.ui.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class State {
	public Collection<Move> moves = new LinkedList<Move>();
	public Collection<Coordinates[]> cycles = new LinkedList<>();;
	public Collection<Player> players = new LinkedList<Player>();
	public Map<UserId, Integer> score = new HashMap<>();
	public Player activePlayer = null;
	public String state;
}
