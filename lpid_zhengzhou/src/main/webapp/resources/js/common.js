/*判断浏览器*/
var Sys = {}; 
var ua = navigator.userAgent.toLowerCase(); 
var s; 
(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : 
(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : 
(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : 
(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : 
(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0; 


/*if (Sys.ie) alert('IE: ' + Sys.ie);  //判断IE浏览器
if (Sys.firefox) alert('Firefox: ' + Sys.firefox); //判断火狐浏览器 
if (Sys.chrome) alert('Chrome: ' + Sys.chrome); //判断谷歌浏览器
if (Sys.opera) alert('Opera: ' + Sys.opera); //判断opera浏览器*/
if (Sys.safari){ //如果是苹果浏览器
	$(".mainframe-right").css("height","auto");
	var iframeHeight = $("#frameHei").height();
	var windowHeight = $(window).height();
	if(iframeHeight < windowHeight){
		$("#frameHei").height(windowHeight);
	}
}else{
	var windowH = $(window).height() || $(document).height();
	$(".mainframe-left,.mainframe-right").height(windowH - 51);
}

function getElem(initParams) {
    if(initParams == null){ //如果形参没有传，则让使用默认属性
        this.elem = $(window) || $(document); //默认属性
        this.that = $(".tooltip-content"); //默认属性
    }else{ //否则让this的键值等于新对象的键值
        for(var key in initParams){  //遍历对象键值
            this[key] = initParams[key]; 
        }
    }
}

//计算盒子上下左右居中
getElem.prototype.getbox = function(){
    var bodyWidth = this.elem.width(); //获取主窗口的宽度,默认是window
    var bodyHeight = this.elem.height();//获取主窗口的高度，默认是window
    var thatWidth = $(this.that).width();  //获取要居中元素的宽度
    var thatHeight = $(this.that).height(); //获取要居中元素的高度
    
    $(this.that).css("left",(bodyWidth - thatWidth) / 2); //要居中元素的left
    $(this.that).css("top",(bodyHeight - thatHeight) / 2); //要居中元素的top
}

/*修改密码弹窗*/
var tooltipCenter = new getElem({
    elem : $(window) || $(document), //window对象
    that : ".tooltip-content" //要居中的元素
})
tooltipCenter.getbox();

/*登陆样式弹窗*/
var loginCentent = new getElem({
	elem : $(window) || $(document), //window对象
    that : ".login-main" //要居中的元素
})
loginCentent.getbox();


/**
 * 弹窗函数
 * 
 * params{
 *   conformText：弹出conform样式的提示信息
 *   title：弹出窗口上方的title内容
 *   addContent：弹出窗追加内容的id或class或dom对象
 * }
 * 
 */
function tooltipElem(params){
	
	//conform弹出框
    if(params.conformText != null){ //params.conformText用来判断是否是删除按钮
        $(".delete-content").empty();//每次调用之前先清空
        var del = '<p>' + params.conformText + '</p><div class="delete-button"><button class="btn btn-primary" type="button">提交</button><button class="btn btn-default close-lg" type="button">取消</button></div>'
        $(params.addContent).append(del);
    }
    //普通窗口弹出框
	if($(".tooltip-box").length < 1) { //第一次调用窗口
        var boxContent = '<div class="tooltip-box"><div class="tooltip-diaphane"></div><div class="tooltip-content"><div class="tooltip-title"><button class="close close-lg">×</button><h3 class="title-txt">'+params.title+'</h3></div><div class="content"></div></div></div>';
        $(document.body).append(boxContent);
    }else{
    	$(".title-txt").text(params.title); //多次调用
    }
    $(".content").append($(params.addContent)); //要在弹窗里面显示的内容   
    $(".tooltip-box").show();
    $(params.addContent).siblings().hide(); //让其它窗口内容隐藏
    $(params.addContent).show(); //当前窗口内容显示
    $(".close-lg").click(function(){
    	closeTooltip();
    	$(".formtips").remove();
    })
}

/**
 * 关闭弹出的窗口
 */
function closeTooltip() {
	$(".tooltip-box").hide();
}

//数据以树的形式展示
function dataTree(params){
    //加载之前先清空tree的内容
    $(params.treeBox).empty();
    if(params.dataParam != null){  //判断数据有没有
        var jsonData = JSON.parse(params.dataParam);
        for(var i = 0; i < jsonData.length; i++){
            var li = '<li><strong>';
            if(jsonData[i].check){
                li += '<input class="pitchon" data-id=' + jsonData[i].id + ' checked="checked" type="checkbox" /><font>'+jsonData[i].name+'</font></strong><dl>';
            }else{
                li += '<input class="pitchon" data-id=' + jsonData[i].id + ' type="checkbox" /><font>'+jsonData[i].name+'</font></strong><dl>';
            }
            
            for(var j = 0; j < jsonData[i].child.length; j++){
                if(jsonData[i].child[j].check){
                    li += '<dd><input checked="checked" data-id=' + jsonData[i].child[j].id + ' name="checkbox" type="checkbox" /><a href="javascript:void(0);">'+jsonData[i].child[j].name+'</a></dd>';
                }else{
                    li += '<dd><input name="checkbox" data-id=' + jsonData[i].child[j].id + ' type="checkbox" /><a href="javascript:void(0);">'+jsonData[i].child[j].name+'</a></dd>';
                }
                
            }
            li += '</dl></li>'
            $(params.treeBox).append(li);
        }
        
        //点击展开
        $("#permissionAllot font").click(function(){
            if($(this).parent().next().is(":visible")){
                $(this).parent().next().hide();
            }else{
                $(this).parent().next().show();
            }
        })
        
        $("#permissionAllot dd input").each(function(){ //点击权限设置，检测子元素是否被选中
            if ($(this).is(":checked")) {
                $(this).parents("dl").prev().find("input").prop("checked","true");
            };
        })

        //点击判断input是否选中
         $(".pitchon").click(function(){ //点击父元素input让子元素input选中取消
            if($(this).is(":checked")){
                $(this).parent().next().find("input").prop("checked",true);
            }else{
                $(this).parent().next().find("input").prop("checked",false);
            }
        })
        
        $("#permissionAllot dd input").click(function(){ //点击子元素input让父元素input选中取消
            if($(this).is(":checked")){
                $(this).parents("dl").prev().find("input").prop("checked","true");
            }else{
               if($(this).parents("dl").find("input").is(":checked") == false){
                    $(this).parents("dl").prev().find("input").removeAttr("checked");
               };
            }
        })
    }
}

/*鼠标划入效果*/
getElem.prototype.mousmove = function(that){
	if($(window).width() < 768){
		$(that).find("i").addClass(this.addClassName);
		$(this.showElem).show();
		$(this.parentElem).css("width","45%");
	}
}

/*鼠标划出效果*/
getElem.prototype.mousout = function(that){
	if($(window).width() < 768){
		$(that).find("i").removeClass(this.addClassName);
		$(this.showElem).hide();
		$(this.parentElem).css("width","15%");
	}
}

/*二级导航展开、隐藏效果*/
getElem.prototype.subnav = function (that) {
	if($(window).width() < 768){  //如果小于768加载这个
		if($(that).next($(this.showElem)).is(":hidden")){
			$(this.hideElem).hide();
			$(that).next($(this.showElem)).show();
		}else{
			$(this.showElem).hide();
		}
	}else{  //如果大于768加载这个
		if($(that).next($(this.showElem)).is(":visible")){
			$(that).parent().removeClass(this.className);
			$(that).next($(this.showElem)).slideUp(700);
		}else{
			$(that).parents($(this.parentElem)).find($(this.showElem)).hide()
			$(that).parents($(this.parentElem)).find("li").removeClass(this.className);
			$(that).parent().addClass(this.className);
			$(that).next($(this.showElem)).slideDown(700);
		}
	}
}

getElem.prototype.getStatus = function(that){
		$(this.childElem).removeClass(this.className);
		$(that).addClass(this.className);
}


/*划入效果参数*/
var immigrate = new getElem({
	addClassName : "glyphicon-chevron-right", //要增加的class
	showElem : ".nav-sidebar li h3 a", //要显示的元素
	parentElem : ".mainframe-left" //父元素的class
})

/*划出效果参数
var emigration = new getElem({
	addClassName : "glyphicon-chevron-right", //要增加的class
	showElem : ".nav-sidebar li h3 a", //要显示的元素
	parentElem : ".mainframe-left" //父元素的class
})*/

/*二级导航要传的参数*/
var subnavShow = new getElem({
	showElem : ".second-menu", //要显示的元素
	hideElem : ".mainframe-left ul li dl", //要隐藏的元素
	parentElem : ".nav-sidebar",
	className : "active"
})

var subnavStatus = new getElem({
	childElem : ".second-menu dd", 
	className : "active-dd" //要增加的class
})

/*
 * 截取字符串长度，汉子一个等于两
 * 
 * */
function getStrLength(str) {
	var cArr = str.match(/[^\x00-\xff]/ig);
	return str.length + (cArr == null ? 0 : cArr.length);
}
























