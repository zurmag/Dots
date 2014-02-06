
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

setTimeout(init, 1000);

function init(){
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var uid = response.authResponse.userID;
			//var accessToken = response.authResponse.accessToken;
			$.getJSON('players/'+uid+'/activeGames?fullState=true', function(data){
				if (data.length > 0){					
					globals.activeGame = new game(data[0].size, 'games/' + data[0].id, data[0].state);
					
				}
			});
	    } 
		else {
		    console.error("something went wrong :(");
		}
});    
}