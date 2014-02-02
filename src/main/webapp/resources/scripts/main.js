
globals.centralPanel = new CentralPanel("central-panel");
function onProfile(){
	globals.centralPanel.showProfile();
}

function onNewGame(){
	
	var gameSettings = {size: "Medium", players: 2};
	if (!globals.activeGame){
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
		globals.games = {};
		for (var i = 0; i< data.length; i++){
			globals.games[data[i].id] = data[i]; 
		}
		globals.centralPanel.showGames(data);		
	});
	
}






      
     