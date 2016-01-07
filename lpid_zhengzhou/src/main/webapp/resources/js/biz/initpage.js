/**
 * initpage.jsp
 */
$(function() {
	// 退出A标签点击事件
	$("#logout").click(function() {
		$("#logout").attr("href" , ctx + "/login/logout");
	});	
	
	/**
	 * 侧边栏的导航切换
	 */
	// 一级导航
	$("#sidebar h3").click(function() {
		var elem = $(this).parent().find("dl"); // 获取dl元元素
		$("#sidebar dl").hide();
		$("#sidebar h3").removeClass("active");
		$(this).addClass("active");
		if (elem.is(":hidden")) {
			elem.show();
		} else {
			elem.hide();
		}
	})

	/**
	 * 侧边栏的二级导航
	 */
	$("#sidebar a").click(function() {
		$("#sidebar a").removeClass("active");
		$(this).addClass("active");
	})

	/**
	 * 获取当前时间
	 */
	function formatDate(date) { // 格局化日期：yyyy-MM-dd
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var day = date.getDate();
		var hours = date.getHours();
		var minute = date.getMinutes();
		var second = date.getSeconds();
		if (month < 10) {
			month = "0" + month;
		}
		if (day < 10) {
			day = "0" + day;
		}
		if (hours < 10) {
			hours = "0" + hours;
		}
		if (minute < 10) {
			minute = "0" + minute;
		}
		if (second < 10) {
			second = "0" + second;
		}
		$("#date").text(
				year + "-" + month + "-" + day + " " + hours + ":" + minute
						+ ":" + second);
	}
	formatDate(new Date());
	setInterval(function() {
		var d = new Date();
		formatDate(d);
	}, 1000);

})