package com.games.dots.logic;

import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Move;


public class RandomBot extends AsyncBot{

	private final int maxY=10;
	private int lastX = 5, lastY = 0;
	public RandomBot(Game game) {
		super(game);
	}

	@Override
	public void onPlayerMove(GameMessage message) {
		if (lastY == maxY){
			lastX++;lastY = 0;
		}else{
			lastY++;
		}
		Coordinates coordinates = new Coordinates(lastX, lastY);
		Move move = new Move(m_id, coordinates);
		m_game.makeMove(move);
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
