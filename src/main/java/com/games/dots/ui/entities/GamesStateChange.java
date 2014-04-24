package com.games.dots.ui.entities;

import java.util.LinkedList;
import java.util.List;

public class GamesStateChange {
	public String state;
	public IPlayer activePlayer;
	public IPlayer newPlayer;
	public IPlayer removedPlayer;
	public List<Integer> winners = new LinkedList<>();
}
