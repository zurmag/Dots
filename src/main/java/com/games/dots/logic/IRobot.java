package com.games.dots.logic;

import com.games.dots.ui.entities.GameMessage;

public interface IRobot {
	void onPlayerMove(GameMessage message);
	
}
