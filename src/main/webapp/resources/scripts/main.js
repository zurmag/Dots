 

function onNewGame(){
	
	globals.centralPanel.showNewGameDialog();
	globals.statusPanel.hideActiveGameStatus();
	announce('info', 'Choose your game');
	
}

function showActiveGame(){
	globals.statusPanel.showActiveGameStatus(globals.activeGame);
	globals.centralPanel.showBoard();	
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
	globals.controlPanel = new ControlPanel('control-panel');
	globals.controlPanel.showNoGameControl();
	globals.server = new ServerProxy();
	FB.getLoginStatus(function(response) {
		if (response.status === 'connected') {
			var uid = response.authResponse.userID;
			//var accessToken = response.authResponse.accessToken;
			$.getJSON('fbusers/'+uid+'/activeGames?fullState=true', function(data){
				if (data.length > 0){
					var settings = {size: data[0].size, id: data[0].id, state: data[0].state.state, widthPx: globals.gameWidth};
					globals.activeGame = new Game(settings, data[0].state);
					globals.controlPanel.showGameControl();
					showActiveGame();
					
					announce('info', 'Welcome back');
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
