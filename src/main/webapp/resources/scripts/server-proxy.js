

function ServerProxy(){
	var m_socket = false;
	var m_stompClient = false;
	var m_connected = false;
	var m_subscribtions = {};
	init();
	
	function init(){		
		connect();
	    
	}
	
	function connect(){
		console.debug(' connecting');
		m_socket = new SockJS("ws");	
		m_stompClient = Stomp.over(m_socket);
		m_stompClient.connect(' ',' ', function(){
	    	console.debug('successfuly connected');
	    	var keys = Object.keys(m_subscribtions);
	    	for (var i = 0; i < keys.length;i++){
	    		m_stompClient.subscribe(keys[i], m_subscribtions[keys[i]]);
	    	}
	    	m_connected = true;
	    	
	    },function(error){	    	
	    	announce('error', 'disconnected');
	    	setTimeout(connect, 1000);
	    });
	}
	
	this.newGame = function newGame(data, callBack){
		
		post('games' ,JSON.stringify(data.gameSettings), data.token ,callBack);
	};
	
	this.addPlayerToGame = function addPlayerToGame(player, gameId, callback){
		post('games/' + gameId + '/players', JSON.stringify(player),null, callback);
	};
	
	this.makeMove = function(gameId, player, coordinates){
		var actionUrl = '/action' + '/games/' + gameId + '/players/' + player.id + '/moves';			
		m_stompClient.send(actionUrl, {}, JSON.stringify(coordinates));
	};
	
	this.onMove = function onMove(gameId, callBack){
		var url = '/sub/games/' + gameId;
		if (m_connected){
			try{
				m_stompClient.subscribe(url, callBack);
			}catch(e){
				console.debug('failed to subscribe hope it will be better on reconnect');
				m_stompClient.disconnect();
				connect();
			}
		}
		m_subscribtions[url] = callBack;
			
	};
	
	this.disconnectGame = function disconnectGame(playerId, gameId, success){
		var url = 'games/' + gameId + '/players/' + playerId;
		delete m_subscribtions[url];
		$.ajax({
			url:url,
			type:"DELETE",
			dataType : 'html',
			success : success
		});
		
		var url = '/sub/games/' + gameId;
		if (m_connected){
			try{
				m_stompClient.unsubscribe(url, function(){});
			}catch(e){
				console.debug('failed to unsubscribe hope it will be better on reconnect');
			}			
		}
		
	};
	
	this.getPlayers = function getPlayers(gameId, success){
		var url = 'games/' + gameId + '/players';
		$.get(url, success);
	};
	
	function post(url, data, authorization, success){
		$.ajax({
			  url:url,
			  type:"POST",
			  data:data,
			  contentType:"application/json; charset=utf-8",
			  success: success,
			  headers: {
				    "Authorization": authorization
				  },
			  error: function (data, textStatus, request){
				  announce('error', textStatus);
			  }
		});
	}
	
	function put(url, data, success){
		$.ajax({
			  url:url,
			  type:"PUT",
			  data:data,
			  contentType:"application/json; charset=utf-8",
			  success: success,
			  error: function (data, textStatus, request){
				  announce('error', textStatus);
			  }
		});
	};
	
	

}

console.debug('serverProxy loaded');