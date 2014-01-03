package com.games.dots.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;

import com.games.dots.entities.*;

public class Game {
	public List<Player> players = new ArrayList<Player>();
	public String id;
	public List<Move> moves = new LinkedList<Move>();
	
	
	public Coordinates[] makeMove(Move move){
		moves.add(move);
		
		return null;
		
	}
}
