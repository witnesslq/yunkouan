/**
 * compareshow.jsp
 */
var showCloud = true;
function resolveData(data){
	if(data.type == "views"){ //三视角
		if(showCloud == true){
			console.log('views');
			$("#photoName img").attr("src",picctx + "/" + data.value.photoRelativePath); //可见光大图赋值
			$("#xrayName img").attr("src",picctx + "/" + data.value.xrayImages[0].imageRelativePath); //X光大图赋值
			$("#thumb img").eq(0).attr("src",picctx + "/" + data.value.xrayImages[0].imageRelativePath); //第一个X光缩略图赋值
			$("#thumb img").eq(1).attr("src",picctx + "/" + data.value.xrayImages[1].imageRelativePath); //第二个X光缩略图赋值
			$("#thumb img").eq(2).attr("src",picctx + "/" + data.value.xrayImages[2].imageRelativePath); //第三个X光缩略图赋值
			showCloud = false;
			$("#cloud").hide();
			$("#examine").show();
			
			// 添加日志
			addLogs();
			
			setTimeout(function(){ // 隐藏三视角X光机的数据和云图引擎的数据
				$("#examine").remove();
				$("#cloud img,#cloud div").remove();
				showCloud = true;
			},20000);
		}
	}else if(data.type == "cloud"){ //云图引擎
		if(showCloud == true) {
			var cloudImg = "<img src=\"" + picctx + "/" + data.value.relativePath + "\" />";
			$("#cloud").append(cloudImg);
			$("#cloud").show();
			$("#examine").hide();
			showCloud = false;
			disposeData({
	    		elem:"#cloud", //父元素
	    		childElem:"#cloud img", //子元素img
	    		data:data //数据
	    	});
			
			// 添加日志
			addLogs();
			
			setTimeout(function(){ // 隐藏三视角X光机的数据和云图引擎的数据
				$("#examine img").remove();
				$("#cloud img,#cloud div").remove();
				showCloud = true;
			},20000);
		} 
	}else if(data.type == "label") {
		var total = parseFloat(data.value.total);
		var balance = parseFloat(data.value.balance);
		var percent = Math.floor(balance / total * 100);
		
	    $("#lable").text(percent + "%");
		$(".progress-bar").css("height",percent + "%");
		/*
		console.log("config");
		console.log(data);
		var params = data.value;
		var labelTotal = "";
		var labelRemain = "";
		if(params != null) {
			for(var i = 0; i < params.length; i++) {
				if(params[i].name == "labelTotal"){
					labelTotal = params[i].value;
				} else if(params[i].name == "labelRemain"){
					labelRemain = params[i].value;
				}
			}
		}
		
		console.log("标签总数：" + labelTotal + ",剩余标签数量:" + labelRemain);
		var lableScale = Math.floor(labelRemain/labelTotal*100);
		$("#lable").text(lableScale + "%");
		$(".progress-bar").css("height",lableScale+"%");
		console.log("剩余标签的百分比为：" + lableScale);
		*/
	}
}

//接受后台传送过来的数据
var wsApi = {
    connectedEndpoint:null,
    callbackAdded:false,
    incompleteMessage:"",
    subscribe:function () {
        function callback(response) {
            if (response.transport != 'polling' && response.state == 'messageReceived') {
                if (response.status == 200) {
                    var data = response.responseBody;
                    try {
                        var resultObj = JSON.parse(data);
                        //console.log(resultObj);
                        resolveData(resultObj);
                    } catch (err) {
                        console.log("收图发生错误:" + err);
                    }
                }
            }
        }

        /* transport can be : long-polling, streaming or websocket */
        this.connectedEndpoint = $.atmosphere.subscribe(ctx + '/websockets', !this.callbackAdded ? callback : null, $.atmosphere.request = {transport:'websocket', logLevel:'none'});
        callbackAdded = true;
    },

    send:function (message) {
        console.log("Sending message");
        console.log(message);
        this.connectedEndpoint.push(JSON.stringify(message));
    },

    unsubscribe:function () {
        $.atmosphere.unsubscribe();
    }
};
	
$(function(){
	//接受后台推送的数据
    wsApi.subscribe();
    //延时2秒钟，在测试的时候，发现页面加载完后，不能正常登录上频道，采用了延迟加载。
    setTimeout(function(){
        //默认A频道
        wsApi.send({"type":"unsubscribe", "channel":channel});
        wsApi.send({"type":"subscribe", "channel":channel});
    }, 2000);
    
    // 给3个X光图片缩略图添加鼠标单击事件
    $("#thumb img").click(function(){
    	$("#xrayName img").attr("src",$(this).attr("src"));
    });
    
    $("#lable").text(percent + "%");
	$(".progress-bar").css("height",percent + "%");
});
/* 进度条设置拖动 */
var elemLeft; //获取进度条元素的left
var flag = false; //标识是否可以拖动
var elemW = $(".progress-mian").width();
$(".progress-mian").mousedown(function(e){
	elemLeft = $(".progress-mian").position().left; //获取进度条的left
	flag = true; //标识改为true
	//console.log(elemLeft);
})

$(window).mousemove(function(e){
	if(flag == true){
		var e = e || event; //处理事件兼容性
		var x = e.pageX || e.clientX; //处理事件兼容性
		if(x <= elemLeft){ //如果x <= 进度条的left，已经为0%了
			return false;
		}else if(x >= elemLeft + elemW){ //如果x >= 进度条的left + 进度条的width，已经为100%
			return false;
		}
		$(".progress-mian span").css("left",Math.round((x - elemLeft - 10) / elemW * 100)  + "%"); //x - 进度条的left - 10 / 进度条的width * 100 = 圆点要显示的百分比
		$(".progress-content").css("width",Math.round((x - elemLeft) / elemW * 100) + "%"); //x - 进度条的left / 进度条的width * 100 = 进度条要显示的百分比
		$("#suspect").val(Math.round((x - elemLeft) / elemW * 100));
	}
})

$(window).mouseup(function(e){
	flag = false;
})

// 疑似度调整
$("#btnSuspect").click(function(){
	 //请求后台图片处理
    $.ajax({
        url: ctx + "/config/updateConfigs?suspect=" + $("#suspect").val(),// 标签总数
        type:"POST",
        async:true,
        dataType:"text", 
        success:function(data){
            if(data != '' && data != null){
                
            }
        },
        error:function(err) {
            console.log("参数配置失败！");
        }
    });
});

//测试贴标
$("#label").click(function(){
	 //请求后台图片处理
    $.ajax({
        url: ctx + "/screenscompare/label",
        type:"POST",
        async:true,
        dataType:"text", 
        success:function(data){
            if(data != '' && data != null){
                
            }
        },
        error:function(err) {
            console.log(err);
        }
    });
});

function addLogs() {
	var div_cloud = $("#cloud"); // 云图引擎
	var div_examine = $("#examine"); // 行邮
	var cloud_isVisible = div_cloud.is(":visible");// 云图引擎是否显示
	var examine_isVisible = div_examine.is(":hidden");// 行邮是否隐藏
	
	var screenType ;
	var imageSrc ;
	if(cloud_isVisible) {
		screenType = div_cloud.attr("id");
		imageSrc = $("#cloud img").attr("src");
	}	
	if(!examine_isVisible) {
		screenType = div_examine.attr("id");
		imageSrc = $("#examine #xrayName img").attr("src");
		imageSrc += ";" + $("#examine #photoName img").attr("src");
	}
	
    $.ajax({
        url: ctx + "/screenscompare/addLogs",
        type:"POST",
        async:true,
        dataType:"text", 
        data:"screenType=" + screenType + "&imageSrc=" + imageSrc,
        success:function(data){
            
        },
        error:function(err) {
            console.log(err);
        }
    });
}

$("#btnQuery").click(function (){
    $.ajax({
        url: ctx + "/epc/push",
        type:"POST",
        async:true,
        dataType:"text", 
        data:"epc=2015031700032943",
        success:function(data){
            
        },
        error:function(err) {
            console.log(err);
        }
    });
});