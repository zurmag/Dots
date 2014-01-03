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