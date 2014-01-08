package com.games.dots.controllers;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.entities.ActionList;
import com.games.dots.entities.Coordinates;
import com.games.dots.entities.Move;
import com.games.dots.entities.Player;
import com.games.dots.logic.Game;
import com.games.dots.repositories.IRepository;

@Controller
public class MovesController {
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	IRepository<Game> games;
	
	@Resource(name="playersRepository")
	IRepository<Player> players;
	
	@RequestMapping(value = "/games/{gameId}/players/{playerId}/moves", method = RequestMethod.POST)
	public ResponseEntity<ActionList> PostMove( 
			@RequestBody Coordinates coordinates, 
			@PathVariable String gameId, 
			@PathVariable String playerId,
			UriComponentsBuilder builder){
		UUID id = UUID.randomUUID();
		UriComponents uriComponents = builder.path("/games/{gameId}/players/{playerId}/moves/{id}").buildAndExpand(gameId, id);
		logger.debug("Move created for game Id " + gameId + " MoveId" + id);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    Game game = games.get(gameId);
	    Player player = players.get(playerId);
	    Move move = new Move(player, coordinates);
		ActionList actionList = game.makeMove(move);
		
	    
	    return new ResponseEntity<ActionList>(actionList, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/games/{gameId}/moves/{moveId}", method = RequestMethod.GET)
	public ResponseEntity<?> GetMove(){
		
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
}
