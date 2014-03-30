package com.games.dots.ui.entities;


public class Move {
	
	private UserId m_userId;
	private Coordinates coordinates;	
	
	public Move(UserId playerId, Coordinates coordinates) {
		this.m_userId = playerId;
		this.coordinates = coordinates;
	}
	public UserId getPlayerId() {
		return m_userId;
	}	
	public Coordinates getCoordinates() {
		return coordinates;
	}		
	
	public void setPlayer(UserId userId) {
		m_userId = userId;		
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((coordinates == null) ? 0 : coordinates.hashCode());
		result = prime * result
				+ ((m_userId == null) ? 0 : m_userId.hashCode());
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
		if (m_userId == null) {
			if (other.m_userId != null)
				return false;
		} else if (!m_userId.equals(other.m_userId))
			return false;
		return true;
	}
	
	
	
	
}
