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
	    Game game = games.get(gameId);
	    User player = players.get(playerId);
	    Move move = new Move(player, coordinates);
	    
	    MoveActionResponse actionList = game.makeMove(move); 
	    m_template.convertAndSend("/sub/games/" + gameId, actionList);
	    
	}
	
}
