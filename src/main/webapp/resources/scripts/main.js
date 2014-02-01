function onProfile(){
	globals.centralPanel.showProfile();
}

function onNewGame(){
	
	var gameSettings = {size: "Medium", players: 2};
	if (globals.activeGame == null){
		post('games' ,JSON.stringify(gameSettings), function (data, textStatus, request){
			var location = request.getResponseHeader('location');
			globals.activeGame = new game(gameSettings.size, location);		    	    
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






      
     