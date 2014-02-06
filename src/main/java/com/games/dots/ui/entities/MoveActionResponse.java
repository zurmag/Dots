package com.games.dots.ui.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoveActionResponse {
	public Move move;
	public User activePlayer;
	public String errorMessage;
	public List<Coordinates[]> newCycles = new ArrayList<>();
	public Set<Coordinates> newDeadDots = new HashSet<>();
}
