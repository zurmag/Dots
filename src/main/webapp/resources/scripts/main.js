

function onProfile(){
	globals.centralPanel.showProfile();
	globals.statusPanel.hideActiveGameStatus();
	announce('info', 'Your game profile will be here');
}

function onNewGame(){
	announce('info', 'Your game');
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
		announce('info', 'Welcome back');
		
	}
	globals.statusPanel.showActiveGameStatus(globals.activeGame);
	globals.centralPanel.showBoard();	
	globals.menuPanel.onGameStart();
	
}

function onShowGames(){
	announce('info', 'Games to play');
	$.getJSON('games/',function(data){
		globals.games = {};
		for (var i = 0; i< data.length; i++){
			globals.games[data[i].id] = data[i]; 
		}
		globals.centralPanel.showGames(data);
		globals.statusPanel.hideActiveGameStatus();
	});
	
}

function onPauseResume(){
	announce('info','Unimplemented yet');
	$("#pause-resume span").text(function(i, text){
        return text === "Pause" ? "Resume" : "Pause";
	});
}

function disconnectGame(){
	announce('info', 'Disconnecting from game...');
	globals.activeGame.disconnect();
}

setTimeout(init, 1000);

function init(){
	globals.centralPanel = new CentralPanel("central-panel");
	globals.statusPanel = new GameStatusPanel('status-panel');
	globals.menuPanel = new MenuPanel();
	globals.server = new ServerProxy();
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var uid = response.authResponse.userID;
			//var accessToken = response.authResponse.accessToken;
			$.getJSON('players/'+uid+'/activeGames?fullState=true', function(data){
				if (data.length > 0){			
					var settings = {size: data[0].size, id: data[0].id};
					globals.activeGame = new Game(settings, data[0].state);
					onNewGame();
				}
			});
	    } 
		else {
		    announce('error', "something went wrong :(");
		}
	});    
}

function announce(level, text){
	var classes = $('#announcement-bar').attr('class');
	var bar = $('#announcement-bar');
	bar.removeClass(classes);
	if (level == "info"){		
		bar.addClass('info-announcement');			
	}
	if (level == "error"){
		bar.addClass('error-announcement');
	}
	bar.html(text);
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}
