package com.games.dots.entities;

import com.games.dots.enities.Coordinates;

public class Move {
	private Player player;
	private Coordinates coordinates;	
	
	public Move(Player player, Coordinates coordinates) {
		this.player = player;
		this.coordinates = coordinates;
	}
	public Player getPlayer() {
		return player;
	}	
	public Coordinates getCoordinates() {
		return coordinates;
	}	
	
}
