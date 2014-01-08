package com.games.dots.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.games.dots.controllers.HomeController;
import com.games.dots.entities.*;

public class Game {
	
	private static final Logger m_logger = LoggerFactory.getLogger(Game.class);
	
	public List<Player> players = new ArrayList<Player>();
	public String id;
	SimpleGraph<Coordinates, MyEdge> m_board = new SimpleGraph<>(MyEdge.class);
	SimpleWeightedGraph<Move, MyEdge> m_moves_board = new SimpleWeightedGraph<>(MyEdge.class); 
	public Game(BoardSize size){
		
		//Create vertexes
		for (int i = 0; i<size.value; i++){
			for (int j = 0; j<size.value; j++){
				Coordinates coordinate = new Coordinates(i, j);
				m_board.addVertex(coordinate);
			}
		}
		
		
		//Create adjacencies
		for (int i = 0; i<size.value; i++){
			for (int j = 0; j<size.value; j++){
				Coordinates src = new Coordinates(i, j);
				
				for (int ii = -1; ii <= 1; ii++){
					for (int jj = -1; jj<=1; jj++){
						if ((ii != 0 || jj != 0) &&// not self 
							 i + ii >=0 && j + jj >= 0 && i + ii < size.value && j + jj < size.value) {//within board
							Coordinates trg = new Coordinates(i+ii, j+jj);
							if (!m_board.containsEdge(src, trg)){
								//m_logger.debug(String.format("Adding edge from %s to %s", src, trg));
								m_board.addEdge(src, trg, new MyEdge());
							}
						}
					}
				}
				
			}
		}
		
	}
	
	public List<Move> moves = new LinkedList<Move>();

	
	
	
	public ActionList makeMove(Move move){		
		moves.add(move);
		
		m_moves_board.addVertex(move);
		for (Coordinates coordinates : getAdjacentVertices(move.getCoordinates())){
			Move target = new Move(move.getPlayer(), coordinates);
			DijkstraShortestPath<Move, MyEdge> daijkstra = new DijkstraShortestPath<Move, Game.MyEdge>(m_moves_board, move, target);
			GraphPath<Move, MyEdge> path = daijkstra.getPath();
			double weight = -1;
			if (path != null){
				weight = 1;
				for (MyEdge edge : path.getEdgeList()){
					m_moves_board.setEdgeWeight(edge, weight);					
				}
			}
			
		}
		
		
		
		
		
		return null;
		
	}
	
	private Set<Coordinates> getAdjacentVertices(Coordinates vertex){
		Set<Coordinates> vertexes = new HashSet<Coordinates>();
		for(MyEdge e : m_board.edgesOf(vertex)){
			vertexes.add((Coordinates) e.getTarget());
		}
		
		return vertexes;
	}
	
	
	private class MyEdge extends DefaultWeightedEdge{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Object getSource(){
			return super.getSource();
		}
		
		public Object getTarget(){
			return super.getTarget();
		}
		
		public double getWeight(){
			return super.getWeight();
		}
	}
}
