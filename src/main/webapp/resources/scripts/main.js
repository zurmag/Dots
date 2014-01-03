var stage = new Kinetic.Stage({
       container: 'container',
       width: 578,
       height: 578
     });
     var game;
     
     
     post('games/', '[]', function (data, textStatus, request){
    	 game = new game('big', request.getResponseHeader('location'));
    	 
    	 var playerOne = game.getPlayer('red', 1);
         var playerTwo = game.getPlayer('green', 2);
         
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
      
     