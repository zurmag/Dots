package com.games.dots.ui.controllers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.games.dots.logic.Game;
import com.games.dots.repositories.IRepository;
import com.games.dots.ui.entities.*;

@Controller
public class MovesController {
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	IRepository<Game> games;
	
	@Resource(name="playersRepository")
	IRepository<User> players;
	
	private SimpMessagingTemplate m_template;
	
	@Autowired
	public MovesController(SimpMessagingTemplate template){
		this.m_template = template;
	}
	@MessageMapping("/games/{gameId}/players/{playerId}/moves")
	public void PostMove( 
			@Payload Coordinates coordinates, 
			@DestinationVariable String gameId, 
			@DestinationVariable String playerId){    
		GameStateChange response = null;
		User player = players.get(playerId);
	    
	    if (player == null){
	    	player = new User();
	    	player.id = playerId;
	    	player.userType = UserType.FBUser;
	    }
		Move move = new Move(player, coordinates);
		Game game = games.get(gameId);
		if (game == null){
			response = new GameStateChange();
			response.move = move;
			response.errorMessage = "No game found for id: " + gameId;
		}
		else if (!game.isActive()){
			response = new GameStateChange();
			response.move = move;
			response.errorMessage = "Game id: " + gameId + " is not active";			
		}
		else{
		    
		    
		    		    
		    response = game.makeMove(move);
		}
	    m_template.convertAndSend("/sub/games/" + gameId, response);
	    
	}
	
}
