<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/jsp/taglibs.jsp"%>
<!DOCTYPE html>
<html style="height:100%;">
  <head>
    <meta charset="utf-8">
    <title>国检违禁品智能查验系统</title>
    <script type="text/javascript" src="${ctx}/resources/js/common/jquery.atmosphere-min.js"></script>
  </head>
  <body style="height:100%;">
  	<div class="progress">
  		<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="height: 45%">
  			<span id="lable"></span>
  		</div>
  	</div>
  	<div class="progress-box">
  		疑似度调整
  		<input id="suspect" name="suspect" type="text" value="${suspect}" readonly="readonly"/>
  		<div class="progress-mian">
	  		<div class="progress-content" style="width:${suspect}%;"></div>
	  		<span style="left:${suspect - 2}%;"></span>
	  	</div>
	  	<button id="btnSuspect" type="button">确定</button>
  	</div>
  	
  	<!-- <input type="button" value="模拟贴标指令" id="label"> -->

  	<div style="display: none;">
  		id:${operator.id}<br>
  		channel:${operator.channel}
  		
		<input id = "btnQuery" type="button" value="submit">
  		<input type="text" id="address" /><input type="text" id="plcValue" /><br>
  		<input id = "btnChange" type="button" value="submit">
  	</div>
    <div class="container-fluid min-height-100">
	    <div class="row min-height-100">
	        <div id="cloud" class="first min-height-100" style="display:none;">
	           <!-- <img src="" alt=""/> -->
	        </div>
	        <div id="examine"  class="examine-main" style="display:none;">
	            <ul>
	                <li id="xrayName" style="border-right: 1px solid #ccc;">
	                	<img src="" alt=""/>
	                </li>
	                <li id="photoName">
	                	<img src="" alt=""/>
	                </li>
	            </ul>
	            <dl id="thumb">
	                <dd class="col-xs-4">
	                	<img src="" alt=""/>
	                </dd>
	                <dd class="col-xs-4">
	                	<img src="" alt=""/>
	                </dd>
	                <dd class="col-xs-4">
	                	<img src="" alt=""/>
	                </dd>
	            </dl>
	        </div>
	    </div>
	</div>
	<script type="text/javascript">
		var channel ="A";
		console.log("channel:" + channel);
		
		var balance = parseFloat('${label.balance}');
		var total = parseFloat('${label.total}');
		var percent = balance / total * 100;
		percent = Math.floor(percent);
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/rectangle.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/biz/compareshow.js"></script>
  </body>
</html>