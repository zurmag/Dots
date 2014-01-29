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
	
	this.showGames = function(){		
		activeContainer.style.display='none';
		activeContainer = divs['games-container'];
		activeContainer.style.display='block';
		
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
	
};