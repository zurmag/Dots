package com.games.dots.ui.entities;

public class RealPlayer extends Player {
	private UserId m_userId;

	public UserId getUserId() {
		return m_userId;
	}
	
	public void setUserId(UserId userId){
		m_userId = userId;
	}
	
	
}
