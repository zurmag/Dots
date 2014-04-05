package com.games.dots.ui.controllers;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.games.dots.logic.Game;
import com.games.dots.repositories.IRepository;
import com.games.dots.repositories.PlayersRepository;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.UserId;

@Controller
public class MovesController {
	//private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	IRepository<Game, UserId> games;
	
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
			@DestinationVariable Integer playerId){    
		GameMessage response = null;
	
		Move move = new Move(playerId, coordinates);
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
