function GameStatusPanel(panelDivName){
	
	var m_panelDiv = document.getElementById(panelDivName);
	var self = this;
	this.showActiveGameStatus = function(game){
		var players = game.getPlayers();
		$(m_panelDiv).empty();
		for (var i = 0; i < players.length ; i++){
			var player = players[i];
			self.addPlayer(player);
		}
	};
	
	this.addPlayer = function addPlayer(player){
		if ($('#player'+player.id+'-container').length == 0){
			m_panelDiv.appendChild(createPlayerDiv(player));
			var gameClock = $('#game-clock-'+player.id).FlipClock({
				countdown: true,
				clockFace: 'MinuteCounter'
			});
			gameClock.setTime(10);
			gameClock.start();
			var moveClock = $('#move-clock-'+player.id).FlipClock({
				countdown: true,
				clockFace: 'MinuteCounter'
			});
			$('.flip-clock-label').hide();
			
		}
	};
	
	this.scoreChange = function scoreChange(scores){
		var keys = Object.keys(scores);
		keys.map(function(key) {changePlayerScore(key, scores[key]);});
	};
	
	this.hideActiveGameStatus = function hideActiveGameStatus(){
		$(m_panelDiv).empty();
	};
	
	function changePlayerScore(id, score){
		$("#player"+id+"-info").html("Score: " + score);
	}
	
	function createPlayerDiv(player){
		var playerDiv = document.createElement('div');
		playerDiv.id = "player" + player.id + "-container";		
		
		var playerInfo = document.createElement('div');
		playerInfo.id = "player" + player.id + "-info";
		$(playerInfo).css('float', 'left');
		
		var img = document.createElement('img');
		img.id = "player" + player.id + "img";
		img.src = player.getAvatar();
		playerInfo.appendChild(img);
		
		var score = document.createElement('div');
		score.innerHTML = "Score: " + player.score;
		playerInfo.appendChild(score);
		
		playerDiv.appendChild(playerInfo);
		
		var clockDiv = document.createElement('div');
		$(clockDiv).css('float', 'left');
		
		var gameClock = document.createElement('div');
		gameClock.id = 'game-clock-'+player.id;
		$(gameClock).css('zoom','0.24');
		
		var moveClock = document.createElement('div');
		moveClock.id = 'move-clock-'+player.id;
		$(moveClock).css('zoom','0.24');
		
		clockDiv.appendChild(gameClock);
		clockDiv.appendChild(moveClock);
		
		playerDiv.appendChild(clockDiv);
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