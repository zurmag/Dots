

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
		m_socket = new SockJS("/dots/ws");	
		m_stompClient = Stomp.over(m_socket);
		m_stompClient.connect(' ',' ', function(){
	    	console.debug('successfuly connected');
	    	var keys = Object.keys(m_subscribtions);
	    	for (var i = 0; i < keys.length;i++){
	    		m_stompClient.subscribe(keys[i], m_subscribtions[keys[i]]);
	    	}
	    	m_connected = true;
	    	
	    },function(error){	    	
	    	console.error('disconnected');
	    	setTimeout(connect, 1000);
	    });
	}
	
	this.newGame = function newGame(gameSettings, callBack){
		post('games' ,JSON.stringify(gameSettings), callBack);
	};
	
	this.addPlayerToGame = function addPlayerToGame(player, gameId){
		put('games/' + gameId, JSON.stringify(player), function(){
			console.debug('successfully added player ' + player.id);
		});
	};
	
	this.makeMove = function(gameId, player, coordinates){
		var actionUrl = '/action' + '/games/' + gameId + '/players/' + player.id + '/moves';			
		m_stompClient.send(actionUrl, {}, JSON.stringify(coordinates));
	};
	
	this.onMove = function onMove(gameId, callBack){
		var url = '/sub/games/' + gameId;
		if (m_connected){
			try{
				m_stompClient.subscribe(url, callback);
			}catch(e){
				console.debug('failed to subscribe hope it will be better on reconnect');
			}
		}
		m_subscribtions[url] = callBack;
			
	};
	
	function post(url, data, success){
		$.ajax({
			  url:url,
			  type:"POST",
			  data:data,
			  contentType:"application/json; charset=utf-8",
			  success: success,
			  error: function (data, textStatus, request){
				  console.error(textStatus);
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
				  console.error(textStatus);
			  }
		});
	}

}

console.debug('serverProxy loaded');