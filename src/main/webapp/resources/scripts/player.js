function Player(color, id){
	this.id = id;
	this.color = color;
	this.score = 0;
	this.getAvatar = function getAvatar(){
		return "https://graph.facebook.com/" + this.id + "/picture";
	};
}