package com.games.dots.repositories;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.entities.User;

public class PlayersRepository implements IRepository<User>{

	private Map<String, User> m_repository = new ConcurrentHashMap<String, User>(); 
	
	public PlayersRepository(){
		User player1 = new User();
		player1.id = "1";
		
		User player2 = new User();
		player2.id = "2";
		
		m_repository.put("1", player1);
		m_repository.put("2", player2);
	}
	
	@Override
	public User get(String id) {
		return m_repository.get(id);
	}

	@Override
	public User add(User player) {
		if (player.id == null ){
			UUID id = UUID.randomUUID();		
			player.id = id.toString();
		}
		m_repository.put(player.id, player);
		return player;
	}

	@Override
	public void remove(String id) {
		m_repository.remove(id);
		
	}

}
