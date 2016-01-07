(function($){
	$.fn.verify = function(params){
		var defaults = {
			current : this, //当前失焦的元素
			userLength : 20, //用户名的长度
			errContent : "您好用户名长度不能大于15位，请重新输入！",
			errNull : "用户名不能为空，请重新输入！"
		}
		var options = $.extend(defaults,params);
		
		if($(".popover").length > 0){ //每次先判断有没有提示框，如果有就清空
				$(".popover").remove();
		}

		function promptBox(err){ //提示框
			var box = "<div class='popover'><div class='arrow'></div><div class='popover-content'>" + err + "</div></div>";
			$("body").append(box);
			var popoverH = $(".popover").outerHeight() + 15; //获取弹窗的高度，这里加15是为了不让它们挨在一起
			var popoverW = $(".popover").outerWidth();  //获取弹窗的宽度，
			var currentW = $(options.current).outerWidth(); //获取当前元素的width
			var coordX = $(options.current).position().left; //获取当前元素的left
			var coordY = $(options.current).position().top; //获取当前元素的top
			console.log(coordX);
			console.log(coordY);
			$(".popover").css({"left" : (coordX + currentW) - popoverW, "top" :  coordY - popoverH});
		}

		//截取字符串长度
		function getStrLength(str) {
			var cArr = str.match(/[^\x00-\xff]/ig);
			return str.length + (cArr == null ? 0 : cArr.length);
		}
		var currentValue = $(options.current).val(); //获取当前元素value的长度
		if(getStrLength(currentValue) > options.userLength){
			promptBox(options.errContent);
			return false;
		}else if(currentValue == "" || currentValue == null){
			promptBox(options.errNull);
			return false;
		}
	}
})(jQuery)
