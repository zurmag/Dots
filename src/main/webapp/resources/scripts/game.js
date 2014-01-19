function game(size, location, players){
	
	var offset = 10;
	var distance = 20;
	var boardLayer = null;
	var board = [];
	var boardColor = 'grey';
	var gameLocation = '';
	this.players = [];
	var currentPlayerIndex = 0;
	var pressedDot = null;
	init(size, location, players);

	function init(size, location, players){
		this.players = players;
		
		gameLocation = location;
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

		boardLayer = createBoardLayer(width, height);

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

	this.getBoard = function (){		
		return boardLayer;
	};

	this.setPointMouseDown = function (x, y){
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
	};

	this.setPointMouseUp = function (x, y){
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
			post(gameLocation+'/players/' + getCurrentPlayer().id + '/moves',JSON.stringify(coordinates), function(data){
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
				
			});
			switchPlayer();
		}
		else{//undo
			dot = board[pressedDot.i][pressedDot.j].dot;
			dot.setFill(boardColor);
		}
		pressedDot = null;
		boardLayer.draw();
				
	};
	
	//privates
	function switchPlayer(){
		currentPlayerIndex = ++currentPlayerIndex % this.players.length ;
	}
	
	function getCurrentPlayer(){
		return this.players[currentPlayerIndex];
	}	
}
