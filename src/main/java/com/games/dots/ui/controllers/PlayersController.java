package com.games.dots.ui.controllers;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.games.dots.logic.Game;
import com.games.dots.repositories.GamesRepository;
import com.games.dots.ui.entities.State;

@Controller
public class PlayersController {
	
	@Resource(name="gamesRepository")
	GamesRepository m_games;
	
	@RequestMapping(value = "/players/{playerId}/activeGames", method = RequestMethod.GET)
	public @ResponseBody Collection<com.games.dots.ui.entities.Game> getGames(
			@PathVariable String playerId,
			@RequestParam(required=false) boolean fullState
			){
		List<com.games.dots.ui.entities.Game> games = new LinkedList<>();
		
		for(Game game: m_games.getAactiveGames(playerId)){
			com.games.dots.ui.entities.Game uiGame = new com.games.dots.ui.entities.Game(game);
			games.add(uiGame);
			if (fullState){
				uiGame.state = new State();
				uiGame.state.state = game.getState();
				uiGame.state.moves.addAll(game.getAllMoves());
				uiGame.state.cycles.addAll(game.getAllCycles());
				uiGame.state.players.addAll(game.getPlayers());
				uiGame.state.activePlayer = game.getActivePlayer();
				uiGame.state.score = game.getScore();
				uiGame.size = game.getBoardSize();
			}
		}
		
		return games;
	}
	

}
