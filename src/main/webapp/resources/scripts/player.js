function Player(other){
	this.id = other.id;
	this.userId = other.userId;
	this.color = other.color;
	this.score = other.score;
	this.getAvatar = function getAvatar(){
		if (this.userId){
			return "https://graph.facebook.com/" + this.userId.id + "/picture";
		}
		else{
			return "http://www.monsterbacklinks.com/pics/197282-1XSPkH1395252528.jpg";
		}
	};
}