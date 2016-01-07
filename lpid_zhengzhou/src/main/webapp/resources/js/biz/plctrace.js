/**
 * initpage.jsp
 */
$(function() {
	function controlled(plcId,value){
		$.ajax({
			type : "post",
			url : ctx + "/modbus/plcHandle",
			data : "plcId=" + plcId + "&value=" + value,
			dataType : "json",
			success : function(data) { 
			}
		});
	}
	
	$("#controlled").click(function(){ //控制皮带是否独立运行
		var value = $("#controlledVlaue").val();
		var plcId = 15;
		if(value == 0){
			vaule = 1;
		}else{
			value = 0;
		}
		controlled(plcId,value);
	})
	
	//接受后台推送的数据
    wsApi.subscribe();
    //延时2秒钟，在测试的时候，发现页面加载完后，不能正常登录上频道，采用了延迟加载。
    setTimeout(function(){
        //默认A频道
        wsApi.send({"type":"unsubscribe", "channel":channel});
        wsApi.send({"type":"subscribe", "channel":channel});
    }, 2000);
});


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
                        if(resultObj.controlled == 0){ //控制皮带是否独立运行
                        	$("#controlled .switch-content").animate({"margin-left":"-51%"},300);
                        }else if(resultObj.controlled == 1){
                        	$("#controlled .switch-content").animate({"margin-left":"0"},300);
                        }
                        
                        //上料皮带
                        if(resultObj.firstBeltRun == 0){ //停止
                        	$(".rack-box").removeClass("success").addClass("error");
                        }else if(resultObj.firstBeltRun == 1){ //运行
                        	$(".rack-box").removeClass("error").addClass("success");
                        }
                       
                        //X光机皮带
                        if(resultObj.xrayMachineRun == 0){ //停止
                        	$(".xray-belt-front,.xray-belt-behind").removeClass("success").addClass("error");
                        }else if(resultObj.xrayMachineRun == 1){ //运行
                        	$(".xray-belt-front,.xray-belt-behind").removeClass("error").addClass("success");
                        }
                        
                        //推箱皮带
                        if(resultObj.secondBeltRun == 0){ //停止
                        	$(".taper-case-belt").removeClass("success").addClass("error");
                        }else if(resultObj.secondBeltRun == 1){ //运行
                        	$(".taper-case-belt").removeClass("error").addClass("success");
                        }
                        
                        //贴标机
                        if(resultObj.all == 0){ //停止
                        	$(".labellers").removeClass("success").addClass("error");
                        }else if(resultObj.all == 1){ //运行
                        	$(".labellers").removeClass("error").addClass("success");
                        }
                        
                        //推杆系统
                        if(resultObj.puttersPush == 1){ //推出
                        	$(".push-rods").removeClass("push-rods-Out").addClass("push-rods-In");
                        }else if(resultObj.puttersReturn == 1){ //收回
                        	$(".push-rods").removeClass("push-rods-In").addClass("push-rods-Out");
                        }
                        console.log(resultObj);
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