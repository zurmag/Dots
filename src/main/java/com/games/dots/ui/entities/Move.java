package com.games.dots.ui.entities;


public class Move {
	
	private User m_player;
	private Coordinates coordinates;	
	
	public Move(User player, Coordinates coordinates) {
		this.m_player = player;
		this.coordinates = coordinates;
	}
	public User getPlayer() {
		return m_player;
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
		result = prime * result + ((m_player == null) ? 0 : m_player.hashCode());
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
		if (m_player == null) {
			if (other.m_player != null)
				return false;
		} else if (!m_player.equals(other.m_player))
			return false;
		return true;
	}
	public void setPlayer(User user) {
		m_player = user;
		
	}
	
}
