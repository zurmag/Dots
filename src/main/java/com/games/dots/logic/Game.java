package com.games.dots.logic;

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
import java.util.Observable;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.games.dots.ui.entities.BoardSize;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.IPlayer;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.RealPlayer;
import com.games.dots.ui.entities.UserId;

public class Game {
	
	private static final List<String> colors = Arrays.asList(new String[]{"red", "green", "blue"});
	private static final Logger m_logger = LoggerFactory.getLogger(Game.class);
	
	private Map<Integer, IPlayer> m_playersMap = new HashMap<>();
	private int m_maxNumberOfPlayers;
	private int m_currentPlayerIndex = 0;
	private BoardSize m_size;
	public String id;
	public String m_state = "waiting";
	
	private GamePosition m_gamePosition;
	private Map<Coordinates, Move> m_moves = new HashMap<Coordinates, Move>();
	
	
	private Map<Move, GameMessage> m_tryMoves = new HashMap<>();
	
	public MyObservable onPlayerMove = new MyObservable();
	public MyObservable onError = new MyObservable();
	
	public Game (GameSettings settings){
		m_maxNumberOfPlayers = settings.players;
		try {
			m_size = BoardSize.valueOf(settings.size);
		} catch (IllegalArgumentException e) {
			m_logger.warn("Unknown size %s", settings.size);
			m_size = BoardSize.Medium;			
		}
		m_gamePosition = new GamePosition(m_size);
	}
	
	public int getMaxNumberOfPlayers(){
		return m_maxNumberOfPlayers;
	}
	
	public Collection<IPlayer> getPlayers(){
		return m_playersMap.values();
	}
	
	public BoardSize getBoardSize(){
		return m_size;
	}

	public GameMessage addPlayer(UserId userId) {
		RealPlayer player = new RealPlayer();
		player.setGameId(this.id); 
		player.setUserId(userId);
		return this.addPlayer(player);
	}
	
	public GameMessage addPlayer(IPlayer player) {
		player.setId(m_playersMap.size());
		player.setColor(colors.get(m_playersMap.size()));//next color
		GameMessage stateChange = new GameMessage();stateChange.gameId = id;
		if (m_playersMap.size() < m_maxNumberOfPlayers){
			m_playersMap.put(player.getId(), player);
			stateChange.newState.newPlayer = player;
			
			if (m_playersMap.size() == m_maxNumberOfPlayers){
				stateChange.newState.state = "active";
				m_state = "active";
			}
			m_gamePosition.addColor(player.getColor());
		}
		else{
			stateChange.errorMessage = "No more players";
		}
		
		return stateChange;
		
	}
	
	
	
	public GameMessage removePlayer(Integer playerId) {
		GameMessage stateChange = new GameMessage(); stateChange.gameId = id;
		if (!m_playersMap.containsKey(playerId)) 
			return null;
		if (m_playersMap.size() == 1) {
			return close();
		}
		
		IPlayer player = m_playersMap.get(playerId);
		IPlayer activePlayer = getActivePlayer();
		
		if (player.equals(activePlayer)) {
			nextTurn();activePlayer = getActivePlayer();
		}
		m_playersMap.remove(playerId);			
		m_currentPlayerIndex = activePlayer.getId();
		if (m_playersMap.size() == 1){
			return close();
		}
		
		stateChange.newState.removedPlayer = player;
		stateChange.newState.state = m_state = "waiting";
		
		return stateChange;
	}
	
	public synchronized GameMessage makeMove(Move move){
		
		GameMessage actionResponse;
		try {
			actionResponse = _makeMove(move);
			onPlayerMove.setChanged();
			onPlayerMove.notifyObservers(actionResponse);
		} catch (Exception e) {
			actionResponse = new GameMessage();
			actionResponse.move = move; actionResponse.gameId = id;
			actionResponse.errorMessage = e.getMessage();
			onError.setChanged();
			onError.notifyObservers(actionResponse);
		}
		
		return actionResponse;
	}
	
	public GameMessage tryMakeMove(Move move){
		GameMessage actionResponse;
		try {
			actionResponse = _makeMove(move);
		} catch (Exception e) {
			actionResponse = new GameMessage();
			actionResponse.move = move;
			actionResponse.gameId = id;
		}
		if (actionResponse.errorMessage != null)
			m_tryMoves.put(move, actionResponse);
		return actionResponse;
	}
	
	public void revertMove(Move move){
		if (!m_tryMoves.containsKey(move))
			return;
		GameMessage message = m_tryMoves.get(move);
		//Revert move
		m_moves.remove(message.move);
		m_gamePosition.MovesBoard.removeVertex(message.move);
		
		for(Coordinates c : message.removedDots){
			createBoardPoint(c);
		}
		m_tryMoves.remove(move);
	}
	
	private GameMessage _makeMove(Move move) throws Exception{
		GamePositionDiff diff = new GamePositionDiff(m_gamePosition);
		if (move.getPlayerId() != getActivePlayer().getId()){
			throw new Exception("Not your turn please be patient");
		}
		
		if (!m_gamePosition.Board.containsVertex(move.getCoordinates())){
			throw new Exception("There is no point here");
		}
		
		diff.Moves.add(move);
		m_gamePosition.MovesBoard.addVertex(move);
		Map<Integer, LinkedList<Coordinates[]>> newCycles = new HashMap<>();
			
		
		//checking for stepping into cycle
		for (String color : m_gamePosition.EmptyCycles.keySet()){
			if (color == m_playersMap.get(m_currentPlayerIndex).getColor())
				continue;

			int cyclePlayerIndex = colors.indexOf(color);
			for (Coordinates[] cycle : m_gamePosition.EmptyCycles.get(color)){
				if (isDeadPoint(move, cycle)){
					
					newCycles.put(cyclePlayerIndex, new LinkedList<Coordinates[]>());
					newCycles.get(cyclePlayerIndex).add(cycle);					
					diff.RemovedEmptyCycles.get(color).add(cycle);
					
					break;					
				}
			}
			
			if (newCycles.containsKey(cyclePlayerIndex))
				break;
		}		
		
		//have not stepped into enemy cycle
		if (newCycles.isEmpty()){
			newCycles.put(move.getPlayerId(), new LinkedList<Coordinates[]>());
			for (Coordinates[] cycle : createAndGetNewCycles(move)){
				
				newCycles.get(move.getPlayerId()).add(cycle);			
			}
		}
		
		for (int playerId : newCycles.keySet()){
			IPlayer player = m_playersMap.get(playerId);
			for (Coordinates[] cycle : newCycles.get(playerId)){
				Set<Coordinates> removedPoints = getRemovedPoints(cycle);
				Set<Move> capturedPoints = getCapturedMoves(removedPoints, playerId);
				String color = m_playersMap.get(playerId).getColor();
				if (capturedPoints.size() > 0){
					diff.RemovedDots.addAll(removedPoints);
					diff.CapturedDots.put(playerId, new LinkedList<Move>());
					diff.CapturedDots.get(playerId).addAll(capturedPoints);
					diff.NewCycles.get(color).add(cycle);
					
					player.setScore(player.getScore() + diff.CapturedDots.size());					
						
				}
				else{
					diff.NewEmptyCycles.get(color).add(cycle);
				}
			}
		}
		
		
		applyPositionDiff(diff);
		GameMessage gameMessage = diff.toGameMessage();
		gameMessage.move = move;
		gameMessage.gameId = id;
		nextTurn();
		gameMessage.newState.activePlayer = getActivePlayer();
		return gameMessage;
	}	
	
	private void applyPositionDiff(GamePositionDiff diff){
		for (Move move : diff.Moves){
			m_moves.put(move.getCoordinates(), move);
			m_gamePosition.MovesBoard.addVertex(move);
		}
		
		m_gamePosition.Board.removeAllVertices(diff.RemovedDots);
		for (Coordinates coordinate: diff.RemovedDots){
			m_gamePosition.MovesBoard.removeVertex(m_moves.get(coordinate));						
		}
		
		for (String color : diff.NewCycles.keySet()){
			m_gamePosition.Cycles.get(color).addAll(diff.NewCycles.get(color));
		}
		
		for (String color : diff.NewEmptyCycles.keySet()){
			m_gamePosition.EmptyCycles.get(color).addAll(diff.NewEmptyCycles.get(color));
		}
		
		for (String color : diff.RemovedEmptyCycles.keySet()){
			m_gamePosition.EmptyCycles.get(color).removeAll(diff.RemovedEmptyCycles.get(color));
		}
	}

	private List<Coordinates[]> createAndGetNewCycles(Move move) {
		
		List<Coordinates[]> cycles = new LinkedList<Coordinates[]>();
		//change
		for (Coordinates coordinates : getAdjacentVertices(move.getCoordinates())){
			Move target = new Move(move.getPlayerId(), coordinates);
			if (!m_gamePosition.MovesBoard.containsVertex(target)) //only my moves
				continue;			
			
			DijkstraShortestPath<Move, MyEdge> daijkstra = new DijkstraShortestPath<Move, MyEdge>(m_gamePosition.MovesBoard, move, target);
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
			MyEdge newEdge = m_gamePosition.MovesBoard.addEdge(move, target);
			m_gamePosition.MovesBoard.setEdgeWeight(newEdge, weight);
		}
		return cycles;
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
	
	private Coordinates[] fixCycle(Collection<Coordinates> vertexes){
		Deque<Coordinates> newCycle = new LinkedList<Coordinates>();
		List<Coordinates> leftovers = new LinkedList<Coordinates>();
		do {
			leftovers = new LinkedList<Coordinates>();
			for (Coordinates v : vertexes){
				if (newCycle.isEmpty())
					newCycle.add(v);
				else{
					if (m_gamePosition.Board.containsEdge(v, newCycle.peekLast())){
						newCycle.addLast(v);
					}
					else if (m_gamePosition.Board.containsEdge(v, newCycle.peekFirst())){
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
		for(MyEdge e : m_gamePosition.Board.edgesOf(vertex)){
			vertexes.add((Coordinates) e.getSource());
			vertexes.add((Coordinates) e.getTarget());
		}
		vertexes.remove(vertex);
		return vertexes;
	}
	
	private Set<Coordinates> getRemovedPoints(Coordinates[] cycle){
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
				for (int x = left.x+1; x <right.x;x++){
					Coordinates c = new Coordinates(x, left.y);
					deadPoints.add(c);
				}
			}
			
			i = j-1;
		}
		
		return deadPoints;
		
	}

	private Set<Move> getCapturedMoves(Set<Coordinates> points, int playerId){
		Set<Move> capturedMoves= new HashSet<>();
		for (IPlayer otherPlayer : m_playersMap.values()){
			if (otherPlayer.getId() == playerId) continue;
			for (Coordinates c : points){
				Move move = new Move(otherPlayer.getId(), c);
				if (m_gamePosition.MovesBoard.containsVertex(move))
					capturedMoves.add(move);								
			}
		}
		return capturedMoves;
		
	}
	
	public GameMessage close() {
		GameMessage stateChange = new GameMessage(); stateChange.gameId = id;
		stateChange.newState.state = "closed";
		List<Integer> winners = new LinkedList<>();
		int maxScore = 0;
		for (IPlayer player : m_playersMap.values()){
			if (player.getScore() > maxScore){
				winners.clear();
				winners.add(player.getId());
			}
			else if (player.getScore() == maxScore){
				winners.add(player.getId());
			}
		}
		stateChange.newState.winners = winners;
		return stateChange;
	}

	public boolean isOpenForRegistartion() {
		
		return m_playersMap.size() < m_maxNumberOfPlayers;
	}
	
	public boolean isActive(){
		return !isOpenForRegistartion();
	}
	
	public Collection<Move> getAllMoves() {
		return m_moves.values();
	}

	public Collection<Coordinates[]> getAllCycles() {
		Collection<Coordinates[]> list = new LinkedList<Coordinates[]>();
		for (String color : m_gamePosition.Cycles.keySet()){
			list.addAll(m_gamePosition.Cycles.get(color));
		}
		return list;
	}

	public IPlayer getActivePlayer() {
		return m_playersMap.get(m_currentPlayerIndex);
	}
	
	public void nextTurn() {
		m_currentPlayerIndex ++; m_currentPlayerIndex %= m_playersMap.size();
	}

	public String getState() {
		return m_state;
	}

	public IPlayer getPlayer(Integer playerId) {
		return m_playersMap.get(playerId);
		
	}
	
	private void createBoardPoint(Coordinates coordinate){
		m_gamePosition.Board.addVertex(coordinate);
		int x = coordinate.x;
		int y = coordinate.y;
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j<=1; j++){
				if ((i != 0 || j != 0) &&// not self 
					x + i >=0 && y + j >= 0 && x + i < m_size.value && y + j < m_size.value) {//within board
					Coordinates trg = new Coordinates(x+i, y+j);
					
					if (m_gamePosition.Board.containsVertex(trg) && !m_gamePosition.Board.containsEdge(coordinate, trg)){
						//m_logger.debug(String.format("Adding edge from %s to %s", src, trg));
						m_gamePosition.Board.addEdge(coordinate, trg, new MyEdge());
					}
				}
			}
		}
		
	}
	
	public class MyObservable extends Observable{
		@Override
		public void setChanged(){
			super.setChanged();
		}
	}
}
