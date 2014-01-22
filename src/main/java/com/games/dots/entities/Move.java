package com.games.dots.entities;


public class Move {
	
	private User player;
	private Coordinates coordinates;	
	
	public Move(User player, Coordinates coordinates) {
		this.player = player;
		this.coordinates = coordinates;
	}
	public User getPlayer() {
		return player;
	}	
	public Coordinates getCoordinates() {
		return coordinates;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (coordinates == null) {
			if (other.coordinates != null)
				return false;
		} else if (!coordinates.equals(other.coordinates))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}
	
}
