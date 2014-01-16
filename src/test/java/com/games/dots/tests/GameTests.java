package com.games.dots.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.games.dots.entities.ActionList;
import com.games.dots.entities.BoardSize;
import com.games.dots.entities.Coordinates;
import com.games.dots.entities.Move;
import com.games.dots.entities.Player;
import com.games.dots.logic.Game;


public class GameTests {
	@Test
	public void gameCreationTest(){
		Game smallGame = new Game(BoardSize.Small);
		Game mediumGame = new Game(BoardSize.Medium);
		Game largeGame = new Game(BoardSize.Large);

		assertNotNull(smallGame);
		assertNotNull(mediumGame);
		assertNotNull(largeGame);		
	}
	
	@Test
	public void SimpleCycleTest(){
		//Arrange
		Game smallGame = new Game(BoardSize.Small);
		Player player1 = new Player();player1.id = "1";		
		Player player2 = new Player();player2.id = "2";
		smallGame.players.add(player1);
		smallGame.players.add(player2);
		
		Coordinates[] coordinates = new Coordinates[5];
		coordinates[0] = new Coordinates(1, 1);
		coordinates[1] = new Coordinates(0, 1);
		coordinates[2] = new Coordinates(1, 0);
		coordinates[3] = new Coordinates(1, 2);
		coordinates[4] = new Coordinates(2, 1);
		Move[] moves = new Move[5];
		moves[0] = new Move(player1, coordinates[0]);
		moves[1] = new Move(player2, coordinates[1]);
		moves[2] = new Move(player2, coordinates[2]);
		moves[3] = new Move(player2, coordinates[3]);
		moves[4] = new Move(player2, coordinates[4]);
		
		//Act			
		smallGame.makeMove(moves[0]);smallGame.makeMove(moves[1]);smallGame.makeMove(moves[2]);smallGame.makeMove(moves[3]);
		ActionList actionList = smallGame.makeMove(moves[4]);
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(1, actionList.newDeadDots.size());
		
	}
	
	@Test
	public void BigCycleTest(){
		//Arrange
		Game smallGame = new Game(BoardSize.Small);
		Player player1 = new Player();player1.id = "1";		
		Player player2 = new Player();player2.id = "2";
		smallGame.players.add(player1);
		smallGame.players.add(player2);
		
		List<Coordinates> player1Coordinates = new ArrayList<Coordinates>();
		List<Coordinates> player2Coordinates = new ArrayList<Coordinates>();
		List<Move> player1Moves = new ArrayList<Move>();
		List<Move> player2Moves = new ArrayList<Move>();
		player1Coordinates.add(new Coordinates(1, 1));
		player1Coordinates.add(new Coordinates(1, 2));
		player2Coordinates.add(new Coordinates(1, 0));
		player2Coordinates.add(new Coordinates(0, 1));
		player2Coordinates.add(new Coordinates(0, 2));
		player2Coordinates.add(new Coordinates(0, 3));		
		player2Coordinates.add(new Coordinates(2, 1));
		player2Coordinates.add(new Coordinates(2, 2));
		player2Coordinates.add(new Coordinates(2, 3));		
		player2Coordinates.add(new Coordinates(1, 3));
		
		for(Coordinates c : player1Coordinates){
			player1Moves.add(new Move(player1, c));
		}
		
		for(Coordinates c : player2Coordinates){
			player1Moves.add(new Move(player2, c));
		}		
		
		//Act
		ActionList actionList = null;
		for(Move m : player1Moves){
			actionList = smallGame.makeMove(m);
		}
		
		for(Move m : player2Moves){
			actionList = smallGame.makeMove(m);
		}
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(2, actionList.newDeadDots.size());
		
	}
	
}

