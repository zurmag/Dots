package com.games.dots.ui.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameSettings {
	public String size;
	public int players;
	public boolean isRobot;
	
}
