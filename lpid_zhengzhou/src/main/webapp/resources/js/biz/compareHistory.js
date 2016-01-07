/**
 * compareHistory.jsp
 * 
 */
$(function() {
	function findRunningPackageModel(id){
		$.ajax({
			type : "post",
			url : ctx + "/screensCompareHistory/findRunningPackage",
			data : "id=" + id,
			dataType : "json",
			success : function(data) { 
				if(data != "" && data != null){
					console.log(data);
					with(data){
						if(isNotEmpty(rfidNo)) $("#rfidNo").text(rfidNo);
						if(isNotEmpty(xrayImages[0].imageRelativePath)) $("#xrayImages1").attr("src",picctx + "/" + xrayImages[0].imageRelativePath);
						if(isNotEmpty(xrayImages[1].imageRelativePath)) $("#xrayImages2").attr("src",picctx + "/" + xrayImages[1].imageRelativePath);
						if(isNotEmpty(xrayImages[2].imageRelativePath)) $("#xrayImages3").attr("src",picctx + "/" + xrayImages[2].imageRelativePath);
						if(isNotEmpty(photoRelativePath)) $("#photoRelativePath").attr("src",picctx + "/" + photoRelativePath);
					}
				}
			},
			error:function(data){
				console.log("查询历史数据失败！");
			}
		});
	}
	
	function isNotEmpty(args) {
		return (args != null && args != "")
	}
	
	$(document).on("click",".history-main li",function(){
		console.log(123);
		var id = $(this).attr("data-id");
		findRunningPackageModel(id);
		$(".popup-main").show();
	})
	
	$(".closes").click(function(){
		$(".popup-main").hide();
	})
	
	
});
