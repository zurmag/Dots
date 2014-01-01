package com.games.dots;
import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.games.dots.enities.Coordinates;

@Controller
public class MovesController {
	@RequestMapping(value = "/games/{gameId}/moves", method = RequestMethod.POST)
	public @ResponseBody Coordinates PostMove( @RequestBody Coordinates coordinates){
		return coordinates;
	}
	
	@RequestMapping(value = "/games/{gameId}/moves", method = RequestMethod.GET)
	public @ResponseBody Coordinates GetMove(){
		Coordinates coordinates = new Coordinates();
		return coordinates;
	}
}
