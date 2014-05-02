package com.games.dots.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.games.dots.logic.Game;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.IdType;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.RealPlayer;
import com.games.dots.ui.entities.UserId;


public class GameTests {
	@Test
	public void gameCreationTest(){
		GameSettings settings = new GameSettings();
		settings.players = 2;
		
		settings.size = "Small";
		Game smallGame = new Game(settings);
		settings.size = "Medium";
		Game mediumGame = new Game(settings);
		settings.size = "Big";
		Game largeGame = new Game(settings);

		assertNotNull(smallGame);
		assertNotNull(mediumGame);
		assertNotNull(largeGame);		
	}
	
	@Test
	public void SimpleCycleTest(){
		//Arrange
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = createPlayer();player1.setColor("red");
		RealPlayer player2 = createPlayer();player2.setColor("green");
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		Coordinates[] coordinates = new Coordinates[8];
		coordinates[0] = new Coordinates(1, 1);
		coordinates[1] = new Coordinates(0, 1);
		coordinates[2] = new Coordinates(5, 5);
		coordinates[3] = new Coordinates(1, 0);
		coordinates[4] = new Coordinates(5, 6);
		coordinates[5] = new Coordinates(1, 2);
		coordinates[6] = new Coordinates(5, 7);
		coordinates[7] = new Coordinates(2, 1);
		Move[] moves = new Move[8];
		moves[0] = new Move(player1.getId(), coordinates[0]);
		moves[1] = new Move(player2.getId(), coordinates[1]);
		moves[2] = new Move(player1.getId(), coordinates[2]);
		moves[3] = new Move(player2.getId(), coordinates[3]);
		moves[4] = new Move(player1.getId(), coordinates[4]);
		moves[5] = new Move(player2.getId(), coordinates[5]);
		moves[6] = new Move(player1.getId(), coordinates[6]);
		moves[7] = new Move(player2.getId(), coordinates[7]);
		
		
		//Act
		for (int i = 0; i < moves.length-1 ;i++) {
			smallGame.makeMove(moves[i]);
		}		
		
		GameMessage actionList = smallGame.makeMove(moves[moves.length-1]);
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(1, actionList.removedDots.size());
		
	}
	
	@Test
	public void BigCycleTest(){
		//Arrange
		
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = new RealPlayer();player1.setId(0);		
		RealPlayer player2 = new RealPlayer();player2.setId(1);
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		List<Move> moves = new ArrayList<>();
		moves.add(new Move(player1.getId(), new Coordinates(1,1)));
		moves.add(new Move(player2.getId(), new Coordinates(1,0)));
		moves.add(new Move(player1.getId(), new Coordinates(1,2)));
		moves.add(new Move(player2.getId(), new Coordinates(0,1)));
		moves.add(new Move(player1.getId(), new Coordinates(8,2)));
		moves.add(new Move(player2.getId(), new Coordinates(0,2)));
		moves.add(new Move(player1.getId(), new Coordinates(8,3)));
		moves.add(new Move(player2.getId(), new Coordinates(0,3)));
		moves.add(new Move(player1.getId(), new Coordinates(8,4)));
		moves.add(new Move(player2.getId(), new Coordinates(2,1)));
		moves.add(new Move(player1.getId(), new Coordinates(8,5)));
		moves.add(new Move(player2.getId(), new Coordinates(2,2)));
		moves.add(new Move(player1.getId(), new Coordinates(8,6)));
		moves.add(new Move(player2.getId(), new Coordinates(2,3)));
		moves.add(new Move(player1.getId(), new Coordinates(8,7)));
		moves.add(new Move(player2.getId(), new Coordinates(1,3)));
		
		//Act		
		for (int i = 0; i< moves.size()-1;i++) {
			smallGame.makeMove(moves.get(i));
		}
		
		GameMessage actionList = smallGame.makeMove(moves.get(moves.size()-1));
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(2, actionList.removedDots.size());
		
	}
	@Test
	public void RevertFirstMoveTest(){
		//Arrange
		
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = new RealPlayer();player1.setId(0);		
		RealPlayer player2 = new RealPlayer();player2.setId(1);
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		Move move = new Move(player1.getId(), new Coordinates(1,1));
		
		//act
		smallGame.tryMakeMove(move);
		smallGame.revertMove(move);
		
		//assert
		Assert.assertEquals(0, smallGame.getAllMoves().size());
		Assert.assertEquals(smallGame.getBoardSize().value * smallGame.getBoardSize().value, smallGame.getGamePosition().Board.vertexSet().size());
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().Cycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().EmptyCycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		Assert.assertEquals(0, smallGame.getGamePosition().MovesBoard.vertexSet().size());

	}
	
	@Test
	public void RevertThreeMoveTest(){
		//Arrange
		
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = new RealPlayer();player1.setId(0);		
		RealPlayer player2 = new RealPlayer();player2.setId(1);
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		
		List<Move> moves = new ArrayList<>();
		moves.add(new Move(player1.getId(), new Coordinates(1,1)));
		moves.add(new Move(player2.getId(), new Coordinates(1,0)));
		moves.add(new Move(player1.getId(), new Coordinates(1,2)));
		
		
		
		//act
		
		for (int i = 0; i< moves.size()-1;i++) {
			smallGame.tryMakeMove(moves.get(i));
		}
		
		for (int i = moves.size()-1; i >= 0  ; i--) {
			smallGame.revertMove(moves.get(i));
		}
		
		//assert
		Assert.assertEquals(0, smallGame.getAllMoves().size());
		Assert.assertEquals(smallGame.getBoardSize().value * smallGame.getBoardSize().value, smallGame.getGamePosition().Board.vertexSet().size());
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().Cycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().EmptyCycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		Assert.assertEquals(0, smallGame.getGamePosition().MovesBoard.vertexSet().size());

	}
	
	@Test
	public void RevertAfterThreeMoveTest(){
		//Arrange
		
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = new RealPlayer();player1.setId(0);		
		RealPlayer player2 = new RealPlayer();player2.setId(1);
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		
		List<Move> moves = new ArrayList<>();
		moves.add(new Move(player1.getId(), new Coordinates(1,1)));
		moves.add(new Move(player2.getId(), new Coordinates(1,0)));
		moves.add(new Move(player1.getId(), new Coordinates(1,2)));
		
		for (int i = 0; i< moves.size();i++) {
			smallGame.makeMove(moves.get(i));
		}
		
		//act
		
		
		Move move = new Move(player1.getId(), new Coordinates(0,2)); 
		smallGame.tryMakeMove(move);
		smallGame.revertMove(move);
		
		//assert
		Assert.assertEquals(3, smallGame.getAllMoves().size());
		Assert.assertEquals(smallGame.getBoardSize().value * smallGame.getBoardSize().value, smallGame.getGamePosition().Board.vertexSet().size());
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().Cycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().EmptyCycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		Assert.assertEquals(3, smallGame.getGamePosition().MovesBoard.vertexSet().size());

	}
	
	@Test
	public void revertCycleTest(){
		//Arrange
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		RealPlayer player1 = createPlayer();player1.setColor("red");
		RealPlayer player2 = createPlayer();player2.setColor("green");
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		List<Move> moves = new ArrayList<>();
		moves.add(new Move(player1.getId(), new Coordinates(1, 1)));
		moves.add(new Move(player2.getId(), new Coordinates(0, 1)));
		moves.add(new Move(player1.getId(), new Coordinates(5, 5)));
		moves.add(new Move(player2.getId(), new Coordinates(1, 0)));
		moves.add(new Move(player1.getId(), new Coordinates(5, 6)));
		moves.add(new Move(player2.getId(), new Coordinates(1, 2)));
		moves.add(new Move(player1.getId(), new Coordinates(5, 7)));
		moves.add(new Move(player2.getId(), new Coordinates(2, 1)));
		
		
		//act
		
		for (int i = 0; i< moves.size()-1;i++) {
			smallGame.tryMakeMove(moves.get(i));
		}
		
		for (int i = moves.size()-1; i >= 0  ; i--) {
			smallGame.revertMove(moves.get(i));
		}
		
		//assert
		Assert.assertEquals(0, smallGame.getAllMoves().size());
		Assert.assertEquals(smallGame.getBoardSize().value * smallGame.getBoardSize().value, smallGame.getGamePosition().Board.vertexSet().size());
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().Cycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		for (Collection<Coordinates[]> cycles : smallGame.getGamePosition().EmptyCycles.values()){
			Assert.assertEquals(0, cycles.size());
		}
		
		Assert.assertEquals(0, smallGame.getGamePosition().MovesBoard.vertexSet().size());
	}
	
	private RealPlayer createPlayer() {
		RealPlayer player = new RealPlayer();
		UserId userId = new UserId();		
		userId.id = UUID.randomUUID().toString();
		userId.type = IdType.FBUser;
		player.setUserId(userId);
		
		return player;
	}
	
}

