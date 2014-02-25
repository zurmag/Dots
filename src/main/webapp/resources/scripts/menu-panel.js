function MenuPanel(){
	
	this.onGameStart = function onGameStart(){
		$('#first-menu').hide();
		$('#active-game-menu').show();
		$('#after-game-menu').end();
	};
	
	this.onGameEnd = function onGameEnd(){
		$('#active-game-menu').hide();
		$('#after-game-menu').show();
	};
	
	this.returnToMainMenu = function returnToMainMenu(){
		$('#active-game-menu').hide();
		$('#after-game-menu').hide();
		$('#first-menu').show();
	};
	
}