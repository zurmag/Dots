package com.games.dots.ui.entities;

import java.util.Collection;
import java.util.LinkedList;

public class State {
	public Collection<Move> moves = new LinkedList<Move>();
	public Collection<Coordinates[]> cycles = new LinkedList<>();;
	public Collection<User> players = new LinkedList<User>();
	public User currentPlayer = null;
}
