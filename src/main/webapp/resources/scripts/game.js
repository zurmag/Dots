function Game(settings, state){
	
	var self = this;
	//board params
	var offset = 30;
	var distance = 20;
	var boardLayer = null;
	var board = [];
	var boardColor = "grey";
	var stage = null;
	
	//game params
	var m_state = 'waiting';
	var m_gameId = '';
	var m_players = {};
	var m_activePlayer = null;
	var m_me = null;
	var m_pressedDot = null;
	
	//events
	self.onScoreChange = function(scores){
		globals.statusPanel.scoreChange(scores);
	};
	
	//public
	this.addPlayer = function addPlayer(player){
		m_players[player.id] = player;
	};
	
	this.getActivePlayer = function getActivePlayer(){
		return m_activePlayer;
	};
	
	this.getPlayers = function(){
		var keys = Object.keys(m_players);
		var values = keys.map(function(k) { return m_players[k]; });
		return values;
	};
	
	this.disconnect = function disconnect(){
		globals.server.disconnectGame(m_me.id, m_gameId, function(){
			announce('info', 'Game completed. You won!');
		});
		globals.activeGame = false;
	};
	
	init(settings);
	
	//private
	function init(settings){
		m_gameId = settings.id;
		var width = 0;
		var height = 0;		
		if (settings.size == 'small'){
			width = height = 10;
		}
		else if (settings.size == 'big'){
			width = height = 30;
		}
		else {//medium
			width = height = 20;
		}
		
		if (settings.state){
			m_state = settings.state;
		}
		
		for (var x = 0; x < width; x++) {
			board[x] = [];
			for (var y = 0; y < height; y++) {
				board[x][y] = null;
			};
		};

		var widthPx = offset * 2 + (width-1) * distance;
		
		stage = new Kinetic.Stage({
		       container: 'board-container',
		       width: widthPx,
		       height: widthPx
		});
		
		boardLayer = createBoardLayer(width, height);
		stage.add(boardLayer);
		
		stage.on('mousedown', function () {
	    	var mousePos = stage.getPointerPosition();        
	        setPointMouseDown(mousePos.x, mousePos.y);
	    });

	    stage.on('mouseup', function () {
	    	var mousePos = stage.getPointerPosition();
	    	setPointMouseUp(mousePos.x, mousePos.y);
	    });
	    
	    m_me = {color: settings.color};
	    FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				var uid = response.authResponse.userID;
				//var accessToken = response.authResponse.accessToken;
				var player = new Player(m_me.color, uid);
								
				m_players[player.id] = player;
				m_activePlayer = player;
				m_me = player;
				
				if (state != null){
					restoreState(state);		
				}else{
					globals.server.addPlayerToGame(m_me, m_gameId);
					announce('info', 'Waiting for other players...');
				}
				
		    } 
			else {
				announce('error', "something went wrong :(");
			}
		});
	    
	    globals.server.onMove(m_gameId,recieveMessage);
	    
	    
	}

	function createBoardLayer(){
		
		var radius = 2;
		var color = boardColor;

		var layer = new Kinetic.Layer();


		for (var xx = 0; xx < board.length; xx++){
			for (var yy = 0; yy < board[xx].length; yy++){
				
				
				radius = 2;
				color = boardColor;
				
				var circle = new Kinetic.Circle({
			        x: offset + xx*distance,
			        y: offset + yy*distance,
			        radius: radius,
			        fill: color
	      		});

		  	    // add the shape to the layer
		        layer.add(circle);
		        var coordinate = {};
		        coordinate.x = xx; coordinate.y = yy;
		        board[xx][yy] = new BoardCell(circle, null, coordinate);
			}
		}
	    

	    var rect = new Kinetic.Rect({
		        x: 0,
		        y: 0,
		        width: offset + distance * board.length,
		        height: offset + distance * board[0].length,
		        fill: 'white',
		        opacity: 0.1	        
		    });
		layer.add(rect);
	    return layer;
	}
	
	function setPointMouseDown (x, y){
		if (m_state == 'closed'){
			return;
		}else if (m_state == 'waiting'){
			announce('info', 'Please wait for other players');
			return;
		}
		var i = Math.round(Math.abs((x-offset)/distance));
		var j = Math.round(Math.abs((y-offset)/distance));
		var cell = board[i][j];

		if (cell.player == null){//valid cell
			m_pressedDot = {};
			m_pressedDot.i = i;
			m_pressedDot.j = j;					
			gMouseDown(i, j, m_me.color);
		}		
	}
	
	function gMouseDown(i, j, color){
		var cell = board[i][j];
		cell.dot.setFill(color);
		boardLayer.draw();
	}

	function setPointMouseUp (x, y){
		if (m_state == 'closed'){
			return;
		}else if (m_state == 'waiting'){
			announce('info', 'Please wait for other players');
			return;
		}
		var i = Math.round(Math.abs((x-offset)/distance));
		var j = Math.round(Math.abs((y-offset)/distance));
		var coordinates = {};
		coordinates.x = i;
		coordinates.y = j;
		if (m_pressedDot == null)//invalid point pressed
		{
			return;
		}
		
		if (i == m_pressedDot.i && j == m_pressedDot.j){
			gMouseUp(i, j);		
			board[i,j].player = m_activePlayer;			
			globals.server.makeMove(m_gameId, m_me, coordinates);
		}
		else{//undo
			coordinates = {x:m_pressedDot.i, y:m_pressedDot.j};
			undoMove(coordinates);
			
		}
		m_pressedDot = null;
		
				
	}
	
	function gMouseUp(i, j){
		dot = board[i][j].dot;
		dot.setRadius(3);
		dot.setStroke('black');
		dot.setStrokeWidth(1);
		boardLayer.draw();
	}	
	
	function undoMove(coordinates){
		dot = board[coordinates.x][coordinates.y].dot;
		dot.setFill(boardColor);
		
		dot.setRadius(3);
		dot.setStroke('white');
		dot.setStrokeWidth(1);
		boardLayer.draw();
		board[coordinates.x][coordinates.y].player = null;
	}	
	
	function restoreState(state){
		console.debug("state recieved");
		console.debug(state);
		
		for (var i = 0 ; i < state.players.length; i++){
			var player = new Player(state.players[i].color, state.players[i].id);
			m_players[player.id] = player;
			if (m_me.id == player.id){
				m_me = player;
			}
		}
		for (var i = 0; i < state.moves.length; i++){
			
			m_activePlayer = new Player(state.moves[i].player.color, state.moves[i].player.id);state.moves[i].player;
			restoreMove(state.moves[i]);			
		}
		m_activePlayer = new Player(state.activePlayer.color, state.activePlayer.id);
		drawCycles(state.cycles);
		
		globals.statusPanel.showActiveGameStatus(self);
		self.onScoreChange(state.score);
		
	}
	
	function restoreMove(move){
		var coordinates = move.coordinates;
		gMouseDown(coordinates.x, coordinates.y, move.player.color);
		gMouseUp(coordinates.x, coordinates.y);
		board[coordinates.x][coordinates.y].player = move.player;
		
	}
	function drawCycles(cycles){
		for (var cycleIndex = 0; cycleIndex < cycles.length; cycleIndex++){
			var cycle = cycles[cycleIndex];
			cycle.push(cycle[0]);
			for (var index = 0; index < cycle.length - 1; index++){
				startPoint = board[cycle[index].x][cycle[index].y];
				endPoint = board[cycle[index+1].x][cycle[index+1].y];
				var line = new Kinetic.Line({
					points: [startPoint.getRealCoordinate(), endPoint.getRealCoordinate()],
					strokeWidth: 1,
					stroke: startPoint.player.color
				});
				boardLayer.add(line);
				boardLayer.draw();
			}
		}
	}
	
	function endGame(){
		globals.menuPanel.onGameEnd();
		globals.activeGame = false;
	}
	
	function activate(){
		announce('info', 'Start playing');
	}
	
	//callback
	function recieveMessage(message){
		var data = JSON.parse(message.body);
		
		if (data.errorMessage != null){
			errorHandler(data);
			return;
		}
				
		if (data.newState != null){
			stateChangeHandler(data.newState);
		}
		
		if (data.move != null){
			restoreMove(data.move);
		}
				
		if (data.newCycles.length > 0){
			drawCycles(data.newCycles);
		}		
		
		if (data.scoreChange != undefined && Object.keys(data.scoreChange).length > 0){
			self.onScoreChange(data.scoreChange);
		}
		console.debug(message);
	}
	
	function errorHandler(message){
		if (message.move != null){
			if(message.move.player.id == m_me.id){
				undoMove(message.move.coordinates);
				announce('error', message.errorMessage);
			}
			//else not my fault
		}else{
			announce('error', message.errorMessage);
		}
	}
	
	function stateChangeHandler(newState){
		m_state = newState.state;
		if (m_state){
			if (m_state == 'closed'){
				endGame();
			}
			else if (m_state == 'active'){
				activate();
			}
			else if(m_state == 'waiting'){
				announce('info', 'Waiting...');
			}
			else{
				console.error('unknown state received: '+ newState.newState );
			}			
		}
		
		if (newState.activePlayer){
			if (newState.activePlayer.id == m_me.id){
				announce('info', 'Your turn!');				
			}
			else{
				announce('info', newState.activePlayer.color +'s turn');
			}
			m_activePlayer = new Player(newState.activePlayer.color, newState.activePlayer.id);
		}
		
		if (newState.newPlayer){
			var player = new Player(newState.newPlayer.color, newState.newPlayer.id);
			m_players[newState.newPlayer.id] = player;
			globals.statusPanel.addPlayer(player);
		}
	}
	
}

