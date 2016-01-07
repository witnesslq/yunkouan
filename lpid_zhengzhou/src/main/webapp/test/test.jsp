<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html class="html-height">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>测试页面</title>
    <link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css" type="text/css" />
    <link rel="stylesheet" href="${ctx}/resources/css/style.css" type="text/css" />
    <script type="text/javascript" src="${ctx}/resources/js/jquery/jquery.min.js" ></script>
    <script type="text/javascript" src="${ctx}/resources/js/bootstrap.js" ></script>
    <script type="text/javascript" src="${ctx}/resources/js/common.js" ></script>
  </head>
  <body>
  	
    <input id="barcode" type="text" value="">
    <br>
    <input id="sendBarcode" type="button" value="发送条码"><br>
    <hr>
     包裹编号:<input id="packNo" type="text" value="">
    <input id="sendPhototube1" type="button" value="模拟扫描仪后的光电管"><br>
    <hr>
    <input id="sendPhototube2" type="button" value="模拟x光机前的光电管">
    <hr>
    <input type="button" id="remote" value="远程查验">
    <hr>
    <input type="button" id="playBlack" value="回放"><hr>
    <input type="button" id="lpidBtn" value="海关分拣">&nbsp;&nbsp;<input type="button" id="lpidCancleBtn" value="海关放行"><br>
    <input type="button" id="aqsiqBtn" value="检验检疫分拣">&nbsp;&nbsp;<input type="button" id="aqsiqCancleBtn" value="检验检疫放行">
    <hr>
    <label>当前包裹所在区域：(A:海关分拣口;B:检验检疫分拣口;C:综合分拣口)</label><input type="text" id="areaNo" name="areaNo" > <br>
    <input type="button" id="sortBtn" value="是否分拣" /><br>
		分拣结果：<label id="message" name="message"></label>
		<script type="text/javascript">
			$(document).ready(function(){
				// 海关分拣
				$("#lpidBtn").click(function(){
					sorting(1,1);
				});
				// 海关放行
				$("#lpidCancleBtn").click(function(){
					sorting(1,0);
				});
				
				// 检验检疫分拣
				$("#aqsiqBtn").click(function(){
					sorting(2,1);
				});
				
				// 检验检疫放行
				$("#aqsiqCancleBtn").click(function(){
					sorting(2,0);
				});
				
				//personType:下达命令者分类(1:海关人员;2:检验检疫人员)
				//sortFlag:分拣指令(0:不分拣;1:分拣)
				function sorting(personType,sortFlag){
					$.ajax({
						url:"${ctx}/test/sorting",
						data:"personType=" + personType + "&sortFlag=" + sortFlag + "&packNo=" + $("#packNo").val(),
						dataType:"json",
						type:"post",
						success:function(data){
							console.log(data);
						},
						error:function(err){
							console.log(err);
						}
					});
				}
				
				$("#sortBtn").click(function(){
					$.ajax({
						url:"${ctx}/test/isSorting",
						data:"areaNo=" + $("#areaNo").val() + "&packNo=" + $("#packNo").val(),
						dataType:"json",
						type:"post",
						success:function(data){
							console.log(data);
							$("#message").text(data==1?"分拣":"不分拣");
						},
						error:function(err){
							console.log(err);
						}
					});
				});
				
				
				$("#remote").click(function(){
					$.ajax({
						url:"${ctx}/remoteCheck/addRemoteImage",
						data:"barcode=111",
						dataType:"json",
						type:"post",
						success:function(data){
							console.log(data);
						},
						error:function(err){
							console.log(err);
						}
					});
				});
				
				$("#playBlack").click(function(){
					$.ajax({
						url:"${ctx}/screensCompareHistory/imagePlayBlack",
						dataType:"json",
						type:"post",
						success:function(data){
							console.log(data);
						},
						error:function(err){
							console.log(err);
						}
					});
				});
			});
		</script>
    <script type="text/javascript">
        //发送条码
        $("#sendBarcode").click(function(){
            $.ajax({
                url:"${ctx}/barcode/send?barcode=" + $("#barcode").val(),
                type:"POST",
                async:true,
                dataType:"text", 
                success:function(data){
                	console.log("发送条码成功：" + $("#barcode").val());
                },
                error:function(err) {
                    console.log("发送条码时发生错误:" + err);
                }
            });
        });
        
       // 模拟扫描仪后的光电管
        $("#sendPhototube1").click(function(){
            $.ajax({
                url:"${ctx}/screenscompare/phototubeTest1?packNo=" + $("#packNo").val(),
                type:"POST",
                async:true,
                dataType:"text", 
                success:function(data){
                	console.log("模拟光电管成功！");
                },
                error:function(err) {
                    console.log("模拟光电管错误:" + err);
                }
            });
        });
        
      	// 模拟x光机前的光电管
        $("#sendPhototube2").click(function(){
            $.ajax({
                url:"${ctx}/screenscompare/phototubeTest2?packNo=" + $("#packNo").val(),
                type:"POST",
                async:true,
                dataType:"text", 
                success:function(data){
                	console.log("模拟光电管成功！");
                },
                error:function(err) {
                    console.log("模拟光电管错误:" + err);
                }
            });
        });
    </script>
  </body>
</html>