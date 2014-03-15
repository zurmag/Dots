function GameStatusPanel(panelDivName){
	
	var m_panelDiv = document.getElementById(panelDivName);
	
	this.showActiveGameStatus = function(game){
		var players = game.getPlayers();
		$(m_panelDiv).empty();
		for (var i = 0; i < players.length ; i++){
			var player = players[i];
			m_panelDiv.appendChild(createPlayerDiv(player));
		}
	};
	
	this.addPlayer = function(player){
		m_panelDiv.appendChild(createPlayerDiv(player));
	};
	
	this.scoreChange = function scoreChange(scores){
		var keys = Object.keys(scores);
		keys.map(function(key) {changePlayerScore(key, scores[key]);});
	};
	
	this.hideActiveGameStatus = function hideActiveGameStatus(){
		var activePlayerDiv = createOrGetElement('div', 'active-player');
		activePlayerDiv.style.display='none';
	};
	
	function changePlayerScore(id, score){
		$("#player"+id+"-info").html("Score: " + score);
	}
	
	function createPlayerDiv(player){
		var playerDiv = document.createElement('div');
		playerDiv.id = "player" + player.id + "-container";
		
		var img = document.createElement('img');
		img.id = "player" + player.id + "img";
		img.src = player.getAvatar();
		playerDiv.appendChild(img);
		
		var playerInfo = document.createElement('div');
		playerInfo.id = "player" + player.id + "-info";
		playerInfo.innerHTML = "Score: " + player.score;
		playerDiv.appendChild(playerInfo);
		return playerDiv;
	}
	
	function createOrGetElement(el, id){
		var querry = $(el + '#' + id);
		var element = false;
		if (querry.length == 0){
			element = document.createElement(el);
			element.id = id;			
		}else{
			element = querry[0];		
		}
		return element;
	}
}