function CentralPanel(panelDivName){
	var self = this;
	var divs = {};
	var activeContainer;
	
	var panelDiv = document.getElementById(panelDivName);
	//Constructor
	
	
	divs['dashboard-container'] = createDashboardContainer(); 
	activeContainer = divs["dashboard-container"];	
	
	divs['new-game-container'] = createNewGameContainer();
	panelDiv.appendChild(divs['new-game-container']);
	
	divs['board-container'] = createBoardContainer();
	panelDiv.appendChild(divs['board-container']);	 
	
	panelDiv.appendChild(activeContainer);
	
	this.showDashboard = function showDashboard(){		
		activeContainer.style.display='none';
		activeContainer = divs['dashboard-container']; 
		activeContainer.style.display='block';
	};
	
	this.showNewGameDialog = function showNewGameDialog(){
		activeContainer.style.display='none';
		activeContainer = divs['new-game-container']; 
		activeContainer.style.display='block';		
		$( ".radio" ).buttonset();
		
		globals.controlPanel.showNewGameControl();
	};
	
	this.showBoard = function showBoard(){
		activeContainer.style.display='none';
		activeContainer = divs['board-container'];
		activeContainer.style.display='block';
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
	
	function createDashboardContainer(){
		var dashboardContainerDiv = document.createElement('div');
		dashboardContainerDiv.style.display='none';
		dashboardContainerDiv.id = "dashboard-container";
		dashboardContainerDiv.innerHTML="Dashboard";
		return dashboardContainerDiv;
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
	
};