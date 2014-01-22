<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<html>
<head>
	<title>Home</title>
	<c:if test="${redirectUrl != null}">
   		<script>
               top.location.href = "${redirectUrl}";   
           </script>
   	</c:if>   	
   	
   	<script src="https://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
   	<script src="https://d3lp1msu2r81bx.cloudfront.net/kjs/js/lib/kinetic-v4.7.4.min.js" type="text/javascript"></script>
   	
   	<script>
   	$(function () {

   	    //then load the JavaScript file
   	    $.getScript('resources/scripts/utilities.js');
   	    $.getScript('resources/scripts/player.js');
   	 	$.getScript('resources/scripts/board-cell.js');
   		$.getScript('resources/scripts/game.js');   		
   		$.getScript('resources/scripts/main.js');
   	});
   	</script>
   	
   	
            
    <!-- <script src="resources/scripts/player.js" type="text/javascript"></script>
    <script src="resources/scripts/board-cell.js" type="text/javascript"></script>
    <script src="resources/scripts/game.js" type="text/javascript"></script>
    <script src="resources/scripts/utilities.js" type="text/javascript"></script>
    <script src="resources/scripts/main.js" type="text/javascript"></script> -->
    
</head>
<body>
	<div id="game-container"></div>    
</body>
</html>
