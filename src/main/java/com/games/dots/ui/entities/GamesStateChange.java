package com.games.dots.ui.entities;

import java.util.LinkedList;
import java.util.List;

public class GamesStateChange {
	public String state;
	public User activePlayer;
	public User newPlayer;
	public User removedPlayer;
	public List<String> winners = new LinkedList<String>();
}
