package com.games.dots.controllers;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.games.dots.enities.Coordinates;

@Controller
public class MovesController {
	@RequestMapping(value = "/games/{gameId}/moves", method = RequestMethod.POST)
	public ResponseEntity<?> PostMove( @RequestBody Coordinates coordinates, @RequestParam String gameId, UriComponentsBuilder builder){
		UUID id = UUID.randomUUID();
		UriComponents uriComponents = builder.path("/games/{gameId}/moves/{id}").buildAndExpand(id);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(uriComponents.toUri());
	    
	    Coordinates[] loop = new Coordinates[4];	    
	    loop[0] = new Coordinates();loop[0].x = 0;loop[0].y = 1;
	    loop[1] = new Coordinates();loop[1].x = 1;loop[1].y = 2;
	    loop[2] = new Coordinates();loop[2].x = 2;loop[2].y = 1;
	    loop[3] = new Coordinates();loop[3].x = 1;loop[3].y = 0;
	    
	    return new ResponseEntity<Coordinates[]>(loop, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/games/{gameId}/moves/{moveId}", method = RequestMethod.GET)
	public ResponseEntity<?> GetMove(){
		
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
}
