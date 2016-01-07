/**
 * plcview.jsp
 */
/* 向PLC写入数据 */
$(function() {
	var channel ="plcChannel";
	
	$("#btnPlc").click(function(){
		var plcId = $("#plcId").val();
		var plcValue = $("#plcValue").val();
		console.log("plcId:" + plcId);
		console.log("plcValue:" + plcValue);
		$.ajax({
			type : "post",
			url : ctx + "/modbus/plcHandle",
			data : "plcId=" + plcId + "&plcValue=" + plcValue ,
			dataType : "json",
			success : function(data) {
				if(true == data) {
					alert("向PLC写入数据成功！plcId:" + plcId + ";plcValue:" + plcValue);
					console.log("向PLC写入数据成功！plcId:" + plcId + ";plcValue:" + plcValue);
				} else {
					alert("向PLC写入数据失败！！！plcId:" + plcId + ";plcValue:" + plcValue);
					console.log("向PLC写入数据失败！！！plcId:" + plcId + ";plcValue:" + plcValue);
				}
			}
		});
	});
	
	//接受后台推送的数据
    wsApi.subscribe();
    //延时2秒钟，在测试的时候，发现页面加载完后，不能正常登录上频道，采用了延迟加载。
    setTimeout(function(){
        //默认A频道
        wsApi.send({"type":"unsubscribe", "channel":channel});
        wsApi.send({"type":"subscribe", "channel":channel});
    }, 2000);
});
	
/* 返回PLC数据 */
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
                    console.log(data);
                    try {
                        var resultObj = JSON.parse(data);
                        $("#belt_stop").val(resultObj.beltStop);
                        $("#xray_off").val(resultObj.xrayOff);
                        $("#xray_startup").val(resultObj.xrayStartup);
                        $("#controlled").val(resultObj.controlled);
                        $("#belt_startup").val(resultObj.beltStartup);
                        $("#write_package_no").val(resultObj.writePaggageNo);
                        $("#bag-push").val(resultObj.bagPush);
                        $("#read_package_no").val(resultObj.readPaggageNo);
                        $("#package_length").val(resultObj.paggageLength);
                        $("#read_package_no2").val(resultObj.packageNo2);
                        $("#putters_push_status").val(resultObj.puttersPush);
                        $("#putters_return_status").val(resultObj.puttersReturn);
                        $("#decals_status").val(resultObj.decals);
                        $("#far_on-off_status").val(resultObj.farOnOff);
                        $("#near_on-off_status").val(resultObj.nearOnOff);
                        $("#energy_status").val(resultObj.energy);
                        $("#first_belt_run_status").val(resultObj.firstBeltRun);
                        $("#second_belt_run_status").val(resultObj.secondBeltRun);
                        $("#x-ray_machine_run_status").val(resultObj.xrayMachineRun);
                        $("#stop_status").val(resultObj.stop);
                        $("#all_status").val(resultObj.all);
                        $("#putters_origin_status").val(resultObj.puttersOrigin);
                        $("#origin_on-off_status").val(resultObj.originOnOff);
                        $("#bag-pushing_status").val(resultObj.bagPushing);
                    } catch (err) {
                        console.log("接受PLC数据错误:" + err);
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