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
</head>
<body>
	<div id="fb-root"></div>
	<script>
      window.fbAsyncInit = function() {
        FB.init({
          appId      : '${fbApiId}',
          status     : true,
          xfbml      : true
        });
        
        console.debug("hello");
      };

      (function(d, s, id){
         var js, fjs = d.getElementsByTagName(s)[0];
         if (d.getElementById(id)) {return;}
         js = d.createElement(s); js.id = id;
         js.src = "//connect.facebook.net/en_US/all.js";
         fjs.parentNode.insertBefore(js, fjs);
       }(document, 'script', 'facebook-jssdk'));
    </script>
	<div id="game-container"></div>    
</body>
</html>
