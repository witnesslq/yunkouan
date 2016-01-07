/**
 * 实时查验画面js
 */

/**
 * 接收后台websocket推送的信息
 */
function receiveMessage(data){
	// 获取云图引擎Div的显示状态
	var displayVal = $("#cloud").css("display");
	
	// 获取实时查验Div的显示状态
	var examineVal = $("#examine").css("display");
	
	// 云图引擎数据
	if(data.state == "cloud") {
		$("#cloud").show();
		$("#examine").hide();
		
		$("#cloud img").remove();
		var img = '<img src="${ctx}' + data.imgPath + '" alt=""/>';
		$("#cloud").append(img);
	} else if(data.state == "examine"){ // 实时查验数据
		$("#examine").show();
		$("#cloud").hide();
		
		
	}
}