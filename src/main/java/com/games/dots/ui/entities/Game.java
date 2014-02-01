package com.games.dots.ui.entities;

import java.util.LinkedList;
import java.util.List;

public class Game {
	public String id;
	public BoardSize size;
	public List<User> players = new LinkedList<User>();
	public int maxPlayers;
}
