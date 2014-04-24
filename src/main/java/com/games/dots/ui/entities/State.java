package com.games.dots.ui.entities;

import java.util.Collection;
import java.util.LinkedList;

public class State {
	public Collection<Move> moves = new LinkedList<Move>();
	public Collection<Coordinates[]> cycles = new LinkedList<>();;
	public Collection<IPlayer> players = new LinkedList<IPlayer>();
	public IPlayer activePlayer = null;
	public String state;
}
