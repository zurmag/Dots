package com.games.dots.logic;

import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.IPlayer;

public interface IRobot extends IPlayer {
	void onPlayerMove(GameMessage message);
	
}
