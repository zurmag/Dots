<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<html>
<head>
	<title>Dots</title>	
	<c:if test="${redirectUrl != null}">
   		<script>
               top.location.href = "${redirectUrl}";   
           </script>
   	</c:if>   	
   	
   	<link rel="stylesheet" href="resources/css/cupertino/jquery-ui-1.10.4.custom.min.css">
   	<link rel="stylesheet" type="text/css" href="resources/css/main.css" />
   	
   	<script src="https://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
   	<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
   	
   	<script src="https://d3lp1msu2r81bx.cloudfront.net/kjs/js/lib/kinetic-v4.7.4.min.js" type="text/javascript"></script>
   	
   	<script>
   	var globals = {activeGame: false};
   	$(document).ready(function () {
   		
   		$( "input[type=submit], a, button" )
			.button()
     		.click(function( event ) {
       			event.preventDefault();
     		});
		});
   		$.ajaxSetup({ cache: true });   	    
   	    
   		$.getScript('//connect.facebook.net/en_UK/all.js', function(){
   		    FB.init({
   		      appId: '${fbAppId}',
   		    }); 		    
   		});
   		
		
   	</script>    
</head>
<body>	
	
	<div id="fb-root"></div>
	<div class="fb-like" data-href="https://apps.facebook.com/simple-games-dots/" data-layout="standard" data-action="like" data-show-faces="true" data-share="true"></div>	
	<div id="menu-panel" class="Panel">
	<button onclick='onProfile()'>Profile</button><Br/>
	<button onclick='onNewGame()'>New Game</button><Br/>
	<button onClick='onShowGames()'>Show games</button><Br/>
	
		
	</div>
	<div id="central-panel" class=Panel>		
	</div>
	<div id="players-panel" class="Panel"></div>
	<script type="text/javascript" src='resources/scripts/utilities.js'></script>
	<script type="text/javascript" src='resources/scripts/player.js'></script>
	<script type="text/javascript" src='resources/scripts/board-cell.js'></script>
	<script type="text/javascript" src='resources/scripts/central-panel.js'></script>
	<script type="text/javascript" src='resources/scripts/game.js'></script>
	<script type="text/javascript" src='resources/scripts/main.js'></script>
</body>
</html>
