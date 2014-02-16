function GameStatusPanel(panelDivName){
	
	var m_panelDiv = document.getElementById(panelDivName);
	
	
	this.showGameStatus = function showGameStatus(game){
		var img = false;
		if ($('img#activ-player-img').length < 1){
			var activePlayerDiv = createOrGetElement('div', 'active-player');
			activePlayerDiv.style.display='none';
			m_panelDiv.appendChild(activePlayerDiv);
			
			img = createOrGetElement('img', 'active-player-img');
			activePlayerDiv.appendChild(img);
			activePlayerDiv.style.display='block';
		}else{
			img = $('img#activ-player-img')[0];
		}
		
		img.src = game.getActivePlayer().getAvatar();
		
	};
	
	
	
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