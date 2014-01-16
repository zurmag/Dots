package com.games.dots.repositories;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.entities.Player;

public class PlayersRepository implements IRepository<Player>{

	private Map<String, Player> m_repository = new ConcurrentHashMap<String, Player>(); 
	
	public PlayersRepository(){
		Player player1 = new Player();
		player1.id = "1";
		
		Player player2 = new Player();
		player2.id = "2";
		
		m_repository.put("1", player1);
		m_repository.put("2", player2);
	}
	
	@Override
	public Player get(String id) {
		return m_repository.get(id);
	}

	@Override
	public Player add(Player player) {
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
