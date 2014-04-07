package com.games.dots.repositories;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.ui.entities.User;
import com.games.dots.ui.entities.UserId;

public class PlayersRepository{

	private Map<UserId, User> m_repository = new ConcurrentHashMap<UserId, User>(); 
	
	public PlayersRepository(){
		
	}
	
	public User get(UserId id) {
		return m_repository.get(id);
	}

	public User add(User player) {
		
		m_repository.put(player.id, player);
		return player;
	}

	public void remove(UserId id) {
		m_repository.remove(id);
		
	}

}
