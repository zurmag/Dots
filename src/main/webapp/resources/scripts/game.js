function Game(settings, state){
	
	var offset = 10;
	var distance = 20;
	var boardLayer = null;
	var board = [];
	var boardColor = "grey";
	var m_gameId = '';
	var m_players = {};
	var m_activePlayer = null;
	var m_me = null;
	var pressedDot = null;
	var stage = null;
	var self = this;
	init(settings);
	
	
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

		for (var x = 0; x < width; x++) {
			board[x] = [];
			for (var y = 0; y < height; y++) {
				board[x][y] = null;
			};
		};

		stage = new Kinetic.Stage({
		       container: 'board-container',
		       width: 578,
		       height: 578
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
	    
	    FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				var uid = response.authResponse.userID;
				//var accessToken = response.authResponse.accessToken;
				var player = new Player(settings.color, uid);
								
				m_players[player.id] = player;
				m_activePlayer = player;
				m_me = player;
				
				if (state != null){
					restoreState(state);		
				}else{
					addPlayerToGame();
				}
				
		    } 
			else {
			    console.error("something went wrong :(");
			}
		});
	    
	    globals.server.onMove(m_gameId,recieveResponse);
	    
	    
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
	
	//privates
	function setPointMouseDown (x, y){
		var i = Math.round(Math.abs((x-offset)/distance));
		var j = Math.round(Math.abs((y-offset)/distance));
		var cell = board[i][j];

		if (cell.player == null){//valid cell
			pressedDot = {};
			pressedDot.i = i;
			pressedDot.j = j;					
			gMouseDown(i, j, m_me.color);
		}		
	}
	
	function gMouseDown(i, j, color){
		var cell = board[i][j];
		cell.dot.setFill(color);
		boardLayer.draw();
	}

	function setPointMouseUp (x, y){
		var i = Math.round(Math.abs((x-offset)/distance));
		var j = Math.round(Math.abs((y-offset)/distance));
		var coordinates = {};
		coordinates.x = i;
		coordinates.y = j;
		if (pressedDot == null)//invalid point pressed
		{
			return;
		}
		
		if (i == pressedDot.i && j == pressedDot.j){
			gMouseUp(i, j);		
			board[i,j].player = m_activePlayer;			
			globals.server.makeMove(m_gameId, m_me, coordinates);
		}
		else{//undo
			coordinates = {x:pressedDot.i, y:pressedDot.j};
			undoMove(coordinates);
			
		}
		pressedDot = null;
		
				
	}
	
	function gMouseUp(i, j){
		dot = board[i][j].dot;
		dot.setRadius(3);
		dot.setStroke('black');
		dot.setStrokeWidth(1);
		boardLayer.draw();
	}
	
	function recieveResponse(message){
		var data = JSON.parse(message.body);
		if (data.errorMessage != null){
			if (data.move != null){
				undoMove(data.move.coordinates);
			}
			console.error(message);
			return;
		}
		
		if (data.currentPlayer)
			
			m_activePlayer = new Player(data.currentPlayer.color, data.currentPlayer.id);
		if (data.move != null){
			restoreMove(data.move);
		}
		
		
		if (data.newCycles.length > 0){
			for (var cycleIndex = 0; cycleIndex < data.newCycles.length; cycleIndex++){
				var cycle = data.newCycles[cycleIndex];
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
		globals.statusPanel.showGameStatus(self);
		console.debug(message);
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
	
	function addPlayerToGame(){
		
		globals.server.addPlayerToGame(m_me, m_gameId);
	}
	
	this.addPlayerToGame = addPlayerToGame;
	
	this.getActivePlayer = function getActivePlayer(){
		return m_activePlayer;
	};
	
	function restoreState(state){
		console.debug("state recieved");
		console.debug(state);
		m_activePlayer = new Player(state.currentPlayer.color, state.currentPlayer.id);
		for (var i = 0 ; i < state.players.length; i++){
			var player = new Player(state.players[0].color, state.players[0].id);
			m_players[player.id] = player;
		}
		for (var i = 0; i < state.moves.length; i++){
			
			m_activePlayer = new Player(state.moves[i].player.color, state.moves[i].player.id);state.moves[i].player;
			restoreMove(state.moves[i]);			
		}
		
		
	}
	
	function restoreMove(move){
		var coordinates = move.coordinates;
		gMouseDown(coordinates.x, coordinates.y, move.player.color);
		gMouseUp(coordinates.x, coordinates.y);
		board[coordinates.x][coordinates.y].player = move.player;
		
	}
	
	
	
}

