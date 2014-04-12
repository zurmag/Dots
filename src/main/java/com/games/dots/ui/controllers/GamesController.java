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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.logic.Game;
import com.games.dots.repositories.GamesRepository;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.IdType;
import com.games.dots.ui.entities.Player;
import com.games.dots.ui.entities.UserId;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

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
	public ResponseEntity<com.games.dots.ui.entities.Game> postGame(
			@RequestBody GameSettings gameSettings,
			@RequestHeader String Authorization,
			UriComponentsBuilder builder 
			){
	
		Game game = new Game(gameSettings);
		
		FacebookClient facebookClient = new DefaultFacebookClient(Authorization);
		User fbuser = facebookClient.fetchObject("me", User.class);
		UserId userId = new UserId();
		userId.type = IdType.FBUser;
		userId.id = fbuser.getId();
		Player player = new Player();
		player.gameId = game.id; player.color = "red";
		player.userId = userId;
		game.addPlayer(player);
		m_games.add(userId, game); m_games.add(game);
		logger.info("Game created with Id" + game.id + "userId:" + fbuser.getId());
		UriComponents uriComponents = builder.path("/games/{id}").buildAndExpand(game.id);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    com.games.dots.ui.entities.Game uiGame = new com.games.dots.ui.entities.Game(game);
	    return new ResponseEntity<com.games.dots.ui.entities.Game>(uiGame, headers, HttpStatus.OK);	    
	}
	
	@RequestMapping(value = "/games/{gameId}/players", method = RequestMethod.POST)
	public ResponseEntity<Integer> addPlayerToGame(
			@PathVariable String gameId	, @RequestBody Player player
			){
		logger.debug("addPlayerToGame");		
		player.gameId = gameId;
		Game game = m_games.get(gameId);
		m_games.add(player.userId, game);
		GameMessage stateChange = game.addPlayer(player); 
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
			Player player = game.getPlayer(playerId);
			GameMessage stateChange = game.removePlayer(playerId);
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
