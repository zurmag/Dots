function BoardCell(dot, player, coordinate){
	this.dot = dot;
	this.player = player;
	this.coordinate = coordinate;
	
	this.getRealCoordinate = function(){
		return this.dot.getAbsolutePosition();
	};
}