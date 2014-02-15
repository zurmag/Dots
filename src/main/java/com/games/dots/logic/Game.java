package com.games.dots.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.jgrapht.GraphPath;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.WeightedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.games.dots.ui.entities.GameStateChange;
import com.games.dots.ui.entities.BoardSize;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.User;

public class Game {
	
	private static final Logger m_logger = LoggerFactory.getLogger(Game.class);
	ScheduledThreadPoolExecutor m_threadPool = new ScheduledThreadPoolExecutor(1);
	
	private List<User> m_players = new ArrayList<User>();
	private Map<String, User> m_playersMap = new HashMap<>();
	private int m_maxNumberOfPlayers;
	private int m_currentPlayerIndex = 0;
	private BoardSize m_size;
	public String id;
	SimpleGraph<Coordinates, MyEdge> m_board = new SimpleGraph<>(MyEdge.class);
	WeightedGraph<Move, MyEdge> m_moves_board = new WeightedPseudograph<>(MyEdge.class);
	List<Coordinates[]> m_cycles = new LinkedList<Coordinates[]>();
	public Game (GameSettings settings){
		m_maxNumberOfPlayers = settings.players;
		try {
			m_size = BoardSize.valueOf(settings.size);
		} catch (IllegalArgumentException e) {
			m_logger.warn("Unknown size %s", settings.size);
			m_size = BoardSize.Medium;			
		}
		initBoard();
	}
	
	private void initBoard (){
		
		//Create vertexes
		for (int i = 0; i<m_size.value; i++){
			for (int j = 0; j<m_size.value; j++){
				Coordinates coordinate = new Coordinates(i, j);
				m_board.addVertex(coordinate);
			}
		}
		
		
		//Create adjacencies
		for (int i = 0; i<m_size.value; i++){
			for (int j = 0; j<m_size.value; j++){
				Coordinates src = new Coordinates(i, j);
				
				for (int ii = -1; ii <= 1; ii++){
					for (int jj = -1; jj<=1; jj++){
						if ((ii != 0 || jj != 0) &&// not self 
							 i + ii >=0 && j + jj >= 0 && i + ii < m_size.value && j + jj < m_size.value) {//within board
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

	public int getMaxNumberOfPlayers(){
		return m_maxNumberOfPlayers;
	}
	
	public Collection<User> getPlayers(){
		return m_players;
	}
	
	public BoardSize getBoardSize(){
		return m_size;
	}
	public Map<Coordinates, Move> m_moves = new HashMap<Coordinates, Move>();

	
	
	
	public synchronized GameStateChange makeMove(Move move){
		GameStateChange actionResponse = new GameStateChange();
		move.setPlayer(m_playersMap.get(move.getPlayer().id));
		
		actionResponse.move = move;
		if (!move.getPlayer().equals(m_players.get(m_currentPlayerIndex))){
			actionResponse.errorMessage = "Not your turn please be patient";
			return actionResponse;
		}
		
		m_moves.put(move.getCoordinates(), move);		
		m_moves_board.addVertex(move);
		
		for (Coordinates[] cycle : m_cycles){
			if (isDeadPoint(move, cycle)){
				actionResponse.newCycles.add(cycle);
				actionResponse.newDeadDots.add(move.getCoordinates());
				break;
			}
		}
		if (actionResponse.newCycles.isEmpty()){			
		
			for (Coordinates[] cycle : createAndGetNewCycles(move)){			
					
				m_cycles.add(cycle);			
				Set<Coordinates> deadPoints = getDeadPoints(cycle, move.getPlayer());
				if (deadPoints.size() > 0){
					
					actionResponse.newDeadDots.addAll(deadPoints);
					actionResponse.newCycles.add(cycle);
								
				}				
			
				
			}
		}
		
		m_board.removeAllVertices(actionResponse.newDeadDots);
		for (Coordinates coordinate: actionResponse.newDeadDots){
			m_moves_board.removeVertex(m_moves.get(coordinate));						
		}
		m_currentPlayerIndex ++; m_currentPlayerIndex %= m_players.size();
		actionResponse.currentPlayer = m_players.get(m_currentPlayerIndex);
		return actionResponse;
	}	
	
	private boolean isDeadPoint(Move move, Coordinates[] cycle) {
	
		boolean xLessThan = false, xBiggerThan = false, yLessThan = false, yBiggerThan = false;
		for (int i = 0;i<cycle.length;i++){
			if (move.getCoordinates().x < cycle[i].x){
				xLessThan = true;
			}
			if (move.getCoordinates().x > cycle[i].x){
				xBiggerThan = true;
			}
			if (move.getCoordinates().y < cycle[i].y){
				yLessThan = true;
			}
			if (move.getCoordinates().y > cycle[i].y){
				yBiggerThan = true;
			}
			
		}
		return xLessThan && xBiggerThan && yLessThan && yBiggerThan;
	}

	private List<Coordinates[]> createAndGetNewCycles(Move move) {
		
		List<Coordinates[]> cycles = new LinkedList<Coordinates[]>();
		
		for (Coordinates coordinates : getAdjacentVertices(move.getCoordinates())){
			Move target = new Move(move.getPlayer(), coordinates);
			if (!m_moves_board.containsVertex(target)) //only my moves
				continue;			
			
			DijkstraShortestPath<Move, MyEdge> daijkstra = new DijkstraShortestPath<Move, MyEdge>(m_moves_board, move, target);
			GraphPath<Move, MyEdge> path = daijkstra.getPath();
			double weight = 0;
			if (path != null){
				
				Set<Coordinates> vertexes = new HashSet<>();
				List<MyEdge> edgeList = path.getEdgeList();
				for (ListIterator<MyEdge> listIterator = edgeList.listIterator(); listIterator.hasNext(); ){
					MyEdge edge = listIterator.next();					
					vertexes.add(((Move) edge.getSource()).getCoordinates());
					vertexes.add(((Move) edge.getTarget()).getCoordinates());
				}
				
				Coordinates[] cycle = fixCycle(vertexes);
				cycles.add(cycle);				
				
			}
			MyEdge newEdge = m_moves_board.addEdge(move, target);
			m_moves_board.setEdgeWeight(newEdge, weight);
		}
		return cycles;
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
	
	private Set<Coordinates> getDeadPoints(Coordinates[] cycle, User me){
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
				for (User otherPlayer : m_players){
					if (otherPlayer.equals(me)) continue;
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

	public GameStateChange addPlayer(User user) {
		GameStateChange stateChange = new GameStateChange();
		if (m_players.size() < m_maxNumberOfPlayers){
			m_players.add(user);
			m_playersMap.put(user.id, user);
			stateChange.newPlayer = user;
		}
		else{
			//TODO: Think about error
		}
		
		return stateChange;
			
		
	}
	
	public boolean isOpenForRegistartion() {
		
		return m_players.size() < m_maxNumberOfPlayers;
	}
	
	public boolean isActive(){
		//TODO add check for active players 
		return !isOpenForRegistartion();
	}
	
	public Collection<Move> getAllMoves() {
		return m_moves.values();
	}

	public Collection<Coordinates[]> getAllCycles() {
		return m_cycles;
	}

	public User getActivePlayer() {
		return m_players.get(m_currentPlayerIndex);
	}	
	
}
