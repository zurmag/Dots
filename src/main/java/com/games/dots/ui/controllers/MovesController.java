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
import com.games.dots.repositories.PlayersRepository;
import com.games.dots.ui.entities.*;

@Controller
public class MovesController {
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	IRepository<Game> games;
	
	@Resource(name="playersRepository")
	PlayersRepository players;
	
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
		GameMessage response = null;
		UserId id = new UserId();
		id.id = playerId;
		id.type = IdType.FBUser;
		User user = players.get(id);
	    
	    if (user == null){
	    	user = new User();
	    	user.id = new UserId();
	    	user.id.id = playerId;
	    	user.id.type = IdType.FBUser;
	    }
	    Player player = new Player();
	    player.id = user.id;
		Move move = new Move(player, coordinates);
		Game game = games.get(gameId);
		if (game == null){
			response = new GameMessage();
			response.move = move;
			response.errorMessage = "No game found";
		}
		else if (!game.isActive()){
			response = new GameMessage();
			response.move = move;
			response.errorMessage = "Game is not active please wait for other players";			
		}
		else{
			response = game.makeMove(move);
		}
	    m_template.convertAndSend("/sub/games/" + gameId, response);
	    
	}
	
}
