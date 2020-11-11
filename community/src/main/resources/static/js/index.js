$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	$("#hintModal").modal("show");
	//发送AJAX请求之前，将CSRF令牌设置到请求的消息头中
	// var token=$("meta[name='_csrf']").attr("content");
	// var header=$("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function(e,xhr,options){
	// 	xhr.setRequestHeader(header,token);
	// });

	//获取标题,内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	$.post(
		"/discuss/add",
		{"title":title,"content":content},
		function(data){
			data = $.parseJSON(data);
			//显示提示框并自动关闭
			$("#hintBody").text(data.msg);
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code == 0){
					window.location.reload();
				}
			}, 2000);
		}
	);
}