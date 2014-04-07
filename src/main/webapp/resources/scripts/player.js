function Player(other){
	this.id = other.id;
	this.userId = other.userId;
	this.color = other.color;
	this.score = other.score;
	this.getAvatar = function getAvatar(){
		return "https://graph.facebook.com/" + this.userId.id + "/picture";
	};
}