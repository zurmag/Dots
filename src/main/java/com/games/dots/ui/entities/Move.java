package com.games.dots.ui.entities;


public class Move {
	
	private int m_playerId;
	private Coordinates coordinates;	
	
	public Move(int playerId, Coordinates coordinates) {
		this.m_playerId = playerId;
		this.coordinates = coordinates;
	}
	public int getPlayerId() {
		return m_playerId;
	}	
	public Coordinates getCoordinates() {
		return coordinates;
	}		
	
	public void setPlayerId(int playerId) {
		m_playerId = playerId;		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result + m_playerId;
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
		if (m_playerId != other.m_playerId)
			return false;
		return true;
	}
	
	
	
	
}
