package com.games.dots.controllers;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.entities.Player;
import com.games.dots.logic.Game;
import com.games.dots.repositories.IRepository;

@Controller
public class GamesController {
	
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Autowired
	IRepository<Game> games;

	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ResponseEntity<String> postGame(@RequestBody Player[] players, UriComponentsBuilder builder){		
		Game game = games.Create();
		game.players = Arrays.asList(players);
		logger.info("Game created with Id" + game.id);
		UriComponents uriComponents = builder.path("/games/{id}").buildAndExpand(game.id);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());		
	    return new ResponseEntity<String>(game.id, headers, HttpStatus.OK);	    
	}
}
