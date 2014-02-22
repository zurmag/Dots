function CentralPanel(panelDivName){
	var divs = {};
	var activeContainer;
	
	var panelDiv = document.getElementById(panelDivName);
	//Constructor
	
	
	divs['profile-container'] = createProfileContainer(); 
	activeContainer = divs["profile-container"];
	panelDiv.appendChild(activeContainer);		
	
	divs['board-container'] = createBoardContainer();
	panelDiv.appendChild(divs['board-container']);
	 
		
	divs['games-container'] = createGamesContainer();
	panelDiv.appendChild(divs['games-container']);
		
	this.showProfile = function(){		
		activeContainer.style.display='none';
		activeContainer = divs['profile-container']; 
		activeContainer.style.display='block';
	};
	
	this.showBoard = function(){		
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
			gameObj = globals.games[this.id];
			console.debug(gameObj);
			if (!globals.activeGame){
				globals.activeGame = new Game({
					size: gameObj.size, 
					id: gameObj.id,
					color: 'green'
				});
				onNewGame();
			}
			else{
				announce('info', 'you are in active game!');
			}
			
		});
	};
	
	function createProfileContainer(){
		var profileContainerDiv = document.createElement('div');
		profileContainerDiv.style.display='none';
		profileContainerDiv.id = "profile-container";
		profileContainerDiv.innerHTML="Profile";
		return profileContainerDiv;
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
		gamesContainerDiv.innerHTML="Games";
		return gamesContainerDiv;
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
			var connectedPlayers = "";
			for(var i = 0;i < data.players.length;i++){
				connectedPlayers += data.players[i].id;
				connectedPlayers += " ";
			}
			td = document.createElement('td');td.innerHTML=connectedPlayers;tr.appendChild(td);
			return tr;
		}		
		
	}
	
	
	
};