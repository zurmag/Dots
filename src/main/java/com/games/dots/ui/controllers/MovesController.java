package com.games.dots.ui.controllers;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.logic.Game;
import com.games.dots.repositories.IRepository;
import com.games.dots.ui.entities.ActionList;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.User;

@Controller
public class MovesController {
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	IRepository<Game> games;
	
	@Resource(name="playersRepository")
	IRepository<User> players;
	
	@MessageMapping("/games/{gameId}/players/{playerId}/moves")
    @SendTo("/topic/greetings")
	public ResponseEntity<ActionList> PostMove( 
			@RequestBody Coordinates coordinates, 
			@PathVariable String gameId, 
			@PathVariable String playerId,
			UriComponentsBuilder builder){
		UUID id = UUID.randomUUID();
		UriComponents uriComponents = builder.path("/games/{gameId}/players/{playerId}/moves/{id}").buildAndExpand(gameId, playerId, id);
		logger.debug("Move created for game Id " + gameId + " MoveId" + id);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    Game game = games.get(gameId);
	    User player = players.get(playerId);
	    Move move = new Move(player, coordinates);
		ActionList actionList = game.makeMove(move);
		
	    
	    return new ResponseEntity<ActionList>(actionList, headers, HttpStatus.CREATED);
	}
	
}
