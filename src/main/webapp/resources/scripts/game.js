function game(size, location, state){
	
	var offset = 10;
	var distance = 20;
	var boardLayer = null;
	var board = [];
	var boardColor = 'grey';
	var m_gameLocation = '';
	var m_gameId = '';
	var players = [];
	var currentPlayerIndex = 0;
	var pressedDot = null;
	var stage = null;
	var m_stompClient = false;
	
	init(size, location);
	
	
	function init(size, location){
		
		m_gameLocation = location;
		m_gameId = location.split('/').pop();
		var width = 0;
		var height = 0;		
		if (size == 'small'){
			width = height = 10;
		}
		else if (size == 'big'){
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
	    
	    if (state != null){
			restoreState(state);		
		}else{
			addPlayerToGame();
		}
	    
	    connectGame();	    
	}

	function connectGame(){
		var socket = new SockJS("/dots/ws");
		m_stompClient = Stomp.over(socket);
	    m_stompClient.connect(' ',' ', function(){
	    	console.debug('game '+ m_gameId + ' successfuly connected');
	    	m_stompClient.subscribe('/sub/games/' + m_gameId, recieveResponse);
	    },function(error){
	    	console.error('game '+ m_gameId + 'failed to connect');
	    	console.error('error: '+ error);
	    });
	    
	}
	
	function createBoardLayer(){
		
		var radius = 2;
		var color = boardColor;

		var layer = new Kinetic.Layer();


		for (var xx = 0; xx < board.length; xx++){
			for (var yy = 0; yy < board[xx].length; yy++){
				if (board[xx][yy] != null){
					color = board[xx][yy].color;
					radius = 3;
				}				
				else {
					radius = 2;
					color = boardColor;
				}
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
			cell.dot.setFill(getCurrentPlayer().color);
			boardLayer.draw();
		}		
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
			cell = board[i][j]; 
			dot = board[i][j].dot;
			dot.setRadius(3);
			dot.setStroke('black');
			dot.setStrokeWidth(1);			
			cell.player = getCurrentPlayer();
			
			var actionUrl = '/action' + '/games/' + m_gameId + '/players/' + getCurrentPlayer().id + '/moves';			
			m_stompClient.send(actionUrl, {}, JSON.stringify(coordinates));
		}
		else{//undo
			coordinates = {x:pressedDot.i, y:pressedDot.j};
			undoMove(coordinates);
			/*dot = board[pressedDot.i][pressedDot.j].dot;
			dot.setFill(boardColor);*/
		}
		pressedDot = null;
		boardLayer.draw();
				
	}
	
	function recieveResponse(message){
		var data = JSON.parse(message.body);
		if (data.errorMessage != null){
			if (data.move != null){
				undoMove(data.move.coordinates);
			}
			console.error(message);
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
		console.debug(message);
	}
	
	function undoMove(coordinates){
		dot = board[coordinates.x][coordinates.y].dot;
		dot.setFill(boardColor);
	}
	
	function switchPlayer(){
		currentPlayerIndex = ++currentPlayerIndex % this.players.length ;
	}
	
	function getCurrentPlayer(){
		return players[currentPlayerIndex];
	}
	
	function addPlayerToGame(){
		FB.getLoginStatus(function(response) {
			if (response.status === 'connected') {
				var uid = response.authResponse.userID;
				//var accessToken = response.authResponse.accessToken;
				var player = new Player('red', uid);
				put(m_gameLocation, JSON.stringify(player), function(){
					console.debug('successfully added player ' + uid);
				});
				players.push(player);
		    } 
			else {
			    console.error("something went wrong :(");
			}
		});
	};
	this.addPlayerToGame = addPlayerToGame;
	
	function restoreState(state){
		console.debug(state);
	}
}

