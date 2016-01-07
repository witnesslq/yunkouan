<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <title>同屏比对显示画面__A</title>
    <meta charset="utf-8">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <link type="text/css" href="${ctx}/resources/css/style.css" rel="stylesheet" />
    <script type="text/javascript">
        var webRoot = "${ctx}";
    </script>
  </head>
  <body>
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.atmosphere.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.hotkeys.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/Chart.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/ZoomIn.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/screen/screencompare.js"></script>
    
    <script type="text/javascript">
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
                                alert(data);
                            } catch (err) {
                                console.log("收图发生错误:" + err);
                            }
                        }
                    }
                }

                /* transport can be : long-polling, streaming or websocket */
                this.connectedEndpoint = $.atmosphere.subscribe('${ctx}/websockets',
                        !this.callbackAdded ? callback : null,
                        $.atmosphere.request = {transport:'websocket', logLevel:'none'});
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
            
            //当前用户所在的频道
            var channel = 'A';
            
            //接受后台推送的数据
            wsApi.subscribe();
            //延时2秒钟，在测试的时候，发现页面加载完后，不能正常登录上频道，采用了延迟加载。
            setTimeout(function(){
			    //默认A频道
			    wsApi.send({"type":"unsubscribe", "channel":channel});
			    wsApi.send({"type":"subscribe", "channel":channel});
            }, 2000);
            
        });
        
    </script>
  </body>
</html>