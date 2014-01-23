var stage = new Kinetic.Stage({
       container: 'game-container',
       width: 578,
       height: 578
 });

var game;
var gameId = 1;
var gameSize = "Medium";
var playerOne = new Player('red', 1);
var playerTwo = new Player('green', 2);
var players = [playerOne, playerTwo];

post('games' + "?boardSize=" + gameSize, JSON.stringify(players), function (data, textStatus, request){
	game = new game(gameSize, request.getResponseHeader('location'), players);
     
    var layer = game.getBoard();
     
    // add the layer to the stage
    stage.add(layer);
    stage.on('mousedown', function () {
    	var mousePos = stage.getPointerPosition();        
        game.setPointMouseDown(mousePos.x, mousePos.y);
    });

    stage.on('mouseup', function () {
    	var mousePos = stage.getPointerPosition();
    	game.setPointMouseUp(mousePos.x, mousePos.y);
    });
});
      
     