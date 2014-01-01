<html>
<head>
	<title>Home</title>
</head>
<body>
    <div id="container"></div>
    <script src="http://d3lp1msu2r81bx.cloudfront.net/kjs/js/lib/kinetic-v4.7.4.min.js" type="text/javascript"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>        
    <script src="resources/scripts/player.js" type="text/javascript"></script>
    <script src="resources/scripts/board-cell.js" type="text/javascript"></script>
    <script src="resources/scripts/game.js" type="text/javascript"></script>
    <script defer="defer">



      var stage = new Kinetic.Stage({
        container: 'container',
        width: 578,
        height: 578
      });
      
      var game = new game('big');
      var playerOne = game.getPlayer('red');
      var playerTwo = game.getPlayer('green');
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

   
    </script>
  </body>
</html>
