package com.games.dots.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.dots.logic.Game;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.Player;
import com.games.dots.ui.entities.UserId;

public class GamesRepository implements IRepository<Game, UserId> {

	private Map<UserId, Game> m_User2Game = new ConcurrentHashMap<>(); 
	private Map<String, Game> m_storage = new ConcurrentHashMap<>(); 
	
	@Override
	public Game get(UserId id) {
		return m_User2Game.get(id);
	}
	
	@Override
	public Game get(String id) {
		return m_storage.get(id);
	}

	@Override
	public Game add(UserId id, Game game) {
		if(game.id == null){
			UUID gameId = UUID.randomUUID();
			game.id = gameId.toString();
		}
		m_User2Game.put(id, game);
		return game;
	}
	
	@Override
	public String add(Game game) {
		if(game.id == null){
			UUID gameId = UUID.randomUUID();
			game.id = gameId.toString();
		}
		m_storage.put(game.id, game);
		return game.id;
	}

	@Override
	public void remove(UserId id) {
		m_User2Game.remove(id);		
	}
	
	@Override
	public void remove(String id) {
		for (Player player : m_storage.get(id).getPlayers()) {
			m_User2Game.remove(player.userId);
		}
		m_storage.remove(id);		
	}

	public Collection<Game> getAllOpenGames() {
		List<Game> openGames = new LinkedList<Game>();
		
		for(Game game : m_User2Game.values()){
			if (game.isOpenForRegistartion()){
				openGames.add(game);
			}
		}
		return openGames;		
	}

	public Collection<Game> getAll() {
		
		return m_User2Game.values();
	}

	public Collection<Game> getActiveGames(UserId userId) {
		ArrayList<Game> activeGames = new ArrayList<Game>();
		Game game = m_User2Game.get(userId);
		if (game != null)
			activeGames.add(game);
		//TODO: many games per userId not supported here
		return activeGames;
	}

	public Collection<Game> getSimilarGames(GameSettings gameSettings) {
		
		List<Game> games = new LinkedList<Game>();
		for(Game game : m_storage.values()){
			if (game.getState().equals("waiting") &&
				game.getBoardSize().toString().equals(gameSettings.size)){
				games.add(game);
			}
		}
		
		return games;
		
	}

}
