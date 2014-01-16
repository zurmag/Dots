package com.games.dots.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.WeightedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.games.dots.entities.ActionList;
import com.games.dots.entities.BoardSize;
import com.games.dots.entities.Coordinates;
import com.games.dots.entities.Move;
import com.games.dots.entities.Player;

public class Game {
	
	private static final Logger m_logger = LoggerFactory.getLogger(Game.class);
	
	public List<Player> players = new ArrayList<Player>();
	public String id;
	SimpleGraph<Coordinates, MyEdge> m_board = new SimpleGraph<>(MyEdge.class);
	WeightedGraph<Move, MyEdge> m_moves_board = new WeightedPseudograph<>(MyEdge.class); 
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
		ActionList actionList = new ActionList();
		moves.add(move);
		
		
		m_moves_board.addVertex(move);
		for (Coordinates coordinates : getAdjacentVertices(move.getCoordinates())){
			Move target = new Move(move.getPlayer(), coordinates);
			if (!m_moves_board.containsVertex(target)) //only my moves
				continue;			
			
			DijkstraShortestPath<Move, MyEdge> daijkstra = new DijkstraShortestPath<Move, MyEdge>(m_moves_board, move, target);
			GraphPath<Move, MyEdge> path = daijkstra.getPath();
			double weight = 0;
			if (path != null){
				weight = 1;
				Set<Coordinates> vertexes = new HashSet<>();
				List<MyEdge> edgeList = path.getEdgeList();
				
				
				for (ListIterator<MyEdge> listIterator = edgeList.listIterator(); listIterator.hasNext(); ){
					MyEdge edge = listIterator.next();
					
					vertexes.add(((Move) edge.getSource()).getCoordinates());
					vertexes.add(((Move) edge.getTarget()).getCoordinates());
					m_moves_board.setEdgeWeight(edge, weight);					
				}
				Coordinates[] cycle = fixCycle(vertexes);				
				
				Set<Coordinates> deadPoints = getDeadPoints(cycle, move.getPlayer());
				if (deadPoints.size() > 0){
					actionList.newDeadDots.addAll(deadPoints);
					actionList.newCycles.add(cycle);
				}
			}
			MyEdge newEdge = m_moves_board.addEdge(move, target);
			m_moves_board.setEdgeWeight(newEdge, weight);
		}
		
		
		
		
		
		return actionList;
		
	}	
	
	private Coordinates[] fixCycle(Collection<Coordinates> vertexes){
		Deque<Coordinates> newCycle = new LinkedList<Coordinates>();
		List<Coordinates> leftovers = new LinkedList<Coordinates>();
		do {
			leftovers = new LinkedList<Coordinates>();
			for (Coordinates v : vertexes){
				if (newCycle.isEmpty())
					newCycle.add(v);
				else{
					if (m_board.containsEdge(v, newCycle.peekLast())){
						newCycle.addLast(v);
					}
					else if (m_board.containsEdge(v, newCycle.peekFirst())){
						newCycle.addFirst(v);
					}
					else{
						leftovers.add(v);
					}
				}
			}
			vertexes = leftovers;
			 
		} while(!leftovers.isEmpty());
		
		return (Coordinates[]) newCycle.toArray(new Coordinates[0]);
	}
	
	
	private Set<Coordinates> getAdjacentVertices(Coordinates vertex){
		Set<Coordinates> vertexes = new HashSet<Coordinates>();
		for(MyEdge e : m_board.edgesOf(vertex)){
			vertexes.add((Coordinates) e.getSource());
			vertexes.add((Coordinates) e.getTarget());
		}
		vertexes.remove(vertex);
		return vertexes;
	}
	
	private Set<Coordinates> getDeadPoints(Coordinates[] cycle, Player me){
		Set<Coordinates> deadPoints = new HashSet<>();
		//sort by second coordinate:
		Coordinates[] newCycle =cycle.clone();
		Arrays.sort(newCycle, new Comparator<Coordinates>() {
			@Override public int compare(Coordinates c1, Coordinates c2) {
				if (c1.y != c2.y)
					return c1.y - c2.y;
				else
					return c1.x - c2.x ;
			}		
		});
		
		
		Coordinates left, right;
		for (int i = 0; i < newCycle.length; i++){
			left = right= newCycle[i];
			int j = i;
			for (; j < newCycle.length && left.y == newCycle[j].y ;j++){
				right = newCycle[j];
			}
			
			if (left.x < right.x){
				for (Player otherPlayer : players){
					if (otherPlayer == me) continue;
					for (int x = left.x+1; x <right.x;x++){
						Coordinates c = new Coordinates(x, left.y);
						Move move = new Move(otherPlayer, c);
						if (m_moves_board.containsVertex(move))
							deadPoints.add(c);								
					}
				}
			}
			i = j-1;
			
		}
		
		return deadPoints;
		
	}
	
	
	
	
	
}
