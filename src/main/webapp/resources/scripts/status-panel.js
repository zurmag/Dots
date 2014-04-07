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
			
			var gameClockDiv = $('#game-clock-'+player.id);
			var gameClock = $(gameClockDiv).FlipClock({countdown: true,clockFace: 'MinuteCounter'});
			gameClockDiv.height(0);
			rescale(gameClockDiv, 0.24, 0.24);						
			
			var moveClockDiv = $('#move-clock-'+player.id);
			var moveClock = $(moveClockDiv).FlipClock({countdown: true,clockFace: 'MinuteCounter'});
			moveClockDiv.height(0);
			rescale(moveClockDiv, 0.24, 0.24);
			$('.flip-clock-label').hide();
			
			gameClock.setTime(10);
			gameClock.start();
			moveClock.setTime(10);
			moveClock.start();
			
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
		$("#player"+id +"-info div").html("Score: " + score);
	}
	
	function createPlayerDiv(player){
		var playerDiv = document.createElement('div');
		playerDiv.id = "player" + player.id + "-container";		
		$(playerDiv).css('height','70px');
		var playerInfo = document.createElement('div');
		playerInfo.id = "player" + player.id + "-info";
		$(playerInfo).css('float', 'left').css('height', 'inherit');
		
		var img = document.createElement('img');
		img.id = "player" + player.id + "img";
		img.src = player.getAvatar();
		playerInfo.appendChild(img);
		
		var score = document.createElement('div');
		score.innerHTML = "Score: " + player.score;
		playerInfo.appendChild(score);
		
		playerDiv.appendChild(playerInfo);
		
		var clockDiv = document.createElement('div');
		clockDiv.id = 'clocks-'+player.id;
		$(clockDiv).css('float', 'left').css('height', 'inherit');
		var gameClock = document.createElement('div');
		gameClock.id = 'game-clock-'+player.id;
		
		var moveClock = document.createElement('div');
		moveClock.id = 'move-clock-'+player.id;		
		clockDiv.appendChild(gameClock);
		clockDiv.appendChild(moveClock);
		
		playerDiv.appendChild(clockDiv);
		
		return playerDiv;
	}
	
	function rescale(elem, ratioX, ratioY){
		elem = $(elem);
		var height = elem.height();
	    var width = elem.width();
	    
	    //elem.toggleClass('rescaled');
	    elem.css('transform', 'scale('+ratioX +', '+ratioY+')');
	    elem.css('transform-origin','0 0');
	    //elem.css('width', parseInt(width*ratioX) + 'px');
	    //elem.css('height', parseInt(height*ratioY) + 'px');
	}
}