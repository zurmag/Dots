

function onProfile(){
	globals.centralPanel.showProfile();
}

function onNewGame(){
	
	var gameSettings = {size: "Medium", players: 2};
	if (!globals.activeGame){
		globals.server.newGame(gameSettings, function (data, textStatus, request){
			gameSettings.location = request.getResponseHeader('location');
			gameSettings.id = request.getResponseHeader('location').split('/').pop();
			gameSettings.color = 'red';
			globals.activeGame = new Game(gameSettings);
		});				
	}
	else{
		//TODO: replace it with user anouncement
		console.debug("you are currently in active game. Multiple games are not supported yet");
	}
	
	globals.centralPanel.showBoard();
	globals.statusPanel.showGameStatus(globals.activeGame);
	
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
	globals.centralPanel = new CentralPanel("central-panel");
	globals.statusPanel = new GameStatusPanel('status-panel');
	globals.server = new ServerProxy();
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var uid = response.authResponse.userID;
			//var accessToken = response.authResponse.accessToken;
			$.getJSON('players/'+uid+'/activeGames?fullState=true', function(data){
				if (data.length > 0){			
					var settings = {size: data[0].size, id: data[0].id};
					globals.activeGame = new Game(settings, data[0].state);
				}
			});
	    } 
		else {
		    console.error("something went wrong :(");
		}
});    
}