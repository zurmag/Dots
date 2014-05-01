package com.games.dots.logic;

import java.util.*;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.WeightedPseudograph;

import com.games.dots.ui.entities.BoardSize;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.Move;

public class GamePosition {
	public SimpleGraph<Coordinates, MyEdge> Board = new SimpleGraph<>(MyEdge.class);
	public Map<String, Collection<Coordinates[]>> Cycles = new HashMap<>();
	public Map<String, Collection<Coordinates[]>> EmptyCycles = new HashMap<>();
	public WeightedGraph<Move, MyEdge> MovesBoard = new WeightedPseudograph<>(MyEdge.class);
	
	private BoardSize m_size;
	
	public GamePosition(BoardSize size){
		m_size = size;
		initBoard();
	}
	
	public void addColor(String color){
		Cycles.put(color, new LinkedList<Coordinates[]>());
		EmptyCycles.put(color, new LinkedList<Coordinates[]>());

	}
	private void initBoard (){
		
		//Create vertexes
		for (int i = 0; i < m_size.value; i++){
			for (int j = 0; j < m_size.value; j++){
				Coordinates coordinate = new Coordinates(i, j);
				createBoardPoint(coordinate);
			}
		}		
	}
	
	//create empty point
	public void createBoardPoint(Coordinates coordinate){
		Board.addVertex(coordinate);
		int x = coordinate.x;
		int y = coordinate.y;
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j<=1; j++){
				if ((i != 0 || j != 0) &&// not self 
					x + i >=0 && y + j >= 0 && x + i < m_size.value && y + j < m_size.value) {//within board
					Coordinates trg = new Coordinates(x+i, y+j);
					
					if (Board.containsVertex(trg) && !Board.containsEdge(coordinate, trg)){
						//m_logger.debug(String.format("Adding edge from %s to %s", src, trg));
						Board.addEdge(coordinate, trg, new MyEdge());
					}
				}
			}
		}		
	}
	
}
