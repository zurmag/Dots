var stage = new Kinetic.Stage({
       container: 'container',
       width: 578,
       height: 578
     });
     var game;
     
     var playerOne = new Player('red', 1);
     var playerTwo = new Player('green', 2);
     
     post('games/', JSON.stringify([playerOne, playerTwo]), function (data, textStatus, request){
    	 game = new game('big', request.getResponseHeader('location'));
         
         var currentPlayer = null;
         var layer = game.getBoard();
         
         // add the layer to the stage
         stage.add(layer);
         stage.on('mousedown', function () {
           currentPlayer = (currentPlayer == playerOne)?playerTwo:playerOne;
           var mousePos = stage.getPointerPosition();        
           game.setPointMouseDown(mousePos.x, mousePos.y, currentPlayer);
         });

         stage.on('mouseup', function () {
           //player = (currentPlayer == playerOne)?playerTwo:playerOne;
           var mousePos = stage.getPointerPosition();
           game.setPointMouseUp(mousePos.x, mousePos.y, currentPlayer);
         });
     });
      
     