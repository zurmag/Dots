function Player(color, userId){
	this.id = null;
	this.userId = userId;
	this.color = color;
	this.score = 0;
	this.getAvatar = function getAvatar(){
		return "https://graph.facebook.com/" + this.userId.id + "/picture";
	};
}