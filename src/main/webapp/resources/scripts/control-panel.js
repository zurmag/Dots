function ControlPanel(panelDivName){
	var self = this;
	var panelDiv = document.getElementById(panelDivName);
	
	var divs = {};
	var activeContainer;
	
	divs['no-game-control'] = createNoGameControl();
	activeContainer = divs['no-game-control'];
	panelDiv.appendChild(activeContainer);
	
	divs['new-game-control'] = createNewGameControl();
	panelDiv.appendChild(divs['new-game-control']);
	
	divs['game-control'] = createGameControl();
	panelDiv.appendChild(divs['game-control']);
	
	
	
	Object.keys(divs).map(function (key){
		var div = divs[key];
		$(div.id + ' button').each(function(index, element){
			var callback = element.click;
			$(element).button().unbind('click').click(callback);
		});
		div.style.display = 'none';
	});
	
	self.showGameControl = function showGameControl(){
		showContainer('game-control');
	};
	
	self.showNoGameControl = function showNoGameControl(){
		showContainer('no-game-control');
	};
	
	self.showNewGameControl = function showNewGameControl(){
		showContainer('new-game-control');
	};
	
	function showContainer(container){
		activeContainer.style.display='none';
		activeContainer = divs[container]; 
		activeContainer.style.display='block';
	}
	
	function createGameControl(){
		var div = document.createElement('div');
		var button = document.createElement('button');
		button.click = disconnectGame;
		button.innerHTML = 'Quit';
		div.appendChild(button);
		return div;
	}
	
	function createNoGameControl(){
		var div = document.createElement('div');
		var button = document.createElement('button');
		button.click = globals.centralPanel.showNewGameDialog;
		button.innerHTML = 'Play';
		div.appendChild(button);
		
		button = document.createElement('button');
		button.click = function(){
			globals.centralPanel.showDashboard();
			globals.statusPanel.hideActiveGameStatus();
		};
		button.innerHTML = 'Dashboard';
		div.appendChild(button);
		return div;
	}
	
	function createNewGameControl(){
		var div = document.createElement('div');
		var button = document.createElement('button');
		button.click = globals.centralPanel.submitGame;
		button.innerHTML = 'Start Game';
		div.appendChild(button);
		return div;
	}
	
}