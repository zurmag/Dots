package com.games.dots.controllers;

import java.util.Arrays;

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
	
	@Autowired
	IRepository<Game> games;

	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ResponseEntity<?> postGame(@RequestBody Player[] players, UriComponentsBuilder builder){		
		Game game = games.Create();
		game.players = Arrays.asList(players);
		
		UriComponents uriComponents = builder.path("/games/{id}").buildAndExpand(game.id);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());		
	    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);	    
	}
}
