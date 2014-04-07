package com.games.dots.ui.entities;

import java.util.LinkedList;
import java.util.List;

public class Game {
	public String id;
	public BoardSize size;
	public State state = null;
	public int maxPlayers;
	
	public Game(){	}
	public Game(com.games.dots.logic.Game other){
		
		this.id = other.id;
		this.size = other.getBoardSize();
		this.maxPlayers = other.getMaxNumberOfPlayers();	
		this.state = new State();
		this.state.players = other.getPlayers();
	}
}
