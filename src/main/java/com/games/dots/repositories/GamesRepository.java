package com.games.dots.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.logic.Game;
import com.games.dots.ui.entities.Player;

public class GamesRepository implements IRepository<Game> {

	private Map<String, Game> m_storage = new ConcurrentHashMap<String, Game>(); 
	
	@Override
	public Game get(String id) {
		return m_storage.get(id);
	}

	@Override
	public Game add(Game game) {
		if(game.id == null){
			UUID id = UUID.randomUUID();
			game.id = id.toString();
		}
		m_storage.put(game.id, game);
		return game;
	}

	@Override
	public void remove(String id) {
		m_storage.remove(id);		
	}

	public Collection<Game> getAllOpenGames() {
		List<Game> openGames = new LinkedList<Game>();
		
		for(Game game : m_storage.values()){
			if (game.isOpenForRegistartion()){
				openGames.add(game);
			}
		}
		return openGames;		
	}

	public Collection<Game> getAll() {
		
		return m_storage.values();
	}

	public Collection<Game> getActiveGames(String playerId) {
		ArrayList<Game> activeGames = new ArrayList<Game>();
		for (Game game : m_storage.values()){
			for (Player user : game.getPlayers()){
				if (user.id.id.equals(playerId)){
					activeGames.add(game);
					break;
				}
			}
		}
		return activeGames;
	}

}
