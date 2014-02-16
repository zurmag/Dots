function Player(color, id){
	this.id = id;
	this.color = color;
	this.getAvatar = function getAvatar(){
		return "https://graph.facebook.com/" + this.id + "/picture";
	};
}