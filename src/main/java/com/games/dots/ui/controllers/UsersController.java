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
import com.games.dots.ui.entities.IdType;
import com.games.dots.ui.entities.State;
import com.games.dots.ui.entities.UserId;

@Controller
public class UsersController {
	
	@Resource(name="gamesRepository")
	GamesRepository m_games;
	
	@RequestMapping(value = "/fbusers/{userId}/activeGames", method = RequestMethod.GET)
	public @ResponseBody Collection<com.games.dots.ui.entities.Game> getGames(
			@PathVariable String userId,
			@RequestParam(required=false) boolean fullState
			){
		List<com.games.dots.ui.entities.Game> games = new LinkedList<>();
		UserId id = new UserId();
		id.id = userId;id.type = IdType.FBUser;
		for(Game game: m_games.getActiveGames(id)){
			com.games.dots.ui.entities.Game uiGame = new com.games.dots.ui.entities.Game(game);
			games.add(uiGame);
			if (fullState){
				uiGame.state = new State();
				uiGame.state.state = game.getState();
				uiGame.state.moves.addAll(game.getAllMoves());
				uiGame.state.cycles.addAll(game.getAllCycles());
				uiGame.state.players.addAll(game.getPlayers());
				uiGame.state.activePlayer = game.getActivePlayer();
				uiGame.size = game.getBoardSize();
			}
		}
		
		return games;
	}
	

}
