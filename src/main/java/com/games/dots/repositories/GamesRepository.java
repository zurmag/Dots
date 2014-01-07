package com.games.dots.repositories;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.logic.Game;

public class GamesRepository implements IRepository<Game> {

	Map<String, Game> storage = new ConcurrentHashMap<String, Game>(); 
	
	@Override
	public Game get(String id) {
		return storage.get(id);
	}

	@Override
	public Game add(Game game) {
		if(game.id == null){
			UUID id = UUID.randomUUID();
			game.id = id.toString();
		}
		storage.put(game.id, game);
		return game;
	}

	@Override
	public void remove(String id) {
		storage.remove(id);		
	}

}
