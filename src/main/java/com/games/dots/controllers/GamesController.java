package com.games.dots.controllers;

import java.util.Arrays;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.entities.BoardSize;
import com.games.dots.entities.Player;
import com.games.dots.logic.Game;
import com.games.dots.repositories.GamesRepository;

@Controller
public class GamesController {
	
	private final Logger logger = LoggerFactory.getLogger(GamesController.class);
	
	@Resource(name="gamesRepository")
	GamesRepository games;

	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ResponseEntity<String> postGame(@RequestBody Player[] players, UriComponentsBuilder builder){
		String size = null;
		if (size == null || size.isEmpty()){
			size = "Medium";
		}
		Game game = new Game(BoardSize.valueOf(size));
		game.players = Arrays.asList(players);
		games.add(game);
		logger.info("Game created with Id" + game.id);
		UriComponents uriComponents = builder.path("/games/{id}").buildAndExpand(game.id);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    return new ResponseEntity<String>(game.id, headers, HttpStatus.OK);	    
	}
}
