function CentralPanel(panelDivName){
	var self = this;
	var divs = {};
	var activeContainer;
	
	var panelDiv = document.getElementById(panelDivName);
	//Constructor
	
	
	divs['profile-container'] = createProfileContainer(); 
	activeContainer = divs["profile-container"];	
	
	divs['new-game-container'] = createNewGameContainer();
	panelDiv.appendChild(divs['new-game-container']);
	
	divs['board-container'] = createBoardContainer();
	panelDiv.appendChild(divs['board-container']);	 
		
	divs['games-container'] = createGamesContainer();
	panelDiv.appendChild(divs['games-container']);
	
	divs['control-container'] = createControlContainer();
	panelDiv.appendChild(divs['control-container']);
	
	panelDiv.appendChild(activeContainer);
	panelDiv.appendChild(divs['control-container']);
	
	this.showProfile = function showProfile(){		
		activeContainer.style.display='none';
		activeContainer = divs['profile-container']; 
		activeContainer.style.display='block';
	};
	
	this.showNewGameDialog = function showNewGameDialog(){
		activeContainer.style.display='none';
		activeContainer = divs['new-game-container']; 
		activeContainer.style.display='block';		
		$( ".radio" ).buttonset();
		
		//$('#control-button').button({label: 'Start Game'}).unbind('click').click(submitGame);
		
		globals.controlPanel.showNewGameControl();
	};
	
	this.showBoard = function showBoard(){		
		activeContainer.style.display='none';
		activeContainer = divs['board-container'];
		activeContainer.style.display='block';		
	};
	
	this.showGames = function(data){		
		activeContainer.style.display='none';
		activeContainer = divs['games-container'];
		activeContainer.style.display='block';		
		activeContainer.innerHTML = '';
		activeContainer.appendChild(createGamesView(data));
		$('div#games-container button').click(function (){
			joinGame(this.id);			
			
		});
	};
	
	self.submitGame = function submitGame(){
		var gameSize = $("#radio-game-size :radio:checked").attr('id').replace('input-','');
		var playersNumber = $("#radio-players-number :radio:checked").attr('id').replace('input-','');
		var gameSettings = {size: gameSize, players: playersNumber, widthPx: globals.gameWidth};
		FB.getLoginStatus(function(response) {
			var newGameData = {gameSettings: gameSettings, token: response.authResponse.accessToken};
			globals.server.newGame(newGameData, function (data, textStatus, request){
				
				var settings = {size: data.size, id: data.id, state: data.state.state, widthPx: globals.gameWidth};
				globals.activeGame = new Game(settings, data.state);
				showActiveGame();
				globals.controlPanel.showGameControl();
			});
		});
		
		
	};
	
	function createProfileContainer(){
		var profileContainerDiv = document.createElement('div');
		profileContainerDiv.style.display='none';
		profileContainerDiv.id = "profile-container";
		profileContainerDiv.innerHTML="Profile";
		return profileContainerDiv;
	}
	
	function createNewGameContainer(){
		var newGameContainerDiv = createNewGameView();		
		newGameContainerDiv.style.display='none';
		newGameContainerDiv.id = "new-game-container";		
		return newGameContainerDiv;
	}
	
	function createBoardContainer(){
		var boardContainerDiv = document.createElement('div');
		boardContainerDiv.style.display='none';
		boardContainerDiv.id = "board-container";
		return boardContainerDiv;
	}
	
	function createGamesContainer(){
		var gamesContainerDiv = document.createElement('div');
		gamesContainerDiv.style.display='none';
		gamesContainerDiv.id = "games-container";
		return gamesContainerDiv;
	}
	
	function createNewGameView(){
		var label;
		var div = document.createElement('div');
		
		var gameSizeDiv = document.createElement('div');
		gameSizeDiv.id = 'game-size-div';
		div.appendChild(gameSizeDiv);		
		label = document.createElement('h3');
		label.innerHTML = 'Game size';
		gameSizeDiv.appendChild(label);
		gameSizeDiv.appendChild(createRadioInputs(['Small', 'Medium', 'Big'], 'Medium', 'game-size'));
		
		var playernumberDiv = document.createElement('div');
		playernumberDiv.id = 'player-number-div';
		div.appendChild(playernumberDiv);
		label = document.createElement('h3');
		label.innerHTML = 'Players in game';
		playernumberDiv.appendChild(label);
		playernumberDiv.appendChild(createRadioInputs(['2', '3', '4'], '2', 'players-number'));
		
		
		
		return div;
	}
	
	function createControlContainer(){
		var div = document.createElement('div');
		
		var button = document.createElement('button');
		button.id = 'control-button';
		button.innerHTML = 'Play';
		$(button).button().unbind('click').click(function(){
				self.showNewGameDialog();
		});
		div.appendChild(button);
		
		return div;
	}
	
	function createRadioInputs(labels, defaultLabel, name){
		
		var gameSizeForm = document.createElement('form');
		gameSizeForm.id = 'form-' + name;
		
		var div = document.createElement('div');
		div.className = 'radio';
		div.id = 'radio-'+name;
		for (var i = 0;i<labels.length;i++){
			var label = labels[i];
			var input = document.createElement('input');
			input.id = 'input-'+label;
			input.type = 'radio';
			input.name = name;
			
			if (label == defaultLabel){
				input.checked = 'checked';
			}
			var labelTag = document.createElement('label');
			$(labelTag).attr('for',input.id);
			labelTag.innerHTML = label;			
			div.appendChild(input);
			div.appendChild(labelTag);
		}
		div.appendChild(gameSizeForm);
		return div;
	}
	
	function createGamesView(gamesArray){
		var table = document.createElement('table');
		table.border=1;
		
		table.appendChild(createHeadersRow());
		for(var i = 0; i< gamesArray.length; i++){
			table.appendChild(createDataRow(gamesArray[i]));
		}
		
		return table;
				
		function createHeadersRow(){
			var headersRow = document.createElement('tr');
			var header = document.createElement('th');header.innerHTML='Connect?';				
			headersRow.appendChild(header);			
			header = document.createElement('th');header.innerHTML='size';				
			headersRow.appendChild(header);			
			header = document.createElement('th');header.innerHTML='max players';				
			headersRow.appendChild(header);
			header = document.createElement('th');header.innerHTML='connected players';				
			headersRow.appendChild(header);
			return headersRow;
		}
		
		function createDataRow(data){
			var tr = document.createElement('tr');
			var td;
			td = document.createElement('td');
			var button = document.createElement('button');
			button.id = data.id;
			button.innerHTML='Yes!';
			td.appendChild(button);
			tr.appendChild(td);
			td = document.createElement('td');td.innerHTML=data.size;tr.appendChild(td);
			td = document.createElement('td');td.innerHTML=data.maxPlayers;tr.appendChild(td);
			var connectedPlayers = document.createElement('div');
			for(var i = 0;i < data.state.players.length;i++){
				var img = document.createElement('img');
				var player = new Player(data.state.players[i]);
				img.src = player.getAvatar();
				connectedPlayers.appendChild(img);
			}
			td = document.createElement('td');td.appendChild(connectedPlayers);tr.appendChild(td);
			return tr;
		}		
		
	}
	
	
	
};