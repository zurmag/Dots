package com.games.dots.ui.entities;

import java.util.LinkedList;
import java.util.List;

public class GamesStateChange {
	public String state;
	public Player activePlayer;
	public Player newPlayer;
	public Player removedPlayer;
	public List<Integer> winners = new LinkedList<>();
}
