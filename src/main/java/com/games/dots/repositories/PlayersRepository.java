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
	public Player add(Player player) {
		if (player.id == null ){
			UUID id = UUID.randomUUID();		
			player.id = id.toString();
		}
		repository.put(player.id, player);
		return player;
	}

	@Override
	public void remove(String id) {
		repository.remove(id);
		
	}

}
