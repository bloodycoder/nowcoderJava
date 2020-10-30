$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	$("#hintModal").modal("show");
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