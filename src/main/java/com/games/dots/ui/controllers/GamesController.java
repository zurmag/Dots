package com.games.dots.ui.controllers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.logic.Game;
import com.games.dots.repositories.GamesRepository;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.Player;

@Controller
public class GamesController {
	
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	GamesRepository m_games;

	private SimpMessagingTemplate m_template;
	
	@Autowired
	public GamesController(SimpMessagingTemplate template){
		this.m_template = template;
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ResponseEntity<String> postGame(
			@RequestBody GameSettings gameSettings, 
			UriComponentsBuilder builder 
			){
	
		Game game = new Game(gameSettings);
		m_games.add(game);
		logger.info("Game created with Id" + game.id);
		UriComponents uriComponents = builder.path("/games/{id}").buildAndExpand(game.id);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    return new ResponseEntity<String>(game.id, headers, HttpStatus.OK);	    
	}
	
	@RequestMapping(value = "/games/{gameId}/players", method = RequestMethod.POST)
	public ResponseEntity<Integer> addPlayerToGame(
			@PathVariable String gameId	, @RequestBody Player player
			){
		logger.debug("addPlayerToGame");		
		player.gameId = gameId;
		GameMessage stateChange = m_games.get(gameId).addPlayer(player); 
		m_template.convertAndSend("/sub/games/" + gameId, stateChange);
		return new ResponseEntity<Integer>(player.id, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/games/{gameId}/players/{playerId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removePlayerFromGame(
			@PathVariable String gameId	, @PathVariable Integer playerId
			){
		logger.debug("removePlayerFromGame");		
		
		Game game = m_games.get(gameId);
		if (game != null){
			GameMessage stateChange = game.removePlayer(playerId);
			Player player = game.getPlayer(playerId);
			if (player != null){
				m_games.remove(player.userId);
			}
			
			if (stateChange != null) {
				m_template.convertAndSend("/sub/games/" + gameId, stateChange);
				if ("closed".equals(stateChange.newState.state)) {
					m_games.remove(gameId);
				}
			}
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.GET)
	public @ResponseBody Collection<com.games.dots.ui.entities.Game> getGames(){
		List<com.games.dots.ui.entities.Game> games = new LinkedList<>();
		
		for(Game game: m_games.getAll()){
			games.add(new com.games.dots.ui.entities.Game(game));			
		}
		
		return games;
	}
	
	@RequestMapping(value = "/games/{gameId}/players", method = RequestMethod.GET)
	public @ResponseBody Collection<com.games.dots.ui.entities.Player> getGamePlayers(
			@PathVariable String gameId){
				
		return m_games.get(gameId).getPlayers();
		
	}
}
