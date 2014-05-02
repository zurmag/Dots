package com.games.dots.logic;

import java.util.Random;

import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Move;


public class RandomBot extends AsyncBot{

	private int[][] m_board;
	public RandomBot(Game game) {
		super(game);
		int boardSize = game.getBoardSize().value;
		m_board = new int[boardSize][boardSize];
		for (int i = 0; i < boardSize; i++){
			for (int j = 0; j< boardSize; j++){
				m_board[i][j] = -1;//free
			}
		}
	}

	@Override
	public void onPlayerMove(GameMessage message) {
		if (message.errorMessage != null)
			return;
		else if (message.newState.activePlayer.getId() == m_id){
			boolean safeMove = true;
			
			do{
				safeMove = true;
				Move move = generateMove();
				GameMessage gameMessage = m_game.tryMakeMove(move);
				m_game.revertMove(move);
				for (int playerId : gameMessage.scoreChange.keySet()){
					if (playerId == m_id) continue;
					safeMove = !gameMessage.scoreChange.containsKey(playerId);
				}
				if (safeMove)
					m_game.makeMove(move);
			}while (!safeMove);
			
		}
		for (Coordinates c : message.removedDots){
			m_board[c.x][c.y] = message.move.getPlayerId();
		}
		m_board[message.move.getCoordinates().x][message.move.getCoordinates().y] = message.move.getPlayerId();
	}
	
	public Move generateMove(){
		Random r = new Random();
		int x, y;
		do{
			x = r.nextInt(m_board.length);
			y = r.nextInt(m_board.length);
		}while(m_board[x][y] != -1);
		Coordinates coordinates = new Coordinates(x, y);
		return new Move(m_id, coordinates);
	}
	//Player
	int m_id;
	String m_color;
	int m_score = 0;
	@Override
	public int getId() {
		return m_id;
	}
	
	public void setId(int id){
		m_id = id;
	}
	@Override
	public String getGameId() {
		return m_game.id;
	}

	@Override
	public String getColor() {
		return m_color;
	}
	
	public void setColor(String color){
		m_color = color;
	}

	@Override
	public int getScore() {
		return m_score;
	}

	@Override
	public void setScore(int score) {
		m_score = score;
	}

}
