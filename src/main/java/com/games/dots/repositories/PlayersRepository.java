package com.games.dots.repositories;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.entities.Player;

public class PlayersRepository implements IRepository<Player>{

	private Map<String, Player> repository = new ConcurrentHashMap<String, Player>(); 
	
	@Override
	public Player get(String id) {
		return repository.get(id);
	}

	@Override
	public Player Create() {
		UUID id = UUID.randomUUID();
		Player player = new Player();
		player.id = id.toString();
		return player;
	}

	@Override
	public void Remove(String id) {
		repository.remove(id);
		
	}

}
