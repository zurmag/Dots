package com.games.dots.ui.entities;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class State {
	public Collection<Move> moves = new LinkedList<Move>();
	public Collection<Coordinates[]> cycles = new LinkedList<>();;
	public Collection<User> players = new LinkedList<User>();
	public Map<String, Integer> score = new HashMap<>();
	public User activePlayer = null;
	public String state;
}
