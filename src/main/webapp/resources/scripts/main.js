function onProfile(){
	globals.centralPanel.showProfile();
}

function onNewGame(){
	
	var gameSettings = {size: "Medium", players: 2};
	if (globals.activeGame == null){
		post('games' ,JSON.stringify(gameSettings), function (data, textStatus, request){
			var location = request.getResponseHeader('location');
			globals.activeGame = new game('board-container', gameSettings.size, location);		    	    
			addPlayerToGame(location);
		});		
	}
	else{
		//TODO: replace it with user anouncement
		console.debug("you are currently in active game. Multiple games are not supported yet");
	}
	
	globals.centralPanel.showBoard();
}

function onShowGames(){
	$.getJSON('games/',function(data){
		console.debug(data);
		globals.centralPanel.showGames(data);		
	});
	
}

function addPlayerToGame(gameLocation){
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var uid = response.authResponse.userID;
			//var accessToken = response.authResponse.accessToken;
			var player = new Player('red', uid);
			put(gameLocation, JSON.stringify(player), function(){
				console.debug('successfully added player ' + uid);
			});
	    } 
		else {
		    console.error("something went wrong :(");
		}
	});
}




      
     