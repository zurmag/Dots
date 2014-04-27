<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<html>
<head>
	<title>Dots</title>	
	<c:if test="${redirectUrl != null}">
   		<script>
               top.location.href = "${redirectUrl}";   
           </script>
   	</c:if>   	
   	
   	<link rel="stylesheet" type="text/css" href="resources/css/cupertino/jquery-ui-1.10.4.custom.min.css">
   	<link rel="stylesheet" type="text/css" href="resources/css/main.css" />
   	<link rel="stylesheet" type="text/css" href="resources/css/flipclock.css">
   	
   	<script src="https://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
   	<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
   	<script src="https://d1fxtkz8shb9d2.cloudfront.net/sockjs-0.3.4.min.js"></script>
	<script src="resources/scripts/stomp.js"></script>
	<script src="resources/scripts/flipclock.min.js"></script>
   	<script src="https://d3lp1msu2r81bx.cloudfront.net/kjs/js/lib/kinetic-v4.7.4.min.js" type="text/javascript"></script>
   	<script src="resources/scripts/server-proxy.js"></script>
   	<script>
   	window.globals = {activeGame: false, gameWidth: ${gameWidth}};
   	$(document).ready(function () {
   		
   		$( "button" )
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
	<div id="wrapper">
		<div style='width: ${gameWidth + statusPanelWidth}px; height: ${gameHeight + 20}px'>
			<div id="central-panel" class=Panel style='width: ${gameWidth}px; height: ${gameHeight}px;'>
				<img src="resources/images/Beta_Testing.jpeg" style="position: absolute; top: 40; left: 0;opacity: 0.2"/>
				<div id="announcement-bar" class="info-announcement" style="height: 20">Welcome</div>
			</div>
			<div id="status-panel" class="Panel" style='width: ${statusPanelWidth}px; height: ${statusPanelHeight}px;'></div>
		</div>
		<div id="control-panel" style='width: ${controlPanelWidth}px; height: ${controlPanelHeight}px; '></div>
	</div>
	<script type="text/javascript" src='resources/scripts/player.js'></script>
	<script type="text/javascript" src='resources/scripts/board-cell.js'></script>
	<script type="text/javascript" src='resources/scripts/central-panel.js'></script>
	<script type="text/javascript" src='resources/scripts/status-panel.js'></script>
	<script type="text/javascript" src='resources/scripts/control-panel.js'></script>
	<script type="text/javascript" src='resources/scripts/menu-panel.js'></script>
	<script type="text/javascript" src='resources/scripts/game.js'></script>
	<script type="text/javascript" src='resources/scripts/main.js'></script>
</body>
</html>
