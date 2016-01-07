/**
 * index.jsp
 */
/*当没有session的时候，解决右边iframe里面嵌套登陆页面的bug。*/
if (window != top){
	top.location.href = location.href;
}

$(function() {
	// input控件(用户名、密码)接收到键盘按下后触发事件
	$("input").keydown(function(e){
		if(e.keyCode == 13) {
			// 调用登录按钮单击事件
			$("#login").click();	
		} 
	});
	
	// 点击登录按钮触发事件
	$("#login").click(function() {
		var loginName = $("#loginName").val();
		var password = $("#password").val();
		if (loginName == "" || loginName == null) {
			tooltip("用户名不能为空！");
			return false;
		} else if (password == "" || password == null) {
			tooltip("密码不能为空！");
			return false;
		}
		
		var indexPage = $("#indexPage").val();
		$.ajax({
			type : "post",
			url : ctx + "/login/login",
			data : "loginName=" + loginName + "&password=" + password + "&indexPage=" + indexPage,
			dataType : "json",
			success : function(data) {
				console.log(data);
				if (data != "" && data != null && data.status == 0) {
					window.location.href = ctx + "/login/initPage";
				} else {
					tooltip(data.message);
				}
			}
		});
	});
	
	// 点击重置按钮触发事件
	$("#reset").click(function(){
		//清空用户名文本框、密码文本框
		$("input").val("");
	});
	
	$("#loginName").focus();
});