function onNewGame(){
	
	var m_game;
	var gameSettings = {size: "Medium", players: 2};
	
	post('games' ,JSON.stringify(gameSettings), function (data, textStatus, request){
		m_game = new game('board-container', gameSettings.size, request.getResponseHeader('location'));
	     
	    
	    
	});	
}




      
     